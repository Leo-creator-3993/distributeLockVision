package com.future.rocket.distributelock.redis.init;

import com.future.rocket.distributelock.redis.lock.RedisDistributeLock;
import redis.clients.jedis.Jedis;

import static com.future.rocket.distributelock.redis.constant.Constants.REPOSITORY_KEY;
import static com.future.rocket.distributelock.redis.constant.Constants.REPOSITORY_TERMINAL;

public class RepositoryInitializing {
    public static void main(String[] args) {
        Jedis redisClient = new Jedis("172.28.202.236", 6379);
        RedisDistributeLock redisDistributeLock = new RedisDistributeLock(redisClient);
        init(redisDistributeLock, redisClient);
    }

    //初始化库存和终止标志
    public static void init(RedisDistributeLock redisDistributeLock, Jedis redisClient) {
        String lockName = "init_lock";
        String lockIdentifier = redisDistributeLock.requireLock(lockName, 2000);
        try{
            if (null != lockIdentifier) {
                redisClient.set(REPOSITORY_KEY, String.valueOf(100));
                redisClient.set(REPOSITORY_TERMINAL, String.valueOf(false));
            }
        }finally{
            redisDistributeLock.releaseLock(lockName, lockIdentifier);
        }
    }
}
