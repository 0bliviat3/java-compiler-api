package com.compiler.wan.api.service;

import com.compiler.wan.api.domain.ExecutionContent;

public interface CompilerService {

    public ExecutionContent compileCode(String code);

    public ExecutionContent runCode(ExecutionContent executionContent);

}
