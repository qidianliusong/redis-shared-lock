package com.ls.redisSharedLock;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, String value) {
        getStringRedisTemplate().opsForValue().set(key, value);
    }

    public void set(String key, String value, int expireTime, TimeUnit unit) {
        getStringRedisTemplate().opsForValue().set(key, value, expireTime, unit);
    }

    public void set(String key, Object value) {
        getRedisTemplate().opsForValue().set(key, value);
    }

    public Boolean setNX(String key, Object value) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value);
    }
    
    public Boolean setNx(String key,Object value,long expireTime,TimeUnit unit){
        return getRedisTemplate().execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().setIfAbsent(key, value);
                operations.expire(key, expireTime, unit);
                List exec = operations.exec();
                if(exec == null || exec.isEmpty())
                    return false;
                return (Boolean) exec.get(0);
            }
        });
    }

    public void set(String key, Object value, int expireTime, TimeUnit unit) {
        getRedisTemplate().opsForValue().set(key, value, expireTime, unit);
    }

    public Long increment(String key, int i) {
        return getStringRedisTemplate().boundValueOps(key).increment(i);
    }

    public String get(String key) {
        return getStringRedisTemplate().opsForValue().get(key);
    }

    public Object getObj(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }

    public Set<String> keys(String pattern){
        return getRedisTemplate().keys(pattern);
    }

    /**
     * 获得过期时间（以秒为单位）
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return getStringRedisTemplate().getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean expire(String key,long timeout,TimeUnit unit){
        return getRedisTemplate().expire(key, timeout, unit);
    }

    public void delete(String key) {
        getRedisTemplate().delete(key);
    }

    public void delete(Set<String> keys){
        getRedisTemplate().delete(keys);
    }

    public boolean haskey(String key) {
        return getStringRedisTemplate().hasKey(key);
    }

    public Long rightPush(String key, Object value) {
        return getRedisTemplate().opsForList().rightPush(key, value);
    }

    public Object leftPop(String key) {
        return getRedisTemplate().opsForList().leftPop(key);
    }

    public List<Object> getList(String key) {
        return getRedisTemplate().opsForList().range(key, 0, -1);
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
