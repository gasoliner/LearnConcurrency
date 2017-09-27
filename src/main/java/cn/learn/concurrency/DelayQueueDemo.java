package cn.learn.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created by Ww on 2017/9/25.
 */
class DelayedTask implements Runnable, Delayed {
    private static int counter = 0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;
    protected static List<DelayedTask> sequence =
            new ArrayList<>();
    public DelayedTask(int delayInMilliseconds) {
        delta = delayInMilliseconds;
        trigger = System.nanoTime() + NANOSECONDS.convert(delta,MILLISECONDS);
        sequence.add(this);//这种用法需要注意！！
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(
                trigger - System.nanoTime(),NANOSECONDS
        );
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask)o;
        if (trigger < that.trigger) return -1;
        if (trigger > that.trigger) return 1;
        return 0;
    }

    @Override
    public void run() {
        System.out.println(this + " ");
    }

    @Override
    public String toString() {
        return String.format("[%1$-4d]",delta) + " Task " + id;
    }

    public String summary() {
        return "(" + id
                 + ":" +delta + ")";
    }
    public static class EndSentinel extends DelayedTask {
        private ExecutorService executorService;
        public EndSentinel(int delay,ExecutorService executorService){
            super(delay);
            this.executorService = executorService;
        }
        public void run() {
            for (DelayedTask task:
                    sequence) {
                System.out.println(task.summary() + " ");
            }
            System.out.println();
            System.out.println(this + "Calling shutdownNow()");
            executorService.shutdownNow();
        }
    }
}
class DelayTaskConsumer implements Runnable {

    private DelayQueue<DelayedTask> taskDelayQueue;

    public DelayTaskConsumer(DelayQueue<DelayedTask> taskDelayQueue) {
        this.taskDelayQueue = taskDelayQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                taskDelayQueue.take().run();//Run task with th current thread
            }
        } catch (InterruptedException e) {
//            Acceptable way t exit
        }
        System.out.println("Finished DelayedTaskConsumer");
    }
}
public class DelayQueueDemo {
    public static void main(String[] args) {
        Random random = new Random(47);
        ExecutorService executorService = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> taskDelayQueue = new DelayQueue<>();
//        Fill with tasks that have random delays;
        for (int i = 0; i <20; i++) {
            taskDelayQueue.put(new DelayedTask(random.nextInt(5000)));
        }
//        Set the stopping point
        taskDelayQueue.add(new DelayedTask.EndSentinel(5000,executorService));
        executorService.execute(new DelayTaskConsumer(taskDelayQueue));
    }
}
