package com.ztrios.etarms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
//@Profile("dev") // keep this dev-only
public class BcryptGenerateRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("====================================");
        System.out.println("RAW PASSWORD     : " + rawPassword);
        System.out.println("BCrypt HASH      : " + encodedPassword);
        System.out.println("====================================");
    }
}
