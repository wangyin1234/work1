package com.wison.purchase.packages.config;

import com.wison.purchase.packages.utils.Threads;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class BeanConfig {

    private ExecutorService executorService;

    @Bean(name = "executorService", destroyMethod = "shutdown")
    public ExecutorService taskExecutor() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        this.executorService = executorService;
        return executorService;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).callTimeout(60, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 销毁事件
     */
    @PreDestroy
    public void destroy() {
        try {
            log.info("====关闭后台任务线程池====");
            Threads.shutdownAndAwaitTermination(executorService);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
