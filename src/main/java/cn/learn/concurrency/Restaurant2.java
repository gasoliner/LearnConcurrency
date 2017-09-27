package cn.learn.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ww on 2017/9/21.
 */
class Meal {
    private final int orderNum;

    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Meal " + orderNum;
    }
}
class WaitPerson implements Runnable {//服务员
    private Restaurant2 restaurant;

    public Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public WaitPerson(Restaurant2 restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {


                try {
                    lock.lock();
                    while (restaurant.meal == null) {
                        condition.await();//...for the chef to 做这个食物
                    }
                } finally {
                    lock.unlock();
                }
//                synchronized (this) {
//                    while (restaurant.meal == null) {
//                        wait();//...for the chef to 做这个食物
//                    }
//                }
                System.out.println("-Wait person got " + restaurant.meal);
//                synchronized (restaurant.chef) {
//                    restaurant.meal = null;
//                    restaurant.chef.notifyAll();// Ready for another
//                }

                try {
                    restaurant.chef.lock.lock();
                    restaurant.meal = null;
                    restaurant.chef.condition.signalAll();
                } finally {
                    restaurant.chef.lock.unlock();
                }

            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson interrupted");
        }
    }
}
class Chef implements Runnable {
    private Restaurant2 restaurant;
    private int count = 0;

    public Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public Chef(Restaurant2 restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                synchronized (this) {
//                    while (restaurant.meal != null) {
//                        wait();//...for the meal to 被端走
//                    }
//                }

                try {
                    lock.lock();
                    while (restaurant.meal != null) {
                        condition.await();//...for the meal to 被端走
                    }
                } finally {
                    lock.unlock();
                }

                if (++count == 10) {
                    System.out.println("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                System.out.println("Order up ! ");


                try {
                    restaurant.waitPerson.lock.lock();
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.condition.signalAll();
                    System.out.println("-");
                } finally {
                    restaurant.waitPerson.lock.unlock();
                }


//                synchronized (restaurant.waitPerson) {
//                    restaurant.meal = new Meal(count);
//                    restaurant.waitPerson.notifyAll();
//                    System.out.println("-");
//                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Chef interrupted");
        }
    }
}
public class Restaurant2 {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);

    public Restaurant2() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }

    public static void main(String[] args) {
        new Restaurant2();
    }
}
