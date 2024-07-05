package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.aniwhere.domain.shop.product.domain.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopMapper {

    @Select("SELECT * FROM product")
    List<Product> findAllProducts();

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
