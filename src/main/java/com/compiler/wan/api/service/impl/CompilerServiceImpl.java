package com.compiler.wan.api.service.impl;

import com.compiler.wan.api.config.ExecutionStatus;
import com.compiler.wan.api.domain.ExecutionContent;
import com.compiler.wan.api.service.CompilerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class CompilerServiceImpl implements CompilerService {
    @Override
    public ExecutionContent compileCode(ExecutionContent executionContent) {

        Path defaultPath = Paths.get("tmp"); //TODO: property 처리

        try {

            String tmpDict = "tmp_"+ UUID.randomUUID().toString(); //TODO: 상수처리
            Path tmpPath = defaultPath.resolve(tmpDict);
            Files.createDirectories(tmpPath);


            // 임시 파일에 Java 소스 저장
            Path tempSourceFile = tmpPath.resolve("Main.java"); //TODO: 상수처리
            log.info("fileName: " + tempSourceFile.getFileName().toString());

            Files.write(tempSourceFile, executionContent.getCode().getBytes());

            // 컴파일러 설정
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler
                    .getStandardFileManager(diagnostics, null, null);

            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjects(tempSourceFile.toFile());
            JavaCompiler.CompilationTask task = compiler
                    .getTask(null, fileManager, diagnostics, null, null, compilationUnits);

            // 컴파일 실행
            boolean isSuccess = task.call();
            fileManager.close();

            if (isSuccess) {
                executionContent.setCompilePath(tmpPath.toString());
            } else {
                executionContent.setStatus(ExecutionStatus.COMPILE_ERROR);

                // 컴파일 오류 처리
                StringBuilder errorMessages = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    errorMessages.append(diagnostic.getMessage(null)).append("\n");
                }
                executionContent.setMessage(errorMessages.toString());
            }

        } catch (Exception e) {
            executionContent.setStatus(ExecutionStatus.SERVER_ERROR);
            executionContent.setMessage(e.getMessage());
        }

        return executionContent;
    }

    @Override
    public ExecutionContent runCode(ExecutionContent executionContent) {

        Path classFilePath = Paths.get(executionContent.getCompilePath());

        log.info(classFilePath.toString());

        try {

            //클래스 실행
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "java", "-cp", classFilePath.toString(), "Main"); //TODO: 상수처리
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 실행 결과 읽기
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            executionContent.setMessage(output.toString());

            if (exitCode == 0) {
                executionContent.setStatus(ExecutionStatus.SUCCESS);
            } else {
                executionContent.setStatus(ExecutionStatus.EXCEPTION);
            }
        } catch (Exception e) {
            executionContent.setStatus(ExecutionStatus.SERVER_ERROR);
            executionContent.setMessage(e.getMessage());
        }

        //TODO: 임시 디렉토리 삭제


        return executionContent;
    }
}
