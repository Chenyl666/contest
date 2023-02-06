package com.contest.pojo.filesys;

import com.contest.entity.filesys.FileInstanceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileUploadSessionObj {
    private String sessionId;
    private String fileMd5;
    private String fileName;
    private String filePath;
    private String userId;
}
