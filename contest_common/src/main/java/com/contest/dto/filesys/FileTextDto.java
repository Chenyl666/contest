package com.contest.dto.filesys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileTextDto {
    private String fileName;
    private boolean publicPerm;
    private String text;
    private boolean timeLimit;
}
