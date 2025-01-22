package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "error"; // имя шаблона, например, error.html
    }
}
