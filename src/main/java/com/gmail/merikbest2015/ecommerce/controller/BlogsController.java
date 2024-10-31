package com.gmail.merikbest2015.ecommerce.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogsController
{

    // Định tuyến cho đường dẫn /our-story
    @GetMapping("/blogs")
    public String blogsPage() {
        return "blogs";  // Trả về trang our-story.html
    }
}
