package com.future.rocket.distributelock.redis.supplier;

import java.util.function.Supplier;

public class ProductFactory {

    private ProductFactory() {
    }

    private static ProductFactory INSTANCE = new ProductFactory();

    public static ProductFactory getINSTANCE() {
        return INSTANCE;
    }

    public Supplier<String> genProduct() {
        return () -> "FutureFooProductX";
    }
}
