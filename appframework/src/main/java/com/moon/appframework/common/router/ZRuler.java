package com.moon.appframework.common.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.moon.appframework.action.RouterAction;
import com.moon.appframework.common.properties.RuleProperties;
import com.moon.appframework.common.util.StringUtils;
import com.moon.appframework.core.XActivity;
import com.moon.appframework.core.XFragment;
import com.moon.appframework.core.XFragmentManager;

import java.util.Map;

/**
 * @author kidcrazequ
 *
 */
public class ZRuler {

    private RuleProperties rulePropertiese = null;

    private ZRuler(final RuleProperties properties){
        this.rulePropertiese = properties;
    }

    public void rule(Context context, RouterAction action, String url, Map<String, Object> map) {

        if(rulePropertiese == null) return;

        String className = rulePropertiese.getString(url, "");

        if(StringUtils.isEmpty(className)) return;

        Bundle args = mapToBundle(map);

        if(action.isActivity){
            Class<? extends XActivity> xaclass = null;
            try {
                xaclass = (Class<? extends XActivity>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(context, xaclass);
            intent.putExtras(args);
            context.startActivity(intent);
        }else {
            Class<? extends XFragment> xfclass = null;
            try {
                xfclass = (Class<? extends XFragment>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            XFragmentManager.add(xfclass, args);
        }
    }

    public static ZRuler createRule(final RuleProperties properties) {
       return new ZRuler(properties);
    }

    private Bundle mapToBundle(Map<String, Object> map){
        Bundle bundle = new Bundle();

        if(map.isEmpty()){
            return bundle;
        }

        for(String key : map.keySet()){
            Object value = map.get(key);
            if(value instanceof Integer){
                bundle.putInt(key, (Integer)value);
            }else if(value instanceof Boolean){
                bundle.putBoolean(key, (Boolean)value);
            }else if(value instanceof Short){
                bundle.putShort(key, (Short)value);
            }else if(value instanceof Long){
                bundle.putLong(key, (Long)value);
            }else if(value instanceof Float){
                bundle.putFloat(key, (Float)value);
            }else if(value instanceof Double) {
                bundle.putDouble(key, (Double) value);
            }else if(value instanceof String){
                bundle.putString(key, (String) value);
            }
        }
        return bundle;
    }
}
