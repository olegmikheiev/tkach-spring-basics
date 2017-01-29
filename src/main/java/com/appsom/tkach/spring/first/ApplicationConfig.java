package com.appsom.tkach.spring.first;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public ApplicationImpl app() {
        return new ApplicationImpl();
    }
}
