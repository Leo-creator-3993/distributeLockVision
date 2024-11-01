package com.future.rocket.distributelock.redis.spring.client;

import com.future.rocket.distributelock.redis.spring.lock.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class XClientA  {
    @Autowired
    private RepositoryService repositoryService;

    @Async
    public void run(String... args) throws Exception {
        System.out.println(String.format("%s => XClientA 开始执行...", Thread.currentThread().getName()));
        while (true) {
            if (!repositoryService.decreaseInventory() || repositoryService.isTerminate()) {
                break;
            }
            Thread.sleep(500);
        }
        System.out.println("XClientA 进程结束.");
    }
}
