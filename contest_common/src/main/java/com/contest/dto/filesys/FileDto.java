package com.contest.dto.filesys;

import com.contest.entity.filesys.FileInstanceEntity;
import com.contest.entity.filesys.FileReferenceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileDto {
    private Long fileId;
    private String fileMd5;
    private String fileName;
    private Long fileSize;
    private String filePath;
    private Integer referenceNum;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    public FileReferenceEntity toFileReferenceEntity(){
        return FileReferenceEntity
                .builder()
                .fileMd5(this.fileMd5)
                .fileId(this.fileId)
                .fileName(this.fileName)
                .createdBy(this.createdBy)
                .createdDate(this.createdDate)
                .updatedBy(this.updatedBy)
                .updatedDate(this.updatedDate)
                .build();
    }

    public FileInstanceEntity toFileInstanceEntity(){
        return FileInstanceEntity
                .builder()
                .fileMd5(this.fileMd5)
                .fileSize(this.fileSize)
                .filePath(this.filePath)
                .referenceNum(this.referenceNum)
                .createdBy(this.createdBy)
                .createdDate(this.createdDate)
                .updatedBy(this.updatedBy)
                .updatedDate(this.updatedDate)
                .referenceNum(
                        (this.referenceNum == null)? 0:this.referenceNum
                )
                .build();
    }
}
