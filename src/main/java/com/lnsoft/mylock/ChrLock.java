package com.lnsoft.mylock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 非公平锁，还可以继续抢占
 * 手写lock，保证原子性：lock可以用在代码块，Atomic只等单个
 * synchronized是关键字，是jvm层实现的，也是cas机制，synchronized是jvm锁，重量级锁，c语言锁
 * cas轻量锁，无锁机制，jdk锁
 * </>
 * Created By Chr on 2019/3/13/0013.
 */
public class ChrLock implements Lock {

    //定义13,不具备原子性
//    Thread thread = null;

    //具备原子性，引用
    AtomicReference<Thread> amrf = new AtomicReference<>();

    //等待列表：存放没有抢到13的线程
    public LinkedBlockingQueue<Thread> waiter = new LinkedBlockingQueue<>();

    //加锁
    @Override
    public void lock() {
        /*if (thread == null) {//13线程没有被持有
            thread = thread.currentThread();//当前线程付给13
        } else {//13已被持有
            LockSupport.park();//类似停车场
        }*/
        //================================================
        //一直抢，if可能只抢一次
        while (!amrf.compareAndSet(null, Thread.currentThread())) {//旧，新,比较和交换,是否抢占成功、
//            抢不到，当前线程等待
            waiter.add(Thread.currentThread());
            //让线程等待，卡住等待
            LockSupport.park();
            //如果执行到这里，就可以其抢占13了，将当前线程从等待列表删除
            waiter.remove(Thread.currentThread());
        }
    }

    //释放
    @Override
    public void unlock() {
        /*if (Thread.currentThread().equals(thread)) {//当前执行释放锁的对象，是不是该13号
            //释放
            thread = null;
        } else {

        }*/
        //================================================
        //判断是不是持有13的线程，是就释放，不是就不释放
        if (amrf.compareAndSet(Thread.currentThread(), null)) {//cas，比较和等待：比较当前线程是否是当前13
            //如果释放，通知所有线程（等待列表）去抢占，唤醒
            Object[] objects = waiter.toArray();//等待列表数组化
            for (Object object : objects) {
                //拿出所有等待的线程
                Thread next = (Thread) object;
                LockSupport.unpark(next);//唤醒
            }
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
