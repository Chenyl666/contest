package com.contest.util;

import java.io.File;

public class FileUtils {
    public static Boolean fileExists(File file){
        return file.exists();
    }

    public static Boolean fileExists(String filePath){
        return new File(filePath).exists();
    }

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

    @SuppressWarnings("all")
    public static void renameFile(File file,String newName){
        file.renameTo(new File(file.getParent().concat("/").concat(newName)));
    }

    @SuppressWarnings("all")
    public static void renameFile(String filePath,String newName){
        File file = new File(filePath);
        file.renameTo(new File(file.getParent().concat("/").concat(newName)));
    }

    public static Long getFileSize(String filePath){
        File file = new File(filePath);
        return file.length();
    }
}
