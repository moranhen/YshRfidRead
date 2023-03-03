package com.ysh.rfid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
//@ServletComponentScan(basePackages = "cn.somepackage")//将会自动扫描@WebSerlet,@WebFilter,@WebListener注解
public class MvcConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(MvcConfig.class);
    /**
     * 接收Put请求 <br>
     * Filter that parses form data for HTTP PUT, PATCH, and DELETE
     * requestsand exposes it as Servlet request parameters. By default the Servlet
     * speconly requires this for HTTP POST.
     */
    @Bean
    public FormContentFilter httpPutFormContentFilter() {
        return new FormContentFilter();
    }
    /**
     注意，还必须要添加了支持PUT等跨域请求
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET","PUT","DELETE","PATCH")
                .allowedOrigins("*")
                .maxAge(1800);
    }

}
