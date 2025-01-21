package com.future;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 鉴权启动程序
 *
 * @author Future Platform Group
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = { "com.future.module.oauth.mapper", "com.future.*.mapper"})
public class OauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthApplication.class, args);
        System.out.println("鉴权启动成功");
    }

}
