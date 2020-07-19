package com.neoniou.daily.util;

import java.util.concurrent.*;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
public class ThreadUtil {

    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(1,
            1,
            0,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());;

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void run(Runnable task) {
        THREAD_POOL.execute(task);
    }

    public static void shutdown() {
        THREAD_POOL.shutdown();
    }
}
