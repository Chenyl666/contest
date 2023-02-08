package com.contest.service.md5lock;

import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Md5锁
 * */
@Component
public class ConcurrentHashMapMd5Lock implements Md5Lock{

    private final WeakConcurrentMap<String, Lock> md5LocksMap = new WeakConcurrentMap<>(true);

    @Override
    public void lock(String fileMd5) {
        Lock md5Lock = md5LocksMap.get(fileMd5);
        if(md5Lock != null){
            md5Lock.lock();
            return;
        }
        md5LocksMap.put(fileMd5, new ReentrantLock());
        md5LocksMap.get(fileMd5).lock();
    }

    @Override
    public void unlock(String fileMd5) {
        Optional<Lock> lockOptional = Optional.ofNullable(md5LocksMap.get(fileMd5));
        if(lockOptional.isPresent()){
            Lock lock = lockOptional.get();
            lock.unlock();
        }
    }
}
