package com.wraith.money.repository.encoding;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

/**
 * User: rowan.massey
 * Date: 18/08/12
 * Time: 13:55
 */
@Component
public class Encoding {

    PasswordEncoder encoder;

    public Encoding() {
        encoder = new StandardPasswordEncoder();
    }

    public String encodePassword(String password) throws NoSuchAlgorithmException {
        return encoder.encode(password);
    }

    public Boolean validPassword(String password, String encodedPassword) throws NoSuchAlgorithmException {
        return encoder.matches(password, encodedPassword);
    }
}
