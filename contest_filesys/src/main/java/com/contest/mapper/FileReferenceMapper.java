package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.filesys.FileReferenceEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileReferenceMapper extends BaseMapper<FileReferenceEntity> {
    @Delete("delete from file_reference where file_id = #{fileId} and created_by = #{userId}")
    public boolean deleteByFileIdAndUserId(@Param("fileId") String fileId,@Param("userId") String userId);
}
