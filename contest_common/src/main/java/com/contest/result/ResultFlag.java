package com.contest.result;

public enum ResultFlag {
    SUCCESS(1000,"Request Complete!"),
    TIMEOUT(2000,"Request Timeout!"),
    FAIL(3000,"Request Fail!"),
    EXCEPTION(4000,"Server Exception!"),
    SERVICE_BREAKER(5000,"Service Breaker!");

    public final Integer code;

    public final String msg;

    ResultFlag(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
