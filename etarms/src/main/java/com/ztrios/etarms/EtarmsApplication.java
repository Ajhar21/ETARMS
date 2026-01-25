package com.ztrios.etarms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class EtarmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EtarmsApplication.class, args);
    }
}
