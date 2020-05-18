package com.rabbitforever.generateJavaMVC.services;

import java.util.Map;

public abstract class TypeMappingMgr {
	protected Map<String, String> typeConversionMap;
	protected String dataBaseType;
	abstract public String mappingTo(String databaseFieldType) throws Exception;
}
