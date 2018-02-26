package hk.ebsl.mfms.utility; 

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * @author pklam
 *
 * Oct 18, 2005
 * 
 */
public class StringUtil 
{ 
	private static String numberFormat = "#,##0.00";
	private static Integer bigDecimalNumberOfDecimal = null;

	public static void init(ResourceBundle resources)
	{
		String format = resources.getString("application.format.double.display");
		if (format != null) numberFormat = format;
		String numDecimal = resources.getString("application.format.big.decimal.places");
		if (numDecimal != null) bigDecimalNumberOfDecimal = Integer.parseInt(numDecimal);
	}
	
    /**
     * Convert a String Object to a specific Type
     *
     * @param string the String to be converted.
     * @param types the Object Type to be converted to.
     * @return the converted Object.
     * @throws ParseException if date parsing error occur
     * @throws NumberFormatException if number parsing error occur 
     * 
     **/       
    public static Object toObject(String string, int types) 
    	throws ParseException, NumberFormatException
    {
    	
    	if (string == null || "".equals(string.trim()))
    		return null;
    	
    	switch (types)
    	{
    		case Types.DATE:
    			return DateUtil.toDate(string);
    		case Types.TIMESTAMP:
    			return DateUtil.toTimestamp(string);
    		case Types.VARCHAR:
    			return string;
    		case Types.INTEGER:
    			return new Integer(string);
			case Types.NUMERIC:
				return new Integer(string);
    		case Types.DOUBLE:
    			return new Double(string);
    		case Types.BIGINT:
    			return new Long(string);
			case Types.SMALLINT:
				return new Integer(string);
    		case Types.LONGVARCHAR:
    			return string;
    		default:
            	return null;
    	}
    }    

	/**
	 * Convert a String Object to a specific Class object.
	 * 
	 * @param objClass
	 * @param string
	 * @return
	 */
	public static Object toObject(Class objClass, String string)
	{
		try
		{
			if (string == null || string.length() == 0) return null;
		
			if (objClass == int.class || objClass == java.lang.Integer.class)
			{
				return new Double(string).intValue();
			
			} else if (objClass == double.class || objClass == java.lang.Double.class)
			{
				return new Double(new DecimalFormat(numberFormat).parse(string).doubleValue());
			
			} else if (objClass == java.lang.Long.class || objClass == long.class)
			{
				return new Long(string);
			
			} else if (objClass == java.util.Calendar.class)
			{
				java.util.Calendar calendar = java.util.Calendar.getInstance();			
				calendar.setTimeInMillis(DateUtil.toTimestamp(string).getTime());
				
				return calendar;
		
			} else if (objClass == java.sql.Timestamp.class)
			{
					return DateUtil.toTimestamp(string);
			} else if (objClass == java.util.Date.class)
			{
				return DateUtil.toDate(string);
			
			} else if (objClass == boolean.class || objClass == Boolean.class)
			{
				return new Boolean(string.equals("true") ? true : false);
				
			}else
			{
				return string;
			}
			
		} catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Convert an Object to String
	 *
	 * @param object the Object to be converted.
	 * @return the String representation of the object.
	 * 
	 **/       
	public static String toString(Object object)
	{
		if (object != null)
		{
			if (object instanceof java.sql.Timestamp)
			{
				return DateUtil.toTimeString((java.sql.Timestamp)object);
			
			} else if (object instanceof java.util.Calendar)
			{
				return DateUtil.toTimeString(((java.util.Calendar)object).getTime());
			
			} else if (object instanceof java.util.Date)
			{
				return DateUtil.toDateString((java.util.Date)object);
			
			} else if (object instanceof java.math.BigInteger)
			{
				return bigInt2String((java.math.BigInteger)object);
			
			} else if (object instanceof Double)
			{
				return double2String(new DecimalFormat(numberFormat),((java.lang.Double)object).doubleValue());

			} else if (object instanceof Long)
			{
				return long2String((Long)object);

			} else
			{
				return object.toString();
			}
		} else
		{
			return "";
		}   
	}

	/**
	 * Convert double to string.
	 * 
	 * @param format
	 * @param d
	 * @return
	 */
	public static String double2String(NumberFormat format, double d)
	{
		if (format == null)
		{
			return new Double(d).toString();
		} else
		{
			return format.format(d);
		}
	}
	public static String double2String(double d)
	{
		return new DecimalFormat(numberFormat).format(d);
	}
	
	
	
	
	/**
	 * Convert BigInteger to string.
	 * @param bigInt
	 * @return
	 */
	public static String bigInt2String(
			BigInteger bigInt)
	{
		if (bigInt == null) 
		{
			return null;
		} else
		{
			return bigInt.toString();	
		}
	}
	
	/**
	 * Convert int to string with minimum length.
	 * 
	 * @param integer
	 * @param minlength
	 * @return
	 */
	public static String int2String(
			int integer, 
			int minlength)
	{
		String numInt = new Integer(integer).toString();
		
		return padZero(numInt, minlength);
	}

	/**
	 * Convert Long to string.
	 * 
	 * @param longValue
	 * @return
	 */
	public static String long2String(
			Long longValue)
	{
		if (longValue == null) 
		{
			return null;
		} else
		{
			return longValue.toString();	
		}
	}

	/**
	 * Pad Zero to the String.
	 * 
	 * @param string
	 * @param minlength
	 * @return
	 */
	public static String padZero(
		String string, 
		int minlength)
	{
		if (string == null) return null;
		
		int zeros = minlength - string.length();
		for (int i=0; i<zeros; i++)
		{
			string = "0"+string;
		}
		
		return string;
	}	

	/**
	 * Replace substring with another substring.
	 *  
	 * @param origin the original string.
	 * @param oldStr the substring to be replaced.
	 * @param newStr the new substring for replacement.
	 * @return 
	 */
	public static String replaceAll(
		String origin,
		String oldStr,
		String newStr)
	{
		StringBuffer strBuf = new StringBuffer(origin);
		int from = 0, index;
		while ((index = origin.indexOf(oldStr, from)) != -1)
		{			
			strBuf.replace(index, index + oldStr.length(), newStr);
			from = index + newStr.length();
			origin = strBuf.toString();
		}
		return strBuf.toString();
	}
	
	/**
	 * Split a string into 2 by the delimiter.
	 * 
	 * @param origin
	 * @param delimiter
	 * @return
	 */
	public static String[] split(
		String origin, 
		String delimiter)
	{
		String prefix = "";
		String suffix = "";
	
		if (origin != null)
		{
			int index = origin.indexOf(delimiter);
			
			if (index < 0) suffix = origin;
			else
			{
				prefix = origin.substring(0, index);
				suffix = origin.substring(index + delimiter.length());
			}
		}
		
		return new String[] { prefix, suffix };
	}

	/** @return true if asVal is null or empty string or space(s); 
	 *          otherwise false.
	 */
	public static boolean isSpace(String asVal) {
		return (asVal==null || asVal.trim().length() <= 0);
	}
	
	/** @return srcStr if is not "" and null, else return null
	 * 
	 */
	public static String getNotSpaceValueOrNull(String srcStr){
		String result=StringUtil.isSpace(srcStr) ? null : srcStr;
		return result;	
	}
	
	/** @return srcStr if is not "" and null, else return defaultStr 
	 * 
	 */
	public static String getNotSpaceValueOrDefault(String srcStr,String defaultStr){
		String result=StringUtil.isSpace(srcStr) ? defaultStr : srcStr;
		return result;	
	}
	
	/** @return srcObject.toString if is not null, else return defaultStr
	 * 
	 */
	public static String getNotNullValueOrDefault(Object srcObject,String defaultStr){		
		String result= (srcObject!=null) ? srcObject.toString() : defaultStr;
		return result;	
	}
	public static String getNumberFormat() {
		return numberFormat;
	}
	
	public static Integer getBigDecimalNumberOfDecimal() {
		return bigDecimalNumberOfDecimal;
	}

	public static Map<String, Object> toMap(String properties)
	{
		try
		{
			Map<String, Object> map = new HashMap<String, Object>();
			
			Properties prop = new Properties();
			prop.load(new ByteArrayInputStream(properties.getBytes()));
			
			Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<Object, Object> entry = it.next();
				map.put((String)entry.getKey(), entry.getValue());
			}
			return map;
			
		} catch (Exception e)
		{
			return null;
		}
	}
	
	/** This method compares the 2 arguments' value. Basically, it uses the 
	 *  {@link String#compareTo(String)} or {@link String#compareToIgnoreCase(String)} 
	 *  if abIgnoreCase is true to compare the 2 values. In addition, this method returns
	 *  0 if both of the args are null; greater than 0 if asCand1 is null; or less than 0
	 *  if asCand2 is null.
	 * @param asCand1
	 * @param asCand2
	 * @param abIgnoreCase
	 * @return 
	 */
	public static int compareEvenNull(String asCand1, String asCand2, boolean abIgnoreCase) {
	int iRtn=0;
	
		if (asCand1!=null && asCand2!=null) {
			if (abIgnoreCase)
				iRtn = asCand1.compareToIgnoreCase(asCand2);
			else
				iRtn = asCand1.compareTo(asCand2);
		} else if (asCand1!=null)
			iRtn = -1;
		else if (asCand2!=null)
			iRtn = 1;
		
		return iRtn;
	}
	
	/**
	 * This method checks if the str contains only characters = character.
	 * 
	 * @param str
	 * @param character
	 * @return
	 */
	public static boolean isPlainCharacter(String str, String character)
	{
		if (character.length() == 0 && str.length() > 0) return false;
		
		char checkingChar = character.charAt(0);
		for (char ch : str.toCharArray())
			if (ch != checkingChar) return false;
		
		return true;
	}

	/**
	 * This method gets the first <length> substring from string.
	 * 
	 * @param string
	 * @param length
	 * @return
	 */
	public static String substring(String string, Integer length)
	{
		return string == null ? null : (string.length() < length ? string : string.substring(0, length));
	}
	
	/**
	 * This method removes HTML tag from the string.
	 * 
	 * @param string
	 * @return
	 */
	public static String removeHTMLTag(String string)
	{
		try
		{
			if (string == null) return null;
		
			Reader in = new BufferedReader (new StringReader(string));
		    Html2Text parser = new Html2Text();
		    parser.parse(in);
		    in.close();
		    return parser.getText();
		     
		} catch (Exception e)
		{
			return null;
		}
	}
	
	private static class Html2Text extends HTMLEditorKit.ParserCallback {
		 StringBuffer s;

		 public Html2Text() {}

		 public void parse(Reader in) throws IOException {
		   s = new StringBuffer();
		   ParserDelegator delegator = new ParserDelegator();
		   // the third parameter is TRUE to ignore charset directive
		   delegator.parse(in, this, Boolean.TRUE);
		 }

		 public void handleText(char[] text, int pos) {
		   s.append(text);
		 }

		 public String getText() {
		   return s.toString();
		 }
	}
	
	public static void main(String[] argv)
	{
//		BigDecimal dc = new BigDecimal("1234567890.0123456789");
//		dc = dc.round(new MathContext(6));
//		System.out.println(dc.toPlainString());
//		
//		System.out.println(new BigDecimal("1234567890.01234").setScale(6, BigDecimal.ROUND_HALF_UP));
		
		String a = "<font color=\"red\"> a<10 </font>";
		System.out.print(StringUtil.removeHTMLTag(a));
	}
	
} 
