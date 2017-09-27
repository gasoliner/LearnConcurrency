package cn.learn.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ww on 2017/9/25.
 */
class PrioritizedTask implements Runnable,Comparable<PrioritizedTask> {

    private Random random = new Random(47);
    private static int counter = 0;
    private final int id = counter++;
    private final int priority;
    protected static List<PrioritizedTask> sequence =
            new ArrayList<>();

    public PrioritizedTask(int priority) {
        this.priority = priority;
        sequence.add(this);
    }

    @Override
    public int compareTo(PrioritizedTask o) {
        return priority < o.priority ? 1 :
                (priority > o.priority ? -1 : 0);
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(250));
        } catch (InterruptedException e) {
//            Acceptable way to exit;
        }
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("[%1$-3d]",priority) + " Task " +id;
    }

    public String summary() {
        return "(" + id + ":" + priority + ")";
    }
    public static class EndSentinel extends PrioritizedTask {
        private ExecutorService executorService;
        public EndSentinel(ExecutorService executorService) {
            super(-1);
            this.executorService = executorService;
        }
        public void run() {
            int count = 0;
            for (PrioritizedTask prioritizedTask:
                    sequence) {
                if (++count % 5 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println(this + " Calling shutdownNow()");
            executorService.shutdownNow();
        }
    }
}
class PrioritizedTaskProducer implements Runnable {

    private Random random = new Random(47);
    private Queue<Runnable> queue;
    private ExecutorService executorService;

    public PrioritizedTaskProducer(Queue<Runnable> queue, ExecutorService executorService) {
        this.queue = queue;
        this.executorService = executorService;//Used for EndSentinel
    }

    @Override
    public void run() {
//        Unbounded queue; never blocks
//        FIll it up fast with random priorities;
        for (int i = 0; i <20; i++) {
            queue.add(new PrioritizedTask(random.nextInt(10)));
            Thread.yield();
        }
//        Trickle in highest-priority jobs;
        try {
            for (int i = 0; i < 10; i++) {
                TimeUnit.MILLISECONDS.sleep(250);
                queue.add(new PrioritizedTask(10));
            }
//            Add jobs,lowest priority first;
            for (int i = 0; i < 10; i++) {
                TimeUnit.MILLISECONDS.sleep(250);
            }
//                A sentinel to stop all the tasks
            queue.add(new PrioritizedTask.EndSentinel(executorService));
        } catch (InterruptedException e) {
//            Acceptable way to exit;
        }
        System.out.println("Finished PrioritizedTaskProducer");
    }
}
class PrioritizedTaskConsumer implements Runnable {
    private PriorityBlockingQueue<Runnable> queue;
    public PrioritizedTaskConsumer(PriorityBlockingQueue<Runnable> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Use current thread to run the task;
                queue.take().run();
            }
        } catch (InterruptedException e) {
//            Acceptable way to exit;
        }
        System.out.println("Finished PrioritizedTaskConsumer");
    }
}
public class PriorityBlockingQueueDemo {
    public static void main(String[] args) {
        Random random = new Random(47);
        ExecutorService executorService = Executors.newCachedThreadPool();
        PriorityBlockingQueue<Runnable> queue =
                new PriorityBlockingQueue<>();
        executorService.execute(new PrioritizedTaskProducer(queue,executorService));
        executorService.execute(new PrioritizedTaskConsumer(queue));
    }
}
