package com.durlabh.codes.secure_boot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/secure")
    public String secureHelloWorld() {
        return "Hello World But Secured";
    }
}
