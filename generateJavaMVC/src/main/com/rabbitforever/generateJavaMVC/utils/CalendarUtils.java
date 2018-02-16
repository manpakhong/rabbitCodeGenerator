package com.rabbitforever.generateJavaMVC.utils;




import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CalendarUtils {
	private static final Logger log = LoggerFactory.getLogger(CalendarUtils.class);

	private static final String SIMPLE_DATE_TIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	private static final String SIMPLE_DATE_TIME_FORMAT_WITH_UTC_TZ_OFFSET_STRING = "yyyy-MM-dd HH:mm:ss Z";
	public static final String UTC_TIMEZONE_STRING = "Etc/UTC";

	public static void recomputeCalendar(Calendar cal){
		String refreshCal = "refreshCal - M: " + cal.get(Calendar.DAY_OF_MONTH) + "H:" + cal.get(Calendar.HOUR_OF_DAY);
		 if (log.isDebugEnabled()){
			 log.debug("CalendarUtils.convertCalendar2TimeZoneCalendar(), after recompute: %s", refreshCal);
		 }
	}
	
	public static java.sql.Date convertJavaDate2SqlDate(Date javaDate){
		java.sql.Date sqlDate = null;
		try{
			if (javaDate != null){
				sqlDate = new java.sql.Date(javaDate.getTime());
			}
		} catch (Exception e){
			log.error("CalendarUtils.convertJavaDate2SqlDate()- Exception:", e);
		}
		return sqlDate;
	}
	
	public static Date convertSqlDate2JavaDate(java.sql.Date sqlDate){
		Date javaDate = null;
		try{
			if (sqlDate != null){
				javaDate = new Date(sqlDate.getTime());
			}
		} catch (Exception e){
			log.error("CalendarUtils.convertJavaDate2SqlDate()- Exception:", e);
		}
		return javaDate;
	}
	
	public static Timestamp convertSqlDate2SqlTimestamp(Date date){
		Timestamp timestamp= null;
		try{
			if (date != null){
				timestamp = new Timestamp(date.getTime());
			}
		} catch (Exception e){
			log.error("CalendarUtils.convertJavaDate2SqlDate()- Exception:", e);
		}
		return timestamp;
	}
	
	public static String convertCalendar2MySqlDateString(Calendar calendar) {
		StringBuilder mysqlDateSb = new StringBuilder();
		mysqlDateSb.append(calendar.get(Calendar.YEAR));
		mysqlDateSb.append("-");
		mysqlDateSb.append(paddingZero2Units(calendar.get(Calendar.MONTH) + 1));
		mysqlDateSb.append("-");
		mysqlDateSb.append(paddingZero2Units(calendar.get(Calendar.DAY_OF_MONTH)));
		mysqlDateSb.append(" ");
		mysqlDateSb.append(paddingZero2Units(calendar.get(Calendar.HOUR)));
		mysqlDateSb.append(":");
		mysqlDateSb.append(paddingZero2Units(calendar.get(Calendar.MINUTE)));
		mysqlDateSb.append(":");
		mysqlDateSb.append(paddingZero2Units(calendar.get(Calendar.SECOND)));
		return mysqlDateSb.toString();
	}

	private static String paddingZero2Units(Integer intPart){
		String intPartStr = intPart.toString();
		if(intPartStr.length() == 1){
			intPartStr = "0" + intPartStr;
		}
		return intPartStr;
	}
	
	public static Date convertCalendar2TimeZoneDate(Calendar calendar, TimeZone toTimeZone) {
		Calendar newCal = convertCalendar2TimeZoneCalendar(calendar, toTimeZone);
		return newCal.getTime();
	}
	
	public static Calendar convertCalendar2TimeZoneCalendar(Calendar cal,TimeZone toTimeZone){
		Calendar newCal = null;
		try	{
			String dateFormatString = convertDate2SimpleDateFormatString(cal.getTime(), toTimeZone);
			newCal = convertSimpleDateString2Calendar(dateFormatString, toTimeZone);
			
			// !!!!!! the following line must be existed to trigger recomputing after setting time zone
			String beforeConvert = "before - M: " + cal.get(Calendar.DAY_OF_MONTH) + "H:" + cal.get(Calendar.HOUR_OF_DAY);
			if (log.isDebugEnabled()){
			 log.debug("CalendarUtils.convertCalendar2TimeZoneCalendar(), before convert: %s", beforeConvert);
			}
			cal.setTimeZone(toTimeZone);
			// !!!!!! the following line must be existed to trigger recomputing after setting time zone
			String afterConvert = "after - M: " + newCal.get(Calendar.DAY_OF_MONTH) + "H:" + newCal.get(Calendar.HOUR_OF_DAY);
			if (log.isDebugEnabled()){
				 log.debug("CalendarUtils.convertCalendar2TimeZoneCalendar(), after convert: %s", afterConvert);			
			}		
		} catch (Exception e){
			log.error("CalendarUtils.convertCalendar2TimeZoneCalendar()- Exception:", e);
		}
		return newCal;
	}
	
	public static String convertDate2SimpleDateFormatString(Date date, TimeZone tz){
		DateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT_WITH_UTC_TZ_OFFSET_STRING);
		formatter.setTimeZone(tz);
		String str = formatter.format(date);
		return str;
	}
	
	public static Calendar convertSimpleDateString2Calendar(String stringFormat, TimeZone tz){
		Calendar cal = null;
		try{
			List<String> datePartList = CommonUtils.regMatch(stringFormat, "\\d{4}-\\d{2}-\\d{2}");
			List<String> timePartList = CommonUtils.regMatch(stringFormat, "\\d{2}:\\d{2}:\\d{2}");
			List<String> zoneOffsetList = CommonUtils.regMatch(stringFormat, "[+-]\\d{4}");
			String datePart = "";
			String timePart = "";
			String zoneOffset = "";
			if (datePartList != null && datePartList.get(0) != null){
				datePart = datePartList.get(0);
			}
			if (timePartList != null && timePartList.get(0) != null){
				timePart = timePartList.get(0);
			}
			if (zoneOffsetList != null && zoneOffsetList.get(0) != null){
				zoneOffset = zoneOffsetList.get(0);
			}
			String [] datePartArray = datePart.split("-");
			String [] timePartArray = timePart.split(":");
			
			Integer month = null;
			Integer dayOfMonth = null;
			Integer year = null;
			
			Integer hour = null;
			Integer minute = null;
			Integer second = null;
			
			if (datePartArray.length == 3){
				if (CommonUtils.isInteger(datePartArray[0])){
					year = new Integer(datePartArray[0]);
				}
				if (CommonUtils.isInteger(datePartArray[1])){
					month = new Integer(datePartArray[1]);
				}
				if (CommonUtils.isInteger(datePartArray[2])){
					dayOfMonth = new Integer(datePartArray[2]);
				}
			}
			if (timePartArray.length == 3){
				if (CommonUtils.isInteger(timePartArray[0])){
					hour = new Integer(timePartArray[0]);
				}
				if (CommonUtils.isInteger(timePartArray[1])){
					minute = new Integer(timePartArray[1]);
				}
				if (CommonUtils.isInteger(timePartArray[2])){
					second = new Integer(timePartArray[2]);
				}
			}
			if (month != null && dayOfMonth != null && year != null && hour != null && minute != null && second != null){
				cal = Calendar.getInstance(tz);
				cal.set(Calendar.MONTH, (month.intValue() - 1));
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth.intValue());
				cal.set(Calendar.YEAR, year.intValue());
				cal.set(Calendar.HOUR_OF_DAY, hour.intValue());
				cal.set(Calendar.MINUTE, minute.intValue());
				cal.set(Calendar.SECOND, second.intValue());
			}
			recomputeCalendar(cal);
			
		} catch (Exception e){
			log.error("CalendarUtils.getCalendarByString()- Exception:", e);
		}
		return cal;
	}	
	
	public static Calendar getTimeZoneEndMaxTimeZoneCalendarOfTheMonth(TimeZone tz){
		Calendar cal = getMaxTimeZoneCalendarToday(tz);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		recomputeCalendar(cal);
		return cal;
	}
	
	public static Calendar getTimeZoneBeginningTimeZoneMinCalendarOfTheMonth(TimeZone tz){
		Calendar cal = getMinTimeZoneCalendarToday(tz);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		recomputeCalendar(cal);
		return cal;
	}
	
	public static void moveCalendar2EndMaxOfTheMonth(Calendar cal){
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		recomputeCalendar(cal);
	}
	
	public static Calendar getUtcEndMaxCalendarOfTheMonth(){
		Calendar cal = getMaxUtcCalendarToday();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		recomputeCalendar(cal);
		return cal;
	}
	
	public static void moveCalendar2BeginningMinOfTheMonth(Calendar cal){
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		recomputeCalendar(cal);
	}
	
	public static Calendar getUtcBeginningMinCalendarOfTheMonth(){
		Calendar cal = getMinUtcCalendarToday();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		recomputeCalendar(cal);
		return cal;
	}
	
	public static Calendar getMinTimeZoneCalendarToday(TimeZone tz) {
		Calendar cal = Calendar.getInstance(tz);
		trimCalendar2Minimum(cal);
		return cal;
	}

	public static Calendar getMaxTimeZoneCalendarToday(TimeZone tz) {
		Calendar cal = Calendar.getInstance(tz);
		trimCalendar2Maximum(cal);
		return cal;
	}

	public static Calendar getMinHostCalendarToday() {
		TimeZone tz = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tz);
		trimCalendar2Minimum(cal);
		return cal;
	}

	public static Calendar getMaxHostCalendarToday() {
		TimeZone tz = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tz);
		trimCalendar2Maximum(cal);
		return cal;
	}

	public static TimeZone getUtcTimeZone() {
		TimeZone tz = TimeZone.getTimeZone("Etc/UTC");
		return tz;
	}
	
	public static Calendar getUtcCalendarTodayThisHourTrimMinimum(){
		Calendar cal = getUtcCalendarToday();
		trimCalendarMinuteSecond(cal);
		return cal;
	}

	public static Calendar getUtcCalendarToday(){
		TimeZone utcTz = TimeZone.getTimeZone("Etc/UTC");
		Calendar cal = Calendar.getInstance(utcTz);
		return cal;
	}
	public static Calendar getMinUtcCalendarToday() {
		Calendar cal = getUtcCalendarToday();
		trimCalendar2Minimum(cal);
		return cal;
	}

	public static Calendar getMaxUtcCalendarToday() {
		Calendar cal = getUtcCalendarToday();
		trimCalendar2Maximum(cal);
		return cal;
	}

	public static void trimCalendarMinuteSecond(Calendar cal){
		if (cal != null){
			cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND,
					cal.getActualMinimum(Calendar.MILLISECOND));
			recomputeCalendar(cal);
		}
	}
	
	public static void trimCalendar2Minimum(Calendar cal) {
		if (cal != null) {
			cal.set(Calendar.HOUR_OF_DAY,
					cal.getActualMinimum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND,
					cal.getActualMinimum(Calendar.MILLISECOND));
			recomputeCalendar(cal);
		}
	}

	public static Date trimDateMinuteSecond(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		trimCalendarMinuteSecond(cal);
		recomputeCalendar(cal);
		Date rtnDate = cal.getTime();
		return rtnDate;
	}
	
	public static Date trimDate2Minimum(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		trimCalendar2Minimum(cal);
		recomputeCalendar(cal);
		Date rtnDate = cal.getTime();
		return rtnDate;
	}
	
	public static Date trimDate2Maximum(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		trimCalendar2Maximum(cal);
		recomputeCalendar(cal);
		Date rtnDate = cal.getTime();
		return rtnDate;
	}
	
	public static void trimCalendar2HourMinimum(Calendar cal){
		if (cal != null){
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
			recomputeCalendar(cal);
		}
	}
	
	public static void trimCalendar2Maximum(Calendar cal) {
		if (cal != null) {
			cal.set(Calendar.HOUR_OF_DAY,
					cal.getActualMaximum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND,
					cal.getActualMaximum(Calendar.MILLISECOND));
			recomputeCalendar(cal);

		}
	}


	
	public static Integer changeDate2Unixtime(Date date){
		Integer rtnUnixtime = null;
		if (date != null){
			rtnUnixtime = (int) (date.getTime() / 1000);
		}
		return rtnUnixtime;
	}
}
