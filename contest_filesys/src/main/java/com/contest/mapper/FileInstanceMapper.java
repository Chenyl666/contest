package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.filesys.FileInstanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileInstanceMapper extends BaseMapper<FileInstanceEntity> {
    @Update("update file_instance set ref_num=ref_num+1 where file_md5 = #{fileMd5}")
    public void incrReferenceNum(@Param("fileMd5") String fileMd5);
}
