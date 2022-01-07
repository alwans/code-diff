package com.test.diff.services.internal;

import cn.hutool.crypto.SecureUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.test.diff.common.domain.ClassInfo;
import com.test.diff.common.domain.MethodInfo;
import com.test.diff.common.enums.DiffResultTypeEnum;
import com.test.diff.services.consts.GitConst;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.exceptions.BizException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通过java文件来解析类信息
 * @author wl
 */
@Slf4j
public class JavaFileCodeComparator implements ICodeComparator{

    @Override
    public ClassInfo getDiffClassInfo(DiffEntry diffEntry, String oldFilePath, String newFilePath) {
        String className,packName;
        if(diffEntry.getNewPath().contains(GitConst.JAVA_DEFAULT_PATH)){
            className = diffEntry.getNewPath().split("\\.")[0].split(GitConst.JAVA_DEFAULT_PATH)[1];
            packName = diffEntry.getNewPath().split("/")[0];
        }else{
            className = diffEntry.getNewPath().split("\\.")[0];
            packName = "";
        }

        //新增类
        if(DiffEntry.ChangeType.ADD.equals(diffEntry.getChangeType())){
            return ClassInfo.builder()
                    .className(className)
                    .diffType(DiffResultTypeEnum.ADD)
                    .packageName(packName)
                    .build();
        }
        //删除类 需要获取删除类的所有方法
        else if(DiffEntry.ChangeType.DELETE.equals(diffEntry.getChangeType())){
            return ClassInfo.builder()
                    .className(className)
                    .diffType(DiffResultTypeEnum.DEL)
                    .packageName(packName)
                    .build();
        }
        List<MethodInfo> diffMethods = new ArrayList<>();
        //获取新类的所有方法
        List<MethodInfo> newMethodInfoResults = parseMethods(newFilePath);
        //如果新类为空，没必要比较
        if (CollectionUtils.isEmpty(newMethodInfoResults)) {
            return null;
        }
        //获取旧类的所有方法
        List<MethodInfo> oldMethodInfoResults = parseMethods(oldFilePath);
        //如果旧类为空，新类的方法所有为增量
        if (CollectionUtils.isEmpty(oldMethodInfoResults)) {
            diffMethods = newMethodInfoResults;
        } else {   //否则，计算增量方法
            List<String> md5s = oldMethodInfoResults.stream().map(MethodInfo::getMd5).collect(Collectors.toList());
//            diffMethods = newMethodInfoResults.stream().filter(m -> !md5s.contains(m.getMd5())).collect(Collectors.toList());
            List<String> oldUri = oldMethodInfoResults.stream()
                    .map(methodInfo -> methodInfo.getMethodName()+methodInfo.getParams()).collect(Collectors.toList());
            for(MethodInfo m: newMethodInfoResults){
                //修改过的方法
                if(oldUri.contains(m.getMethodName()+m.getParams()) && !md5s.contains(m.getMd5()) ){
                    m.setDiffType(DiffResultTypeEnum.MODIFY);
                    diffMethods.add(m);
                    oldUri.remove(m.getMethodName()+m.getParams());
                }
                //新增的方法
                else if(!oldUri.contains(m.getMethodName()+m.getParams())){
                    m.setDiffType(DiffResultTypeEnum.ADD);
                    diffMethods.add(m);
                }
                else if(oldUri.contains(m.getMethodName()+m.getParams()) && md5s.contains(m.getMd5())){
                    oldUri.remove(m.getMethodName()+m.getParams());
                }
            }
            List<MethodInfo> delMethodList = oldMethodInfoResults.stream()
                    .filter(m -> oldUri.contains(m.getMethodName()+m.getParams()))
                    .map(m -> {m.setDiffType(DiffResultTypeEnum.DEL);return m;})
                    .collect(Collectors.toList());
            List<MethodInfo> finalDiffMethods = diffMethods;
            delMethodList.stream().collect(Collectors.toCollection(() -> finalDiffMethods));
        }
        //没有增量方法，过滤掉
        if (CollectionUtils.isEmpty(diffMethods)) {
            return null;
        }
        return ClassInfo.builder()
                .className(className)
                .packageName(packName)
                .diffType(DiffResultTypeEnum.MODIFY)
                .methodInfos(diffMethods)
                .build();
    }
    /**
     * 解析类获取类的所有方法
     *
     * @param classFile
     * @return
     */
    public static List<MethodInfo> parseMethods(String classFile){
        List<MethodInfo> list = new ArrayList<>();
        try (FileInputStream in = new FileInputStream(classFile)){
            JavaParser javaParser = new JavaParser();
            CompilationUnit cu = javaParser.parse(in).getResult().orElseThrow(() -> new BizException(StatusCode.JAVA_PARSER_ERROR));
            //由于jacoco不会统计接口覆盖率，没比较计算接口的方法，此处排除接口类
            final List<?> types = cu.getTypes();
            boolean isInterface = types.stream().filter(t -> t instanceof ClassOrInterfaceDeclaration).anyMatch(t -> ((ClassOrInterfaceDeclaration) t).isInterface());
            if (isInterface) {
                return list;
            }
            cu.accept(new MethodVisitor(), list);
            return list;
        } catch (Exception e) {
            log.error("使用javaParser解析{}文件失败", classFile, e);
            throw new BizException(StatusCode.JAVA_PARSER_ERROR);
        }
    }

    /**
     * javaparser工具类核心方法，主要通过这个类遍历class文件的方法，此方法主要是获取出代码的所有方法，然后再去对比方法是否存在差异
     */
    private static class MethodVisitor extends VoidVisitorAdapter<List<MethodInfo>> {
        @Override
        public void visit(MethodDeclaration n, List<MethodInfo> list) {
            //删除注释
            n.removeComment();
            //计算方法体的hash值，疑问，空格，特殊转义字符会影响结果，导致相同匹配为差异？建议提交代码时统一工具格式化
            String md5 = SecureUtil.md5(n.toString());
            //参数处理
            StringBuilder params = new StringBuilder();
            NodeList<Parameter> parameters = n.getParameters();
            if(!CollectionUtils.isEmpty(parameters)){
                for (int i = 0; i < parameters.size(); i++) {
                    String param = parameters.get(i).getType().toString();
                    params.append(param.replaceAll(" ", ""));
                    //和asm一致，每个参数都使用分号结尾
                    params.append(";");
                }
            }else{
                params.append("");
            }
            MethodInfo methodInfo = MethodInfo.builder()
                    .md5(md5)
                    .methodName(n.getNameAsString())
                    .params(params.toString())
                    .build();
            list.add(methodInfo);
            super.visit(n, list);
        }

    }

}
