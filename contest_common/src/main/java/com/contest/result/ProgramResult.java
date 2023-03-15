package com.contest.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProgramResult {

    public static final Integer PASS = 1;

    public static final Integer ERROR = 2;

    public static final Integer MEMORY_OVERFLOW = 3;

    public static final Integer TIMEOUT = 4;

    private Integer number;

    private Integer code;

    private Float score;

    private String message;

    public static ProgramResult buildAllPass(Integer number, Float score){
        return ProgramResult
                .builder()
                .code(PASS)
                .number(number)
                .message("正确")
                .score(score)
                .build();
    }

    public static ProgramResult buildError(Integer number){
        return ProgramResult
                .builder()
                .code(ERROR)
                .number(number)
                .message("错误")
                .score(0f)
                .build();
    }

    public static ProgramResult buildMemoryOverFlow(Integer number){
        return ProgramResult
                .builder()
                .code(MEMORY_OVERFLOW)
                .score(0f)
                .number(number)
                .message("内存溢出")
                .build();
    }

    public static ProgramResult buildTimeout(Integer number){
        return ProgramResult
                .builder()
                .code(TIMEOUT)
                .score(0f)
                .number(number)
                .message("超时")
                .build();
    }
}
