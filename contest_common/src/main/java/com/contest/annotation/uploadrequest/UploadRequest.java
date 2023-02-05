package com.contest.annotation.uploadrequest;

import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadRequest {
}
