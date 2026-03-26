package com.ruoyi.flowable;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 工作流模块启动程序
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
@MapperScan({"com.ruoyi.flowable.mapper", "com.ruoyi.flowable.core.mapper"})
public class RuoyiFlowableApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuoyiFlowableApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  工作流模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}