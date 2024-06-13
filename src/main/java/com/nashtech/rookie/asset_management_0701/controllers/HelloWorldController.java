package com.nashtech.rookie.asset_management_0701.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello-world")
public class HelloWorldController {

    @GetMapping()
    public String helloWorld() {
        return "Hello World";
    }


}
