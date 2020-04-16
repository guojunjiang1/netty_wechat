package com.itheima.hchat;

import com.itheima.hchat.util.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author GuoJunJiang
 * @version 1.0
 * @date 2020/4/8 9:36
 */
@SpringBootApplication
@MapperScan(basePackages = "com.itheima.hchat.mapper")
public class HchatApplication {
    public static void main(String[] args) {
        SpringApplication.run(HchatApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }
}
