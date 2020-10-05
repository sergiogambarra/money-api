package com.gambarra.money.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        System.out.println(encoder.encode("admin"));
    }
}
