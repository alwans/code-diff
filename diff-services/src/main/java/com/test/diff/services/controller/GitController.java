package com.test.diff.services.controller;

import com.test.diff.services.base.controller.result.BaseResult;
import com.test.diff.services.params.ProjectBranchParams;
import com.test.diff.services.params.ProjectParams;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/git")
public class GitController {

    @GetMapping("/branch/list")
    public BaseResult getBranchList(@Validated ProjectParams projectParam){
        return null;
    }

    @GetMapping
    public BaseResult getCommitList(@Validated ProjectBranchParams branchParams){
        return null;
    }
}
