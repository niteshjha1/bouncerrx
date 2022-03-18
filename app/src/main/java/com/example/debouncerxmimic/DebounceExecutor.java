package com.example.debouncerxmimic;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * To mimic debounce operator of ReactiveX
 */
public class DebounceExecutor {
    private ScheduledFuture<Object> future;
    private ScheduledExecutorService executor;
    public static final String LOG_TAG = "DebounceRxMimic.DebounceExecutor";

    DebounceExecutor(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public void cancel() {
        if (future != null && !future.isDone())
            future.cancel(false);
    }

    public ScheduledFuture<Object> debounce(long delay, Callable<Object> task) {
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
        future = executor.schedule(task, delay, TimeUnit.MILLISECONDS);
        return future;
    }

    class InnerClass {
        /**
         * "newSingleThreadScheduledExecutor" creates an executor with a single thread that will never do anything in parallel to the existing thread.
         *  It has just one and threads can't execute more than 1 thing at a time.
         */

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
//            ScheduledExecutorService executor = Executors.newScheduledThreadPool(4) ;
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Debouncer");
                t.getState();
                t.setDaemon(true);
                Log.i(LOG_TAG, String.valueOf(t.getId()));
                return t;
            }
        });

        Callable<Object> refreshUICallableTaskA = () -> {
            Log.i(LOG_TAG, "Task A Current Thread name " + Thread.currentThread().getName());
            return null;
        };
        Callable<Object> refreshUICallableTaskB = () -> {
            Log.i(LOG_TAG, "Task B Current Thread priority " + Thread.currentThread().getPriority());
            return null;
        };

        Callable<Object> refreshUICallableTaskC = () -> {
            Log.i(LOG_TAG, "Task C Current Thread id " + Thread.currentThread().getId());
            return null;
        };

        void bounceBounce() {
            try {
                int count = 0;
                while (true) {
                    Log.i(LOG_TAG, "Time in Seconds : " + (5 * count++));
                    Thread.sleep(5000);
                    DebounceExecutor debounceExecutor = new DebounceExecutor(executor);
                    debounceExecutor.debounce(5000, refreshUICallableTaskA);
                    debounceExecutor.debounce(5000, refreshUICallableTaskB);
                    debounceExecutor.debounce(5000, refreshUICallableTaskC); // Always calls only the last task (filters out)
                }
            } catch (InterruptedException ex) {
                Log.i(LOG_TAG,"We have an exception called " + ex);
            }
        }
    }
}



