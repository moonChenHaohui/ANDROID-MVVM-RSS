package com.moon.myreadapp.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by moon on 15/12/6.
 */
public class VibratorHelper {

    public enum TIME{
        SHORT(100),
        NORMAL(200),
        LONG(400);

        long time;
        private TIME(long t){
            this.time = t;
        }
    }
    private static Vibrator vibrator;

    private static Vibrator getInstance(){
        if (vibrator == null){

          vibrator = (Vibrator) Globals.getApplication().getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibrator;
    }
    public static void shock(TIME time){
     getInstance().vibrate(time.time);
    }
}
