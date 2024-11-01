package com.future.rocket.distributelock.db;

import com.future.rocket.distributelock.db.lock.DbDistributedLock;

public class ProcessA {
    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("ProcessA try to get lock...");
                DbDistributedLock lock = new DbDistributedLock("my_lock", "ProcessA");
                if (lock.acquireLock(10)) {
                    System.out.println("ProcessA acquired lock and is performing task...");
                    Thread.sleep(5000); // 模拟任务执行
                    System.out.println("ProcessA finish task!");
                    lock.releaseLock();
                    lock.close();
                    break;
                } else {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
