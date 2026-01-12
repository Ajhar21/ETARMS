package com.ztrios.etarms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
//@Profile("dev") // optional but strongly recommended
public class BcryptVerificationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123";
        String storedHash = "$2a$10$X9v7Hk1H.kf5JYxBZ2aLe.8KszxG7A5qfR9jTjvC9QjqYozx4h4kO";

        boolean matches = encoder.matches(rawPassword, storedHash);

        System.out.println("====================================");
        System.out.println("BCrypt password match result: " + matches);
        System.out.println("====================================");
    }
}
