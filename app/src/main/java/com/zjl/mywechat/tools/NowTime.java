package com.zjl.mywechat.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dllo on 16/10/31.
 */

public class NowTime {

    public static String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
}
