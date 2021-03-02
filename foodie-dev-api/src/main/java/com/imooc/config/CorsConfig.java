package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    public CorsConfig() {
    }
    @Bean
    public CorsFilter corsFilter(){
        //添加cors配置信息
        CorsConfiguration config=new CorsConfiguration();
        //配置可以跨域的信息
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://159.75.103.199:8080");
        config.addAllowedOrigin("http://159.75.103.199");
        //设置是否允许发送cookie信息
        config.setAllowCredentials(true);
        //设置允许请求的方式
        config.addAllowedMethod("*");
        //设置允许的header
        config.addAllowedHeader("*");
        //为所有跨域请求的路径添加此配置
        UrlBasedCorsConfigurationSource corsSourse=new UrlBasedCorsConfigurationSource();
        corsSourse.registerCorsConfiguration("/**",config);
        //返回CorsFilter
        return new CorsFilter(corsSourse);
    }

}
