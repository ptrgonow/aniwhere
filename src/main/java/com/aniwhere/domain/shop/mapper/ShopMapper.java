package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.aniwhere.domain.shop.product.domain.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopMapper {

    @Select("SELECT COUNT(*) FROM product")
    int getTotalProductCount();

    @Select("SELECT product_id as productId, name, image, category, price, quantity, created_at as createdAt FROM product LIMIT #{limit} OFFSET #{offset}")
    List<Product> findAllProductsWithLimit(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT product_id as productId, name, image, category, price, quantity, created_at as createdAt FROM product WHERE category LIKE '%강아지%' and category not like '%고양이%' LIMIT #{limit} OFFSET #{offset}")
    List<Product> findDogProducts(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT product_id as productId, name, image, category, price, quantity, created_at as createdAt FROM product WHERE category LIKE '%고양이%' and category not like '%강아지%' LIMIT #{limit} OFFSET #{offset}")
    List<Product> findCatProducts(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT product_id as productId, name, image, category, price, quantity, created_at as createdAt FROM product WHERE category LIKE '%공용%' LIMIT #{limit} OFFSET #{offset}")
    List<Product> findOtherProducts(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT product_id AS productId, name AS name, image AS image, price AS price, detail_url AS detailUrl, category AS category FROM product WHERE product_id = #{productId}")
    Product findProductById(Integer productId);

    @Insert("INSERT IGNORE INTO product(name, image, price, category) " +
            "VALUES (#{name}, #{image}, #{price}, #{category})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void saveProduct(ProductDTO product);

    @Update("UPDATE product SET name = #{name}, image = #{image}, price = #{price}, " +
            "category = #{category} WHERE product_id = #{productId}")
    void updateProduct(ProductDTO product);

    @Delete("DELETE FROM product WHERE product_id = #{productId}")
    void deleteProduct(Integer productId);

    @Select("SELECT EXISTS(SELECT 1 FROM cart WHERE user_id = #{userId} AND product_id = #{productId})")
    boolean existsCartItem(@Param("userId") String userId, @Param("productId") Integer productId);

    @Select("SELECT price FROM product WHERE product_id = #{productId}")
    String getProductPriceById(Integer productId);

    @Insert("INSERT INTO cart (user_id, product_id, quantity, total_price) VALUES (#{userId}, #{productId}, #{quantity}, #{totalPrice})")
    void insertCartItem(Cart cart);

    @Select("SELECT SUM(total_price) FROM cart WHERE user_id = #{userId} AND checked = 'Y'")
    Integer getTotalOrderPrice(String userId);

    @Select("SELECT c.cart_id as cartId, c.quantity, p.product_id as productId, c.user_id as userId, p.name, p.image, p.price, c.checked, c.total_price as totalPrice FROM cart c JOIN product p ON c.product_id = p.product_id WHERE c.user_id = #{userId}")
    List<Cart> getCartItemsByUserId(String userId);

    @Select("SELECT * FROM cart WHERE cart_id = #{cartId}")
    Cart getCartItemById(Integer cartId);

    @Update("UPDATE cart SET quantity = #{quantity}, total_price = #{totalPrice} WHERE cart_id = #{cartId}")
    void updateCartItemQuantity(@Param("cartId") Integer cartId, @Param("quantity") Integer quantity, @Param("totalPrice") Integer totalPrice);

    @Update("UPDATE cart SET checked = #{checked} WHERE cart_id = #{cartId}")
    void updateCartItemChecked(@Param("cartId") Integer cartId, @Param("checked") String checked);

    @Delete("DELETE FROM cart WHERE cart_id = #{cartId}")
    void deleteCartItem(Integer cartId);

    @Select("SELECT COUNT(*) FROM product WHERE category LIKE '%강아지%' and category not like '%고양이%'")
    int getTotalDogProductCount( );

    @Select("SELECT COUNT(*) FROM product WHERE category LIKE '%고양이%' and category not like '%강아지%'")
    int getTotalCatProductCount( );

    @Select("SELECT COUNT(*) FROM product WHERE category LIKE '%공용%'")
    int getTotalOtherProductCount( );

    @Select("SELECT product_id as productId, name, image, category, price, created_at as createdAt, quantity FROM product WHERE name LIKE CONCAT('%', #{keyword}, '%') LIMIT #{limit} OFFSET #{offset}")
    List<Product>searchProducts(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM product WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    int getTotalSearchResults(@Param("keyword") String keyword);

}
