package cn.learn.concurrency;

/**
 * Created by Ww on 2017/9/18.
 */
public class MutiLock {
    public synchronized void f1(int count) {
        if (count-- > 0) {
            System.out.println("f1 call f2 " + count);
            f2(count);
        }
    }

    private synchronized void f2(int count) {
        if (count-- > 0) {
            System.out.println("f2 call f1 " + count);
            f1(count);
        }
    }

    public static void main(String[] args) {
        final MutiLock mutiLock = new MutiLock();
        new Thread() {
            public void run() {
                mutiLock.f1(10);
            }
        }.start();
    }
}
