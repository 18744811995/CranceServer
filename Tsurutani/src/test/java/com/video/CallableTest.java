package com.video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableTest {
    public static void main(String[] ages) throws ExecutionException, InterruptedException {
        String locaFilePath="testetestetetst";
        //1. 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        List<Future<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int num=i;
            Future<String> future = pool.submit(new Callable<String>(){
                @Override
                public String call() throws Exception {
                    Thread.sleep(2000);
                    String data="callable;"+locaFilePath+";i="+num;
                    System.out.println("call执行了"+num);
                    return data;
                }

            });
            list.add(future);
        }
        pool.shutdown();
        for (Future<String> future : list) {
            System.out.println(future.get());
        }
    }
}
