package com.raja.drools.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WXD on 2018/5/4.
 */
public class DateUtils {
    /**
     * 将时间戳转为“%s小时%s分钟%s秒”这样的字符串格式
     */
    public static  String long2Date(long time){
        String str = "%s小时%s分钟%s秒";
        BigDecimal temp = BigDecimal.valueOf(time);
        BigDecimal tempmiao = temp.divide(BigDecimal.valueOf(1000),0,BigDecimal.ROUND_HALF_UP);
        BigDecimal[] temp2 = tempmiao.divideAndRemainder(BigDecimal.valueOf(60));
        BigDecimal miao = temp2[1];
        BigDecimal fenTemp = temp2[0];
        BigDecimal[] fenTemp2 = fenTemp.divideAndRemainder(BigDecimal.valueOf(60));
        BigDecimal fen = fenTemp2[1];
        BigDecimal xiaoshiTemp = fenTemp2[0];
        return String.format(str,xiaoshiTemp,fen,miao);
    }

    /**
     *获取指定日期前一天凌晨+指定小时的时间
     */
    public static Date getPreDawnPlusTime(Date date,Integer plusHours,Integer plusMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, plusHours);
        calendar.set(Calendar.MINUTE, plusMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     *获取指定日期凌晨+指定小时的时间
     */
    public static Date getDawnPlusTime(Date date,Integer plusHours,Integer plusMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, plusHours);
        calendar.set(Calendar.MINUTE, plusMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     *获取指定日期当天凌晨时间
     */
    public static Date getDawnTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 将传入日期，加/减 1小时2分钟27秒这样的字符串，返回新的时间
     */
    public static Date plusStrTime(Date date,String plusTimeStr,Boolean isPlus) {
        String[] houArr = plusTimeStr.split("小时");
        int hour = 0;
        String minuStr;
        if (houArr.length > 1) {
            hour = Integer.valueOf(houArr[0]);
            minuStr = houArr[1];
        } else {
            minuStr = houArr[0];
        }
        String[] minuArr = minuStr.split("分钟");
        int minu = 0;
        String secondStr;
        if (minuArr.length > 1 ) {
            minu = Integer.valueOf(minuArr[0]);
            secondStr = minuArr[1];
        } else {
            secondStr = minuArr[0];
        }
        int second = Integer.valueOf(secondStr.split("秒")[0]);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.err.println("hour"+hour+"minu"+minu+"second"+second);

        if (true == isPlus) {
            calendar.add(Calendar.HOUR_OF_DAY,hour);
            calendar.add(Calendar.MINUTE, minu);
            calendar.add(Calendar.SECOND, second);
        } else {
            calendar.add(Calendar.HOUR_OF_DAY,-hour);
            calendar.add(Calendar.MINUTE, -minu);
            calendar.add(Calendar.SECOND, -second);
        }
        return calendar.getTime();
    }

    //传入的字符串格式为小时：分：09:30,拼上传入date的年月日，作为新日期返回
    public static Date string2Date(String dateStr,Date date){
        String[] arr = dateStr.split(":");
        Integer hour = Integer.parseInt(arr[0]);//会自动去掉第一个0，比如09
        Integer minute = Integer.parseInt(arr[1]);
        return DateUtils.getDawnPlusTime(date,hour,minute);
    }
}
