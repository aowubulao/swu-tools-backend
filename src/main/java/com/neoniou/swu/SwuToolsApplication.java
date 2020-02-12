package com.neoniou.swu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Neo.Zzj
 */
@SpringBootApplication
@EnableScheduling
public class SwuToolsApplication {

    public static void main(String[] args) {

        SpringApplication.run(SwuToolsApplication.class, args);
    }
}
