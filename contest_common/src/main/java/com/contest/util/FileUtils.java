package com.contest.util;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;

public class FileUtils {

    @SuppressWarnings("all")
    public static void deleteFile(File file){
        if(file.exists()){
            file.delete();
        }
    }

    @SuppressWarnings("all")
    public static void deleteFile(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }

    @SneakyThrows
    public static void createFile(File file){
        File parentFile = file.getParentFile();
        if(!parentFile.exists() && !parentFile.mkdirs()){
            throw new IOException("文件夹创建失败！");
        }
        if(!file.exists() && !file.createNewFile()){
            throw new IOException("文件创建失败！");
        }
    }

    @SneakyThrows
    public static void writeFileByAppend(InputStream in, File targetFile){
        createFile(targetFile);
        byte[] buf = new byte[1024];
        OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile,true));
        int bufCnt;
        while((bufCnt = in.read(buf))!=-1){
            out.write(buf,0,bufCnt);
        }
        out.flush();
        out.close();
    }



}
