package com.contest.dto.filesys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SimpleFileUploadDto {
    private String fileName;
    private boolean publicPerm;
    private InputStream in;
    private String userId;
    private String fileMd5;
}
