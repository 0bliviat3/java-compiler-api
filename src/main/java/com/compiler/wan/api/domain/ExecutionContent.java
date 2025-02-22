package com.compiler.wan.api.domain;

import com.compiler.wan.api.config.ExecutionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


/*
상태 값 분류 -> Enum 처리
1. 성공
2. 컴파일 에러
3. 예외 발생

컴파일시 임시 컴파일 path -> UUID로 처리

결과값
성공의 경우 출력값, 그 외의 경우 에러메세지

실행시킨 코드
 */
@Getter
@Setter
public class ExecutionContent {

    private ExecutionStatus status;
    private String compilePath;
    private String message;
    private String code;
    private Integer maxMemory;
    private Integer maxTime;

    @JsonIgnore
    private MultipartFile inputFile;

}
