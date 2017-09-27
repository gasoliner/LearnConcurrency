//package cn.learn.concurrency;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by Ww on 2017/9/20.
// */
//class Car {
//    private boolean waxOn = false;
//    public synchronized void waxed() {
//        waxOn = true;
//        notifyAll();
//    }
//    public synchronized void buffed() {
//        waxOn = false;
//        notifyAll();
//    }
//    public synchronized void waitForWaxing() throws InterruptedException {
//        while (waxOn == false) {
//            wait();
//        }
//    }
//    public synchronized void waitForBuffed() throws InterruptedException {
//        while (waxOn == true) {
//            wait();
//        }
//    }
//
//}
//
//class WaxOn implements Runnable {
//    private Car car;
//
//    public WaxOn(Car car) {
//        this.car = car;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!Thread.interrupted()) {
//                System.out.println("Wax On!");
//                TimeUnit.MILLISECONDS.sleep(200);
//                car.waxed();
//                car.waitForBuffed();
//            }
//        } catch (InterruptedException e) {
//            System.out.println("Exiting via interrupt");
//        }
//        System.out.println("Ending Wax On task");
//    }
//}
//class WaxOff implements Runnable {
//    private Car car;
//
//    public WaxOff(Car car) {
//        this.car = car;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!Thread.interrupted()) {
//                car.waitForWaxing();
//                System.out.println("Wax Off! ");
//                TimeUnit.MILLISECONDS.sleep(200);
//                car.buffed();//任务被挂起并释放当前对象的锁
//            }
//        } catch (InterruptedException e) {
//            System.out.println("Exiting via interrupt! ");
//        }
//        System.out.println("Ending Wax Off task");
//    }
//}
//
//public class WaxOMatic {
//    public static void main(String[] args) throws InterruptedException {
//        Car car = new Car();
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute(new WaxOff(car));
//        executorService.execute(new WaxOn(car));
//        TimeUnit.SECONDS.sleep(5);
//        executorService.shutdownNow();
//    }
//}
