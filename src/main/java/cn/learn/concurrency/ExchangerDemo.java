//package cn.learn.concurrency;
//
//import sun.nio.cs.Surrogate;
//
//import java.util.List;
//import java.util.concurrent.*;
//
///**
// * Created by Ww on 2017/9/27.
// */
//class ExchangerProducer<T> implements Runnable {
//    private Surrogate.Generator generator;
//    private Exchanger<List<T>> exchanger;
//    private List<T> holder;
//
//    public ExchangerProducer(Surrogate.Generator generator, Exchanger<List<T>> exchanger, List<T> holder) {
//        this.generator = generator;
//        this.exchanger = exchanger;
//        this.holder = holder;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!Thread.interrupted()) {
//                for (int i = 0; i < ExchangerDemo.size; i++) {
//                    holder.add(generator.next());
////                    Exchange full for empty
//                    holder = exchanger.exchange(holder);
//                }
//            }
//        } catch (InterruptedException e) {
////            OK to terminate this way
//        }
//    }
//}
//class ExchangerConsumer<T> implements Runnable {
//    private Exchanger<List<T>> exchanger;
//    private List<T> holder;
//    private volatile T value;
//
//    public ExchangerConsumer(Exchanger<List<T>> exchanger, List<T> holder) {
//        this.exchanger = exchanger;
//        this.holder = holder;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!Thread.interrupted()) {
//                holder = exchanger.exchange(holder);
//                for (T x:
//                        holder) {
//                    value = x;
//                    holder.remove(x);
//                }
//            }
//        } catch (InterruptedException e) {
////            OK to terminate this way;
//        }
//        System.out.println("Final value: " + value);
//    }
//}
//public class ExchangerDemo {
//    static int size = 10;
//    static int delay = 5;
//
//    public static void main(String[] args) throws InterruptedException {
//        if (args.length > 0) {
//            size = new Integer(args[0]);
//        }
//        if (args.length > 1) {
//            delay = new Integer(args[1]);
//        }
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        Exchanger<List<Fat>> exchanger = new Exchanger<>();
//        List<Fat>
//                producerList = new CopyOnWriteArrayList<>(),
//                consumerList = new CopyOnWriteArrayList<>();
//        executorService.execute(new ExchangerProducer<Fat>(exchanger,BasicGenerator.create(Fat.class),producerList));
//        executorService.execute(new ExchangerConsumer<Fat>(exchanger,consumerList));
//        TimeUnit.SECONDS.sleep(delay);
//        executorService.shutdownNow();
//    }
//}
