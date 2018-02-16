package com.rabbitforever.generateJavaMVC.commons;

public class JavaOracle {
	public JavaOracle(){
		
	}
	public static String mapOracleType2JavaType(String _mySqlType){
		String javaType = "String";
		switch (_mySqlType){
			case "VARCHAR2":
				javaType = "String";
				break;
			case "CHAR":
				javaType = "String";
				break;
			case "TIMESTAMP(6)":
				javaType = "Date";
			case "DATE":
				javaType = "Date";
				break;
			case "TEXT":
				javaType = "String";
				break;
			case "NUMBER":
				javaType = "int";
				break;
		}
		return javaType;
	} // end mapMySqlType2JavaType
} // end class
