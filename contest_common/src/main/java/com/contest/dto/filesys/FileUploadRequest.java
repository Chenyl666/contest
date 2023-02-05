package com.contest.dto.filesys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileUploadRequest {
    private String fileMd5;
    private Integer sumPiece;
    private Long pieceSize;
    private String fileName;
    private String userId;
}
