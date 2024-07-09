package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.aniwhere.domain.shop.product.domain.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopMapper {

    @Select("SELECT COUNT(*) FROM product")
    int getTotalProductCount();

    @Select("SELECT product_id as productId, name, image, category3, link, price FROM product LIMIT #{limit} OFFSET #{offset}")
    List<Product> findAllProductsWithLimit(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT product_id as productId, name, image, category3, link, price FROM product WHERE category3 LIKE '%강아지%' LIMIT #{limit} OFFSET #{offset}")
    List<Product> findDogProducts(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT product_id as productId, name, image, category3, price, link FROM product WHERE category3 LIKE '%고양이%' LIMIT #{limit} OFFSET #{offset}")
    List<Product> findCatProducts(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT product_id as productId, name, image, category3, link, price FROM product WHERE category3 NOT LIKE '%강아지%' AND name NOT LIKE '%고양이%' LIMIT #{limit} OFFSET #{offset}")
    List<Product> findOtherProducts(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM product WHERE product_id = #{productId}")
    Product findProductById(Integer productId);

    @Insert("INSERT IGNORE INTO product(name, link, image, price, hprice, brand, maker, category1, category2, category3, category4, naver_product_id) " +
            "VALUES (#{name}, #{link}, #{image}, #{price}, #{hprice}, #{brand}, #{maker}, #{category1}, #{category2}, #{category3}, #{category4}, #{naverProductId})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void saveProduct(ProductDTO product);

    @Update("UPDATE product SET name = #{name}, link = #{link}, image = #{image}, price = #{price}, hprice = #{hprice}, brand = #{brand}, " +
            "maker = #{maker}, category1 = #{category1}, category2 = #{category2}, category3 = #{category3}, category4 = #{category4}, naver_product_id = #{naverProductId} WHERE product_id = #{productId}")
    void updateProduct(ProductDTO product);

    @Delete("DELETE FROM product WHERE product_id = #{productId}")
    void deleteProduct(Integer productId);
}
