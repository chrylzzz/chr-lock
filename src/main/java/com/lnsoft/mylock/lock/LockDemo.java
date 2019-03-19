package com.lnsoft.mylock.lock;

/**
 * 测试cas
 * Created By Chr on 2019/3/13/0013.
 */
public class LockDemo {
    int i = 0;

    public void incr() {
        i++;
    }

    public static void main(String args[]) throws InterruptedException {
        LockDemo lockDemo = new LockDemo();

        for (int j = 0; j < 2; j++) {//两个线程
            new Thread(() -> {
                for (int k = 0; k < 10000; k++) {//每个线程+10000次
                    lockDemo.incr();
                }
            }).start();
        }

        Thread.sleep(1000L);
        //
        System.out.println(lockDemo.i);
    }
}
