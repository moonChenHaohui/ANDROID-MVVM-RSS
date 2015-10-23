package com.moon.myreadapp.common.adapter.base;

import android.support.annotation.StringRes;

import com.moon.myreadapp.R;
import com.moon.myreadapp.util.BuiltConfig;
import com.moon.myreadapp.util.Globals;

/**
 * Created by moon on 15/10/23.
 */
public class FeedAdapterHelper {


    public static enum TimeType {
        TODATY(1,R.string.time_today),
        YESTORY(2,R.string.time_yesterday),
        BEFORE_YESTORY(3,R.string.time_before_yesterday),
        BBEFORE_YESTORY(4,R.string.time_bbefore_yesterday),
        MORE_BEFORE(5,R.string.time_more_before);

        private final int id;
        private final String type;

        TimeType(int id,@StringRes int resId){
            this.id = id;
            this.type = BuiltConfig.getString(resId);
        }

        public int getId() {
            return id;
        }


        public String getType() {
            return type;
        }

    }
    /**
     * 转换时间类型->list type
     * @param time
     * @return
     */
    public static TimeType getDayType (String time){
        //TODO 根据今天的时间与获取的时间做对比,得到相对应的类型
        if (time.equals("1")){
            return TimeType.TODATY;
        }
        return TimeType.YESTORY;
    }
}
