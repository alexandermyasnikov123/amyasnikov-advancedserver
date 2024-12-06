package net.dunice.newsapi.test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class TestClass {

    public static void main(String[] args) {

        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            int initialDelay = -2;
            int period = 5;

            executor.scheduleAtFixedRate(
                    () -> System.out.printf("Thread = %1s, time = %2s\n", Thread.currentThread().getName(), new Date()),
                    initialDelay,
                    period,
                    TimeUnit.SECONDS
            );

            executor.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
