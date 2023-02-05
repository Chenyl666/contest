package com.contest.config.filesys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSystemEnvInfo {

    private String env;

    private FileSystemEnv fileSystemEnv;

    public String buildFilePath(String fileName){
        String prefix = fileSystemEnv.getPathPrefix();
        String split = fileSystemEnv.getPathSplit();
        return prefix.concat(split).concat(fileName);
    }

}
