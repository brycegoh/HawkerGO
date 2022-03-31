package com.example.hawkergo.utils.ui;

import android.view.View;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class DebouncedOnClickListener implements View.OnClickListener {

    public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    HashMap<Integer, ScheduledFuture<?>> tracker = new HashMap<>();

    final long TIME_DELAY = 1000;
    final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    public abstract void onDebouncedClick(View view);

    @Override
    public void onClick(View view) {

        Integer key = view.getId();
        ScheduledFuture<?> scheduledTask = tracker.containsKey(key) ? tracker.get(key) : null;
        if (scheduledTask != null) {
            scheduledTask.cancel(false); // don't interrupt if is running
        } else {
            tracker.put(key, (ScheduledFuture<?>) executor.schedule(new Runnable() {
                @Override
                public void run() {
                    onDebouncedClick(view);
                }
            }, TIME_DELAY, TIME_UNIT));
        }

    }
}
