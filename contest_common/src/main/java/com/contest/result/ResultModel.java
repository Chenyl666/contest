package com.contest.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResultModel<T> {
    private ResultFlag resultFlag;
    private Integer resultCode;
    private String message;
    private T data;

    public static ResultModel<String> buildSuccessResultModel(){
        return ResultModel.<String>builder()
                .resultFlag(ResultFlag.SUCCESS)
                .resultCode(ResultFlag.SUCCESS.code)
                .message(ResultFlag.SUCCESS.msg)
                .build();
    }

    public static ResultModel<String> buildSuccessResultModel(String message){
        return ResultModel.<String>builder()
                .resultFlag(ResultFlag.SUCCESS)
                .resultCode(ResultFlag.SUCCESS.code)
                .message(message)
                .build();
    }

    public static <T> ResultModel<T> buildSuccessResultModel(String message,T data){
        return ResultModel.<T>builder()
                .resultFlag(ResultFlag.SUCCESS)
                .resultCode(ResultFlag.SUCCESS.code)
                .message(ResultFlag.SUCCESS.msg)
                .data(data)
                .build();
    }

    public static ResultModel<String> buildFailResultModel(){
        return ResultModel.<String>builder()
                .resultFlag(ResultFlag.FAIL)
                .resultCode(ResultFlag.FAIL.code)
                .message(ResultFlag.FAIL.msg)
                .build();
    }

    public static ResultModel<String> buildFailResultModel(String message){
        return ResultModel.<String>builder()
                .resultFlag(ResultFlag.FAIL)
                .resultCode(ResultFlag.FAIL.code)
                .message(message)
                .build();
    }

    public static <T> ResultModel<T> buildFailResultModel(String message,T data){
        return ResultModel.<T>builder()
                .resultFlag(ResultFlag.FAIL)
                .resultCode(ResultFlag.FAIL.code)
                .message(message == null?ResultFlag.FAIL.msg:message)
                .data(data)
                .build();
    }

    public static ResultModel<String> buildContinueResultModel(){
        return ResultModel.<String>builder()
                .resultFlag(ResultFlag.CONTINUE)
                .resultCode(ResultFlag.CONTINUE.code)
                .build();
    }

    public static ResultModel<String> buildContinueResultModel(String message){
        return ResultModel.<String>builder()
                .resultFlag(ResultFlag.CONTINUE)
                .resultCode(ResultFlag.CONTINUE.code)
                .message(message)
                .build();
    }
}
