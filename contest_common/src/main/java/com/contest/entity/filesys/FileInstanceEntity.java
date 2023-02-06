package com.contest.entity.filesys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName("file_instance")
public class FileInstanceEntity {
    @TableId
    private String fileMd5;
    @TableField("file_size")
    private Long fileSize;
    @TableField("file_path")
    private String filePath;
    @TableField("ref_num")
    private Integer referenceNum;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;
    @TableField("created_by")
    private String createdBy;
    @TableField("updated_by")
    private String updatedBy;

    public void incrementReferenceNum(){
        this.referenceNum++;
    }
}
