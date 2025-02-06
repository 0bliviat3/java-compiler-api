package com.compiler.wan.api.controller;

import com.compiler.wan.api.config.ExecutionStatus;
import com.compiler.wan.api.domain.ExecutionResult;
import com.compiler.wan.api.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CompilerAPIController {

    private final CompilerService compilerService;

    @Autowired
    public CompilerAPIController(final CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/compile")
    public ExecutionResult compileCode(@RequestBody String code) {
        ExecutionResult result = compilerService.compileCode(code);

        if (result.getStatus().equals(ExecutionStatus.COMPILE_ERROR)) {
            return result;
        }

        return compilerService.runCode(result);
    }

}
