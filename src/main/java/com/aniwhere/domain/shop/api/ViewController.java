package com.aniwhere.domain.shop.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller("shopViewController")
@RequestMapping("/shop")
public class ViewController {



    @GetMapping("/cart")
    public String cart()  {return "animall/shop-cart";
    }

    @GetMapping("/agreement")
    public String getAgreement(Model model) {
        Path filePath = Paths.get("src/main/resources/static/documents/mall_agreement.txt");
        try {
            String content = Files.readString(filePath);
            model.addAttribute("agreementText", content);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("agreementText", "파일을 읽을 수 없습니다.");
        }
        return "agreement";
}
