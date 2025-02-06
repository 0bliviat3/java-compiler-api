package com.compiler.wan.api.service.impl;

import com.compiler.wan.api.domain.ExecutionResult;
import com.compiler.wan.api.service.CompilerService;
import org.springframework.stereotype.Service;

@Service
public class CompilerServiceImpl implements CompilerService {
    @Override
    public ExecutionResult compileCode(String code) {
        return null;
    }

    @Override
    public ExecutionResult runCode(ExecutionResult executionResult) {
        return null;
    }
}
