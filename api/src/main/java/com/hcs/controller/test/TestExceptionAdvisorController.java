package com.hcs.controller.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestExceptionAdvisorController {

    @GetMapping("/number/{id}")
    public int NumberFormatException(@PathVariable("id") String testStr) {
        return Integer.parseInt(testStr);
    }
}

