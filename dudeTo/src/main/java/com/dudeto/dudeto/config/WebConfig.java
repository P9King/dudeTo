package com.dudeto.dudeto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConfigurationProperties
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/files/**"; // view에서 접근할 경로
    private  String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }
}
