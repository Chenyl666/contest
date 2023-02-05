package com.contest.config.filesys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LinuxFileSystemEnv implements FileSystemEnv {
    @Value("${filesys.linux.path-prefix}")
    private String pathPrefix;

    @Value("${filesys.linux.path-split}")
    private String pathSplit;

    @Override
    public String getPathSplit() {
        return this.pathSplit;
    }

    @Override
    public String getPathPrefix() {
        return this.pathPrefix;
    }
}
