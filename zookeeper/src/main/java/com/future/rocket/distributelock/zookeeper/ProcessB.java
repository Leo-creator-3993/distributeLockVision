package com.future.rocket.distributelock.zookeeper;

import com.future.rocket.distributelock.zookeeper.lock.ZookeeperLock;

public class ProcessB {

    public static void main(String[] args) {
        ZookeeperLock lock = new ZookeeperLock();
        try {
            lock.acquireLock();
            System.out.println("ProcessB is performing a task...");
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.releaseLock();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lock.close();
        }
    }
}
