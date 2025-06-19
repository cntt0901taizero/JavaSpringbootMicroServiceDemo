package com.taichu.camundaapiserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CamundaApiController {
    @GetMapping("test")
    @ResponseStatus(HttpStatus.OK)
    public boolean test() {
        return true;
    }
}
