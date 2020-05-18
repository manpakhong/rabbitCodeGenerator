package com.rabbitforever.generateJavaMVC.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommonUtils {
	private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	private final static String className = CommonUtils.class.getName();

	public static void trimStringArrayElement(String [] strArray){
		try{
			if (strArray != null && strArray.length > 0){
				for (int i=0; i < strArray.length; i++){
					if (strArray[i] != null){
						strArray[i] = strArray[i].trim();
					}
				}
			}
		} catch (Exception e){
			logger.error(className + ".trimStringArrayElement() -", e);
			throw e;
		}
	}
	
	public static boolean isNoDuplicationWithinArray(String [] strArray) throws Exception{
		boolean noDuplication = true;
		try{
			Map<String,String> noDuplicationMap = new HashMap<String, String>();
			for (String key: strArray){
				String value = noDuplicationMap.get(key);
				if (value != null){
					noDuplication = false;
					break;
				}
				noDuplicationMap.put(key, key);
			}
			
		} catch (Exception e){
			logger.error(className + ".splitByDelimited() -", e);
			throw e;
		}
		return noDuplication;
	}
	
	public static List<String> splitByDelimited(String string, String delimited) {
		List<String> stringList = null;
		try {
			String[] stringArray = string.split(delimited);
			stringList = Arrays.asList(stringArray);
		} catch (Exception e) {
			logger.error(className + ".splitByDelimited() -", e);
			throw e;
		}
		return stringList;
	}

	public static String repeatString(String str, int repeat) {
		String returnStr = null;
		try {
			char[] chars = new char[repeat];
			Arrays.fill(chars, str.charAt(0));
			returnStr = new String(chars);
		} catch (Exception e) {
			logger.error(className + ".repeatString() - str=" + str + ",repeat=" + repeat, e);
			throw e;
		}
		return returnStr;
	}



	public static String replaceLineWithSpace(String sql) {
		String result = null;
		try {
			result = sql.replace("\n", " ");
		} catch (Exception e) {
			logger.error(className + ".replaceLineWithSpace() - sql=" + sql, e);
			throw e;
		}

		return result;
	}

	public static String removeGarbageFromSql(String sql) {
		String result = null;
		try {
			result = sql.replace(";", "");

		} catch (Exception e) {
			logger.error(className + ".removeGarbageFromSql() - sql=" + sql, e);
			throw e;
		}

		return result;
	}

	public static boolean isFileExisted(String filePath){
		boolean isFileExisted = false;
		try{
			File f = new File(filePath);
			if(f.exists() && !f.isDirectory()) {
				isFileExisted = true;
			}
		} catch (Exception e){
			logger.error("CommonUtils.isFileExisted() - ", e);
		}
		return isFileExisted;
	}
	public static boolean deleteFileIfExisted(String filePath){
		boolean isDeleted = false;
		try{
			File f = new File(filePath);
			if (f.exists() && !f.isDirectory()){
				if (f.delete()){
					isDeleted = true;
				}
			}
		} catch (Exception e){
			logger.error("CommonUtils.deleteFileIfExisted() - ", e);
		}
		return isDeleted;
	}
	public static byte [] readFile2ByteArray(String filePath){
		byte [] byteArray = null;
		File file = null;
		InputStream is = null;
		try{
			if (isFileExisted(filePath)){
				file = new File(filePath);
		        is = new FileInputStream(file);
		        
		        // Get the size of the file
		        long length = file.length();
		    
		        if (length > Integer.MAX_VALUE) {
		            logger.warn("File: " + filePath + " - length is too large, larger than integer max value!");
		        }
		    
		        // Create the byte array to hold the data
		        byte[] bytes = new byte[(int)length];
		    
		        // Read in the bytes
		        int offset = 0;
		        int numRead = 0;
		        while (offset < bytes.length
		               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		            offset += numRead;
		        }
		    
		        // Ensure all the bytes have been read in
		        if (offset < bytes.length) {
		            throw new IOException("Could not completely read file "+file.getName());
		        }
		    
		        // Close the input stream and return bytes
		        byteArray = bytes;
		        
			} else {
				logger.warn("CommonUtils.readFile2ByteArray() - filePath: " + filePath +" is not existed");
			}
		} catch (Exception e){
			logger.error("CommonUtils.readFile2ByteArray() - ", e);
		} finally{
			try{
				if (is != null){
					is.close();
					is = null;
				}
			} catch (Exception e){
				logger.error("CommonUtils.readFile2ByteArray() - cannot be closed", e);
			}
		}
		return byteArray;
	}
	public static boolean isNumeric(String str){
		try{
			Double.parseDouble(str);
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}
	public static BigDecimal number2BigDecimal(Number number){
		BigDecimal rtnBigDecimal = null;
		if (number != null){
			rtnBigDecimal = new BigDecimal(number.intValue());
		}
		return rtnBigDecimal;
	}
	public static Integer number2Integer(Number number){
		Integer rtnInteger = null;
		if (number != null){
			rtnInteger = number.intValue();
		}
		return rtnInteger;
	}
	
	public static Long number2Long(Number number){
		Long rtnLong = null;
		if (number != null){
			rtnLong = number.longValue();
		}
		return rtnLong;
	}
	public static String genTimestampString(){
		Date now = new Date();
		String rtnStr = "";
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyyddMMHHmmss");
			rtnStr = format.format(now);
		} catch (Exception e){
			logger.error("CommonUtils.genTimestampString() - ", e);
		}
		return rtnStr;
	}
	public static String genOrgTimestampString(String orgId){
		Date now = new Date();
		String rtnStr = "";
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyyddMMHHmmss");
			rtnStr = format.format(now);
			rtnStr = orgId + "_" + rtnStr;
		} catch (Exception e){
			logger.error("CommonUtils.genOrgTimestampString() - ", e);
		}
		return rtnStr;
	}
	public static String convertDate2MySqlDateString(Date date){
		String mySqlDateString = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mySqlDateString = sdf.format(date);
		} catch (Exception e){
			logger.error("CommonUtils.convertDate2MySqlDateString() - ", e);
		}
		return mySqlDateString;
	}
	
	public static String convertCalendar2MySqlDateString(Calendar cal){
		String mySqlDateString = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(CalendarUtils.getUtcTimeZone());
			mySqlDateString = sdf.format(cal.getTime());
			
		} catch (Exception e){
			logger.error("CommonUtils.convertDate2MySqlDateString() - ", e);
		}
		return mySqlDateString;
	}
//	public static String convertCalendar2MySqlDateString(Calendar cal){
//		String mySqlDateString = "";
//		try{
//			mySqlDateString = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " " + 
//			cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
//		} catch (Exception e){
//			logger.error("CommonUtils.convertCalendar2MySqlDateString() - ", e);
//		}
//		return mySqlDateString;
//	}
	
	public static String convertUnixTime2DateString(Integer unixtime){
		Date unixTimeDate = convertUnixTime2Date(unixtime);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String dateString = sdf.format(unixTimeDate);
		return dateString;
	}
	
	public static Date convertUnixTime2Date(Integer unixtime){
		Date unixTimeDate = new Date((long) unixtime * 1000);
		return unixTimeDate;
	}
	
	public static int convertDate2UnixTime(Date date){
		int unixtime = (int) (date.getTime() / 1000);
		return unixtime;
	}
	
	public static List<String> regMatch(String sourceString, String patternString){
		List<String> matchStrList = new ArrayList<String>();
		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sourceString);
		while(matcher.find()){
			matchStrList.add(matcher.group());
		}
		return matchStrList;
	}
	
	public static boolean isInteger(String str){
		try{
			Integer.parseInt(str);
		} catch (Exception e){
			if (e instanceof NumberFormatException){
				return false;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public static int safeLongToInt(long l) {
		try{
		    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
		        throw new IllegalArgumentException
		            (l + " cannot be cast to int without changing its value.");
		    }
		} catch (Exception e){
			if (logger.isDebugEnabled()){
				logger.debug("CommonUtils.safeLongToInt() - cannot be cast to int without changing its value.");
			}
		}
	    return (int) l;
	}
	public static float safeDoubleToFloat(double d) {
		try{
		    if (d < Float.MIN_VALUE || d > Float.MAX_VALUE) {
		        throw new IllegalArgumentException
		            (d + " cannot be cast to float without changing its value.");
		    }
		} catch (Exception e){
			if (logger.isDebugEnabled()){
				logger.debug("CommonUtils.safeDoubleToFloat() - cannot be cast to float without changing its value.");
			}
		}
	    return (float) d;
	}
	
}
