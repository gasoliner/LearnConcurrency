package cn.learn.concurrency;

/**
 * Created by Ww on 2017/9/14.
 */
class DualSynch {
    private Object object = new Object();
    public synchronized void f() {
        for (int i  = 0;i < 10000; i++) {
            System.out.println("f()");
            Thread.yield();
        }
    }

    public void g() {
        synchronized (object) {
            for (int i =0;i < 10000;i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }
    }
}

public class SyncObject {
    public static void main(String[] args) {
        final DualSynch dualSynch = new DualSynch();
        new Thread() {
            public void run() {
                dualSynch.f();
            }
        }.start();
        dualSynch.g();
    }
}
