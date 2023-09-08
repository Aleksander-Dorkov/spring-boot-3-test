package com.sasho.demo.testConfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Pesho {
    public Pesho() {
        System.out.println("Pesho");
    }

    @Bean
    public String aa(){
        return "aa";
    }
}
