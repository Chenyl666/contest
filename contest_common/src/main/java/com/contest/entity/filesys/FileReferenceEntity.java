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
@TableName("file_reference")
public class FileReferenceEntity {
    @TableId
    private Long fileId;
    @TableField("file_md5")
    private String fileMd5;
    @TableField("file_name")
    private String fileName;
    @TableField("created_date")
    private Date createdDate = new Date();
    @TableField("updated_date")
    private Date updatedDate;
    @TableField("created_by")
    private String createdBy;
    @TableField("updated_by")
    private String updatedBy;
}
