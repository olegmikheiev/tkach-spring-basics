package com.appsom.tkach.spring.core.spring;

import com.appsom.tkach.spring.core.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:client.properties")
@EnableAspectJAutoProxy
public class ApplicationConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Client client() {
        Client client = new Client();
        client.setId(environment.getRequiredProperty("id"));
        client.setFullName(environment.getRequiredProperty("name"));
        client.setGreeting(environment.getProperty("greeting"));
        return client;
    }
}
