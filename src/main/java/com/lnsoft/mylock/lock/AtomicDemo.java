package com.lnsoft.mylock.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created By Chr on 2019/3/19/0019.
 */
public class AtomicDemo {
    //    int i = 0;      不具备原子性
    AtomicInteger i = new AtomicInteger(0);//JDK提供的原子操作类

    public void incr() {
        i.incrementAndGet();//对 i 这个源自的加1操作，原子操作
    }

    public static void main(String args[]) throws InterruptedException {
        AtomicDemo atomicDemo = new AtomicDemo();

        for (int j = 0; j < 2; j++) {
            new Thread(() -> {
                for (int k = 0; k < 10000; k++) {
                    atomicDemo.incr();
                }
            }).start();
        }

        Thread.sleep(1000L);
        //
        System.out.println(atomicDemo.i);
    }
}
