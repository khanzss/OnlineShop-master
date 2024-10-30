package com.gmail.merikbest2015.ecommerce.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController
{

    // Định tuyến cho trang chủ

    // Định tuyến cho đường dẫn /our-story
    @GetMapping("/contacts")
    public String contactPage() {
        return "contacts";  // Trả về trang our-story.html
    }
}
