package com.aniwhere.domain.admin.service;

import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.admin.mapper.AdminMapper;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    public void saveMail(MailDTO mailDTO) {
        // Save mail to the database
        adminMapper.insertMail(mailDTO);

        // Retrieve all user emails
        List<String> userEmails = adminMapper.selectAllUserEmails();

        // Send email to each user
        for (String email : userEmails) {
            sendEmail(email, mailDTO.getTitle(), mailDTO.getContent());
        }
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 에러 출력
            // 에러 처리 로직 추가
        }
    }

    public List<JoinDTO> allMembers(int limit, int offset) {
        return adminMapper.selectAllUsers(limit, offset);
    }

    public int memberCount(){
        return adminMapper.userCount();
    }

    public List<JoinDTO> emptyAdressUsers(int limit, int offset){
        return adminMapper.emptyAdressUsers(limit, offset);
    }

    public List<JoinDTO> emptyPhoneUsers(int limit, int offset){
        return adminMapper.emptyPhoneUsers(limit, offset);
    }

    public int countAddUsers(){
        return adminMapper.countEmptyAddressUsers();
    }

    public int countPhoneUsers(){
        return adminMapper.countEmptyPhoneUsers();
    }


}
