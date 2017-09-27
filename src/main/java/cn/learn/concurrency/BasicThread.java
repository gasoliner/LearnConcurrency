package cn.learn.concurrency;

/**
 * Created by Ww on 2017/9/10.
 */
public class BasicThread {
    public static void main(String[] args) {
        for (int i =0; i < 5;i++) {
            Thread thread = new Thread(new LearnConcurrency());
            thread.start();
            System.out.println("Waiting for LiftOff");
        }
    }
}
