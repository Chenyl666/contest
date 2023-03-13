package com.contest.dto.online;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContestCodeDto {

    public static enum LanguageType{
        java,c,cpp,python,javascript
    }

    private Long answerId;
    private Long questionId;
    private String code;
    private LanguageType languageType;

}
