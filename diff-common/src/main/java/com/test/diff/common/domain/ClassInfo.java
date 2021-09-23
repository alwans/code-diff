package com.test.diff.common.domain;


import com.test.diff.common.consts.GitConst;
import com.test.diff.common.enums.DiffResultTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author wl
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
//        return this.packageName.replaceAll(".","/")+this.className+ GitConst.JAVA_FILE_SUFFIX;
        return this.className;
    }

}

