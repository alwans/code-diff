package com.test.diff.services.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.service.CoverageAppService;
import com.test.diff.services.mapper.CoverageAppMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class CoverageAppServiceImpl extends ServiceImpl<CoverageAppMapper, CoverageApp>
    implements CoverageAppService{
    
    @Resource
    private CoverageAppMapper coverageAppMapper;

    @Override
    public List<CoverageApp> getListByProjectId(long projectId) {
        LambdaQueryWrapper<CoverageApp> query = new LambdaQueryWrapper<>();
        query.eq(CoverageApp::getIsDelete, false);
        query.eq(CoverageApp::getIsDisable, false);
        query.eq(CoverageApp::getProjectId, projectId);
        return coverageAppMapper.selectList(query);
    }

    @Override
    public int create(int projectId, CoverageApp app) {
        app.setStatus(false);
        app.setAddTime(new Date());
        app.setLastTime(new Date());
        app.setIsDelete(false);
        app.setIsDisable(false);
        app.setProjectId(projectId);
        coverageAppMapper.insert(app);
        return app.getId().intValue();
    }

    @Override
    public CoverageApp getAppByProjectIdAndPort(int projectId, int jacocoPort) {
        LambdaQueryWrapper<CoverageApp> query = new LambdaQueryWrapper<>();
        query.eq(CoverageApp::getProjectId, projectId);
        query.eq(CoverageApp::getPort, jacocoPort);
        query.eq(CoverageApp::getStatus, true);
        return coverageAppMapper.selectOne(query);
    }
}




