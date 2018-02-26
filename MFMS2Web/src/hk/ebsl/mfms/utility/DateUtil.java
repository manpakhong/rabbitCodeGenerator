package hk.ebsl.mfms.utility; 

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * @author pklam
 *
 * Oct 18, 2005
 * 
 */
public class DateUtil
{
	private static final Logger logger = Logger.getLogger(DateUtil.class);
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private static SimpleDateFormat dateBeginFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0 Z");
	private static SimpleDateFormat dateEndFormatter = new SimpleDateFormat("yyyy-MM-dd 23:59:59.999 Z");
    private static SimpleDateFormat clockFormatter = new SimpleDateFormat("HH:mm:ss");
    
    public static void init(ResourceBundle resources)
    {    	
    	String dateFormat = resources.getString("application.format.date.display");
    	if (logger.isDebugEnabled()) logger.debug("Date Format: "+dateFormat);
    	if (dateFormat != null) dateFormatter = new SimpleDateFormat(dateFormat);
    	
    	String timeFormat = resources.getString("application.format.date.time.display");
    	if (logger.isDebugEnabled()) logger.debug("Time Format: "+timeFormat);
    	if (timeFormat != null) timeFormatter = new SimpleDateFormat(timeFormat);
    	
    	String dateBeginFormat = resources.getString("application.format.date.begin");
    	if (logger.isDebugEnabled()) logger.debug("Date Begin Format: "+dateBeginFormat);
    	if (dateBeginFormat != null) dateBeginFormatter = new SimpleDateFormat(dateBeginFormat);
    	
    	String dateEndFormat = resources.getString("application.format.date.end");    	
    	if (logger.isDebugEnabled()) logger.debug("Date End Format: "+dateEndFormat);
    	if (dateEndFormat != null) dateEndFormatter = new SimpleDateFormat(dateEndFormat);
    }

	public static String convertTimestampToString(Timestamp ts) {

		if (ts != null) {
			String timeString = datetimeFormatter.format(ts);
			return timeString;
		} else
			return null;
	}

	public static Timestamp convertStringToTimestamp(String str_date) throws ParseException {

		if (str_date.length() != 0) {
			Date date = (Date) datetimeFormatter.parse(str_date);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			return timeStampDate;

		} else
			return null;
	}

    /**
     * Convert a String Object to a Timestamp
     *
     * @param string the String to be converted.
     * @return the converted Timestamp Object.
     * @throws ParseException if date parse error occurs. 
     * 
     **/               
    public static Timestamp toTimestamp(
    	String timestamp)
    		throws ParseException
    {
    	try
    	{
    		SimpleDateFormat timeFormatter = (SimpleDateFormat)DateUtil.timeFormatter.clone();
    		return toTimestamp(timestamp,timeFormatter);
    	} catch (Exception e)
    	{
    		SimpleDateFormat dateFormatter = (SimpleDateFormat)DateUtil.dateFormatter.clone();
    		return toTimestamp(timestamp, dateFormatter);
    	}
    }

    /**
     * Convert a String Object to a Timestamp, representing begin of a Date
     *
     * @param string the String to be converted.
     * @return the converted Timestamp Object.
     * @throws ParseException if date parse error occurs.
     *  
     **/               
    public static Timestamp toBeginTimestamp(
    	String date)
			throws ParseException
    {
    	SimpleDateFormat dateFormatter = (SimpleDateFormat)DateUtil.dateFormatter.clone();
    	return toBeginTimestamp(date,dateFormatter);
    }
    public static Timestamp toBeginTimestamp(Timestamp timestamp)
    {
    	try
    	{
    		if (timestamp == null) return null;
    		return toBeginTimestamp(DateUtil.toTimeString(timestamp));
    	} catch (Exception e)
    	{
    		return null;
    	}
    }
    
    /**
     * Convert a String Object to a Timestamp, representing end of a Date
     *
     * @param string the String to be converted.
     * @return the converted Timestamp Object.
     * @throws ParseException if date parse error occurs.
     * 
     **/               
    public static Timestamp toEndTimestamp(
    	String date)
			throws ParseException
    {
    	SimpleDateFormat dateFormatter = (SimpleDateFormat)DateUtil.dateFormatter.clone();
    	return toEndTimestamp(date,dateFormatter);
    }

    /**
     * Convert a String Object to a Timestamp with specified Date Format, representing begin of a Date
     *
     * @param string the String to be converted.
     * @return the converted Timestamp Object.
     * @throws ParseException if date parse error occurs.
     * 
     **/               
    public static Timestamp toBeginTimestamp(
    	String date, 
    	SimpleDateFormat m_SimpleDateFormat)
			throws ParseException
    {
    	Date theDate = toDate(date,m_SimpleDateFormat);
    	
    	SimpleDateFormat dateBeginFormatter = (SimpleDateFormat)DateUtil.dateBeginFormatter.clone();
    	String beginTime = dateBeginFormatter.format(theDate);
        
    	SimpleDateFormat timeFormatter = (SimpleDateFormat)DateUtil.timeFormatter.clone();
    	return toTimestamp(beginTime,timeFormatter);
    }

    /**
     * Convert a String Object to a Timestamp with specified Date Format, representing end of a Date
     *
     * @param string the String to be converted.
     * @return the converted Timestamp Object.
     * @throws ParseException if date parse error occurs. 
     * 
     **/               
    public static Timestamp toEndTimestamp(
    	String date, 
    	SimpleDateFormat m_SimpleDateFormat) 
    		throws ParseException
    {
    	Date theDate = toDate(date,m_SimpleDateFormat);
    	
    	SimpleDateFormat dateEndFormatter = (SimpleDateFormat)DateUtil.dateEndFormatter.clone();
    	String endTime = dateEndFormatter.format(theDate);
        
    	SimpleDateFormat timeFormatter = (SimpleDateFormat)DateUtil.timeFormatter.clone();
    	return toTimestamp(endTime,timeFormatter);
    }
    
    /**
     * Convert a String Object to a Timestamp with specified Date Format
     *
     * @param string the String to be converted.
     * @return the converted Timestamp Object.
     * @throws ParseException if date parse error occurs.
     * 
     **/               
    public static Timestamp toTimestamp(
    	String timestamp, 
    	SimpleDateFormat m_SimpleDateFormat) 
    		throws ParseException
    {
    	Date date = m_SimpleDateFormat.parse(timestamp);
        Timestamp ret = new Timestamp(date.getTime());
        return ret;                 
    }
    
    /** Align the timestamp to the beginning of its [tOrg] original date.
     * @param tOrg
     * @return a Timestamp with the same date as tOrg but with time at 00:00.
     * @throws ParseException
     */
    public static Timestamp shiftToBeginTimestamp(Timestamp tOrg) throws ParseException {
    	return DateUtil.toBeginTimestamp(DateUtil.toDateString(tOrg)); 
    }

    /**
     * Convert a String Object to a Date
     *
     * @param string the String to be converted.
     * @return the converted Date Object.
     * @throws ParseException if date parse error occurs.
     * 
     **/               
    public static Date toDate(
    	String date)
			throws ParseException
    {
    	SimpleDateFormat dateFormatter = (SimpleDateFormat)DateUtil.dateFormatter.clone();
        return toDate(date,dateFormatter);         
    }

    /**
     * Convert a String Object to a Date with specified Date Format
     *
     * @param string the String to be converted.
     * @return the converted Date Object.
     * @throws ParseException if date parse error occurs.
     * 
     **/         
    public static Date toDate(
    	String date, 
    	SimpleDateFormat m_SimpleDateFormat)
			throws ParseException
    {
    	return m_SimpleDateFormat.parse(date);    
                
    }

	/**
	 * Convert a Date Object to a Time String 
	 *
	 * @param string the Date to be converted.
	 * @return the converted Time String.
	 * 
	 **/   
	public static String toTimeString(Date date)
	{
		SimpleDateFormat timeFormatter = (SimpleDateFormat)DateUtil.timeFormatter.clone();
		return timeFormatter.format(date);
	}

	/**
	 * Convert a Date Object to a Date String 
	 *
	 * @param string the Date to be converted.
	 * @return the converted Date String.
	 * 
	 **/   	
	public static String toDateString(Date date)
	{
		SimpleDateFormat dateFormatter = (SimpleDateFormat)DateUtil.dateFormatter.clone();
		return dateFormatter.format(date);
	}

	/**
	 * Convert a Timestamp Object to a Time String 
	 *
	 * @param string the Timestamp to be converted.
	 * @return the converted Time String.
	 * 
	 **/   		
	public static String toTimeString(Timestamp timestamp)
	{
		SimpleDateFormat timeFormatter = (SimpleDateFormat)DateUtil.timeFormatter.clone();
		return timeFormatter.format(timestamp);
	}

	/**
	 * Convert a Timestamp Object to a Date String 
	 *
	 * @param string the Timestamp to be converted.
	 * @return the converted Date String.
	 * 
	 **/
	public static String toDateString(Timestamp timestamp)
	{
		SimpleDateFormat dateFormatter = (SimpleDateFormat)DateUtil.dateFormatter.clone();
		return dateFormatter.format(timestamp);
	}
	
	/**
	 * Convert a Timestamp Object to a Clock String 
	 *
	 * @param timestamp the Timestamp to be converted.
	 * @return the converted Date String.
	 * 
	 **/	
	public static String toClockString(Timestamp timestamp)
	{
		SimpleDateFormat dateFormatter = (SimpleDateFormat)DateUtil.clockFormatter.clone();
		return dateFormatter.format(timestamp.getTime());
	}	
	

	/**
	 * This method compares two date and return the milisecs difference.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long compare(Date date1, Date date2)
	{
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		
		return time2 - time1;
	}
	
	/**
	 * This method returns a Date that is s days before date.
	 * 
	 * @param date
	 * @param s
	 */
	public static Date subtract(Date date, int s)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.DATE, -1*s);
		
		return calendar.getTime();
	}
	
	/**
	 * This method compares the two date.
	 * 
	 * @param date1
	 * @param date2
	 * @return true if date of date1 is before date2. 
	 */
	public static boolean dateBefore(Date date1, Date date2)
	{
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar1.set(Calendar.HOUR, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
				
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		calendar2.set(Calendar.HOUR, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);

		return calendar1.before(calendar2);
	}
	
	/**
	 * This method gets the Calendar according to the clock string.
	 * 
	 * @param clockString
	 * @return
	 * @throws ParseException
	 */
	public static Calendar toClock(String clockString) 
		throws ParseException
	{
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(clockFormatter.parse(clockString));
		calendar.setTime(getClockFormatter().parse(clockString));
		return calendar;
	}
	
	/**
	 * This method gets the clock string of the calendar.
	 * 
	 * @param calendar
	 */
	public static String toClockString(Calendar calendar)
	{
		//return clockFormatter.format(calendar.getTime());
		return getClockFormatter().format(calendar.getTime());		
	}
	
	/**
	 * This method checks if date is inside start and end inclusively.
	 * 
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isInclusiveRange(Date date, Date startDate, Date endDate)
	{
		if (startDate != null && startDate.after(date)) return false;
		if (endDate != null && endDate.before(date)) return false;
		if (startDate == null) return endDate == null ? true : !endDate.before(date);
		if (endDate == null) return !startDate.after(date);
		return !startDate.after(date) && !endDate.before(date);
	}
	
	/**
	 * This method returns today with beginning time.
	 * @return
	 * @throws Exception
	 */
	public static Date getToday()
		throws Exception
	{
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
		today = dateTimeFormat.parse(dateFormat.format(today) + " 00:00:00.0");
		return today;
	}	
	
	/**
	 * @param adCandidate
	 * @return true if adCandidate is a date of current month; otherwise false.
	 */
	public static boolean isCurrentMonth(Date adCandidate) {
	Calendar cCal = Calendar.getInstance();
	int iCandidateMo, iCandidateYr;
	
		cCal.setTime(adCandidate);
		iCandidateMo = cCal.get(Calendar.MONTH);
		iCandidateYr = cCal.get(Calendar.YEAR);  		
		cCal.setTime(new Date());
		return (iCandidateMo==cCal.get(Calendar.MONTH) 
			&& iCandidateYr==cCal.get(Calendar.YEAR));
	}
	
	/**@param adCandidate1
	 * @param adCandidate2
	 * @return true if adCandidate1 has month before that of adCandidate2. if either of 
	 *         the argument is null, this method returns false too.
	 */
	public static boolean isMonthBefore(Date adCandidate1, Date adCandidate2) {
	boolean bRtn=false;
	Calendar cCal = Calendar.getInstance();
	int iCandidateMo1, iCandidateYr1, iCandidateMo2, iCandidateYr2;
	
		if (adCandidate1!=null && adCandidate2!=null) {
  		cCal.setTime(adCandidate1);
  		iCandidateMo1 = cCal.get(Calendar.MONTH);
  		iCandidateYr1 = cCal.get(Calendar.YEAR);  
  		cCal.setTime(adCandidate2);		
  		iCandidateMo2 = cCal.get(Calendar.MONTH);
  		iCandidateYr2 = cCal.get(Calendar.YEAR);
  		
  		bRtn = (iCandidateYr1 < iCandidateYr2 ||
  			(iCandidateYr1==iCandidateYr2 && iCandidateMo1 < iCandidateMo2));
		}
		return bRtn;
	}
	
	/** This method returns a list of hours in dd format
	 * @param ab24Hrs
	 * @param abRequireLeadingZero
	 * @return a list of hours in dd format
	 */
	public static List<String> getHourList(boolean ab24Hrs, boolean abRequireLeadingZero) {
	final int iLST_SIZE = (ab24Hrs ? 24 : 12);
		return DateUtil.genNumberList(iLST_SIZE, abRequireLeadingZero);
	}
	
	/** This method returns a list of minutes in mm format
	 * @param abRequireLeadingZero
	 * @return a list of minutes in mm format
	 */
	public static List<String> getMinList(boolean abRequireLeadingZero) {
		return DateUtil.genNumberList(60, abRequireLeadingZero);
	}
	
	/** @return a list of number (in String). If abRequireLeadingZero is true, 
	 * 					the elements inside would have a leading zero in the case that the 
	 * 					original char length of the element < 2. 
	 */
	private static List<String> genNumberList(int aiSize, boolean abRequireLeadingZero) {
	final String sPATTERN = (abRequireLeadingZero ? "{0,number,00}" : "{0,number}");
	List<String> lRtn = new ArrayList<String>(aiSize);
	
		for (int iCnt=0; iCnt < aiSize; iCnt++) 
			lRtn.add (MessageFormat.format(sPATTERN,iCnt));
		
		return lRtn;
	}
	
	/**
	 * This method returns true if the 2 ranges startRange1 - endRange1 & startRange2 - endRange2 are overlapping
	 * each other.
	 * 
	 * @param startRange1
	 * @param endRange1
	 * @param startRange2
	 * @param endRange2
	 * @return
	 */
	public static boolean isRangeOverlapped(Date startRange1, Date endRange1, Date startRange2, Date endRange2)
	{
		if (logger.isDebugEnabled()) logger.debug("isRangeOverlapped(): ["+startRange1+","+endRange1+","+startRange2+","+endRange2);
		
		if (startRange2.before(startRange1))
			return endRange2 == null || !endRange2.before(startRange1);
		else
		{
			if (endRange1 == null) return true;
			else return !endRange1.before(startRange2);
		}
	}
	
	/**
	 * This method returns true if startRange1-endRange1 includes startRange2-endRange2
	 * 
	 * @param startRange1
	 * @param endRange1
	 * @param startRange2
	 * @param endRange2
	 * @return
	 */
	public static boolean isRangeIncluded(Date startRange1, Date endRange1, Date startRange2, Date endRange2)
	{
		if (endRange1 == null) return !startRange2.before(startRange1);
		else return !startRange2.before(startRange1) && endRange2 != null && !endRange1.before(endRange2);
	}
	
	/**
	 * @param adOrg
	 * @return
	 */
	public static Date toMonthEnd(Date adOrg) {
	Calendar cRtn = GregorianCalendar.getInstance();	
		cRtn.setTime(adOrg);
		cRtn.roll(Calendar.DAY_OF_MONTH, 0 - cRtn.get(Calendar.DAY_OF_MONTH));
		return cRtn.getTime();
	}
	
	/**
	 * This method uses original as sample to find a date which is in the future.
	 * if original has passed, it will move the month forward until the date is in the future.
	 *   
	 * @param originatl
	 * @return
	 * @throws Exception
	 */
	public static Date getFutureMonthSameDay(Date original) throws Exception 
	{
		Calendar today = Calendar.getInstance();
		if (logger.isDebugEnabled()) logger.debug("Today is ["+today.getTime()+"]");
		
		Calendar curr = Calendar.getInstance();
		curr.setTime(original);
		curr.set(Calendar.MONTH, today.get(Calendar.MONTH));
		
		while (curr.before(today)) curr.add(Calendar.MONTH, 1);

		return curr.getTime();
	}
	
	/**
	 * @return
	 */
	public static SimpleDateFormat getDateBeginFormatter() 
	{
		return dateBeginFormatter;
	}

	/**
	 * @param dateBeginFormatter
	 */
	public static void setDateBeginFormatter(SimpleDateFormat dateBeginFormatter) 
	{
		DateUtil.dateBeginFormatter = dateBeginFormatter;
	}

	/**
	 * @return
	 */
	public static SimpleDateFormat getDateEndFormatter() 
	{
		return dateEndFormatter;
	}

	/**
	 * @param dateEndFormatter
	 */
	public static void setDateEndFormatter(SimpleDateFormat dateEndFormatter) 
	{
		DateUtil.dateEndFormatter = dateEndFormatter;
	}

	/**
	 * @return
	 */
	public static SimpleDateFormat getDateFormatter() 
	{
		return dateFormatter;
	}

	/**
	 * @param dateFormatter
	 */
	public static void setDateFormatter(SimpleDateFormat dateFormatter) 
	{
		DateUtil.dateFormatter = dateFormatter;
	}

	/**
	 * @return
	 */
	public static SimpleDateFormat getTimeFormatter() 
	{
		return timeFormatter;
	}

	/**
	 * @param timeFormatter
	 */
	public static void setTimeFormatter(SimpleDateFormat timeFormatter) 
	{
		DateUtil.timeFormatter = timeFormatter;
	}

	/**
	 * Test Main.
	 * 
	 * @param argv
	 */
	public static void main(String[] argv)
	{
		String date = "2006-12-29 11:17:16.0 +0800";
		for (int i=0; i<500; i++)
		{
			try
			{
				System.out.println(date+"="+DateUtil.toTimestamp(date));

			} catch (Exception e)
			{
				
			}
		}
	}

	/**
	 * @return Returns a cloned clockFormatter.
	 */
	public static SimpleDateFormat getClockFormatter() {
		//return clockFormatter;
		return (SimpleDateFormat)clockFormatter.clone();
	}

	/**
	 * @param clockFormatter The clockFormatter to set.
	 */
	public static void setClockFormatter(SimpleDateFormat clockFormatter) {
		DateUtil.clockFormatter = clockFormatter;
	}

}

