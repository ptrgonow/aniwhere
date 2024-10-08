package com.aniwhere.domain.user.join.service;

import com.aniwhere.domain.user.join.dto.MailCodeCheckDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;  // 의존성 주입을 통해 필요한 객체를 가져옴
    private final Logger logger = Logger.getLogger(MailService.class.getName());
    private static final String senderEmail= "aniwhere03@gmail.com";
    public static HashMap<String, String> codeStorage = new HashMap<>();


    // 메일 양식
    public MimeMessage createMail(String mail, String number){
        generateAuthCode();  // 인증 코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, mail); // 보낼 이메일 설정
            message.setSubject("[AniWhere] 회원가입을 위한 이메일 인증");  // 제목 설정
            String body = getBody(number);
            message.setText(body, "UTF-8", "html");
        } catch (Exception e) {
            logger.severe("메일 생성 실패: " + e.getMessage());
        }

        return message;
    }

    private static String getBody(String number) {
        String body = "";
        body += "<div style='font-family: Arial, sans-serif; width: 600px; margin: auto; padding: 20px; border: 1px solid #ccc;'>";
        body += "<div style='text-align: center;'>";
        body += "<h1 style='color: #333; font-size: 24px;'>AniWhere, Everywhere!</h1>";
        body += "<h2 style='color: #333; font-size: 18px;'>환영합니다!</h2>";
        body += "<p style='color: #333; font-size: 14px;'>회원가입을 위한 요청하신 인증 번호입니다.</p>";
        body += "<div style='margin: 20px 0; padding: 10px; background-color: #F28123; color: #fff; font-size: 20px;'>";
        body += "요청코드 : " + number;
        body += "</div>";
        body += "</div>";
        body += "</div>";
        return body;
    }

    // 실제 메일 전송
    public String sendEmail(String email) {
        String authCode = generateAuthCode();  // 인증 코드 생성
        codeStorage.put(email, authCode);  // 인증 코드를 저장

        MimeMessage message = createMail(email, authCode);
        try {
            logger.info("메일 전송 시도: " + email);
            javaMailSender.send(message);
            logger.info("이메일 전송 성공: " + email);
        } catch (Exception e) {
            logger.severe("이메일 전송 실패: " + e.getMessage());
        }
        return authCode;
    }

    // 인증 코드 확인
    public boolean confirmEmail(MailCodeCheckDTO mailCodeCheckDTO) {
        String email = mailCodeCheckDTO.getEmail();
        String code = mailCodeCheckDTO.getAuthCode();
        String findCode = codeStorage.get(email);

        if (code.equals(findCode)) {
            codeStorage.remove(email);
            return true;
        } else {
            return false;
        }
    }

    // 인증 코드 생성
    private String generateAuthCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    public void sendBulkHtmlMessage(List<String> toList, String subject, String htmlBody) throws MessagingException {
        for (String to : toList) {
            sendHtmlMessage(to, subject, htmlBody);
        }
    }

    // 관리자 회원 관리 메일 발송
    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        javaMailSender.send(message);
    }
}
