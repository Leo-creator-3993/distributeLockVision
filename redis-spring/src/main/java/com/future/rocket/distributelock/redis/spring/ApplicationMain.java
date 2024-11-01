package com.future.rocket.distributelock.redis.spring;

import com.future.rocket.distributelock.redis.spring.client.XClientA;
import com.future.rocket.distributelock.redis.spring.client.XClientB;
import com.future.rocket.distributelock.redis.spring.init.InventoryInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApplicationMain implements CommandLineRunner {

    @Autowired
    private XClientA xClientA;

    @Autowired
    private XClientB xClientB;

    @Autowired
    private InventoryInitializer inventoryInitializer;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        inventoryInitializer.run();
        xClientA.run();
        xClientB.run();
    }
}
