package com.moon.myreadapp.common.event;

import com.moon.appframework.event.XEvent;
import com.moon.myreadapp.mvvm.models.SyncState;

/**
 * Created by moon on 16/1/19.
 */
public class SynchronizeStateEvent implements XEvent {

    private static SyncState syncState;

    public static SyncState getInstance(){
        if (syncState == null){
            syncState = new SyncState();
        }
        return syncState;
    }

    public SynchronizeStateEvent() {
        syncState = getInstance();
    }

    public SyncState getSyncState() {
        return syncState;
    }

}
