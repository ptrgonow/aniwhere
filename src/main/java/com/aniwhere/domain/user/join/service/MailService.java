package com.aniwhere.domain.user.join.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.logging.Logger;

@Service
public class MailService {

    private final Logger logger = Logger.getLogger(MailService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    public String sendAuthCode(String to) {
        // Generate random authentication code
        Random random = new Random();
        String authCode = String.format("%06d", random.nextInt(1000000));

        // Create a simple mail message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("인증 코드");
        message.setText("인증 코드는 " + authCode + "입니다.");

        try {
            mailSender.send(message);
            logger.info("이메일 전송 성공: " + to);
        } catch (Exception e) {
            logger.severe("이메일 전송 실패: " + e.getMessage());
        }

        return authCode;
    }
}
