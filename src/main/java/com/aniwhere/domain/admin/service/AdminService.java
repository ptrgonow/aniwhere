package com.aniwhere.domain.admin.service;

import com.aniwhere.domain.admin.dto.ChartDTO;
import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.admin.dto.UploadImgDTO;
import com.aniwhere.domain.admin.mapper.AdminMapper;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.product.domain.Product;
import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.aniwhere.domain.shop.review.domain.Review;
import com.aniwhere.domain.user.join.domain.Join;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.join.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final JavaMailSender javaMailSender;
    private final MailService mailService;

    @Autowired
    public AdminService(AdminMapper adminMapper, JavaMailSender javaMailSender, MailService mailService) {
        this.adminMapper = adminMapper;
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
    }
  
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

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // 두 번째 인자를 true로 설정하여 HTML 형식으로 보냄
            javaMailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (MessagingException e) {
            System.out.println("이메일 전송 실패 : " + to);
        }
    }

    public List<JoinDTO> allMembers(int limit, int offset) {
        return adminMapper.selectAllUsers(limit, offset);
    }

    public List<JoinDTO> selectAllShopUsers(int limit, int offset){
        return adminMapper.selectAllShopUsers(limit, offset);
    }

    public int memberCount( ) {
        return adminMapper.userCount();
    }

    public int shopUserCount(){
        return adminMapper.shopUserCount();
    }

    public List<JoinDTO> emptyAdressUsers(int limit, int offset) {
        return adminMapper.emptyAdressUsers(limit, offset);
    }

    public List<JoinDTO> emptyPhoneUsers(int limit, int offset) {
        return adminMapper.emptyPhoneUsers(limit, offset);
    }

    public int countAddUsers( ) {
        return adminMapper.countEmptyAddressUsers();
    }

    public int countPhoneUsers( ) {
        return adminMapper.countEmptyPhoneUsers();
    }

    // 회원아이디 검색 및 페이징 처리
    public Map<String, Object> searchUser(String userId, int limit, int offset) {
        List<JoinDTO> users;
        int totalUsers;

        if (userId == null || userId.isEmpty()) {
            users = adminMapper.selectAllUsers(limit, offset);
            totalUsers = adminMapper.userCount();
        } else {
            users = adminMapper.findUserByUserId(userId, limit, offset);
            totalUsers = adminMapper.countByUserId(userId);
        }

        int totalPages = (int) Math.ceil((double) totalUsers / limit);
        int currentPage = offset / limit + 1;

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("totalPages", totalPages);
        response.put("currentPage", currentPage);

        return response;
    }

    // 이미지 업로드 메서드
    public UploadImgDTO uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new IOException("Invalid file path: " + fileName);
        }

        String uploadDir = "src/main/resources/static/images/";
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path copyLocation = Paths.get(uploadDir + File.separator + uniqueFileName);
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        String fileDownloadUri = "/images/" + uniqueFileName;
        UploadImgDTO response = new UploadImgDTO();
        response.setUrl(fileDownloadUri);
        return response;
    }

    // 이메일 전송 메서드
    public void sendMailWithImage(String to, String subject, String body, String fileName) throws MessagingException {
        String imageUrl = "http://localhost:8543/images/" + fileName;
        mailService.sendEmailWithImage(to, subject, body, imageUrl);
    }

    private String cleanHtml (String content){
        return content.replaceAll("<p></p>", ""); // 빈 <p></p> 태그 제거
    }

    public List<OrderSucDTO> allOrders(int limit, int offset) {
        return adminMapper.selectAllOrders(limit, offset);
    }

    public OrderSucDTO findOrderById(String orderId) {
        return adminMapper.selectOrderById(orderId);
    }

    public int countOrders( ) {
        return adminMapper.countOrders();
    }

    public List<OrderDetailDTO> findDetailsById(String orderId) {
        return adminMapper.selectOrderDetailByOrderId(orderId);
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        adminMapper.updateOrderStatus(orderId, newStatus);
    }

    public List<ProductDTO> getAllProducts() {
       return adminMapper.selectAllProducts();
    }

    public List<ChartDTO> getYearChartData( ) {
        return adminMapper.selectYearChartData();
    }

    public List<ChartDTO> getMonthChartData( ) {
        return adminMapper.selectMonthChartData();
    }

    public List<ChartDTO> getYearOnYearChartData( ) {
        return adminMapper.selectYearOnYearChartData();
    }

    public void addProduct(ProductDTO productDTO) {

        Product product = Product.fromDTO(productDTO);

        adminMapper.insertProduct(product);

    }
    public List<Product> searchProducts(String keyword, int limit, int offset) {
        return adminMapper.searchProducts(keyword, limit, offset);
    }

    public int getTotalSearchResults(String keyword) {
        return adminMapper.getTotalSearchResults(keyword);
    }

    public ProductDTO getProductById(Integer productId) {
        return adminMapper.selectProductById(productId);
    }

    public void editProduct(ProductDTO productDTO) {

        Product product = Product.fromDTO(productDTO);

        adminMapper.updateProduct(product);

    }
}
