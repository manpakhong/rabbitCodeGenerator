package com.rabbitforever.generateJavaMVC.services;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class OracleTypeMappingMgr extends TypeMappingMgr {
	private final Logger logger = Logger.getLogger(getClassName());
	private String getClassName(){
		return this.getClass().getName();
	}
	public OracleTypeMappingMgr() throws Exception {
		try {
			init();
		} catch (Exception e) {
			logger.error(getClassName() + ".OracleTypeMappingMgr()", e);
			throw e;
		}
	}
	@Override
	public String mappingTo(String databaseFieldType) throws Exception {
		String mappedTypeString = null;
		try {
			mappedTypeString = typeConversionMap.get(databaseFieldType);
		} catch (Exception e) {
			logger.error(getClassName() + ".OracleTypeMappingMgr() - databaseFieldType=" + databaseFieldType, e);
			throw e;
		}
		return mappedTypeString;
	}
	private void init() throws Exception {
		try {
			if (typeConversionMap == null) {
				this.typeConversionMap = new HashMap<String, String>();
			}
			
			typeConversionMap.put("NUMBER", "Long");
			typeConversionMap.put("VARCHAR2", "String");
			typeConversionMap.put("VARCHAR", "String");
			typeConversionMap.put("TIMESTAMP(6)", "Date");
			typeConversionMap.put("TIMESTAMP", "Date");
			typeConversionMap.put("DATE", "Date");
			typeConversionMap.put("CLOB", "String");
//			typeConversionMap.put("NUMBER", "Long");
//			typeConversionMap.put("NUMBER", "Long");
//			typeConversionMap.put("NUMBER", "Long");
		} catch (Exception e) {
			logger.error(getClassName() + ".init() -", e);
			throw e;
		}
	}
	
}
