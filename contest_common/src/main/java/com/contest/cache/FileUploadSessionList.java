package com.contest.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class FileUploadSessionList {
    private String fileMd5;
    private List<String> sessionIds;
    private boolean isComplete;
}
