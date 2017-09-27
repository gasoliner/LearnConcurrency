package cn.learn.concurrency;

/**
 * Created by Ww on 2017/9/10.
 */
public class Practice1 implements Runnable {

    public Practice1() {
        System.out.println("constructor start!!");
    }

    @Override
    public void run() {
        int count = 3;
        while (count -- != 0) {
            System.out.println("count = " + count);
            Thread.yield();
        }
        System.out.println("task over!!");
        return;
    }
}
