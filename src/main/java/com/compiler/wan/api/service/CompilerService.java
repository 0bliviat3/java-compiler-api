package com.compiler.wan.api.service;

import com.compiler.wan.api.domain.ExecutionResult;

public interface CompilerService {

    public ExecutionResult compileCode(String code);

    public ExecutionResult runCode(ExecutionResult executionResult);

}
