package com.durlabh.codes.secure_boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FormController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/secure")
    public String secureHelloWorld() {
        return "secure.html";
    }

}
