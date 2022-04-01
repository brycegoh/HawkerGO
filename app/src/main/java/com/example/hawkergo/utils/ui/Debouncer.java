package com.example.hawkergo.utils.ui;

import android.view.View;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Implementation had modifications but was done with reference from
 *  Stackoverflow user simon04 from the following link:
 *  https://stackoverflow.com/questions/4742210/implementing-debounce-in-java
 * */

public class Debouncer {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final HashMap<Object, ScheduledFuture<?>> tracker = new HashMap<>();

    final long TIME_DELAY = 1000;
    final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    public void debounce(Object key, Runnable fn) {
        ScheduledFuture<?> scheduledTask = tracker.containsKey(key) ? tracker.get(key) : null;
        if (scheduledTask != null) {
            scheduledTask.cancel(false); // don't interrupt if is running
        } else {
            tracker.put(key, (ScheduledFuture<?>) executor.schedule(
                    new Runnable() {
                @Override
                public void run() {
                    try{
                        fn.run();
                    }
                    catch(Exception e){
                        System.out.println("======================");
                        System.out.println(e.getMessage().toString());
                    }
                    finally {
                        tracker.remove(key);
                    }
                }
            }, TIME_DELAY, TIME_UNIT));
        }

    }


}
