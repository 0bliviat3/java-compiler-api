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

        //TODO: 권한체크 필요
        /*
        권한에 따라서 admin 인경우 memory 크기,시간제한 설정 가능하도록 수정
         */

        log.info("code: " + content.getCode());

        ExecutionContent result = compilerService.compileCode(content);

        if (isERROR(result.getStatus())) {
            log.info("compile ERROR");
            return result;
        }

        return compilerService.runCode(result);
    }

}
