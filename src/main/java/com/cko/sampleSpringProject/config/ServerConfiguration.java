package com.cko.sampleSpringProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//@EnableWebMvc
//public class ServerConfiguration extends WebMvcConfigurerAdapter {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/js/**")
//                .addResourceLocations("/resources/static/js/");
//        registry
//                .addResourceHandler("/css/**")
//                .addResourceLocations("/resources/static/css/");
//        registry
//                .addResourceHandler("/img/**")
//                .addResourceLocations("/resources/static/img/");
//    }
//}