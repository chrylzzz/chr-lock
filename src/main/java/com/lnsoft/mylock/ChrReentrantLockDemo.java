package com.lnsoft.mylock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created By Chr on 2019/3/19/0019.
 */
public class ChrReentrantLockDemo {
    int i = 0;
    //new 一个锁
    Lock lock = new ReentrantLock();

    public void incr() {
        //加锁
        lock.lock();
        try {
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//释放锁
        }
    }

    public static void main(String args[]) throws InterruptedException {

        ChrReentrantLockDemo chrReentrantLockDemo = new ChrReentrantLockDemo();

        for (int j = 0; j < 2; j++) {//两个线程
            new Thread(() -> {
                for (int k = 0; k < 10000; k++) {//每个线程+10000次
                    chrReentrantLockDemo.incr();
                }
            }).start();
        }

        Thread.sleep(1000L);
        //
        System.out.println(chrReentrantLockDemo.i);
    }
}
