package com.neoniou.swu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author neo.zzj
 * @date 2019-11-10
 */
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        //添加cors配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许的域
        corsConfiguration.addAllowedOrigin("*");
        //是否发送cookie信息
        corsConfiguration.setAllowCredentials(true);
        //允许的请求方式
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedMethod("HEAD");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("PATCH");
        //允许的头信息
        corsConfiguration.addAllowedHeader("*");
        //有效时长
        corsConfiguration.setMaxAge(3600L);

        //添加映射路径，拦截所有请求
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        //返回新的config
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}

