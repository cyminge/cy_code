package com.cy.aa;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class TestForkJoinPool2 {

    private static int count;
    
    public static void main(String[] args) {
        
        // 用一个 ForkJoinPool 实例调度“总任务”，然后敬请期待结果……
        long start = System.currentTimeMillis();
        Integer count1 = new ForkJoinPool().invoke(new CountingTask(Paths.get("D:/")));
        System.out.println("count2:" + count1+", spend time:"+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        getFileCount(Paths.get("D:/"));
        System.out.println("count1:"+count+", spend time:"+(System.currentTimeMillis()-start));
        
        start = System.currentTimeMillis();
        final Future<Integer> future = new ForkJoinPool().submit(new CountingTask(Paths.get("D:/")));
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                future.cancel(true);
                
            }
        }).start();
        
        System.out.println("spend time:"+(System.currentTimeMillis()-start));
        try {
            System.out.println("count3:" + future.get()+", spend time:"+(System.currentTimeMillis()-start));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private static void getFileCount(Path path) {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for(Path subPath : ds) {
                if(Files.isDirectory(subPath, LinkOption.NOFOLLOW_LINKS)) {
                    getFileCount(subPath);
                } else {
                    count ++;
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}

class CountingTask extends RecursiveTask<Integer> {
    private Path dir;

    public CountingTask(Path dir) {
        this.dir = dir;
    }

    @Override
    protected Integer compute() {
        int count = 0;
        List<CountingTask> subTasks = new ArrayList<>();

        // 读取目录 dir 的子路径。
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path subPath : ds) {
                if (Files.isDirectory(subPath, LinkOption.NOFOLLOW_LINKS)) {
                    // 对每个子目录都新建一个子任务。
                    subTasks.add(new CountingTask(subPath));
                } else {
                    // 遇到文件，则计数器增加 1。
                    count++;
                }
            }

            if (!subTasks.isEmpty()) {
                // 在当前的 ForkJoinPool 上调度所有的子任务。
                for (CountingTask subTask : invokeAll(subTasks)) {
                    count += subTask.join();
                }
            }
        } catch (IOException ex) {
//            ex.printStackTrace();
            return 0;
        }
        return count;
    }
}
