package com.test.diff.services.internal.jacoco;

import com.test.diff.services.exceptions.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jacoco.cli.internal.JacocoApi;
import org.jacoco.cli.internal.core.tools.ExecFileLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wl
 */
@Slf4j
public class JacocoHandle {

    /**
     * 读取exec文件
     * @param execFilePath exec文件绝对路径
     * @return 返回读取的exec文件数据对象
     */
    public static ExecFileLoader getLoader(String execFilePath){
        File file = new File(execFilePath);
        if(!file.exists()){
            log.error("{}文件不存在，请确认", execFilePath);
            return null;
        }
        ExecFileLoader loader = new ExecFileLoader();
        try {
            loader.load(file);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("读取{} 文件失败", execFilePath);
            return null;
        }
        return loader;
    }

    /**
     * 获取exec文件中的branch name
     * @param loader
     * @return
     */
    public static String getBranchName(ExecFileLoader loader){
        return loader.getExecutionDataStore().getBranchName();
    }

    /**
     * 获取exec文件中的commitId信息
     * @param loader
     * @return
     */
    public static String getCommitId(ExecFileLoader loader){
        return loader.getExecutionDataStore().getCommitId();
    }

    /**
     * dump 探针数据;
     * 因为调用链数据太大了，所以加上--rest参数，每次dump后，重置jacoco探针数据
     * @param host 应用服务器host
     * @param port jacoco tcp server 端口
     * @param execFilePath exec文件保存绝对路径
     * @throws Exception dump数据失败异常
     */
    public static void dump(String host, String port, String execFilePath) throws Exception {
        log.debug("开始dump exec文件，文件存放路径：{}， address:{}, port:{}", execFilePath, host, port);
        int result = JacocoApi.execute("dump", "--destfile", execFilePath, "--address", host, "--port", port, "--reset");
        if(result == -1){
            throw new BizException("dump数据失败");
        }
        log.debug("dump exec文件成功，文件存放路径：{}， address:{}, port:{}", execFilePath, host, port);
    }

    /**
     * merge 合并多个exec数据文件，
     * 2中情况可合并：一：同分支同commit id ；二：同分支，不同commit id
     * @param newSaveFilePath 合并后保存的文件绝对路径
     * @param execFilesPath exec文件路径数组
     * @throws Exception 合并失败异常
     */
    public static void merge(String newSaveFilePath, String... execFilesPath) throws Exception {
        List<String> params = new ArrayList<>();
        params.add("merge");
        params.add("--destfile");
        params.add(newSaveFilePath);
        for(String path : execFilesPath){
            params.add(path);
        }
        int result = JacocoApi.execute(params.toArray(new String[params.size()]));
        if(result == -1){
            throw new BizException("merge 失败");
        }
    }

    /**
     * merge 合并多个exec数据文件，
     * 2中情况可合并：一：同分支同commit id ；二：同分支，不同commit id
     * @param projectId 项目id 不同commit id合并时，需要知道项目id来找出差异类，针对差异类做合并; 同commit可传0
     * @param localPort 本服务对应的端口  --> @Value("${server.port}")
     * @param newSaveFilePath 合并后保存的文件绝对路径
     * @param execFiles exec文件数组
     * @throws Exception
     */
    public static void merge(long projectId, String localPort, String newSaveFilePath, File... execFiles) throws Exception {
        List<String> params = new ArrayList<>();
        params.add("merge");
        params.add("--id");
        params.add(projectId+"");
        params.add("--destfile");
        params.add(newSaveFilePath);
        for(File file : execFiles){
            params.add(file.getAbsolutePath());
        }
        params.add("--diffPort");
        params.add(localPort);
        int result = JacocoApi.execute(params.toArray(new String[params.size()]));
        if(result == -1){
            throw new BizException("merge 失败");
        }
    }

    /**
     * report 生成覆盖率报告，默认html
     * @param execFilePath exec文件绝对路径
     * @param classFilePaths 工程中，多个module的class文件绝对路径集合
     * @param sourceFilePaths 工程中，多个module的java源文件绝对路径
     * @param reportPath 报告保存绝对路径
     * @param diffResult 代码差异列表的序列化字符串。获取全量报告时，此参数为：null
     * @throws Exception 生成报告失败异常
     */
    public static void report(String execFilePath,
                              List<String> classFilePaths,
                              List<String> sourceFilePaths,
                              String reportPath,
                              String diffResult,
                              String filterRules) throws Exception {
        List<String> params = new ArrayList<>();
        params.add("report");
        params.add(execFilePath);
        classFilePaths.stream().forEach(classFilepath -> {
            params.add("--classfiles");
            params.add(classFilepath);
        });
        sourceFilePaths.stream().forEach(sourceFilePath -> {
            params.add("--sourcefiles");
            params.add(sourceFilePath);
        });
        params.add("--html");
        params.add(reportPath);
        if(StringUtils.isNotBlank(diffResult)){
            params.add("--diffFiles");
            params.add(diffResult);
        }
        if(StringUtils.isNotBlank(filterRules)){
            params.add("--filterRules");
            params.add(filterRules);
        }
        log.info("jacoco report 参数："+params);
        int result = JacocoApi.execute(params.toArray(new String[0]));
        if(result == -1){
            throw new BizException("报告生成失败");
        }
    }


}
