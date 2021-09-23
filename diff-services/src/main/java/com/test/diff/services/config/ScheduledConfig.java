package com.test.diff.services.config;

import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.context.Theme;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;

/**
 *
 * @author wl
 */
@Configuration
@EnableScheduling
@Slf4j
public class ScheduledConfig {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource(name = "asyncExecutor")
    private Executor executor;

    /**
     * 周一到周五，9-22点内每半小时获取一次数据
     */
//    @Scheduled(cron = "0 0/30 9-22 ? * MON-FRI")
    @Scheduled(cron = "0 0/30 9-22 * * ?")
    public void pullJacocoData(){
        log.info("开始轮询拉取探针数据...,需要轮询工程总数：{}", projectInfoService.getCollectProject().size());
        projectInfoService.getCollectProject().stream()
                .forEach(projectInfo -> {
                    executor.execute(new Thread(){
                        @Override
                        public void run() {
                            projectInfoService.pullExecData(projectInfo);
                        }
                    });
                });
    }

}
