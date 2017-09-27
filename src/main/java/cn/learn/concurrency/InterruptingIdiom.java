package cn.learn.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ww on 2017/9/18.
 */
class NeedsCleanUp {
    private final int id;

    public NeedsCleanUp(int id) {
        this.id = id;
    }

    public void cleanup() {
        System.out.println("Cleaning up:" + id);
    }
}
class Blocked3 implements Runnable {
    private volatile double aDouble = 0.0;
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            NeedsCleanUp needsCleanUp = new NeedsCleanUp(1);
            try {
                System.out.println("Sleeping");
                TimeUnit.SECONDS.sleep(1);
                NeedsCleanUp needsCleanUp1 = new NeedsCleanUp(2);
                try {
                    System.out.println("Calculating");
                    for (int i = 1; i < 2500000; i++) {
                        aDouble = aDouble + (Math.PI + Math.E) / aDouble;
                        System.out.println("Finished time-consuming operation");
                    }
                } finally {
                    needsCleanUp1.cleanup();
                }
            } catch (InterruptedException e) {
                System.out.println("");
            } finally {
                needsCleanUp.cleanup();
            }
        }
        System.out.println("Exiting via while() test");
    }
}
public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("usage java InterruptingIdiom delay--in-mS");
            System.exit(1);
        }
        Thread thread = new Thread(new Blocked3());
        thread.start();
        TimeUnit.MILLISECONDS.sleep(new Integer(args[0]));
        thread.interrupt();
    }
}
