package cn.learn.concurrency;

/**
 * Created by Ww on 2017/9/26.
 */
public class Fat {
    private volatile double d;
    private static int counter = 0;
    private final int id = counter++;
    public Fat() {
        for (int i = 1; i < 10000; i++) {
            d += (Math.PI + Math.E) / (double)i;
        }
    }
    public void operation() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Fat{" +
                "d=" + d +
                ", id=" + id +
                '}';
    }
}
