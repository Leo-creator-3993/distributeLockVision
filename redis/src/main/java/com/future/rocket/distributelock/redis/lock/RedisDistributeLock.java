package com.future.rocket.distributelock.redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public class RedisDistributeLock {

    private final String _LOCK_KEY_COMPONENT = "xFoo";

    private Jedis redisClient;

    public RedisDistributeLock(Jedis redisClient) {
        this.redisClient = redisClient;
    }

    //获取锁
    public String requireLock(String lockName, long requireTimeout) {
        String lockKey = generateLockKey(lockName);
        String identifier = UUID.randomUUID().toString();
        long end = requireTimeout + System.currentTimeMillis();
        SetParams params = new SetParams().nx().px(requireTimeout);

        //超时前不断尝试获取锁
        while (end - System.currentTimeMillis() > 0) {
            try {
                if("OK".equals(redisClient.set(lockKey, identifier, params))) {
                    return identifier;
                }

                //休眠100毫秒后继续去获取
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    //释放锁
    public boolean releaseLock(String lockName, String identifier) {
        String lockKey = generateLockKey(lockName);
        while (true) {
            redisClient.watch(lockKey);
            if(identifier.equals(redisClient.get(lockKey))) {
                Transaction transaction = redisClient.multi();
                transaction.del(lockKey);
                if(null != transaction.exec()) {
                    return true;
                }
            } else {
                redisClient.unwatch();
                break;
            }
        }
        return false;
    }


    //统一方式构建锁
    private String generateLockKey(String lockName) {
        return String.format("%s:%s", _LOCK_KEY_COMPONENT, lockName);
    }
}
