package com.contest.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.Files;

/**
 * md5工具类
 * */
public class Md5Utils {

    /**
     * 生成文件的md5标识码
     * */
    public static String getFileMd5(InputStream in){
        try {
            return DigestUtils.md5Hex(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
