package com.test.codediff.vo;

import com.test.codediff.consts.GitConst;
import com.test.codediff.enums.DiffResultTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wl
 */
@Getter
@Setter
@Builder
public class ClassInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String packageName;
    private String className;
    private DiffResultTypeEnum diffType;
    private List<MethodInfo> methodInfos;

    /**
     * 返回Asm解析中对应的类名格式
     * @return
     */
    public String getAsmClassName(){
        return this.packageName.replaceAll(".","/")+this.className+ GitConst.JAVA_FILE_SUFFIX;
    }

}

