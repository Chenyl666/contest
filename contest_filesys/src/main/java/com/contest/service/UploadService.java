package com.contest.service;

import com.contest.result.ResultModel;

import java.io.InputStream;

public interface UploadService {
    public ResultModel<String> uploadSimpleFile(InputStream in);
}
