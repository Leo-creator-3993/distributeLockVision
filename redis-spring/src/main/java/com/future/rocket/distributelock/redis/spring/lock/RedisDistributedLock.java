package com.future.rocket.distributelock.redis.spring.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //获取锁
    public String acquireLock(String lockKey, long acquireTimeout, long lockTimeout) {
        String identifier = UUID.randomUUID().toString();
        long end = System.currentTimeMillis() + acquireTimeout;

        while (System.currentTimeMillis() < end) {
            Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, identifier);
            if (Boolean.TRUE.equals(success)) {
                redisTemplate.expire(lockKey, lockTimeout, TimeUnit.MILLISECONDS);
                return identifier;  // 获取锁成功
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;  // 获取锁失败
    }

    //释放锁
    public boolean releaseLock(String lockKey, String identifier) {
        String currentIdentifier = redisTemplate.opsForValue().get(lockKey);
        if (identifier.equals(currentIdentifier)) {
            redisTemplate.delete(lockKey);
            return true;
        }
        return false;
    }
}
