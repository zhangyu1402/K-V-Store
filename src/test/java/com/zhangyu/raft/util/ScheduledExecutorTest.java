package com.zhangyu.raft.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorTest {

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    @Test
    public void fixTastTest() throws InterruptedException {
        Future future = executorService.schedule(() -> System.out.println("run " + System.currentTimeMillis()), 1000, TimeUnit.MILLISECONDS);
        System.out.println("after");
        Thread.sleep(3000);
        System.out.println("finish");


    }

    @Test
    public void StreamTest() {
        Set<Integer> set = new HashSet<>();
        set.add(3000);
        set.add(6000);
        set.add(9000);
        long time = System.currentTimeMillis();
        System.out.println(time);
        set.parallelStream().forEach(e -> {
            try {
                Thread.sleep(e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
        System.out.println(System.currentTimeMillis() - time);
    }
}
