package com.example.hawkergo.utils.ui;

import android.os.SystemClock;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * OnClickListener with a debouncer
 * Prevents multiple clicks from UI.
 * Blocks clicks between previousClickTimeStamp and previousClickTimeStamp + MIN_INTERVAL
 *
 * Implementation was done and modified while referring to the following stackoverflow post answered by GreyBeardedGeek
 *      https://stackoverflow.com/questions/16534369/avoid-button-multiple-rapid-clicks
 */

public abstract class DebouncedOnClickListener implements View.OnClickListener {

    // allowed click interval
    private final long MIN_INTERVAL = 500;
    // track previous clicks
    private Map<Integer, Long> lastClickMap;
    // to be implemented in activity
    public abstract void onDebouncedClick(View view);

    public DebouncedOnClickListener() {
        lastClickMap = new HashMap<>();
    }

    @Override
    public void onClick(View clickedView) {
        int viewId = clickedView.getId();
        Long previousClickTimestamp = lastClickMap.containsKey(viewId) ? lastClickMap.get(viewId) : null;
        Long currentTimestamp = System.currentTimeMillis();
        lastClickMap.put(viewId, currentTimestamp);
        boolean validOnClickEvent = previousClickTimestamp == null || currentTimestamp - previousClickTimestamp > MIN_INTERVAL;
        if(validOnClickEvent) {
            onDebouncedClick(clickedView);
        }
    }
}