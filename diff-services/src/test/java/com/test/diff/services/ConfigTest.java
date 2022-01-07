package com.test.diff.services;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ConfigTest {

    @Value("${report.domain}")
    private String reportDomain;

    @Value("${server.port}")
    private String port;

    @Test
    public void testReportDomain(){
        System.out.println(reportDomain);
    }

    @Test
    public void testServerPort(){
        System.out.println(port);
    }
}
