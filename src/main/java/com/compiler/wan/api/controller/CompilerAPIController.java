package com.compiler.wan.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CompilerAPIController {

    @PostMapping("/compile")
    public ResponseEntity<String> compileCode(@RequestBody String code) {
        return null;
    }

}
