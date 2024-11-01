package com.future.rocket.distributelock.redis.lock;

import redis.clients.jedis.Jedis;

import static com.future.rocket.distributelock.redis.constant.Constants.REPOSITORY_KEY;
import static com.future.rocket.distributelock.redis.constant.Constants.REPOSITORY_TERMINAL;

public class RepositoryService {
    private RedisDistributeLock redisDistributeLock;
    private Jedis redisClient;

    public RepositoryService(Jedis redisClient) {
        this.redisClient = redisClient;
        redisDistributeLock = new RedisDistributeLock(redisClient);
    }

    //产品扣减
    public boolean decrease(String productName) {
        if ("true".equals(redisClient.get(REPOSITORY_TERMINAL))) {
            System.out.println("库存不足, 产品 ==> " + productName);
            return false;
        }

        String identifier = redisDistributeLock.requireLock(productName, 3000);
        if (identifier == null) {
            System.out.println("获取锁失败! productName ==> " + productName);
            return false;
        }

        long repository = Integer.parseInt(redisClient.get(REPOSITORY_KEY));
        if (repository > 0) {
            try {
                repository = redisClient.decr(REPOSITORY_KEY);
                System.out.println(String.format("%s ==> 扣减库存成功, 当前库存剩余 ==> %s ", Thread.currentThread().getName(), repository));
            } finally {
                redisDistributeLock.releaseLock(productName, identifier);
                return true;
            }
        } else {
            redisClient.set(REPOSITORY_TERMINAL, String.valueOf(true));
            return false;
        }
    }
}
