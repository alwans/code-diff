package com.test.codediff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.codediff.entity.ProjectInfo;
import com.test.codediff.service.DiffProjectInfoService;
import com.test.codediff.mapper.DiffProjectInfoMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class DiffProjectInfoServiceImpl extends ServiceImpl<DiffProjectInfoMapper, ProjectInfo>
    implements DiffProjectInfoService{

}




