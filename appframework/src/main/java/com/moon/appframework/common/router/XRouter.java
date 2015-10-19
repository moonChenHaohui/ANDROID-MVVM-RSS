package com.moon.appframework.common.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.properties.RuleProperties;
import com.moon.appframework.core.XApplication;
import com.moon.appframework.core.XFragment;
import com.moon.appframework.core.XFragmentManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kidcrazequ
 *
 */
public class XRouter {

    private static Map<String, Object> queryParames = new HashMap<String, Object>();

    private XRouter(){
    }

    public static void forward(Context context, RouterAction action){
        // parser uri params
        Uri uri = Uri.parse(action.url);
        queryParames = new UrlParser().parse(uri.getQuery());
        // rule
        RuleProperties ruleProperties = new RuleProperties(XApplication.getInstance());
        ZRuler.createRule(ruleProperties).rule(context, action, action.url, queryParames);
    }

    public static void forward(Class<?> clazz, Bundle args){
        XFragmentManager.add((Class<? extends XFragment>) clazz, args);
    }

    public static void forward(Context context, Class<?> clazz, Bundle args){
        Intent intent = new Intent(context, clazz);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    private static Object getQueryParameters(String key){
        return queryParames.get(key);
    }




}
