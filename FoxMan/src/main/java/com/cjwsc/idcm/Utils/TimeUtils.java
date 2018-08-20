package com.cjwsc.idcm.Utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ASUS1 on 2016/4/18.
 * 时间类   -1  已开秒  0 正在疯抢    1 即将开秒
 */
public class TimeUtils {


    private static long sTime;

    public static Date getCurrentDataTime(int i) throws ParseException {
        return new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * i);
    }

    /**
     * 距离结束时间
     *
     * @param mTime HH:mm
     * @return
     */
    public static long getEndTime(String mTime, String mTime2) {
        return Long.parseLong(mTime) - Long.parseLong(mTime2);
    }

    /**
     * 判断两个时间之间的关系
     *
     * @param mTime1 服务器时间
     * @param mTime2
     * @return
     */
    public static long getFromEndTime(String mTime1, String mTime2) {
        return Long.parseLong(mTime2) - Long.parseLong(mTime1);
    }


    /**
     * 判读秒杀是否开始
     * mTime 时间   mServerTime服务器时间
     *
     * @return
     */
    public static boolean getJudgeStart(String mTime, String mServerTime) {
        return Long.parseLong(mTime) - Long.parseLong(mServerTime) > 0;
    }


    //判断活动的状态
    public static ArrayList<String> getTimeState(ArrayList<String> content, String mTime, int postion) {
        for (int i = 0; i < content.size(); i++) {
            int s = compare_date(content.get(i), mTime);
            if (i == postion && s < 0) {
                content.set(i, getHHmmTimes(content.get(i)) + "\n正在疯抢");
                continue;
            }
            if (s == 1) {
                content.set(i, getHHmmTimes(content.get(i)) + "\n即将开秒");
            } else {
                content.set(i, getHHmmTimes(content.get(i)) + "\n已开秒");
            }
        }
        return content;
    }

    /**
     * 比较时间大小
     *
     * @param DATE1 特定时间
     * @param DATE2 当前时间
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            /*Date dt1 = df.parse(getTimes(DATE1));
            Date dt2 = df.parse(getTimes(DATE2));*/
            DATE1=DATE1.replaceAll("T"," ");
            DATE2=DATE2.replaceAll("T"," ");
            Double time = Double.parseDouble(getTimes(DATE1));
            Double time1 = Double.parseDouble(getTimes(DATE2));

            if (time > time1) {
                return 1;
            } else if (time < time1) {
                return -1;
            } else {
                return 0; //刚好开始
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return 0;
    }


    /**
     * 比较时间大小
     *
     * @param DATE1 特定时间
     * @param DATE2 当前时间
     * @return
     */
    public static int compare_dateRemove3zero(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(getTimesRemove3zero(DATE1));
            Date dt2 = df.parse(getTimesRemove3zero(DATE2));

            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0; //刚好开始
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return 0;
    }


    //时间格式转换  yyyy-MM-dd hh:mm 转换 hh:mm
    public static long getTime(String DATE1) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date dt1 = df.parse(DATE1);
            return dt1.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Long.parseLong(null);
    }


    /**
     * 时间戳 转换为HH:mm 格式
     *
     * @param DATE1
     * @return
     */
    public static String getTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sTime = format.parse(DATE1).getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(sTime);
    }


    /**
     * 时间戳 转换为HH:mm 格式
     *
     * @param DATE1 少3个0
     * @return
     */
    public static String getTimesRemove3zero(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.valueOf(format.format(Long.valueOf(DATE1)));
    }

    /**
     * 时间戳 转换为HH:mm 格式
     *
     * @param DATE1
     * @return
     */
    public static String getHHmmTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return String.valueOf(format.format(Long.valueOf(DATE1) * 1000));
    }

    /**
     * 时间戳 转换为yyyy-MM-dd格式
     */
    public static String getDetialTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(Long.valueOf(DATE1) * 1000);
    }
    public static String getDetialTimesNot10000(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(Long.valueOf(DATE1));
    }

    /**
     * 时间戳 转换为yyyy-MM"格式
     */
    public static String getDetialTimess(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(Long.valueOf(DATE1) * 1000);
    }

    /**
     * 获取指定的时间
     */
    public static String getDetialTimess(String DATE1, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(Long.valueOf(DATE1) * 1000);
    }


    /**
     * 时间戳 转换为MM-dd 格式
     */
    public static String getDayTimes(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        BigDecimal db = new BigDecimal(DATE1);
        return format.format(db.multiply(new BigDecimal(1000)));
    }


    /**
     * 时间戳 转换为YYYY-MM 格式
     */
    public static String getY_M(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM");
        BigDecimal db = new BigDecimal(DATE1);


        return format.format(db.multiply(new BigDecimal(1000)));
    }


    public static String getYandM(String DATE1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        BigDecimal db = new BigDecimal(DATE1);


        return format.format(db.multiply(new BigDecimal(1000)));
    }


    /**
     * 时间戳转换成MM月dd日HH:mm
     *
     * @param time
     * @return
     */
    public static String getTimeForSimple(String time) {

        SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH:mm");
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }

    /**
     * 时间戳转换成yyyy-MM-ddHH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getTimeForDetail(String time) {


        SimpleDateFormat format = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }


    /**
     * 时间戳转换成yyyy-MM-ddHH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getTimeForDetail2(String time) {


        SimpleDateFormat format = new SimpleDateFormat(" yyyy年MM月dd日 HH:mm");
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }


    /**
     * 时间戳转换成"MM月dd日 HH:mm"
     *
     * @param time
     * @return
     */
    public static String getTimeForDetailForCustom(String time,String formater) {


        SimpleDateFormat format = new SimpleDateFormat(formater);
        BigDecimal db = new BigDecimal(time);


        return format.format(db.multiply(new BigDecimal(1000)));

    }

    // 将时间戳转为字符串（毫秒去掉3个0）
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        // 例如：

        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time));//z这里原来是new Date(lcc_time*1000 )
        return re_StrTime;
    }

    // 将时间戳转为字符串（毫秒去掉3个0）
    public static String getStrTimeCommon(String cc_time,String fromat) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(fromat);//"yyyy年MM月dd日 HH:mm"
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time ));//z这里原来是new Date(lcc_time*1000 )
        return re_StrTime;
    }


    /**
     * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }


    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String datatoLong(String time) {

        //  String type="yyyy-MM-dd-HH-mm-ss";
        String type = "yyyy-MM-dd";

        SimpleDateFormat sdr = new SimpleDateFormat(type,
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String String2Timestap(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long beginUseTime=0;
        try {
            beginUseTime = sdf.parse(time).getTime()/1000;
        }catch (Exception e){

        }
        return beginUseTime+"";
    }


    public static String getTime() {

        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳

        String str = String.valueOf(time);

        return str;

    }

    public static String getPublicTime(long time) {
        //获取time距离当前的秒数
        int ct = (int) (System.currentTimeMillis() / 1000 - time);

        if (ct <= 0) {
            return "刚刚";
        }

        if (ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟前";
        }
        if (ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if (ct >= 86400 && ct < 2592000) { //86400 * 30
            int day = ct / 86400;
            return day + "天前";
        }
        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

    public static final String FORMAT_TYPE_1 = "yyyy-MM-dd  HH:mm:ss";
    public static final String FORMAT_TYPE_2 = "yyyy.MM.dd";
    public static final String FORMAT_TYPE_3 = "MM月dd日 HH:mm";

    public static String getFormatTime(long time, String format) {

        return new SimpleDateFormat(format).format(new Date(time));

    }

    //将yyyy-mm-dd 转换成时间戳

    public static String getFormatTime2(long time, String format) {

        return new SimpleDateFormat(format).format(new Date(time * 1000));

    }

    // 获取今天的日期
    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
    }


    /**
     * 计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
     根据差值返回多长之间前或多长时间后
     * @param time1 到时时间
     * @param time2 当前时间  System.currentTimeMillis()
     * @param content 需要格式化的文本内容
     * @return
     */
    public static String getDistanceTime(long time1, long time2, String content) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff ;
        String flag;
        if(time1<time2) {
            diff = time2 - time1;
            flag="前";
        } else {
            diff = time1 - time2;
            flag="后";
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff/1000-day*24*60*60-hour*60*60-min*60);


//        return day+"天" + hour+"小时";

        return String.format(content, day, hour);
    }

    private static final DateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String getFormatTimeyyyyMMddHHmm(long timeStamp){
        return DATE_FORMAT.format(new Date(timeStamp));
    }
}
