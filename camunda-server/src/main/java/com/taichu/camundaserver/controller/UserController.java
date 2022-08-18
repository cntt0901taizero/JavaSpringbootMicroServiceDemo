package com.taichu.camundaserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("test")
    @ResponseStatus(HttpStatus.OK)
    public boolean test() {
        return true;
    }
}
