package com.ls.redisSharedLock.lock;

import com.ls.redisSharedLock.RedisClient;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

public class RedisLock {

    private static final long DEFAULT_MAX_LOCK_TIME = 300000l;

    private RedisClient redisClient;

    private String key;

    private Long maxLockTime;

    private boolean locked;


    private RedisLock(RedisClient redisClient, String key, Long maxLockTime) {
        Assert.notNull(redisClient, "redisClient不能为空");
        Assert.hasText(key, "key不能为空");
        this.redisClient = redisClient;
        this.key = key;
        this.maxLockTime = maxLockTime;
    }


    public static RedisLock build(RedisClient redisClient, String key, Long maxLockTime) {
        return new RedisLock(redisClient, key, maxLockTime);
    }

    public static RedisLock build(RedisClient redisClient, String key) {
        return new RedisLock(redisClient, key, DEFAULT_MAX_LOCK_TIME);
    }

    public boolean lock() {
        Boolean b = redisClient.setNx(key, "1", maxLockTime, TimeUnit.MILLISECONDS);
        if (b) {
            locked = true;
            return true;
        }
        locked = false;
        return false;
    }

    public void unlock() {
        if (locked) {
            redisClient.delete(key);
            locked = false;
        }
    }

}
