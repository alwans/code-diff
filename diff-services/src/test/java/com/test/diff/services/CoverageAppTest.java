package com.test.diff.services;

import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.service.CoverageAppService;
import com.test.diff.services.service.CoverageReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CoverageAppTest {

    @Resource
    private CoverageAppService coverageAppService;

    @Test
    public void testInsert(){
        CoverageApp app = new CoverageApp();
        app.setAppName("slh");
        app.setHost("12312");
        app.setPort("123");
        coverageAppService.create(1, app);
    }
}


