package com.gmail.merikbest2015.ecommerce.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoryController
{

    // Định tuyến cho đường dẫn /our-story
    @GetMapping("/our-story")
    public String ourStoryPage() {
        return "our-story";  // Trả về trang our-story.html
    }
}
