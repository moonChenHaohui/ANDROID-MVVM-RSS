package com.moon.myreadapp.util;

/**
 * Created by barry on 15/9/10.
 */
public class BuiltConfig {

    //禁止实例化
    private BuiltConfig() {

    }

    /**
     * 获取配置项的布尔值。如果配置项中写的字符串是“0”即判断为false，否则为true。
     * @param configResId 配置项的资源id
     * @return 某个配置功能是否打开
     */
    public static boolean getBoolean(int configResId) {
        String configString = Globals.getApplication().getString(configResId);
        return !("0".equals(configString));
    }

    /**
     * 获取配置项的整数值。
     * @param configResId 配置项的资源id
     * @return 某个配置项的整数值
     */
    public static int getInt(int configResId) {
        return Integer.parseInt(Globals.getApplication().getString(configResId));
    }

    /**
     * 获取配置项的字符串。
     * @param configResId 配置项的资源id
     * @return 某个配置项的字符串
     */
    public static String getString(int configResId) {
        return Globals.getApplication().getString(configResId);
    }
    public static String getString(int configResId, Object... formatArgs) {
        return Globals.getApplication().getString(configResId,formatArgs);
    }
}
