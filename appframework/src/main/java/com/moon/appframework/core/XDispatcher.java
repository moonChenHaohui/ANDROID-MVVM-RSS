package com.moon.appframework.core;

import android.content.Context;
import android.os.Bundle;


import com.moon.appframework.action.EventAction;
import com.moon.appframework.action.RouterAction;
import com.moon.appframework.action.XAction;
import com.moon.appframework.common.router.XRouter;

import de.halfbit.tinybus.TinyBus;

/**
 * Created by kidcrazequ on 15/8/15.
 */
public class XDispatcher {

    private static TinyBus bus = XApplication.getInstance().bus;
    private Context mContext;

    public static XDispatcher from(final Context context){
        return new XDispatcher(context);
    }

    private XDispatcher(final Context context){
        mContext = context;
    }

    public void dispatch(XAction action){
        if(action == null) return;
        switch (action.type){
            case NETWORK:
                bus.post(action);
                break;
            case EVENT:
                bus.post(((EventAction)action).event);
                break;
            case ROUTER:
                RouterAction routerAction = (RouterAction) action;
                if(routerAction.isNative){
                    if(routerAction.isActivity){
                        XRouter.forward(mContext, routerAction.clazz, routerAction.args == null ? new Bundle() : routerAction.args);
                    }else{
                        XRouter.forward(routerAction.clazz, routerAction.args == null ? new Bundle() : routerAction.args);
                    }
                }else {
                    XRouter.forward(mContext, routerAction);
                }
                break;
        }
    }

    public static void register(Object obj){
        bus.register(obj);
    }

    public static void unregister(Object obj){
        bus.unregister(obj);
    }
}
