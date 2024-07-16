package com.aniwhere.domain.admin.service;

import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.admin.mapper.AdminMapper;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AdminService(AdminMapper adminMapper, JavaMailSender javaMailSender) {
        this.adminMapper = adminMapper;
        this.javaMailSender = javaMailSender;
    }

    // mailDTO 객체를 데이터베이스에 저장합니다.
    // adminMapper를 사용하여 모든 사용자 이메일 주소를 조회합니다.
    // 조회된 이메일 주소를 콘솔에 출력하여 확인합니다.
    // 필요시 이메일 내용을 정리합니다 (cleanHtml 메서드 사용).
    // 모든 사용자에게 이메일을 전송합니다 (sendEmail 메서드 사용).
    public void saveMailAndSendToAllUsers(MailDTO mailDTO) {
        // Save mail to the database
        adminMapper.insertMail(mailDTO);

        // Retrieve all user emails
        List<String> userEmails = adminMapper.selectAllUserEmails();

        // Print the list of retrieved user emails
        System.out.println("Retrieved user emails:");
        for (String email : userEmails) {
            System.out.println(email); // 각 이메일 주소를 줄바꿈하여 출력
        }

        // 이메일 내용 정리 (필요하다면)
        String cleanedContent = cleanHtml(mailDTO.getContent());

        // Send email to each user
        for (String email : userEmails) {
            System.out.println("Sending email to: " + email); // 디버깅을 위한 로그 추가
            sendEmail(email, mailDTO.getTitle(), cleanedContent);
        }
    }

    // MimeMessage 객체를 생성하여 이메일 메시지를 만듭니다.
    // MimeMessageHelper를 사용하여 이메일 수신자, 제목, 내용을 설정합니다. 두 번째 인자를 true로 설정하여 HTML 형식으로 보냅니다.
    // javaMailSender를 사용하여 이메일을 전송합니다.
    // 전송된 이메일 주소를 콘솔에 출력하여 확인합니다.
    // 예외가 발생할 경우 스택 트레이스를 출력합니다.
    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // 두 번째 인자를 true로 설정하여 HTML 형식으로 보냄
            javaMailSender.send(message);
            System.out.println("Email sent to: " + to); // 디버깅을 위한 로그 추가
        } catch (MessagingException e) {
            e.printStackTrace(); // 에러 발생 시 스택 트레이스 출력
        }
    }

    private String cleanHtml(String content) {
        return content.replaceAll("<p></p>", ""); // 빈 <p></p> 태그 제거
    }


    // AdminMapper를 사용하여 모든 사용자 정보를 조회하여 반환합니다.
    // 이 클래스는 기본적으로 이메일 전송과 사용자 조회 기능을 제공합니다.
    // 이메일 전송 시 HTML 형식을 지원하기 위해 MimeMessage와 MimeMessageHelper를 사용합니다.
    // 예외 처리를 통해 오류 발생 시 디버깅을 용이하게 합니다.
    public List<JoinDTO> allMembers() {
        return adminMapper.selectAllUsers();
    }
}
