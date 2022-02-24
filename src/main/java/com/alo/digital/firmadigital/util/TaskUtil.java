package com.alo.digital.firmadigital.util;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TaskUtil {

    private static String PREFIX = "xml#" + Integer.toHexString(new Random().nextInt(240) + 16);
    private static AtomicLong count = new AtomicLong(1);
    private static ExecutorService executor = Executors.newCachedThreadPool(r -> new Thread(r, PREFIX + count.getAndIncrement()));

    private TaskUtil() {
        //PREFIX += PREFIX + x.substring(11);
        //System.out.println(x);
    }

    public static Object exec(Callable task) throws Exception {
        //return submit(timeout, task);
        return submitWithOutTime(task);
    }

    public static void exec(Runnable task) throws Exception {
//        submit(timeout, task);
        submitWithOutTime(task);
    }

    @SuppressWarnings("unchecked")
    private static Object submit(int timeout, Object task) throws Exception {
        if (task == null) throw new IllegalArgumentException("Null 'task' is not allowed");
        if (timeout < 1) throw new IllegalArgumentException("Timeout must be greater than 0");

        Future future = null;

        try {
            if (task instanceof Callable) {
                future = executor.submit((Callable) task);
            } else {
                future = executor.submit((Runnable) task);
            }

            return future.get(timeout, MILLISECONDS);
        } catch (Exception e) {
            if (future != null) future.cancel(true);

            throw e;
        }
    }


    @SuppressWarnings("unchecked")
    private static Object submitWithOutTime(Object task) throws Exception {
        if (task == null) throw new IllegalArgumentException("Null 'task' is not allowed");

        Future future = null;

        try {
            if (task instanceof Callable) {
                future = executor.submit((Callable) task);
            } else {
                future = executor.submit((Runnable) task);
            }


            return future.get();
        } catch (Exception e) {
            if (future != null) future.cancel(true);

            throw e;
        }
    }

}