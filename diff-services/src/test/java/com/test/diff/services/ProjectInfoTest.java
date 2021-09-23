package com.test.diff.services;

import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.service.ProjectInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectInfoTest {

    @Resource
    private ProjectInfoService projectInfoService;

    @Test
    public void testSelect(){
        ProjectInfo projectInfo = projectInfoService.getById(1);
        System.out.println(projectInfo.getCollectStatus());
        System.out.println(projectInfo.getReportStatus());
    }

    @Test
    public void testUpdate(){
        ProjectInfo projectInfo = projectInfoService.getById(1);
        projectInfo.setCollectStatus(1);
        projectInfo.setReportStatus(1);
        projectInfoService.updateById(projectInfo);
    }
}
