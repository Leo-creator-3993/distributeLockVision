package com.future.rocket.distributelock.redis.spring.init;

import com.future.rocket.distributelock.redis.spring.lock.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryInitializer  {

    @Autowired
    private RepositoryService repositoryService;

    public void run(String... args) throws Exception {
        repositoryService.init();
        System.out.println("库存初始化完成。");
    }
}
