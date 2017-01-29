package com.appsom.tkach.spring.first.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component("client")
public class Client {
    @Value("1")
    private String id;
    @Value("John Smith")
    private String fullName;
    @Value("Hello there!")
    private String greeting;

    /*
    public Client(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
    */
}
