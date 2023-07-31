package com.tian.asenghuamarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tian.asenghuamarket.mapper")
public class AsengHuaMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsengHuaMarketApplication.class, args);
    }

}
