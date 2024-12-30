package com.wison.purchase.packages;

import com.wison.base.middleLog.annotation.EnableMiddleLog;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableMiddleLog
@EnableDubbo
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
@MapperScan("com.wison.purchase.packages.module.**.mapper")
public class PurchasePackageApp {

    public static void main(String[] args) {
        SpringApplication.run(PurchasePackageApp.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
