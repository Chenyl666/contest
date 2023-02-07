package com.contest.pojo.filesys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileUploadSession {
    private String sessionId;
    private String fileMd5;
    private String fileName;
    private String filePath;
    private String userId;
    private boolean isPublic;
}
