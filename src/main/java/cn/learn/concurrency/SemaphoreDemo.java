package cn.learn.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ww on 2017/9/26.
 */
class CheckoutTask<T> implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private Pool<T> pool;

    public CheckoutTask(Pool<T> pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            T item = pool.checkOut();
            System.out.println(this + "checking out " + item);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(this + "checking in " + item);
            pool.checkIn(item);
        } catch (InterruptedException e) {
//            Acceptable way to terminate
        }
    }

    @Override
    public String toString() {
        return "CheckoutTask{" +
                "id=" + id +
                ", pool=" + pool +
                '}';
    }
}
public class SemaphoreDemo {
    final static int SIZE = 25;

    public static void main(String[] args) throws Exception {
        final Pool<Fat> pool =
                new Pool<>(Fat.class,SIZE);
        ExecutorService executorService =
                Executors.newCachedThreadPool();
        for (int i = 0; i < SIZE; i++) {
            executorService.execute(new CheckoutTask<>(pool));
        }
        System.out.println("All CheckoutTasks created");
        List<Fat> fatList = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            Fat fat = pool.checkOut();
            System.out.println(i + ": main() thread checked out ");
            fat.operation();
            fatList.add(fat);
        }
        Future<?> blocked = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
//                Semaphore prevents additional checkout
//                so call is blocked
                    pool.checkOut();
                } catch (InterruptedException e) {
                    System.out.println("checkOut() Interrupted");
                }

            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);
        System.out.println("Checking in objects in " + fatList);
        for (Fat f :
             fatList) {
           pool.checkIn(f);
        }
        for (Fat f :
                fatList) {
            pool.checkIn(f);
        }
        executorService.shutdown();
    }
}
