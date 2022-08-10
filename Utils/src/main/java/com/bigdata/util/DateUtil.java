package com.bigdata.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    /*时间计算：计算到天、计算到秒、计算到毫秒*/

    /**
     * <B>方法名：TotalDay(String startDate,String endDate)</B><BR>
     * <B>说明：计算时间天数差</B>
     *
     * @param startDate 开始时间，"yyyy-MM-dd"
     * @param endDate   结束时间，"yyyy-MM-dd"
     * @return String _天
     */
    public static String TotalDay(String startDate, String endDate) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
        Long day = 0L;
        try {
            long end = sdfs.parse(endDate).getTime();
            long str = sdfs.parse(startDate).getTime();
            day = (end - str) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day.intValue() + "天";
    }

    /**
     * <B>方法名：getDateCountDay(Date datevalue, Integer daynum)</B><BR>
     * <B>说明：一个日期的前N天或后N天的日期</B><BR>
     *
     * @param datevalue 日期变量
     * @param daynum    前后N天天数，整数（向前：负整数，向后：正整数）。
     * @return Date
     */
    public static Date getDateCountDay(Date datevalue, Integer daynum) {
        Calendar car = Calendar.getInstance();
        car.setTime(datevalue);
        car.add(Calendar.DATE, daynum);
        return car.getTime();
    }

    /**
     * <B>方法名：getDateCountDay(String datetypeIn,String datevalue, Integer daynum,String datetypeOut)</B><BR>
     * <B>说明：根据指定日期、日期格式、前后天数计算出日期。</B><BR>
     *
     * @param datevalue   日期变量。
     * @param daynum      前后N天天数，整数（向前：负整数，向后：正整数）。
     * @return String
     */
    public static String getDateCountDay(String datetypeIn, String datevalue, Integer daynum, String datetypeOut) {
        SimpleDateFormat sdfin = new SimpleDateFormat(datetypeIn);
        SimpleDateFormat sdfout = new SimpleDateFormat(datetypeOut);
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdfin.parse(datevalue);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        car.setTime(date);
        car.add(Calendar.DATE, daynum);
        return sdfout.format(car.getTime());
    }

    /**
     * <B>方法名：TotalDayHms(String startDate, String endDate)</B><BR>
     * <B>说明：计算时间差，精确到秒。</B>
     *
     * @param startDate 开始时间，"yyyy-MM-dd HH:mm:ss"
     * @param endDate   结束时间，"yyyy-MM-dd HH:mm:ss"
     * @return String 剩余：_天_小时_分_秒
     */
    public static String TotalDayHms(String startDate, String endDate) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long str = 0L, end = 0L;
        try {
            str = sdfs.parse(startDate).getTime();
            end = sdfs.parse(endDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //(24*60*60=86400)一天总秒数,60*60=3600 一小时的总秒数
        //相差总毫秒数。
        Long dayes = end - str;
        //相差总秒数。
        Long dayS = dayes / 1000;
        //剩余天数。
        Long day = dayS / 86400;
        //天数外，剩余秒数。
        Long Seconds = dayS - day * 86400;
        //剩余小时数，整数。
        Long hour = Seconds / 3600;
        //除天数、小时数后剩余秒数。
        Long hourEnds = dayS - day * 86400 - hour * 3600;
        //剩余分钟数，整数。
        Long Minutes = hourEnds / 60;
        //剩余秒数。
        Long tdMin = dayS - day * 86400 - hour * 3600 - Minutes * 60;
        return "相差/剩余：" + day + "天" + hour + "小时" + Minutes + "分" + tdMin + "秒";
    }

    /**
     * <B>方法名：TotalDayHmsS(String startDate, String endDate)</B><BR>
     * <B>说明：计算时间差，精确到毫秒。</B>
     *
     * @param startDate 开始时间，"yyyy-MM-dd HH:mm:ss.SSS"
     * @param endDate   结束时间，"yyyy-MM-dd HH:mm:ss.SSS"
     * @return String 剩余：_天_小时_分_秒_毫秒
     */
    public static String TotalDayHmsS(String startDate, String endDate) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Long str = 0L, end = 0L;
        try {
            str = sdfs.parse(startDate).getTime();
            end = sdfs.parse(endDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long dayes = end - str;
        Long dayS = dayes / 1000;
        Long day = dayS / 86400;
        Long Seconds = dayS - day * 86400;
        Long hour = Seconds / 3600;
        Long hourEnds = dayS - day * 86400 - hour * 3600;
        Long Minutes = hourEnds / 60;
        Long tdMin = dayS - day * 86400 - hour * 3600 - Minutes * 60;
        //()可略：
        Long millisecond = dayes - (day * 86400 * 1000) - (hour * 3600 * 1000) - (Minutes * 60 * 1000) - (tdMin * 1000);
        return "相差/剩余：" + day + "天" + hour + "小时" + Minutes + "分" + tdMin + "秒" + millisecond + "毫秒";
    }

    /**
     * <B>方法名：getDateIsLeapYear(String value,String datetype)</B><BR>
     * <B>说明：判断输入的日期是否是闰年</B><BR>
     *
     * @param value    日期变量
     * @param datetype 日期格式变量
     * @return Boolean
     */
    public static Boolean getDateIsLeapYear(String value, String datetype) {
        SimpleDateFormat sdf = new SimpleDateFormat(datetype);
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        Integer year = car.get(Calendar.YEAR);
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <B>方法名：getDateIsMonthDay(String value,String datetype)</B><BR>
     * <B>说明：2月合规检查</B><BR>
     *
     * @param value    日期变量
     * @param datetype 日期格式变量
     * @return String 提示2月最后天数
     */
    public static String getDateIsMonthDay(String value, String datetype) {
        if (getDateIsLeapYear(value, datetype)) {
            return "2月最后一天为：2月29日 (公历)";
        } else {
            return "2月最后一天为：2月28日 (公历)";
        }
    }
    /*时间戳*/

    /**
     * <B>方法名：getDateToMillisecond(String datestr, String datetype)</B><BR>
     * <B>说明：获取指定日期时间戳/获取指定日期毫秒数</B><BR>
     *
     * @param datestr  日期参数 变量
     * @param datetype 日期格式 变量
     * @return Long
     */
    public static Long getDateMillisecond(String datestr, String datetype) {
        SimpleDateFormat sdf = new SimpleDateFormat(datetype);
        Long mil = 0L;
        try {
            mil = sdf.parse(datestr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mil;
    }

    /**
     * <B>方法名：getTimeInMillis()</B><BR>
     * <B>说明：获取当前时间戳</B><BR>
     *
     * @return Long
     */
    public static Long getTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * <B>方法名：getMillisecondDate(Long numL, String datetype)</B><BR>
     * <B>说明：时间戳转日期/根据毫秒数获取日期</B><BR>
     *
     * @param numL     Long型时间戳，如：1545970996238L
     * @param datetype 日期格式
     * @return String 指定格式日期
     */
    public static String getMillisecondDate(Long numL, String datetype) {
        SimpleDateFormat sdf = new SimpleDateFormat(datetype);
        Long longs = Long.parseLong(String.valueOf(numL));
        return sdf.format(new Date(longs));
    }

    /*日期转换*/

    /**
     * <B>方法名：getDateType(String value)</B><BR>
     * <B>说明：yyyy-MM-dd HH:mm:ss 转 yyyy年M月d日 HH时mm分ss秒 ，日期中月、日含0的，转换后则不含0</B><BR>
     *
     * @param value 日期参数 yyyy-MM-dd HH:mm:ss
     * @return string
     */
    public static String getDateType(String value) {
        String datestr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy年M月d日 HH时mm分ss秒");
        try {
            datestr = sdft.format(sdf.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * <B>方法名：getDateTypeMd(String value)</B><BR>
     * <B>说明：yyyy-MM-dd 转 yyyy年M月d日 ，日期中月、日含0的，转换后则不含0</B><BR>
     *
     * @param value 日期参数 yyyy-MM-dd
     * @return string
     */
    public static String getDateTypeMd(String value) {
        String datestr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy年M月d日");
        try {
            datestr = sdft.format(sdf.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * <B>方法名：getDateTypes(String value)</B><BR>
     * <B>说明：yyyy年MM月dd日 HH时mm分ss秒 转 yyyy-M-d HH:mm:ss ，日期中月、日含0的，转换后则不含0</B><BR>
     *
     * @param value 日期参数 yyyy年MM月dd日 HH时mm分ss秒
     * @return string
     */
    public static String getDateTypes(String value) {
        String datestr = "";
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        try {
            datestr = sdft.format(sdf.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * <B>方法名：getDateTypeMds(String value)</B><BR>
     * <B>说明：yyyy年MM月dd日 转 yyyy-M-d，日期中月、日含0的，转换后则不含0</B><BR>
     *
     * @param value 日期参数 yyyy年MM月dd日
     * @return string
     */
    public static String getDateTypeMds(String value) {
        String datestr = "";
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-M-d");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            datestr = sdft.format(sdf.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * <B>方法名：</B>getDateToStr(Date value, String datetype)<BR>
     * <B>说明：</B>Date日期转字符串类型日期<BR>
     *
     * @param datetype 自定义日期类型
     * @param date     日期变量
     * @return 返回 String 日期
     */
    public static String getDateToStr(Date date, String datetype) {
        return new SimpleDateFormat(datetype).format(date);
    }

    /**
     * <B>方法名：getStrToDate(String value, String datetype)</B><BR>
     * <B>说明：字符串类型日期转Date日期</B><BR>
     *
     * @param value    日期变量
     * @param datetype 自定义日期类型
     * @return 返回Date日期
     */
    public static Date getStrToDate(String value, String datetype) {
        SimpleDateFormat sdf = new SimpleDateFormat(datetype);
        Date dates = null;
        try {
            dates = sdf.parse(value);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return dates;
    }

    /**
     * <B>方法名：getDate(String datetype)</B><BR>
     * <B>说明：根据日期类型返回当前日期</B><BR>
     *
     * @param datetype 日期类型： yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm:ss.SSS、
     *                 yyyy年MM月dd日 HH时mm分ss秒、yyyy-MM-dd、yyyy年MM月dd日、
     *                 HH:mm:ss、yyyy/MM/dd、yyyy、MM、dd、HH、mm、ss、SSS、
     *                 yyyyMMddHHmmssSSS、yyyy-MM-dd 00:00:00.000、yyyy-MM-dd 23:59:59.999
     * @return 返回String类型日期
     */
    public static String getDate(String datetype) {
        SimpleDateFormat sdf = new SimpleDateFormat(datetype);
        return sdf.format(new Date());
    }

    /**
     * <B>方法名：getDateyMdHms()</B><BR>
     * <B>说明：获取当前日期 </B><BR>
     *
     * @param "yyyy-MM-dd HH:mm:ss "
     * @return string
     */
    public static String getDateyMdHms() {
        return getDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * <B>方法名：getDateyMdHmsS()</B><BR>
     * <B>说明：获取当前日期，含毫秒 </B><BR>
     *
     * @param "yyyy-MM-dd HH:mm:ss "
     * @return string
     */
    public static String getDateyMdHmsS() {
        return getDate("yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * <B>方法名：getDateyMd()</B><BR>
     * <B>说明：获取当前日期 </B><BR>
     *
     * @param "yyyy-MM-dd"
     * @return string
     */
    public static String getDateyMd() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * <B>方法名：getDateC()</B><BR>
     * <B>说明：获取当前日期</B><BR>
     *
     * @param "yyyy年MM月dd日 HH时mm分ss秒"
     * @return string
     */
    public static String getDateC() {
        return getDate("yyyy年MM月dd日 HH时mm分ss秒");
    }

    /**
     * <B>方法名：getDateCh()</B><BR>
     * <B>说明：获取当前日期</B><BR>
     *
     * @param "yyyy年MM月dd日"
     * @return string
     */
    public static String getDateCh() {
        return getDate("yyyy年MM月dd日");
    }

    /**
     * <B>方法名：getDateValueyMd(String datevalue)</B><BR>
     * <B>说明：根据指定时间返回yyyy-MM-dd日期</B><BR>
     *
     * @param datevalue 日期变量：
     * @return date 返回日期
     */
    public static Date getDateValueyMd(String datevalue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(datevalue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * <B>方法名：getStartDateValue(String value)</B><BR>
     * <B>说明：根据指定日期返回其开始时间</B><BR>
     *
     * @param value 日期变量，如：2018-01-23 12:23:35.221
     * @return String 如：2018-01-23 00:00:00.000
     */
    public static String getStartDateValue(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        return sdf.format(getDateValueyMd(value));
    }

    /**
     * <B>方法名：getEndDateValue(String value)</B><BR>
     * <B>说明：根据指定日期返回其结束时间</B><BR>
     *
     * @param value 日期变量，如：2018-01-23 12:23:35.221
     * @return String 如：2018-01-23 23:59:59.999
     */
    public static String getEndDateValue(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59.999");
        return sdf.format(getDateValueyMd(value));
    }

    /**
     * <B>方法名：getWeek()</B><BR>
     * <B>说明：星期-中式</B><BR>
     *
     * @return 根据当前日期返回星期数，如：星期一
     */
    public static String getWeek() {
        Calendar car = Calendar.getInstance();
        int weeknum = car.get(Calendar.DAY_OF_WEEK) - 1;
        return getNumToWeek(weeknum);
    }

    /**
     * <B>方法名：getWeekOfDate(String value)</B><BR>
     * <B>说明：星期-中式</B><BR>
     *
     * @param value 日期变量 yyyy-MM-dd
     * @return 根据指定日期返回星期数，如：星期一
     */
    public static String getWeekOfDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        return getNumToWeek(car.get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * <B>方法名：getDateWeek()</B><BR>
     * <B>说明：获取当前日期， 格式：yyyy-MM-dd HH:mm:ss 星期</B><BR>
     *
     * @return string
     */
    public static String getDateWeek() {
        return getDate("yyyy-MM-dd HH:mm:ss") + getWeek();
    }

    /**
     * <B>方法名：getWeekDate()</B><BR>
     * <B>说明：获取当前日期，格式："星期 yyyy-MM-dd HH:mm:ss"</B><BR>
     *
     * @return string
     */
    public static String getWeekDate() {
        return getWeek() + " " + getDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * <B>方法名：getDateWeeks()</B><BR>
     * <B>说明：获取当前日期，格式："yyyy-MM-dd 星期"</B><BR>
     *
     * @return string
     */
    public static String getDateWeeks() {
        return getDate("yyyy-MM-dd") + getWeek();
    }

    /**
     * <B>方法名：getDateWeeks()</B><BR>
     * <B>说明：获取当前日期，格式："MM月dd日 星期"</B><BR>
     *
     * @return string
     */
    public static String getDateWeekmd() {
        return getDate("MM月dd日") + getWeek();
    }

    /**
     * <B>方法名：getDateWeeks()</B><BR>
     * <B>说明：获取当前日期，格式："MM-dd 星期"</B><BR>
     *
     * @return string
     */
    public static String getDateWeekmdC() {
        return getDate("MM-dd") + getWeek();
    }

    /**
     * <B>方法名：getDateWeekMdHm()</B><BR>
     * <B>说明：获取当前日期，格式："MM-dd 星期 HH:mm"</B><BR>
     *
     * @return string
     */
    public static String getDateWeekMdHm() {
        return getDate("MM-dd") + getWeek() + " " + getDate("HH:mm");
    }

    /**
     * <B>方法名：getDateWeekMdHmC()</B><BR>
     * <B>说明：获取当前日期，格式："MM月dd日 星期 HH:mm"</B><BR>
     *
     * @return string
     */
    public static String getDateWeekMdHmC() {
        return getDate("MM月dd日") + getWeek() + " " + getDate("HH:mm");
    }

    /**
     * <B>方法名：getDateWeekC</B><BR>
     * <B>说明：获取当前日期，格式："yyyy年MM月dd日 HH时mm分ss秒 星期"</B><BR>
     *
     * @return string
     */
    public static String getDateWeekC() {
        return getDate("yyyy年MM月dd日 HH时mm分ss秒") + getWeek();
    }

    /**
     * <B>方法名：getDateWeekCh</B><BR>
     * <B>说明：获取当前日期，格式： "yyyy年MM月dd日 星期"</B><BR>
     *
     * @return string
     */
    public static String getDateWeekCh() {
        return getDate("yyyy年MM月dd日") + getWeek();
    }

    /**
     * <B>方法名：getWeekCDate</B><BR>
     * <B>说明：获取当前日期，格式： "yyyy年MM月dd日  星期 HH时mm分ss秒"</B><BR>
     *
     * @return string
     */
    public static String getWeekCDate() {
        return getDateWeekCh() + " " + getDate("HH时mm分ss秒");
    }

    /**
     * <B>方法名：getWeekE</B><BR>
     * <B>说明：星期-英式</B><BR>
     *
     * @return 根据当前日期返回星期数，如：Sunday
     */
    public static String getWeekE() {
        Calendar car = Calendar.getInstance();
        int weeknum = car.get(Calendar.DAY_OF_WEEK) - 1;
        return getNumToWeekE(weeknum);
    }

    /**
     * <B>方法名：getWeekEn</B><BR>
     * <B>说明：星期-英式2</B><BR>
     *
     * @return 根据当前日期返回星期数，如：Mon.
     */
    public static String getWeekEn() {
        Calendar car = Calendar.getInstance();
        int weeknum = car.get(Calendar.DAY_OF_WEEK) - 1;
        return getNumToWeekEh(weeknum);
    }

    /**
     * <B>方法名：getWeekOfDateE(String value)</B><BR>
     * <B>说明：星期-英1</B><BR>
     *
     * @param value 日期变量 yyyy-MM-dd
     * @return 根据指定日期返回星期数，如：Sunday
     */
    public static String getWeekOfDateE(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        return getNumToWeekE(car.get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * <B>方法名：getWeekOfDateEn(String value)</B><BR>
     * <B>说明：星期-英2</B><BR>
     *
     * @param value 日期变量 yyyy-MM-dd
     * @return 根据指定日期返回星期数，如：Mon.
     */
    public static String getWeekOfDateEn(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        return getNumToWeekEh(car.get(Calendar.DAY_OF_WEEK) - 1);
    }

    /**
     * <B>方法名：getMonthE</B><BR>
     * <B>说明：月份-英式</B><BR>
     *
     * @return 根据当前日期返回月份数，如：August
     */
    public static String getMonthE() {
        Calendar car = Calendar.getInstance();
        int Monthnum = car.get(Calendar.MONTH);
        return getMonthE(Monthnum);
    }

    /**
     * <B>方法名：getMonthEn</B><BR>
     * <B>说明：月份-英式2</B><BR>
     *
     * @return 根据当前日期返回月份数，如：Aug.
     */
    public static String getMonthEn() {
        Calendar car = Calendar.getInstance();
        int Monthnum = car.get(Calendar.MONTH);
        return getMonthEn(Monthnum);
    }

    /**
     * <B>方法名：getMonthOfDateE(String value)</B><BR>
     * <B>说明：月份-英1</B><BR>
     *
     * @param value 日期变量 yyyy-MM-dd
     * @return 根据指定日期返回月份数，如：August
     */
    public static String getMonthOfDateE(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        return getMonthE(car.get(Calendar.MONTH));
    }

    /**
     * <B>方法名：getMonthOfDateEn(String value)</B><BR>
     * <B>说明：月份-英2</B><BR>
     *
     * @param value 日期变量 yyyy-MM-dd
     * @return 根据指定日期返回月份数，如：Aug.
     */
    public static String getMonthOfDateEn(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        return getMonthEn(car.get(Calendar.MONTH));
    }

    /**
     * <B>方法名：getDateE()</B><BR>
     * <B>说明：日期-英版1</B><BR>
     *
     * @return String 如：Wednesday,December 28,2016
     */
    public static String getDateE() {
        return getWeekE() + "," + getMonthE() + " " + getDate("dd") + "," + getDate("yyyy");
    }

    /**
     * <B>方法名：getDateEn()</B><BR>
     * <B>说明：日期-英版2</B><BR>
     *
     * @return String 如：Wed.Dec.28, 2016
     */
    public static String getDateEn() {
        return getWeekEn() + getMonthEn() + getDate("dd") + ", " + getDate("yyyy");
    }

    /**
     * <B>方法名：getDateEng()</B><BR>
     * <B>说明：日期-英版3</B><BR>
     *
     * @return String 如：Dec.28, 2016
     */
    public static String getDateEng() {
        return getMonthEn() + " " + getDate("dd") + ", " + getDate("yyyy");
    }

    /**
     * <B>方法名：getDateWeekE(String value)</B><BR>
     * <B>说明：指定日期转英式格式日期。</B><BR>
     *
     * @param value 日期变量 yyyy-MM-dd
     * @return String 如：Sun.Feb.28, 2016
     */
    public static String getDateWeekE(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        return getWeekOfDateEn(value) + getMonthOfDateEn(value) + car.get(Calendar.DATE) + ", " + car.get(Calendar.YEAR);
    }

    /**
     * <B>方法名：getDateWeekEn(String value)</B><BR>
     * <B>说明：指定日期转英式格式日期。</B><BR>
     *
     * @param value 日期变量 yyyy-MM-dd
     * @return String 如：Wednesday,December 28, 2016
     */
    public static String getDateWeekEn(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar car = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        car.setTime(date);
        return getWeekOfDateE(value) + "," + getMonthOfDateE(value) + " " + car.get(Calendar.DATE) + ", " + car.get(Calendar.YEAR);
    }

    /**
     * <B>方法名：getWeekNum(Date date)</B><BR>
     * <B>说明：根据指定日期返回其星期数，周日标识为0 周一标识为1</B><BR>
     *
     * @param date 日期变量
     * @return int 返回其星期数
     */
    public static int getWeekNum(Date date) {
        Calendar car = Calendar.getInstance();
        car.setTime(date);
        return car.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * <B>方法名：getWeekNums(Date date)</B><BR>
     * <B>说明：根据指定日期返回其星期数，周日标识为7 周一标识为1</B><BR>
     *
     * @param date 日期变量
     * @return int 返回其星期数
     */
    public static int getWeekNums(Date date) {
        Calendar car = Calendar.getInstance();
        car.setTime(date);
        int week = car.get(Calendar.DAY_OF_WEEK);
        int weeknum;
        if (week == Calendar.SUNDAY) {
            weeknum = 7;
        } else {
            weeknum = week - 1;
        }
        return weeknum;
    }

    /*
     * 当年第一天
     * */
    public static Date getYearFirstDay() {
        Calendar car = Calendar.getInstance();
        car.set(Calendar.HOUR_OF_DAY, 0);
        car.set(Calendar.MINUTE, 0);
        car.set(Calendar.SECOND, 0);
        car.set(Calendar.MILLISECOND, 0);
        int day = car.getActualMinimum(Calendar.DAY_OF_YEAR);
        car.set(Calendar.DAY_OF_YEAR, day);
        return car.getTime();
    }

    /* 当年最后一天*/
    public static Date getYearLastDay() {
        Calendar car = Calendar.getInstance();
        car.set(Calendar.HOUR_OF_DAY, 23);
        car.set(Calendar.MINUTE, 59);
        car.set(Calendar.SECOND, 59);
        car.set(Calendar.MILLISECOND, 999);
        int day = car.getActualMaximum(Calendar.DAY_OF_YEAR);
        car.set(Calendar.DAY_OF_YEAR, day);
        return car.getTime();
    }


    /*Calendar*/

    /**
     * <B>方法名：getDateToCalendar(Date date)</B><BR>
     * <B>说明：Date转Calendar</B>
     *
     * @param value Calendar日期变量
     * @return String
     */
    public static Calendar getDateToCalendar(Date value) {
        Calendar car = Calendar.getInstance();
        car.setTime(value);
        return car;
    }

    /**
     * <B>方法名：getCalendarToStr(Calendar car, String datetype)</B><BR>
     * <B>说明：Calendar日期转规定格式日期</B>
     *
     * @param car      Calendar日期变量
     * @param datetype 自定义日期类型
     * @return String 返回规定格式的日期
     */
    public static String getCalendarToStr(Calendar car, String datetype) {
        SimpleDateFormat sdf = new SimpleDateFormat(datetype);
        if (car != null) {
            return sdf.format(car.getTime());
        } else {
            return "";
        }
    }

    /**
     * <B>方法名： getCalendarToStr(Calendar car)</B><BR>
     * <B>说明：Calendar日期转yyyy-MM-dd HH:mm:ss</B>
     *
     * @param car Calendar日期变量
     * @return String
     */
    public static String getCalendarToStr(Calendar car) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (car != null) {
            return sdf.format(car.getTime());
        } else {
            return "";
        }
    }

    /**
     * <B>方法名：getCalendarToStrHm(Calendar car)</B><BR>
     * <B>说明：Calendar日期转yyyy-MM-dd HH:mm</B>
     *
     * @param car Calendar日期变量
     * @return String
     */
    public static String getCalendarToStrHm(Calendar car) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (car != null) {
            return sdf.format(car.getTime());
        } else {
            return "";
        }
    }

    /**
     * <B>方法名： getCalendarToStryMd(Calendar car)</B><BR>
     * <B>说明：Calendar日期转yyyy-MM-dd</B>
     *
     * @param car Calendar日期变量
     * @return String
     */
    public static String getCalendarToStryMd(Calendar car) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (car != null) {
            return sdf.format(car.getTime());
        } else {
            return "";
        }
    }

    /**
     * <B>方法名：getCalendarHm(Calendar car)</B><BR>
     * <B>说明：Calendar日期转HH:mm</B>
     *
     * @param car Calendar日期变量
     * @return String
     */
    public static String getCalendarHm(Calendar car) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if (car != null) {
            return sdf.format(car.getTime());
        } else {
            return "";
        }
    }

    /**
     * <B>方法名： getCalendarHms(Calendar car)</B><BR>
     * <B>说明：Calendar日期转HH:mm:ss</B>
     *
     * @param car Calendar日期变量
     * @return String
     */
    public static String getCalendarHms(Calendar car) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if (car != null) {
            return sdf.format(car.getTime());
        } else {
            return "";
        }
    }
    /*Calendar end*/

    public static String getNumToWeek(int weeknum) {
        String[] weekC = {"日", "一", "二", "三", "四", "五", "六"};
        return " 星期" + weekC[weeknum];
    }

    public static String getNumToWeekE(int weeknum) {
        String[] weekE = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return weekE[weeknum];
    }

    public static String getNumToWeekEh(int weeknum) {
        String[] weekEn = {"Sun.", "Mon.", "Tues.", "Wed.", "Thurs.", "Fri.", "Sat."};
        return weekEn[weeknum];
    }

    public static String getMonthE(int monthnum) {
        String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return month[monthnum];
    }

    public static String getMonthEn(int monthnum) {
        String[] month = {"Jan.", "Feb.", "Mar.", "Apr.", "May.", "Jun.", "Jul.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."};
        return month[monthnum];
    }


}
