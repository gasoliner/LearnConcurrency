package cn.learn.concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ww on 2017/9/27.
 */
public class FastSimulation {
    static final int N_ELEMENTS = 100000;
    static final int N_GENS = 30;
    static final int N_EVOLVERS = 50;
    static final AtomicInteger[][] GRID =
            new AtomicInteger[N_ELEMENTS][N_GENS];
    static Random random = new Random(47);
    static class Evoler implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
//                Randomly select an element to work on
                int element = random.nextInt(N_ELEMENTS);
                for (int i = 0; i < N_GENS; i++) {
                    int previous = element - 1;
                    if (previous < 0) previous = N_ELEMENTS - 1;
                    int next = element + 1;
                    if (next >= N_ELEMENTS) next = 0;
                    int oldValue = GRID[element][i].get();
//                    Perform some kind of modeling calculation
                    int newValue = oldValue +
                            GRID[previous][i].get() + GRID[next][i].get();
                    newValue /= 3;   // Average the three values
                    if (!GRID[element][i].compareAndSet(oldValue,newValue)) {
                        /***
                         * Policy here to deal with failure. Here, we
                         * just report it and ignore it; our model
                         * will eventually deal with it,
                         */
                        System.out.println("OldValue changed from " + oldValue);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < N_ELEMENTS; i++) {
            for (int j = 0; j < N_GENS; j++) {
                GRID[i][j] = new AtomicInteger(random.nextInt(1000));
            }
        }
        for (int i = 0; i < N_EVOLVERS; i ++) {
            executorService.execute(new Evoler());
        }
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
    }
}
