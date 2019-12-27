package com.wzh.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.wzh.mapper"})
@ComponentScan(basePackages = {"com.wzh.po","com.wzh.service","com.wzh.controller"})
public class BootmybatisNginxSaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootmybatisNginxSaveApplication.class, args);
    }

}
