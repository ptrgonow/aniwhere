package com.aniwhere.domain.admin.mapper;

import com.aniwhere.domain.admin.dto.ChartDTO;
import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.product.domain.Product;
import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("SELECT id, user_id AS userId, email, user_name AS userName, address, detail_address AS detailAddress, phone, created_at AS createdAt " +
            "FROM user WHERE user_id LIKE CONCAT('%', #{userId}, '%') ORDER BY user_id DESC LIMIT #{limit} OFFSET #{offset}")
    List<JoinDTO> findUserByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM user WHERE user_id LIKE CONCAT('%', #{userId}, '%')")
    int countByUserId(@Param("userId") String userId);

    @Select("SELECT id, user_id as userId, email, user_name as userName, address, detail_address as detailAddress, " +
            "zip_code as zipCode, phone, created_at as createdAt from user order by userId DESC LIMIT #{limit} OFFSET #{offset}")
    List<JoinDTO> selectAllUsers(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM user")
    int userCount( );

    @Insert("INSERT INTO user_mail (title, content) VALUES (#{title}, #{content})")
    void insertMail(MailDTO mailDTO);

    @Select("SELECT id, user_id AS userId, email, user_name AS userName, address, detail_address AS detailAddress, phone, created_at AS createdAt " +
            "FROM user WHERE (detail_address IS NULL OR detail_address = '') OR (address IS NULL OR address = '') " +
            "ORDER BY user_id DESC LIMIT #{limit} OFFSET #{offset}")
    List<JoinDTO> emptyAdressUsers(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT id, user_id AS userId, email, user_name AS userName, address, detail_address AS detailAddress, phone, created_at AS createdAt " +
            "FROM user WHERE phone IS NULL ORDER BY user_id DESC LIMIT #{limit} OFFSET #{offset}")
    List<JoinDTO> emptyPhoneUsers(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM user WHERE (detail_address IS NULL OR detail_address = '') OR (address IS NULL OR address = '')")
    int countEmptyAddressUsers( );

    // ROLE_USER 에게만 메일 보내짐
    @Select("SELECT email FROM user WHERE role = 'ROLE_USER'")
    List<String> selectAllUserEmails();

    @Select("SELECT COUNT(*) FROM user WHERE phone IS NULL")
    int countEmptyPhoneUsers( );

    @Select("SELECT order_id as orderId, user_id as userId, shipping_address1 as shippingAddress1, " +
            "shipping_address2 as shippingAddress2, shipping_address3 as shippingAddress3, " +
            "amount, order_status as orderStatus, order_date as orderDate, recipient_name as recipientName, " +
            "recipient_phone as recipientPhone, order_request as orderRequest from order_success order by" +
            " order_date desc LIMIT #{limit} OFFSET #{offset}")
    List<OrderSucDTO> selectAllOrders(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT os.order_id AS orderId, " +
            "       os.user_id AS userId, " +
            "       u.user_name as userName, " +
            "       u.is_social as isSocial, " +
            "       os.shipping_address1 AS shippingAddress1, " +
            "       os.shipping_address2 AS shippingAddress2, " +
            "       os.shipping_address3 AS shippingAddress3, " +
            "       os.amount, os.order_status AS orderStatus, os.order_date AS orderDate, os.recipient_name AS recipientName, " +
            "       os.recipient_phone AS recipientPhone, os.order_request AS orderRequest " +
            "FROM order_success os " +
            "JOIN user u ON os.user_id = u.user_id " +
            "WHERE os.order_id = #{orderId} " +
            "ORDER BY os.payment_key DESC " +
            "LIMIT 1;")
    OrderSucDTO selectOrderById(@Param("orderId") String orderId);

    @Select("SELECT COUNT(*) FROM order_success")
    int countOrders();

    @Select("SELECT od.product_id AS productId, od.quantity, p.price, p.name, p.image " +
            "FROM order_detail od " +
            "JOIN product p ON od.product_id = p.product_id " +
            "WHERE od.order_id = #{orderId}")
    List<OrderDetailDTO> selectOrderDetailByOrderId(@Param("orderId") String orderId);

    @Update("UPDATE order_success SET order_status = #{newStatus} WHERE order_id = #{orderId}")
    void updateOrderStatus(@Param("orderId") String orderId, @Param("newStatus") String newStatus);

    @Select("SELECT product_id as prodcutId, name, image, price, detail_url as detailUrl, category, " +
            "created_at as createdAt, updated_at as updatedAt, quantity FROM product limit 9")
    List<ProductDTO> selectAllProducts();

    @Select("SELECT DATE_FORMAT(order_date, '%Y-%m') AS date, SUM(amount) AS amount " +
            "FROM order_success " +
            "GROUP BY DATE_FORMAT(order_date, '%Y-%m')")
    List<ChartDTO> selectYearChartData();

    @Select("SELECT DATE_FORMAT(order_date, '%Y-%m-%d') AS date, SUM(amount) AS amount " +
            "FROM order_success " +
            "WHERE DATE_FORMAT(order_date, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m') " +
            "GROUP BY DATE_FORMAT(order_date, '%Y-%m-%d');")
    List<ChartDTO> selectMonthChartData();

    @Select("SELECT DATE_FORMAT(order_date, '%Y') AS date, SUM(amount) AS amount " +
            "FROM order_success " +
            "GROUP BY DATE_FORMAT(order_date, '%Y')")
    List<ChartDTO> selectYearOnYearChartData();

    @Insert("INSERT INTO product (name, image, price, detail_url, category, quantity) VALUES " +
            "(#{name}, #{image}, #{price}, #{detailUrl}, #{category}, #{quantity})")
    void insertProduct(Product product);

    @Select("SELECT product_id as productId, name, image, detail_url AS detailUrl, category, price, created_at as createdAt, quantity FROM product WHERE name LIKE CONCAT('%', #{keyword}, '%') LIMIT #{limit} OFFSET #{offset}")
    List<Product>searchProducts(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM product WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    int getTotalSearchResults(@Param("keyword") String keyword);

    @Select("SELECT product_id as productId, name, image, price, detail_url as detailUrl, category, " +
            "quantity FROM product WHERE product_id = #{productId}")
    ProductDTO selectProductById(@Param("productId") Integer productId);

    @Update("UPDATE product SET name = #{name}, image = #{image}, price = #{price}, detail_url = #{detailUrl}, category = #{category}, quantity = #{quantity} WHERE product_id = #{productId}")
    void updateProduct(Product product);

    @Delete("DELETE FROM product WHERE product_id = #{productId}")
    void deleteProduct(@Param("productId") Integer productId);
}
