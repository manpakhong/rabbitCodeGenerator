package com.rabbitforever.generateJavaMVC.commons;

public class JavaMySql {
	public JavaMySql(){
		
	}
	public static String mapMySqlType2JavaType(String _mySqlType){
		String javaType = "String";
		switch (_mySqlType){
			case "INT UNSIGNED":
				javaType = "int";
				break;
			case "VARCHAR":
				javaType = "String";
				break;
			case "TIMESTAMP":
				javaType = "Date";
				break;
			case "TEXT":
				javaType = "String";
				break;
		}
		return javaType;
	} // end mapMySqlType2JavaType
} // end class
