package com.aniwhere.domain.route.controller;

import com.aniwhere.global.config.util.LoggingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
    ProxyController 클래스는 프록시 역할을 하는 REST 컨트롤러입니다.
    클라이언트의 요청을 받아 대신 다른 서버에 요청을 보내고 그 응답을 반환합니다.
 */
@RestController
public class ProxyController {

    private final LoggingUtil loggingUtil;

    @Autowired
    public ProxyController(LoggingUtil loggingUtil) {
        this.loggingUtil = loggingUtil;
    }

    /**
        이 메서드는 프록시 역할을 합니다.
        프록시는 클라이언트가 직접 다른 서버에 요청하지 않고, 중간에 서버가 대신 요청을 보내주는 역할을 합니다.
        여기서는 클라이언트가 제공한 URL로 GET 요청을 보내고,응답을 클라이언트에게 반환합니다.

        @param urlString 클라이언트가 요청할 URL
        @return URL의 응답 데이터
     */
    @GetMapping(value = "/proxy.json")
    public byte[] html2canvasProxy(@RequestParam("url") String urlString) {
        byte[] data = null;

        try {
            URL url = new URL(URLDecoder.decode(urlString, StandardCharsets.UTF_8));
            data = fetchUrlData(url);
        } catch (MalformedURLException e) {
            loggingUtil.logError(this.getClass(), "잘못된 URL 형식: " + urlString, e);
            data = "잘못된 URL 형식입니다.".getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            loggingUtil.logError(this.getClass(), "예기치 않은 예외 발생", e);
            data = "예기치 않은 예외가 발생했습니다.".getBytes(StandardCharsets.UTF_8);
        }
        return data;
    }

    /**
        주어진 URL 에서 데이터를 가져옵니다.

        @param url 데이터를 가져올 URL
        @return URL의 응답 데이터
     */
    private byte[] fetchUrlData(URL url) {
        byte[] data = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                try (InputStream is = connection.getInputStream()) {
                    data = is.readAllBytes();
                }
            } else {
                loggingUtil.logInfo(this.getClass(), "데이터를 가져오지 못했습니다. 응답 코드: " + responseCode);
                data = ("데이터를 가져오지 못했습니다. 응답 코드: " + responseCode).getBytes(StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            loggingUtil.logError(this.getClass(), "입출력 예외 발생", e);
            data = "입출력 예외가 발생했습니다.".getBytes(StandardCharsets.UTF_8);
        }
        return data;
    }
}
