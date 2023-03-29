package com.contest.dto.contest;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContestResultDto {
    @ColumnWidth(20)
    @ExcelProperty({"排名"})
    private Integer rank;

    @ColumnWidth(20)
    @ExcelProperty({"用户id"})
    private String userId;

    @ColumnWidth(20)
    @ExcelProperty({"用户昵称"})
    private String userName;

    @ColumnWidth(20)
    @ExcelProperty({"竞赛id"})
    private String contestId;

    @ColumnWidth(20)
    @ExcelProperty({"竞赛名称"})
    private String contestSubject;

    @ColumnWidth(20)
    @ExcelProperty("总分数")
    private Float sumScore;
}
