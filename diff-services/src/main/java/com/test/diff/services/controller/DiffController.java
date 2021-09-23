package com.test.diff.services.controller;

import com.test.diff.common.domain.ClassInfo;
import com.test.diff.common.util.JacksonUtil;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.internal.DiffWorkFlow;
import com.test.diff.services.params.ProjectDiffParams;
import com.test.diff.services.base.controller.result.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * @author wl
 */
@RestController
@RequestMapping("api/diff")
public class DiffController {

    @Resource
    private DiffWorkFlow diffWorkFlow;

    @PostMapping(value = "/getDiffResult",produces = "application/json;charset=UTF-8")
    public BaseResult getDiffResult(@RequestBody @Validated ProjectDiffParams params) throws GitAPIException, IOException {
        if(StringUtils.isEmpty(params.getNewVersion()) &&
                StringUtils.isEmpty(params.getOldCommitId())){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "参数错误");
        }
        List<ClassInfo> list = diffWorkFlow.diff(params);
        String sequence = JacksonUtil.serialize(list);
        System.out.println(sequence);
        return BaseResult.success(sequence);

    }
}
