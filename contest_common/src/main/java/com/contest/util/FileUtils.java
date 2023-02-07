package com.contest.util;

import java.io.File;

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

}
