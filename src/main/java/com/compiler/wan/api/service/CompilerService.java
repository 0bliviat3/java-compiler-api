package com.compiler.wan.api.service;

import com.compiler.wan.api.domain.ExecutionContent;

public interface CompilerService {

    public ExecutionContent compileCode(ExecutionContent executionContent);

    public ExecutionContent runCode(ExecutionContent executionContent);

    public void deleteTmpFile(ExecutionContent executionContent);

}
