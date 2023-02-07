package com.contest.service;

import com.contest.dto.user.UserDto;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.result.ResultModel;

public interface DeleteService {
    public ResultModel<String> requestDeleteFile(String fileId, UserDto userDto);
    public void deleteFile(FileReferenceEntity fileReferenceEntity);
}
