package com.neoniou.swu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Neo.Zzj
 */
@SpringBootApplication
@MapperScan("com.neoniou.swu.dao")
public class SwuToolsApplication {

    public static void main(String[] args) {

        SpringApplication.run(SwuToolsApplication.class, args);
    }
}
