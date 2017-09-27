package cn.learn.concurrency;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Ww on 2017/9/11.
 */
public class MyTest {

    @Test
    public void test1() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results =
                new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(executorService.submit(new TaskWithResult(i)));
        }
        for (Future<String> future:
                results) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
    }

    @Test
    public void test2() {
        Date date = new Date();
        int count = 10000;
        while (count-- != 0) {
            System.out.println(count);
        }
        Date date1 = new Date();
        System.out.println(date1.getTime() - date.getTime());
    }

    @Test
    public void test3_union() {
        union("F:\\huakai\\1","F:\\huakai\\2\\1.ts");
    }

    public void union(String dirPath, String toFilePath) {
        File dir = new File(dirPath);
        if (!dir.exists())
            return;
        File videoPartArr[] = dir.listFiles();
        if (videoPartArr.length == 0)
            return;
        File combineFile = new File(toFilePath);
        try (FileOutputStream writer = new FileOutputStream(combineFile)) {
            byte buffer[] = new byte[1024];
            for (File part : videoPartArr) {
                try (FileInputStream reader = new FileInputStream(part)) {
                    while (reader.read(buffer) != -1) {
                        writer.write(buffer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
