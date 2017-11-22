package com.softisland.common.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 *
 * @author weixing
 *
 */
public class DateTimeUtil {
	/**
	 * 日期格式：yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FORMART_PATTERN = "yyyy-MM-dd";
	/**
	 * 日期格式：yyyy年MM月dd
	 */
	public static final String DATE_FORMART_PATTERN_1 = "yyyy年MM月dd";
	/**
	 * 日期格式：yyyyMMdd
	 */
	public static final String DATE_FORMART_PATTERN_2 = "yyyyMMdd";

	/**
	 * 日期格式：yyyy-mm-dd
	 */
	public static final String DATE_FORMART_PATTERN_3 = "yyyy-M-d";

	/**
	 * 日期时间格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_FORMART_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String DATETIME_FORMART_PATTERN_3 = "yyyy-M-d H:m:s";
	/**
	 * 日期时间格式：yyyy年MM月dd HH时mm分ss秒
	 */
	public static final String DATETIME_FORMART_PATTERN_1 = "yyyy年MM月dd HH时mm分ss秒";
	/**
	 * 日期时间格式：yyyyMMddHHmmss
	 */
	public static final String DATETIME_FORMART_PATTERN_2 = "yyyyMMddHHmmss";

	/**
	 * 数据库TIMESTAMP日期时间格式：yyyy-MM-dd HH:mm:ss:SSS
	 */
	public static final String DEFAULT_TIMESTAMP_FORMART_PATTERN = "yyyy-MM-dd HH:mm:ss:SSS";
	/**
	 * 数据库TIMESTAMP日期时间格式：yyyy年MM月dd HH时mm分ss秒SSS
	 */
	public static final String TIMESTAMP_FORMART_PATTERN_1 = "yyyy年MM月dd HH时mm分ss秒SSS";
	/**
	 * 数据库TIMESTAMP日期时间格式：yyyyMMddHHmmssSSS
	 */
	public static final String TIMESTAMP_FORMART_PATTERN_2 = "yyyyMMddHHmmssSSS";
	/**
	 * 获取当前日期(年月日)
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getNowDate()throws Exception{
		return LocalDate.now();
	}

	/**
	 * 根据指定的模式格式化日期字符串，返回日期
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getDateFromStrDate(String strDate,String pattern)throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(strDate, formatter);
	}

	public static Date getOldDateFromStrDate(String strDate,String pattern)throws Exception{
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(strDate);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据日期字符串格式日期字符串，返回日期
	 * @param strDate
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getDateFromStrDate(String strDate)throws Exception{
		return getDateFromStrDate(strDate, DATE_FORMART_PATTERN_3);
	}

	public static Date getOldDateFromStrDate(String strDate)throws Exception{
		return getOldDateFromStrDate(strDate, DEFAULT_DATETIME_FORMART_PATTERN);
	}

	/**
	 * 获取当前时间(时分秒)
	 * @return
	 * @throws Exception
	 */
	public static LocalTime getNowTime()throws Exception{
		return LocalTime.now();
	}

	/**
	 * 获取当前时间（年月日时分秒）
	 * @return
	 * @throws Exception
	 */
	public static LocalDateTime getNowDateTime()throws Exception{
		return LocalDateTime.now();
	}

	/**
	 * 根据指定的模式格式化日期字符串，返回日期时间
	 * @param strDateTime
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static LocalDateTime getDateTimeFromStrDate(String strDateTime,String pattern)throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(strDateTime, formatter);
	}

	/**
	 * 根据日期字符串格式日期字符串，返回日期时间
	 * @param strDateTime
	 * @return
	 * @throws Exception
	 */
	public static LocalDateTime getDateTimeFromStrDate(String strDateTime)throws Exception{
		return getDateTimeFromStrDate(strDateTime, DEFAULT_DATETIME_FORMART_PATTERN);
	}

	/**
	 * 根据日期字符串获取对应数据库的Timestamp
	 * @param strDateTime
	 * @return
	 * @throws Exception
	 */
	public static Timestamp getTimeStampByStrDateTime(String strDateTime)throws Exception{
		//strDateTime = strDateTime.replaceAll("0:0:0:0","00:00:00");
		return Timestamp.from(getDateTimeFromStrDate(strDateTime).toInstant(ZoneOffset.ofHours(8)));
	}

	/**
	 * 获取当前日期的Timestamp
	 * @return
	 * @throws Exception
	 */
	public static Timestamp getNowTimeStamp()throws Exception{
		return Timestamp.from(getNowDateTime().toInstant(ZoneOffset.ofHours(8)));
	}

	/**
	 * 获取当前日期格式化后的字符串，根据pattern进行格式化
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static String getStrNowDate(String pattern)throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return getNowDate().format(formatter);
	}

	/**
	 * 获取当前日期默认格式化后的字符串yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public static String getStrNowDate()throws Exception{
		return getStrNowDate(DEFAULT_DATE_FORMART_PATTERN);
	}

	/**
	 * 获取当前日期格式化后的字符串，根据pattern进行格式化
	 * @param pattern
	 * @param localDate
	 * @return
	 * @throws Exception
	 */
	public static String getStrDate(String pattern,LocalDate localDate)throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return localDate.format(formatter);
	}

	/**
	 * 获取当前日期默认格式化后的字符串yyyy-MM-dd
	 * @param localDate
	 * @return
	 * @throws Exception
	 */
	public static String getStrDate(LocalDate localDate)throws Exception{
		return getStrDate(DEFAULT_DATE_FORMART_PATTERN, localDate);
	}

	/**
	 * 获取当前日期格式化后的字符串，根据pattern进行格式化
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static String getStrNowDateTime(String pattern)throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return getNowDateTime().format(formatter);
	}

	/**
	 * 获取当前日期默认格式化后的字符串yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public static String getStrNowDateTime()throws Exception{
		return getStrNowDateTime(DEFAULT_DATETIME_FORMART_PATTERN);
	}

	/**
	 * 获取当前日期格式化后的字符串，根据pattern进行格式化
	 * @param pattern
	 * @param dateTime
	 * @return
	 * @throws Exception
	 */
	public static String getStrDateTime(String pattern,LocalDateTime dateTime)throws Exception{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return dateTime.format(formatter);
	}

	/**
	 * 对日期进行加减,正数则为加，负数则为减
	 * @param localDate
	 * @param years
	 * @param months
	 * @param days
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getNewDateByAddValue(LocalDate localDate,int years,int months,int days)throws Exception{
		return localDate.plusYears(years).plusMonths(months).plusDays(days);
	}

	/**
	 * 对日期进行加减,正数则为加，负数则为减
	 * @param localDate
	 * @param years
	 * @param months
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getNewDateByAddValue(LocalDate localDate,int years,int months)throws Exception{
		return localDate.plusYears(years).plusMonths(months);
	}

	/**
	 * 对日期中的年进行加减,正数则为加，负数则为减
	 * @param localDate
	 * @param years
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getNewDateByAddYearValue(LocalDate localDate,int years)throws Exception{
		return localDate.plusYears(years);
	}

	/**
	 * 对日期中的月进行加减，正数则为加，负数则为减
	 * @param localDate
	 * @param months
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getNewDateByAddMonthValue(LocalDate localDate,int months)throws Exception{
		return localDate.plusMonths(months);
	}

	/**
	 * 对日期中的日进行加减，正数则为加，负数则为减
	 * @param localDate
	 * @param days
	 * @return
	 * @throws Exception
	 */
	public static LocalDate getNewDateByAddDayValue(LocalDate localDate,int days)throws Exception{
		return localDate.plusDays(days);
	}

	/**
	 * 获取当前日期默认格式化后的字符串yyyy-MM-dd
	 * @param dateTime
	 * @return
	 * @throws Exception
	 */
	public static String getStrDateTime(LocalDateTime dateTime)throws Exception{
		return getStrDateTime(DEFAULT_DATETIME_FORMART_PATTERN, dateTime);
	}

	public static String dateToStrSimpleYMDHMSS(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMART_PATTERN_2);
		return dateFormat.format(date);
	}

	public static String dateToStrSimpleYMDHMSS() {
		return dateToStrSimpleYMDHMSS(new Date());
	}

	/**
	 * 当前时间与传入时间比较大小  return true 表示传入时间大于当前时间  否则表示传入时间小于当前时间
	 * @param dateStr
	 * @return
     */
	public static boolean compareDateWithNow(String dateStr) {
		boolean flag = false;
		try {
			Date date = getOldDateFromStrDate(dateStr, DEFAULT_DATETIME_FORMART_PATTERN);
			int i = date.compareTo(new Date());
			if(i==1) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}
	
	/**
	 * 得到两个日期的天数之差
	 * 按小时计算
	 * @param fDate 开始日期
	 * @param oDate 结束日期
	 * @return 天数差
	 */
	public static int daysBetween(Date fDate, Date oDate)  throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		fDate=sdf.parse(sdf.format(fDate));  
		oDate=sdf.parse(sdf.format(oDate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(fDate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(oDate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
	}

	/**
	 * 得到两个日期的天数之差
	 * 按天数计算
	 * @param fDate 开始日期
	 * @param oDate 结束日期
	 * @return 天数差
	 */
	public static int daysOfTwo(Date fDate, Date oDate) throws Exception{
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(fDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(oDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;
	}

	public static void main1(String[] args) throws Exception {
		System.out.println("getNowDate():"+getNowDate());
		System.out.println("getDateFromStrDate(String strDate,String pattern):"+getDateFromStrDate("2015-02-01", DEFAULT_DATE_FORMART_PATTERN));
		System.out.println("getDateFromStrDate(String strDate):"+getDateFromStrDate("2015-02-01"));
		System.out.println("getNowDateTime():"+getNowDateTime());
		System.out.println("getDateTimeFromStrDate(String strDate,String pattern):"+getDateTimeFromStrDate("2015-01-01 22:22:22", DEFAULT_DATETIME_FORMART_PATTERN));
		System.out.println("getDateTimeFromStrDate(String strDateTime):"+getDateTimeFromStrDate("2015-01-01 22:22:22"));
		System.out.println("getStrNowDate(String pattern):"+getStrNowDate(DATE_FORMART_PATTERN_1));
		System.out.println("getStrNowDate():"+getStrNowDate());
		System.out.println("getStrDate(String pattern,LocalDate localDate):"+getStrDate(DEFAULT_DATE_FORMART_PATTERN, getNowDate()));
		System.out.println("getStrDate(LocalDate localDate):"+getStrDate(getNowDate()));
		System.out.println("getStrNowDateTime(String pattern):"+getStrNowDateTime(DEFAULT_DATETIME_FORMART_PATTERN));
		System.out.println("getStrNowDateTime():"+getStrNowDateTime());
		System.out.println("getStrDateTime(String pattern,LocalDateTime dateTime):"+getStrDateTime(DEFAULT_TIMESTAMP_FORMART_PATTERN, getNowDateTime()));
		System.out.println("getStrDateTime(LocalDateTime dateTime):"+getStrDateTime(getNowDateTime()));
		System.out.println("start:"+System.currentTimeMillis());
		System.out.println(getNowDate().plusDays(-2).toString());
		System.out.println("end++:" + System.currentTimeMillis());
		System.out.println("*************");
		System.out.println("start:" + System.currentTimeMillis());
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DATE, 1);
		LocalDateTime ldt = getDateTimeFromStrDate("2014-9-1 00:00:00");
		Instant instant = ldt.toInstant(ZoneOffset.ofHours(8));
		Timestamp timestamp = Timestamp.from(instant);
		System.out.println(timestamp.toString());
	}
}
