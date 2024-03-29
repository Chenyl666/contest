package com.contest.util;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        file.delete();
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
    public static void createFile(String filePath){
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
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

    @SneakyThrows
    @SuppressWarnings("all")
    public static void writeFile(File file,byte[] content){
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream out = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
        out.write(content);
        out.flush();
    }

    @SneakyThrows
    public static void writeFile(File file, String content){
        writeFile(file,content,false);
    }

    @SneakyThrows
    public static void writeFile(File file, String content, Boolean append){
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        Writer writer = new BufferedWriter(new FileWriter(file,append));
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static boolean fileEquals(String filePath1, String filePath2) throws IOException {
        InputStream in1 = new BufferedInputStream(Files.newInputStream(Paths.get(filePath1)));
        InputStream in2 = new BufferedInputStream(Files.newInputStream(Paths.get(filePath2)));
        String md51 = Md5Utils.getFileMd5(in1);
        String md52 = Md5Utils.getFileMd5(in2);
        in1.close();
        in2.close();
        if(!md51.equals(md52)){
            BufferedReader reader1 = new BufferedReader(Files.newBufferedReader(Paths.get(filePath1)));
            BufferedReader reader2 = new BufferedReader(Files.newBufferedReader(Paths.get(filePath2)));
            StringBuilder stringBuilder1 = new StringBuilder();
            StringBuilder stringBuilder2 = new StringBuilder();
            char[] buf1 = new char[1024];
            char[] buf2 = new char[1024];
            int len;
            while((len = reader1.read(buf1))!=-1){
                stringBuilder1.append(buf1,0,len);
            }
            while((len = reader2.read(buf2))!=-1){
                stringBuilder2.append(buf2,0,len);
            }
            reader1.close();
            reader2.close();
            String s1 = stringBuilder1.toString().replace("\r","");
            String s2 = stringBuilder2.toString().replace("\r","");
            System.out.println(s1);
            System.out.println(s2);
            for (char c : s1.toCharArray()) {
                System.out.print((int)c + " ");
            }
            System.out.println();
            for (char c : s2.toCharArray()) {
                System.out.print((int)c + " ");
            }
            return s1.equals(s2);
        }
        return true;
    }
}
