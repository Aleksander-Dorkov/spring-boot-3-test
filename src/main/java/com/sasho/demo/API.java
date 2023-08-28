package com.sasho.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class API {
    @GetMapping("/")
    public String greeting() {
        return "hello world";
    }
}
