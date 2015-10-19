package com.moon.myreadapp.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Random;

/**
 * 获取手机信息，例如imei、imsi、mac地址等等
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class PhoneInfo {

    public static final String IMEI = "imei";
    public static final String IMSI = "imsi";
    public static final String MACADDRESS = "mac_address";

    private static String phone_imei = "";
    private static String phone_imsi = "";
    private static String phone_wifiaddr = "";

    private static String generateImei() {

        StringBuffer imei = new StringBuffer();

        // 添加当前秒数 毫秒数 5位
        long time = System.currentTimeMillis();
        String currentTime = Long.toString(time);
        imei.append(currentTime.substring(currentTime.length() - 5));

        // 手机型号 6位
        StringBuffer model = new StringBuffer();
        model.append(Build.MODEL.replaceAll(" ", ""));
        while (model.length() < 6) {
            model.append('0');
        }
        imei.append(model.substring(0, 6));

        // 随机数 4位
        Random random = new Random(time);
        long tmp = 0;
        while (tmp < 0x1000) {
            tmp = random.nextLong();
        }

        imei.append(Long.toHexString(tmp).substring(0, 4));

        return imei.toString();

    }

    /**
     * 获取imei，如果系统不能获取，则将动态产生一个唯一标识并保存
     *
     * @param context Context实例
     * @return imsi字串
     */
    public static String getImei(Context context) {

        if (!TextUtils.isEmpty(phone_imei))
            return phone_imei;

        String imei = null;
        SharedPreferences sp = context.getSharedPreferences(IMEI,
                Context.MODE_PRIVATE);
        imei = sp.getString(IMEI, null);
        if (imei == null || imei.length() == 0) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            imei = tm.getDeviceId(); // 获取imei的方法修改
            if (imei == null || imei.length() == 0) {
                imei = generateImei();
            }
            imei = imei.replaceAll(" ", "").trim();
            // imei 小于15位补全 jiuwan
            while (imei.length() < 15) {
                imei = "0" + imei;
            }

            Editor editor = sp.edit();
            editor.putString(IMEI, imei);
            if (Build.VERSION.SDK_INT >= 9) {
                editor.apply();
            } else {
                editor.commit();
            }
        }
        phone_imei = imei.trim();
        return phone_imei;
    }

    /**
     * 获取imsi，如果系统不能获取，则将动态产生一个唯一标识并保存
     *
     * @param context ： Context实例
     * @return imsi字串
     */
    static public String getImsi(Context context) {

        if (!TextUtils.isEmpty(phone_imsi))
            return phone_imsi;

        String imsi = null;
        SharedPreferences sp = context.getSharedPreferences(IMEI,
                Context.MODE_PRIVATE);
        imsi = sp.getString(IMSI, null);
        if (imsi == null || imsi.length() == 0) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
            if (imsi == null || imsi.length() == 0) {
                imsi = generateImei();
            }
            imsi = imsi.replaceAll(" ", "").trim();
            // imei 小于15位补全 jiuwan
            while (imsi.length() < 15) {
                imsi = "0" + imsi;
            }
            Editor editor = sp.edit();
            editor.putString(IMSI, imsi);
            if (Build.VERSION.SDK_INT >= 9) {
                editor.apply();
            } else {
                editor.commit();
            }
        }

        phone_imsi = imsi;
        return phone_imsi;
    }


    /**
     * 获取原始的imei，如果没有返回空字符串，
     *
     * @param context : Context实例
     * @return 手机原生imei，获取失败则返回null
     */
    static public String getOriginalImei(Context context) {

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei != null)
            imei = imei.trim();
        return imei;
    }

    /**
     * 获取原始的imsi，如果没有返回空字符串，
     *
     * @param context : Context实例
     * @return 原生imsi，获取失败则返回null
     */
    static public String getOriginalImsi(Context context) {

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        if (imsi != null)
            imsi = imsi.trim();
        return imsi;
    }


}
