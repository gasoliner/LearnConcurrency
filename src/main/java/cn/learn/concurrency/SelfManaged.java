package cn.learn.concurrency;

/**
 * Created by Ww on 2017/9/12.
 */
public class SelfManaged implements Runnable {

    private int countDown = 5;
    private Thread thread = new Thread(this);

    public SelfManaged() {
        thread.start();
    }

    @Override
    public String toString() {
        return Thread.currentThread().getName() + "(" +countDown + "),";
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(this);
            if (--countDown == 0) {
                return;
            }
        }
    }


}
