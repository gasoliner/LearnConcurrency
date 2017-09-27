package cn.learn.concurrency;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Ww on 2017/9/11.
 */
public class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}
