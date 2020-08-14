package com.rabbitforever.generateJavaMVC.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.policies.SystemParams;

public class Misc {
	private final static Logger logger = LoggerFactory.getLogger(Misc.class);
	private final static String className = PropertiesFactory.class.getName();
	public final static String LANG_EN = "en";
	public final static String LANG_TC = "tc";
	private static PropertiesFactory propertiesFactory;
	public static String convertBundleNameFormat2ClassNameFormat(String bundleName) throws Exception {
		SysProperties sysProperties = null;
		String bundlePrefix = "";
		String voClassName = "";
		try {
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
			if (null != bundleName) {
				int idxOfTablePrefix = bundleName.indexOf(bundlePrefix);

				if (idxOfTablePrefix != -1) {
					String tempClassName = bundleName.substring(idxOfTablePrefix + bundlePrefix.length(),
							bundleName.length());

					String[] splitClassName = null;
					
					if (bundleName.contains("-")) {
						splitClassName = tempClassName.split("-");
					}
					else  {
						splitClassName = tempClassName.split("_");
					}

					for (int i = 0; i < splitClassName.length; i++) {
						voClassName += Misc.upperStringFirstChar(splitClassName[i]);
					} // end for
					
					String voNewClassName = "";
					String [] splitdotted = voClassName.split("\\.");
					if (splitdotted.length > 0) {
						for (int i = 0; i < splitdotted.length; i++) {
							voNewClassName += Misc.upperStringFirstChar(splitdotted[i]);
						} // end for
						
						voClassName = voNewClassName;
					}
				} // end if (idxOfTablePrefix != -1)
			} // end if (null != databaseName)
		} catch (Exception e) {
			logger.error(className + ".convertBundleNameFormat2ClassNameFormat()", e);
			throw e;
		} finally {
			sysProperties = null;
		}
		return voClassName;
	} // end convertTableNameFormat2ClassNameFormat
	public static String upperStringFirstChar(String _s)
	{
		String resultStr = "";
		if (_s.length() > 1)
		{
			resultStr = _s.substring(0, 1).toUpperCase() + _s.substring(1, _s.length());
		}
		else
		{
			resultStr = _s.toUpperCase();
		}
		
		return resultStr;
	} // end upperStringFirstChar()
	public static String lowerStringFirstChar(String _s)
	{
		String resultStr = "";
		if (_s.length() > 1)
		{
			resultStr = _s.substring(0, 1).toLowerCase() + _s.substring(1, _s.length());
		}
		else
		{
			resultStr = _s.toLowerCase();
		}
		
		return resultStr;
	} // end lowerStringFirstChar()


	
	public static String convertTableFieldsFormat2JavaPropertiesFormat(String _tableFieldName) {
		String javaPropertiesName = "";
		if (null != _tableFieldName) {
			_tableFieldName = _tableFieldName.toLowerCase();
			String[] splitTableFieldName = _tableFieldName.split("_");
			
			for (int i = 0; i < splitTableFieldName.length; i++) {
				if (i == 0) {
					javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
				} else {
					javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
				}
			} // end for
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertTableFieldsFormat2JavaPropertiesFormat
	
	public static String convertTableNameFormat2ClassNameFormat(String _databaseName) throws Exception {
		SysProperties sysPropertiesEo = null; 
		String tablePrefix = "";
		String voClassName = "";
		try {
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysPropertiesEo = propertiesFactory.getInstanceOfSysProperties();
			tablePrefix = sysPropertiesEo.getTablePrefix();
			_databaseName = _databaseName.toLowerCase();
			if (null != _databaseName) {
				int idxOfTablePrefix = _databaseName.indexOf(tablePrefix);

				if (idxOfTablePrefix != -1) {
					String tempClassName = _databaseName.substring(idxOfTablePrefix + tablePrefix.length(),
							_databaseName.length());

					String[] splitClassName = null;
					
					if (tempClassName.contains("_")){
						splitClassName = tempClassName.split("_");
					}

					if (splitClassName != null) {
						for (int i = 0; i < splitClassName.length; i++) {
							voClassName += Misc.upperStringFirstChar(splitClassName[i]);
						} // end for
					} else {
						voClassName = Misc.upperStringFirstChar(tempClassName);
					}

				} // end if (idxOfTablePrefix != -1)
			} // end if (null != databaseName)
		} catch (Exception e) {
			logger.error(className + ".convertTableNameFormat2ClassNameFormat()", e);
			throw e;
		} finally {
			sysPropertiesEo = null;
		}
		return voClassName;
	} // end convertTableNameFormat2ClassNameFormat

	public static String convertBundleFieldsFormat2JavaFnNoLangFormat(String bundleLineString, String lang) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];
				javaPropertiesName = "";

				if (firstPartString.contains("_")) {
					String[] splitTableFieldName = firstPartString.split("_");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
				}
				if (firstPartString.contains(".")) {
					String[] splitTableFieldName = firstPartString.split(".");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
				}
			}
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaFnNoLangFormat
	
//	public static String convertBundleFieldsFormat2JavaFnNoLangFormat(String bundleLineString, String lang) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)(\\." + lang + ")\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//				javaPropertiesName = "";
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
//				String thirdPartString = m.group(3);
//				
//				
//				String[] splitTableFieldName = firstPartString.split("_");
//				String upperLang = Misc.upperStringFirstChar(lang);
//				for (int i = 0; i < splitTableFieldName.length; i++) {
//					if (i == 0) {
//						javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//					} else {
//						javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//					}
//				} // end for
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaFnNoLangFormat
	
//	public static String convertBundleFieldsFormat2JavaPropertiesFormat(String bundleLineString) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
////				String thirdPartString = m.group(3);
//				if (!firstPartString.contains(".")) {
//					javaPropertiesName = "";				
//					String[] splitTableFieldName = firstPartString.split("_");
//					String upperLang = "";
//					for (int i = 0; i < splitTableFieldName.length; i++) {
//						if (i == 0) {
//							javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
//						} else {
//							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//						}
//					} // end for
//					javaPropertiesName += upperLang;
//				}
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaPropertiesFormat
	

	
	public static String convertBundleFieldsFormat2JavaPropertiesFormat(String bundleLineString) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];
//				String thirdPartString = m.group(3);
				
				
				if (firstPartString.contains("_")) {
					javaPropertiesName = "";				
					String[] splitTableFieldName = firstPartString.split("_");
					String upperLang = "";
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}
				if (firstPartString.contains(".")) {
					javaPropertiesName = "";				
					String[] splitTableFieldName = firstPartString.split("\\.");
					String upperLang = "";
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}
			}


		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaPropertiesFormat
	
	public static String convertBundleFieldsFormat2JavaPropertiesFormat(String bundleLineString, String lang) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];
				javaPropertiesName = "";

				
				if (firstPartString.contains("_")) {
					String[] splitTableFieldName = firstPartString.split("_");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}
				if (firstPartString.contains(".")) {
					String[] splitTableFieldName = firstPartString.split("\\.");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}
			}
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaPropertiesFormat
	
//	public static String convertBundleFieldsFormat2JavaPropertiesFormat(String bundleLineString, String lang) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)(\\." + lang + ")\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//				javaPropertiesName = "";
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
//				String thirdPartString = m.group(3);
//				
//				
//				String[] splitTableFieldName = firstPartString.split("_");
//				String upperLang = Misc.upperStringFirstChar(lang);
//				for (int i = 0; i < splitTableFieldName.length; i++) {
//					if (i == 0) {
//						javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
//					} else {
//						javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//					}
//				} // end for
//				javaPropertiesName += upperLang;
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaPropertiesFormat

	
	public static String convertBundleFieldsFormat2JavaFnFormat(String bundleLineString) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];


				if (firstPartString.contains("_")) {
					javaPropertiesName = "";					
				
				
				
					String[] splitTableFieldName = firstPartString.split("_");
					String upperLang = "";
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}
				if (firstPartString.contains(".")) {
					javaPropertiesName = "";					
				
				
				
					String[] splitTableFieldName = firstPartString.split("\\.");
					String upperLang = "";
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}
			}
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaFnFormat
	
//	public static String convertBundleFieldsFormat2JavaFnFormat(String bundleLineString) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
//
//				if (!firstPartString.contains(".")) {
//					javaPropertiesName = "";					
//				
//				
//				
//					String[] splitTableFieldName = firstPartString.split("_");
//					String upperLang = "";
//					for (int i = 0; i < splitTableFieldName.length; i++) {
//						if (i == 0) {
//							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//						} else {
//							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//						}
//					} // end for
//					javaPropertiesName += upperLang;
//				}
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaFnFormat
	
	public static String convertBundleFieldsFormat2JavaFnFormat(String bundleLineString, String lang) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];
				javaPropertiesName = "";

				
				if (firstPartString.contains("_")) {
					String[] splitTableFieldName = firstPartString.split("_");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}
				if (firstPartString.contains(".")) {
					String[] splitTableFieldName = firstPartString.split("\\.");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
					javaPropertiesName += upperLang;
				}

			}
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaFnFormat
	
	
//	public static String convertBundleFieldsFormat2JavaFnFormat(String bundleLineString, String lang) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)(\\." + lang + ")\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//				javaPropertiesName = "";
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
//				String thirdPartString = m.group(3);
//				
//				
//				String[] splitTableFieldName = firstPartString.split("_");
//				String upperLang = Misc.upperStringFirstChar(lang);
//				for (int i = 0; i < splitTableFieldName.length; i++) {
//					if (i == 0) {
//						javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//					} else {
//						javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//					}
//				} // end for
//				javaPropertiesName += upperLang;
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaFnFormat
	
	
	public static String convertBundleFieldsFormat2JavaPropertiesNoLangFormat(String bundleLineString, String lang) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];
				javaPropertiesName = "";

				
				if (firstPartString.contains("_")) {
					String[] splitTableFieldName = firstPartString.split("_");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
				}
				if (firstPartString.contains(".")) {
					String[] splitTableFieldName = firstPartString.split("\\.");
					String upperLang = Misc.upperStringFirstChar(lang);
					for (int i = 0; i < splitTableFieldName.length; i++) {
						if (i == 0) {
							javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
						} else {
							javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
						}
					} // end for
				}
			}
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaPropertiesNoLangFormat
//	public static String convertBundleFieldsFormat2JavaPropertiesNoLangFormat(String bundleLineString, String lang) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)(\\." + lang + ")\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//				javaPropertiesName = "";
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
//				String thirdPartString = m.group(3);
//				
//				
//				String[] splitTableFieldName = firstPartString.split("_");
//				String upperLang = Misc.upperStringFirstChar(lang);
//				for (int i = 0; i < splitTableFieldName.length; i++) {
//					if (i == 0) {
//						javaPropertiesName += Misc.lowerStringFirstChar(splitTableFieldName[i]);
//					} else {
//						javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);
//					}
//				} // end for
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaPropertiesNoLangFormat
	
//	public static String convertBundleFieldsFormat2OriginalUnderScoreFormat(String bundleLineString, String lang) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)(\\." + lang + ")\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
//				String thirdPartString = m.group(3);
//
//				if (fullString.contains(".")) {
//					javaPropertiesName = "";
//					javaPropertiesName = firstPartString + "." + lang;
//				}
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaPropertiesFormat
	
	public static String convertBundleFieldsFormat2OriginalUnderScoreFormat(String bundleLineString, String lang) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];
				javaPropertiesName = "";

				if (bundleLineString.contains(".")) {
					javaPropertiesName = "";
					javaPropertiesName = firstPartString + "." + lang;
				}
			}
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaPropertiesFormat
	
	
	public static String convertBundleFieldsFormat2OriginalUnderScoreFormat(String bundleLineString) {
		String javaPropertiesName = null;
		if (null != bundleLineString) {
			String [] stringArr = bundleLineString.split("=");
			if (stringArr.length == 2) {

				String firstPartString = stringArr[0];
				String secondPartString = stringArr[1];


				if (!firstPartString.contains(".")) {
					javaPropertiesName = "";
					
					javaPropertiesName = firstPartString;					
				}

			}
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertBundleFieldsFormat2JavaPropertiesFormat
//	public static String convertBundleFieldsFormat2OriginalUnderScoreFormat(String bundleLineString) {
//		String javaPropertiesName = null;
//		if (null != bundleLineString) {
//			String regEx = "(.*)\\s{0,}=\\s{0,}(.*)";
//			Pattern p = Pattern.compile(regEx);
//			Matcher m = p.matcher(bundleLineString);
//			boolean b = m.matches();
//			
//			if (b){
//
//				String fullString = m.group(0);
//				String firstPartString = m.group(1);
//				String secondPartString = m.group(2);
//
//				if (!firstPartString.contains(".")) {
//					javaPropertiesName = "";
//					
//					javaPropertiesName = firstPartString;					
//				}
//
//			}
//		} // end if (null != _tableFieldName)
//		return javaPropertiesName;
//	} // end convertBundleFieldsFormat2JavaPropertiesFormat
	
	


	public static String convertTableFieldsFormat2JavaFnFormat(String _tableFieldName) {
		String javaPropertiesName = "";
		if (null != _tableFieldName) {
			String[] splitTableFieldName = _tableFieldName.split("_");

			for (int i = 0; i < splitTableFieldName.length; i++) {

				javaPropertiesName += Misc.upperStringFirstChar(splitTableFieldName[i]);

			} // end for
		} // end if (null != _tableFieldName)
		return javaPropertiesName;
	} // end convertTableFieldsFormat2JavaPropertiesFormat
	

} // end class
