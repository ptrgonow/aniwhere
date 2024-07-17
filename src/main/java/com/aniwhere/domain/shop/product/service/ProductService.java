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

    public int getTotalDogProducts() {
        return shopMapper.getTotalDogProductCount();
    }

    public int getTotalCatProducts() {
        return shopMapper.getTotalCatProductCount();
    }

    public int getTotalOtherProducts() {
        return shopMapper.getTotalOtherProductCount();
    }

    public List<Product> findAllProductsWithLimit(int limit, int offset) {
        return shopMapper.findAllProductsWithLimit(limit, offset);
    }

    public List<Product> findDog(int limit, int offset){
        return shopMapper.findDogProducts(limit, offset);
    }

    public List<Product> findCat(int limit, int offset){
        return shopMapper.findCatProducts(limit, offset);
    }

    public List<Product> findOthers(int limit, int offset){
        return shopMapper.findOtherProducts(limit, offset);
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

    public List<Product> searchProducts(String keyword, int limit, int offset) {
        return shopMapper.searchProducts(keyword, limit, offset);
    }

    public int getTotalSearchResults(String keyword) {
        return shopMapper.getTotalSearchResults(keyword);
    }

}
