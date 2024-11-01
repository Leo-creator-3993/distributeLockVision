package com.future.rocket.distributelock.redis.spring.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

    private static final String REPOSITORY_KEY = "inventory:product_12345";
    private static final String REPOSITORY_TERMINAL = "inventory:terminate";

    @Autowired
    private RedisDistributedLock redisDistributeLock;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void init() {
        String lockKey = "init_lock";
        String lockIdentifier = redisDistributeLock.acquireLock(lockKey, 2000, 5000);

        if (lockIdentifier != null) {
            try {
                redisTemplate.opsForValue().set(REPOSITORY_KEY, "50");
                redisTemplate.opsForValue().set(REPOSITORY_TERMINAL, "false");
            } finally {
                redisDistributeLock.releaseLock(lockKey, lockIdentifier);
            }
        }
    }

    public boolean decreaseInventory() {
        String lockKey = "inventory_lock";
        String lockIdentifier = redisDistributeLock.acquireLock(lockKey, 2000, 5000);
        if (lockIdentifier == null) {
            System.out.println("获取锁失败，稍后重试...");
            return false;
        }

        try {
            // 读取库存并手动减少库存值
            String inventoryStr = redisTemplate.opsForValue().get(REPOSITORY_KEY);
            if (inventoryStr != null) {
                int inventory = Integer.parseInt(inventoryStr);

                if (inventory > 0) {
                    inventory--; // 手动减少库存
                    redisTemplate.opsForValue().set(REPOSITORY_KEY, String.valueOf(inventory));
                    System.out.println(String.format("%s ==> 库存扣减成功，当前库存:%s" , Thread.currentThread().getName(), inventory));
                    return true;
                } else {
                    System.out.println("库存不足，扣减失败！");
                    redisTemplate.opsForValue().set(REPOSITORY_TERMINAL, "true");
                    return false;
                }
            }
            return false;
        } finally {
            redisDistributeLock.releaseLock(lockKey, lockIdentifier);
        }
    }

    public boolean isTerminate() {
        return "true".equals(redisTemplate.opsForValue().get(REPOSITORY_TERMINAL));
    }
}
