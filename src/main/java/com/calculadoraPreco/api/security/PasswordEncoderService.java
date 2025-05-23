package com.calculadoraPreco.api.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderService {

    public static PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static String encode(String password){
        return getPasswordEncoder().encode(password);
    }

}
