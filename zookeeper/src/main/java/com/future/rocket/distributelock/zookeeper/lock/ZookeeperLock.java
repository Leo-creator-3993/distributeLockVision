package com.future.rocket.distributelock.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperLock {

    private static final String CONNECT_STRING = "127.0.0.1:2181";
    private static final String LOCK_PATH = "/distributed_lock";

    private final InterProcessMutex lock;
    private final CuratorFramework client;

    public ZookeeperLock() {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        this.client.start();
        this.lock = new InterProcessMutex(client, LOCK_PATH);
    }

    public void acquireLock() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("%s attempting to acquire lock...", threadName));
        lock.acquire();
        System.out.println(String.format("%s acquired lock!", threadName));
    }

    public void releaseLock() throws Exception {
        lock.release();
        String threadName = Thread.currentThread().getName();
        System.out.println(String.format("%s released lock.", threadName));
    }

    public void close() {
        this.client.close();
    }
}
