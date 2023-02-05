package com.contest.config.filesys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WindowsFileSystemEnv implements FileSystemEnv {

    @Value("${filesys.windows.path-prefix}")
    private String pathPrefix;

    @Value("${filesys.windows.path-split}")
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
