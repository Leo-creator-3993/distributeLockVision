package com.future.rocket.distributelock.db;

import com.future.rocket.distributelock.db.config.ConfigLoader;

public class DbLockMain {

    public static void main(String[] args) {
        System.out.println("DB URL: " + ConfigLoader.getProperty("db.url"));
        System.out.println("DB User: " + ConfigLoader.getProperty("db.user"));
        System.out.println("DB Password: " + ConfigLoader.getProperty("db.password"));
    }
}
