package com.neoniou.daily.util;

import java.util.concurrent.*;

/**
 * @author Neo.Zzj
 * @date 2020/7/17
 */
public class ThreadUtil {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
