package cn.learn.concurrency;

import java.io.IOException;

/**
 * Created by Ww on 2017/9/13.
 */
class UnResponseUI {
    private volatile double d = 1;
    public UnResponseUI() throws IOException {
        while (d > 0) {
            d = d + (Math.PI + Math.E) / d;
        }
        System.in.read();
    }
}
public class ResponseUI extends Thread {
    private static volatile double d = 1;
    public ResponseUI() {
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (true) {
            d = d + (Math.PI + Math.E) / d;
        }
    }

    public static void main(String[] args) throws IOException {
        new ResponseUI();
        System.in.read();
        System.out.println(d);
    }
}
