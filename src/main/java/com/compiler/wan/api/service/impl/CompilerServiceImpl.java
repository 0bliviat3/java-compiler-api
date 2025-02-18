package com.compiler.wan.api.service.impl;

import com.compiler.wan.api.config.ExecutionStatus;
import com.compiler.wan.api.domain.ExecutionContent;
import com.compiler.wan.api.service.CompilerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.compiler.wan.api.config.JCompileConfig.*;

@Service
@Slf4j
public class CompilerServiceImpl implements CompilerService {
    @Override
    public ExecutionContent compileCode(ExecutionContent executionContent) {

        Path defaultPath = Paths.get(DEFAULT_PATH.getVal());

        try {
            String tmpDict = DICT_PREFIX.getVal()+ UUID.randomUUID().toString();
            Path tmpPath = defaultPath.resolve(tmpDict);
            Files.createDirectories(tmpPath);

            // 임시 파일에 Java 소스 저장
            Path tempSourceFile = tmpPath.resolve(MAIN_FILE.getVal());
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
                    errorMessages
                            .append(diagnostic.getMessage(null))
                            .append(System.lineSeparator());
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
                    JAVA.getVal(),
                    String.format(XMX.getVal(),
                            Optional.ofNullable(executionContent.getMaxMemory())
                            .orElse(Integer.parseInt(DEFAULT_MEMORY.getVal()))),
                    CP.getVal(), classFilePath.toString(),
                    MAIN.getVal()
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 입력처리
            if (executionContent.getInputFile() != null) {
                readFile(executionContent, process);
            }

            // 실행 결과 읽기
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder output = new StringBuilder();
            String line;

            boolean isFinish = process.waitFor(
                    Optional.ofNullable(executionContent.getMaxTime())
                            .orElse(Integer.parseInt(DEFAULT_TIME.getVal())), TimeUnit.SECONDS);

            if (isFinish) {
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                int exitCode = process.exitValue();

                executionContent.setMessage(output.toString());

                if (exitCode == 0) {
                    executionContent.setStatus(ExecutionStatus.SUCCESS);
                } else {
                    executionContent.setStatus(ExecutionStatus.EXCEPTION);
                }
            } else {
                executionContent.setStatus(ExecutionStatus.TIME_OUT);
                process.destroy();
            }
        } catch (Exception e) {
            executionContent.setStatus(ExecutionStatus.SERVER_ERROR);
            executionContent.setMessage(e.getMessage());
        }

        deleteTmpFile(executionContent);

        return executionContent;
    }

    private void readFile(ExecutionContent executionContent, Process process) throws IOException {
        OutputStream processOutputStream = process.getOutputStream();
        InputStream inputFileStream = executionContent.getInputFile().getInputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputFileStream.read(buffer)) != -1) {
            processOutputStream.write(buffer, 0, length);
        }
        processOutputStream.flush();
        inputFileStream.close();
        processOutputStream.close();
    }

    @Override
    public void deleteTmpFile(ExecutionContent executionContent) {
        File targetDict = new File(executionContent.getCompilePath());

        for (File f : Objects.requireNonNull(targetDict.listFiles())) {
            f.delete();
        }

        targetDict.delete();
    }
}
