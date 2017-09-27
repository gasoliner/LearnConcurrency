package cn.learn.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ww on 2017/9/11.
 */
class ADaemon implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Starting ADemon");
            TimeUnit.MICROSECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("run finally");
        }
    }

}
public class DaemonsDontRunFinally {
    public static void main(String[] args) {
        Thread thread = new Thread(new ADaemon());
        thread.setDaemon(true);
        thread.start();
    }
}