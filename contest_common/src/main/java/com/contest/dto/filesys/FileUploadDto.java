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
public class FileUploadDto {
    /**
     * 文件上传sessionId
     * */
    private String sessionId;
    /**
     * 分片文件流
     * */
    private InputStream pieceInputStream;
    /**
     * 序列号
     * */
    private Integer seqId;
}
