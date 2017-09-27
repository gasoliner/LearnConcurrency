package cn.learn.concurrency;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ww on 2017/9/21.
 */
class Sender implements Runnable {
    private Random random = new Random(47);
    private PipedWriter writer = new PipedWriter();

    public PipedWriter getWriter() {
        return writer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (char c = 'A'; c <= 'z'; c++) {
                    writer.write(c);
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(500));
                }
            }
        } catch (InterruptedException e) {
            System.out.println("writer got InterruptedException !");
        } catch (IOException e) {
            System.out.println("writer got IOException !");
        }
    }
}
class Receiver implements Runnable {
    private PipedReader reader;

    public Receiver(Sender sender) throws IOException {
        reader = new PipedReader(sender.getWriter());
    }

    @Override
    public void run() {
        try {
            while (true) {
//                blocks until characters are there:
                System.out.println("Read: " + (char)reader.read() + ".");
            }
        } catch (IOException e) {
            System.out.println("Receiver got IOException !");
        }
    }
}
public class PipedIO {
    public static void main(String[] args) throws IOException, InterruptedException {
        Sender sender = new Sender();
        Receiver receiver = new Receiver(sender);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(sender);
        executorService.execute(receiver);
        TimeUnit.SECONDS.sleep(10);
        executorService.shutdownNow();
    }
}
