package com.aniwhere.global.config.util;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errors = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        int status = (int) errors.getOrDefault("status", 500);

        if (status == 403) {
            return "error/403-error";
        } else if (status == 404) {
            return "error/404-error";
        } else {
            return "error/500-error";
        }
    }

    @GetMapping("/error/403")
    public String error403Page() {
        return "error/403-error";
    }

    @GetMapping("/error/404")
    public String error404Page() {
        return "error/404-error";
    }

    @GetMapping("/error/500")
    public String error500Page() {
        return "error/500-error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
