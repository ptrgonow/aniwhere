package com.aniwhere.domain.shop.product.service;

import com.aniwhere.domain.shop.mapper.ShopMapper;
import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
@AllArgsConstructor
public class NaverShoppingService {

    private final ShopMapper shopMapper;
    private final ObjectMapper objectMapper;
    private final Environment env;

    @Scheduled(fixedRate = 3600000) // 매시간 실행
    public void searchAndSaveProducts() {
        String clientId = env.getProperty("naver.client.id");
        String clientSecret = env.getProperty("naver.client.secret");

        String query = "고양이";
        int display = 100;

        for (int start = 1; start <= 1000; start += 100) {
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format("https://openapi.naver.com/v1/search/shop.json?query=%s&start=%d&display=%d", query, start, display);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String jsonResponse = response.getBody();

            try {
                JsonNode items = objectMapper.readTree(jsonResponse).get("items");

                for (JsonNode item : items) {
                    ProductDTO product = new ProductDTO();
                    product.setName(item.get("title").asText());
                    product.setLink(item.get("link").asText());
                    product.setImage(item.get("image").asText());
                    product.setPrice(item.get("lprice").asText());
                    product.setHprice(item.has("hprice") ? item.get("hprice").asText() : null);
                    product.setBrand(item.get("brand").asText());
                    product.setMaker(item.get("maker").asText());
                    product.setCategory1(item.get("category1").asText());
                    product.setCategory2(item.get("category2").asText());
                    product.setCategory3(item.get("category3").asText());
                    product.setCategory4(item.get("category4").asText());
                    product.setNaverProductId(item.get("productId").asText());

                    shopMapper.saveProduct(product);
                }
            } catch (Exception e) {
                System.out.println("api 통신 실패" + e.getMessage());
            }
        }
    }
}
