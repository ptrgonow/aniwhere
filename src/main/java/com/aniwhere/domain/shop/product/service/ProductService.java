package com.aniwhere.domain.shop.product.service;

import com.aniwhere.domain.shop.mapper.ShopMapper;
import com.aniwhere.domain.shop.product.domain.Product;
import com.aniwhere.domain.shop.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ShopMapper shopMapper;

    public int getTotal(){
        return shopMapper.getTotalProductCount();
    }

    public List<Product> findAllProductsWithLimit(int limit, int offset) {
        return shopMapper.findAllProductsWithLimit(limit, offset);
    }



    public Product findProductById(Integer productId) {
        return shopMapper.findProductById(productId);
    }

    public void saveProduct(ProductDTO product) {
        shopMapper.saveProduct(product);
    }

    public void updateProduct(ProductDTO product) {
        shopMapper.updateProduct(product);
    }

    public void deleteProduct(Integer productId) {
        shopMapper.deleteProduct(productId);
    }
}
