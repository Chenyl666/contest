package com.contest.dto.online;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContestCodeDto {
    private Long answerId;
    private Long questionId;
    private String code;
    private String languageType;
}
