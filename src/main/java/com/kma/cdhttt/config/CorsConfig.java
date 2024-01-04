package com.kma.cdhttt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/kma/v1/user")
                .allowedOrigins("http://localhost:3000")  // Thay đổi origin của ReactJS của bạn
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
