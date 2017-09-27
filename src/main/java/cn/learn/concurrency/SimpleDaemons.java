package cn.learn.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ww on 2017/9/11.
 */
public class SimpleDaemons implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                TimeUnit.MICROSECONDS.sleep(100);
                System.out.println(Thread.currentThread() + " " + this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread daemon = new Thread(new SimpleDaemons());
            daemon.setDaemon(true);
            daemon.start();//必须在线程启动之前设置为后台线程
        }
        System.out.println("main over");
        TimeUnit.MICROSECONDS.sleep(175);
    }
}
