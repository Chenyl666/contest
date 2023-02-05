package com.contest.config;

import com.contest.config.filesys.FileSystemEnvInfo;
import com.contest.config.filesys.LinuxFileSystemEnv;
import com.contest.config.filesys.WindowsFileSystemEnv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class FileSystemConfiguration {

    @Resource
    private LinuxFileSystemEnv linuxFileSystemEnv;

    @Resource
    private WindowsFileSystemEnv windowsFileSystemEnv;

    @Value("${filesys.env}")
    private String env;

    static class FileSystemConfigReadingException extends Exception{
        public FileSystemConfigReadingException(String message) {
            super(message);
        }
    }

    @Bean
    public FileSystemEnvInfo fileSystemParam() throws FileSystemConfigReadingException {
        FileSystemEnvInfo fileSystemEnvInfo = new FileSystemEnvInfo();
        fileSystemEnvInfo.setEnv(env);
        switch (env){
            case "windows": {
                fileSystemEnvInfo.setFileSystemEnv(windowsFileSystemEnv);
                break;
            }
            case "linux": {
                fileSystemEnvInfo.setFileSystemEnv(linuxFileSystemEnv);
                break;
            }
            default: {
                throw new FileSystemConfigReadingException("config reading wrong!");
            }
        }
        return fileSystemEnvInfo;
    }

}
