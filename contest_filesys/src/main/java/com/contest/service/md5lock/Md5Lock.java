package com.contest.service.md5lock;

public interface Md5Lock {
    public void lock(String fileMd5);
    public void unlock(String fileMd5);
}
