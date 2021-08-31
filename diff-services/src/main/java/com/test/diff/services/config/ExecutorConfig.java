package com.test.diff.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wl
 */
@Configuration

public class ExecutorConfig {

    @Bean("asyncExecutor")
    public Executor asyncServicesExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors()-1);
        executor.setMaxPoolSize(4 * Runtime.getRuntime().availableProcessors());
        executor.setKeepAliveSeconds(30);
        executor.setQueueCapacity(4096);
        executor.setThreadNamePrefix("async thread -");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}
