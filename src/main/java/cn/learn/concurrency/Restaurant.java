//package cn.learn.concurrency;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by Ww on 2017/9/20.
// */
//class Meal {
//    private final int orderNum;
//
//    public Meal(int orderNum) {
//        this.orderNum = orderNum;
//    }
//
//    @Override
//    public String toString() {
//        return "Meal " + orderNum;
//    }
//}
//class WaitPerson implements Runnable {//服务员
//    private Restaurant restaurant;
//
//    public WaitPerson(Restaurant restaurant) {
//        this.restaurant = restaurant;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!Thread.interrupted()) {
//                synchronized (this) {
//                    while (restaurant.meal == null) {
//                        wait();//...for the chef to 做这个食物
//                    }
//                }
//                System.out.println("-Wait person got " + restaurant.meal);
//                synchronized (restaurant.chef) {
//                    restaurant.meal = null;
//                    restaurant.chef.notifyAll();// Ready for another
//                }
//            }
//        } catch (InterruptedException e) {
//            System.out.println("WaitPerson interrupted");
//        }
//    }
//}
//class Chef implements Runnable {
//    private Restaurant restaurant;
//    private int count = 0;
//
//    public Chef(Restaurant restaurant) {
//        this.restaurant = restaurant;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!Thread.interrupted()) {
//                synchronized (this) {
//                    while (restaurant.meal != null) {
//                        wait();//...for the meal to 被端走
//                    }
//                }
//                if (++count == 10) {
//                    System.out.println("Out of food, closing");
//                    restaurant.exec.shutdownNow();
//                }
//                System.out.println("Order up ! ");
//                synchronized (restaurant.waitPerson) {
//                    restaurant.meal = new Meal(count);
//                    restaurant.waitPerson.notifyAll();
//                    System.out.println("-");
//                }
//                TimeUnit.MILLISECONDS.sleep(100);
//            }
//        } catch (InterruptedException e) {
//            System.out.println("Chef interrupted");
//        }
//    }
//}
//public class Restaurant {
//    Meal meal;
//    ExecutorService exec = Executors.newCachedThreadPool();
//    WaitPerson waitPerson = new WaitPerson(this);
//    Chef chef = new Chef(this);
//
//    public Restaurant() {
//        exec.execute(chef);
//        exec.execute(waitPerson);
//    }
//
//    public static void main(String[] args) {
//        new Restaurant();
//    }
//}
