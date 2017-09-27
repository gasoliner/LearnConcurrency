package cn.learn.concurrency;

import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * Created by Ww on 2017/9/11.
 */
public class TaskWithResult implements Callable<String> {
    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "result of TaskWithResult " + id;
    }
}
