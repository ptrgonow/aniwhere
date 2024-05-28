package com.aniwhere.global.config.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PlainTextPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // 평문 비밀번호를 그대로 반환
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 평문 비밀번호와 저장된 비밀번호를 비교
        return rawPassword.toString().equals(encodedPassword);
    }
}
