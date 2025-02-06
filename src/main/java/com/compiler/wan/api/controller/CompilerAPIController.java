package com.compiler.wan.api.controller;

import com.compiler.wan.api.domain.ExecutionContent;
import com.compiler.wan.api.service.CompilerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.compiler.wan.api.config.ExecutionStatus.isERROR;

@RestController
@RequestMapping("/api")
@Slf4j
public class CompilerAPIController {

    private final CompilerService compilerService;

    @Autowired
    public CompilerAPIController(final CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/compile")
    public ExecutionContent compileCode(@RequestBody ExecutionContent content) {

        log.info("code: " + content.getCode());

        ExecutionContent result = compilerService.compileCode(content);

        if (isERROR(result.getStatus())) {
            log.info("compile ERROR");
            return result;
        }

        return compilerService.runCode(result);
    }

}
