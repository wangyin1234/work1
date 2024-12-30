/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wison.purchase.packages.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yinxin
 */
@Configuration
@Slf4j
@EnableAsync
public class AsyncTaskConfig {

    private static final int corePoolSize = 10;

    private static final int maxPoolSize = 100;

    private static final int queueCapacity = 10;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(corePoolSize);
        // 最大线程数
        taskExecutor.setMaxPoolSize(maxPoolSize);
        // 缓存队列
        taskExecutor.setQueueCapacity(queueCapacity);
        // 等待时间
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 初始化
        taskExecutor.initialize();
        return taskExecutor;
    }

}