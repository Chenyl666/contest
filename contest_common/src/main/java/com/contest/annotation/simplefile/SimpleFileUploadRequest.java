package com.contest.annotation.simplefile;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SimpleFileUploadRequest {
}
