package com.aniwhere.domain.shop.product.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController("productRestController")
public class ProductRestController {
    /* 뭐가 안되는 걸까..*/
    @GetMapping("/agreement")
    public String getAgreement(Model model) {
        Path filePath = Paths.get("src/main/resources/static/document/shop_agreement.txt");
        try {
            String content = Files.readString(filePath);
            model.addAttribute("agreementText", content);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("agreementText", "파일을 읽을 수 없습니다.");
        }
        return "agreement";
    }
}
