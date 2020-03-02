package org.phoenix.aladdin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @param date 给定标准格式UTC时间
     * @return 毫秒数
     */
    public static long getMillionsTime(String date) throws ParseException {
        return DateFormat.getDateTimeInstance().parse(date).getTime();
    }
}
