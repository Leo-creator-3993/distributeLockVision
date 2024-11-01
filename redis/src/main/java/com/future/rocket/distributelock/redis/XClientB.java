package com.future.rocket.distributelock.redis;

import com.future.rocket.distributelock.redis.lock.RepositoryService;
import com.future.rocket.distributelock.redis.supplier.ProductFactory;
import redis.clients.jedis.Jedis;

public class XClientB {

    public static void main(String[] args) {
        System.out.println("进程 ==> XClientB 开始启动");
        Jedis redisClient = new Jedis("172.28.202.236", 6379);
        RepositoryService repositoryService = new RepositoryService(redisClient);
        String product = ProductFactory.getINSTANCE().genProduct().get();
        while (true) {
            if(!repositoryService.decrease(product)) {
                break;
            }

            try {
                Thread.sleep(500);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("XClientB ==> 进程结束!");
    }
}
