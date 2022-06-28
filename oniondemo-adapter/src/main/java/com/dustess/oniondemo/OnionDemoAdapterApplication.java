package com.dustess.oniondemo;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author jz
 */
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
@MapperScan("com.dustess.oniondemo.mybatis.mapper")
@EnableFeignClients(basePackages = "com.dustess.oniondemo.feign")
public class OnionDemoAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnionDemoAdapterApplication.class, args);
    }
}
