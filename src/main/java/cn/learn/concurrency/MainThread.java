package cn.learn.concurrency;

/**
 * Created by Ww on 2017/9/10.
 */
public class MainThread {
    public static void main(String[] args) {
//        LearnConcurrency concurrency = new LearnConcurrency();
//        concurrency.run();
        int count = 10;
//        while (count-- != 0) {
        while (true) {
            Practice1 practice1 = new Practice1();
            practice1.run();
        }
    }
}
