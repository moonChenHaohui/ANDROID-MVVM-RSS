package com.moon.appframework.core;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import de.halfbit.tinybus.TinyBus;

/**
 * Created by kidcrazequ on 15/8/24.
 */
public class XApplication extends Application{

    private static XApplication application;
    public TinyBus bus;

    public static XApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        application = this;
        // Event Bus init
        bus = TinyBus.from(this);


    }

}
