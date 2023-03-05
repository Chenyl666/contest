package com.contest.service;

import com.contest.dto.user.UserDto;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.result.ResultModel;

public interface DeleteService {
    public ResultModel<String> requestDeleteFile(String fileId, UserDto userDto);
    /**
     * 用户删除文件
     * */
    public void deleteFile(FileReferenceEntity fileReferenceEntity);

    /**
     * 定时任务删除文件
     * */
    public void deleteBatchFileOfTimeout(String fileId);

    /**
     * 通过url删除文件
     * */
    public void deleteFileByDownloadUrl(String url,UserDto userDto);
}
