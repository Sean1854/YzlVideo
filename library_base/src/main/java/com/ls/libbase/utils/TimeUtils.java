package com.ls.libbase.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    /**
     * 将时间（秒）转为 年-月-日 的形式
     *
     * @param timestamp
     * @return
     */
    public static String convertTimestampToDate(long timestamp) {
        //服务端返回的是秒，需要先转为毫秒
        long time = timestamp * 1000;
        // 创建一个SimpleDateFormat对象，指定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 使用Date类将时间戳转换为日期对象
        Date date = new Date(time);
        // 格式化日期对象为字符串
        return sdf.format(date);
    }
}
