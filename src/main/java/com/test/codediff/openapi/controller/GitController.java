package com.test.codediff.openapi.controller;

import com.test.codediff.base.result.BaseResult;
import com.test.codediff.params.ProjectBranchParams;
import com.test.codediff.params.ProjectParams;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/git")
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
