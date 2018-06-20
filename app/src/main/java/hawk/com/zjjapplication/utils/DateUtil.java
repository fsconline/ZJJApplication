

package hawk.com.zjjapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author jack_sj
 * @company yjlc - 易捷联创
 * @name DateUtil.java
 * @create 2014-10-13
 * @descript
 */
public class DateUtil {
    public static final int DateTypeYear = 0;
    public static final int DateTypeMonth = 1;
    public static final int DateTypeDay = 2;
    public static final int DateTypeHour = 3;
    public static final int DateTypeMinute = 4;
    public static final int DateTypeSecond = 5;
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        // Locale.setDefault(Locale.CHINA);
//		System.out.println(System.currentTimeMillis());
//		Date date = new Date(1372077527627l);// 24 pm
//		System.out.println(formatDate(1372077527627l));
//		Date date2 = new Date(calMonOfWeek.getTimeInMillis());
//		GregorianCalendar gc = new GregorianCalendar();
//		gc.setTime(date2);
//		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
//				"yyyy-MM-dd hh:mm:ss");
//		String dateStr = format.format(gc.getTime());
//		System.out.println(dateStr);
        //1431593011511&1431685800000

        Tools.log(formatDate(1431685800000l));
    }


    public static void formatCalendar(Calendar calendar) {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static String formatDate(long timeMillis) {
        Date date = new Date(timeMillis);// 24 pm
        Calendar calMsgTime = Calendar.getInstance();
        calMsgTime.setTime(date);

        Calendar calToday = Calendar.getInstance();
        formatCalendar(calToday);

        Calendar calYesterday = Calendar.getInstance();
        calYesterday.add(Calendar.DATE, -1);
        formatCalendar(calYesterday);

        // Calendar calMonOfWeek=Calendar.getInstance();
        // calMonOfWeek.set(Calendar.DAY_OF_WEEK-1, Calendar.MONDAY);
        Calendar calMonOfWeek = Calendar.getInstance();
        int day_of_week = calMonOfWeek.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        calMonOfWeek.add(Calendar.DATE, -day_of_week + 1);
        formatCalendar(calMonOfWeek);

        //System.out.println(calMonOfWeek.toString());


        boolean isNotToday = calMsgTime.before(calToday);// falseΪ����,true:֮ǰ

        boolean isYesterday = calMsgTime.before(calToday)
                && calMsgTime.after(calYesterday);// true:����

        boolean isThisWeek = calMsgTime.after(calMonOfWeek);
        //System.out.println(isThisWeek + "");
        //System.out.println(calMonOfWeek.getTimeInMillis());
        //System.out.println(calMsgTime.getTimeInMillis());
        if (isNotToday) {
            if (isYesterday) {
                return ("昨天");
            } else {
                if (isThisWeek) {
                    return (getWeekOfDate(date));
                } else {

                    int year = calMsgTime.get(Calendar.YEAR);
                    int month = calMsgTime.get(Calendar.MONTH) + 1;
                    int day = calMsgTime.get(Calendar.DAY_OF_MONTH);
                    return (year + "-" + month + "-" + day);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//			System.out.println(calMsgTime);
//			Time localTime = new Time("Asia/Hong_Kong");
//			localTime.set(calMsgTime.getTimeInMillis());
//			String time=localTime.format("%H:%M");//返回类型是String
//			System.out.println(date);
            String time = sdf.format(date);
            return time;
//			// 当天日期 am pm
//			int amOrPm = calMsgTime.get(Calendar.AM_PM);
//			System.out.println(amOrPm);
//			// 转换为24小时制式的字串  
//			SimpleDateFormat  df = new SimpleDateFormat("HH:mm");  
//			return ((amOrPm==0?"上午":"下午")+" "+df.format(date));
        }
    }

    public static String formatDataOfToday(String data) {
        try {
            Date date = sdf.parse(data);
            long time = date.getTime();
            return formatDate(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }

    public static String formatDataOfDay(Date date) {
        if (date == null)
            date = new Date();
        return sdf.format(date);
    }


    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getWeek(String str) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return weekDays[1];
    }

    public static String getTodayDateStr() {
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    public static String getCurMonthStartDay() {
        String curDate = getTodayDateStr();
        return curDate.substring(0, 8) + "01";

    }

    public static String getCurMonthToDay() {
        String curDate = getTodayDateStr();
        return curDate.substring(0, 10);
    }

    public static String getDayString(int type) {


        String curDate = getTodayDateStr();
        return getDayString(curDate, type);
    }
//	public static String getDayString(Calendar calendar, int type){
//		String curDate = getTodayDateStr();
//		return curDate.substring(0,10);
//	}

    public static String getDayString(String date, int type) {
        //"yyyy-MM-dd HH:mm:ss"
        String curDate = null;
        if (null == date)
            curDate = getTodayDateStr();
        else
            curDate = date;

        if ("未知".equals(curDate))
            return "";

        switch (type) {
            case DateTypeYear:
                if (curDate.length() > 4)
                    curDate = curDate.substring(0, 4);

                break;
            case DateTypeMonth:
                if (curDate.length() > 7)
                    curDate = curDate.substring(0, 7);
                break;
            case DateTypeDay:
                if (curDate.length() > 10)
                    curDate = curDate.substring(0, 10);
                break;
            case DateTypeHour:
                if (curDate.length() > 13)
                    curDate = curDate.substring(0, 13);
                break;
            case DateTypeMinute:
                if (curDate.length() > 16)
                    curDate = curDate.substring(0, 16);
                break;

        }
        return curDate;
    }

}
