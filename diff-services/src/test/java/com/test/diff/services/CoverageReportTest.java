package com.test.diff.services;

import com.test.diff.services.entity.CoverageReport;
import com.test.diff.services.service.CoverageReportService;
import com.test.diff.services.service.impl.CoverageReportServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CoverageReportTest {

    @Resource
    private CoverageReportServiceImpl coverageReportService;

    @Test
    public void testInsert(){
        coverageReportService.create(1);
    }

    @Test
    public void testUpdate(){
        CoverageReport report = coverageReportService.getById(1);
        report.setReportType(5);
        report.setIsUsed(false);
        report.setOldBranch("master");
        report.setNewBranch("develop");
        report.setLastTime(new Date());
        coverageReportService.updateById(report);
    }
}
