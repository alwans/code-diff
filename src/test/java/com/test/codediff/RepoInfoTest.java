package com.test.codediff;

import com.test.codediff.entity.RepoInfo;
import com.test.codediff.service.RepoInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RepoInfoTest {

    @Resource
    RepoInfoService repoInfoService;

    @Test
    public void testSelect(){
        RepoInfo repoInfo = repoInfoService.getById(1);
        System.out.println(repoInfo.toString());
    }
}
