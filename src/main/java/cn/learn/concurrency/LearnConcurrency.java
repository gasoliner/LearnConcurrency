package cn.learn.concurrency;

/**
 * Hello world!
 *
 */
public class LearnConcurrency implements Runnable {
    protected int countDown = 10;
    private static int taskCount = 0;
//    final初始化后，不希望被修改
    private final int id = taskCount++;
    public LearnConcurrency(){}
    public LearnConcurrency(int countDown) {
        this.countDown = countDown;
    }
    public String status() {
        return "#" + id + "(" + (countDown >0 ?countDown :"Liftoff!") +"),";
    }

    public void run() {
        while (countDown-- > 0) {
            System.out.print(status());
//            对线程调度器建议：我已经执行完生命周期中最重要的部分了，此刻正是切换给其他任务执行一段时间的大好时机
            Thread.yield();
        }
    }
}
