package com.rabbitforever.generateJavaMVC.factories.builders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitforever.generateJavaMVC.bundles.PropertiesBase;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;

public abstract class BundlesBuilder <T> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Properties properties;
	protected PropertiesFactory propertiesFactory;
	private final static String baseFolderRoot = PropertiesBase.PROPERTIES_ROOT_DIR;
	protected String fileName;
	public BundlesBuilder(String fileName) throws Exception{
		this.fileName = fileName;
		init();
	}
	private String getClassName(){
		String className = this.getClassName();
		return className;
	}
	private void init() throws IOException{
		InputStream inputStream = null;
		properties = new Properties();
		try {
			
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();


			if (baseFolderRoot != null) {
				fileName = baseFolderRoot + fileName;
				inputStream = new FileInputStream(fileName);
			} else {
				inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
			}

			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
			}
			
		} catch (Exception e) {
			logger.error(getClassName() + ".init()", e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error(getClassName() + ".init()", e);
				throw e;
			}
		}
	}
	
	protected String getPropValues(String paramName) throws Exception{
		String result = null;
		try {
			if (properties != null){
				result = properties.getProperty(paramName);
			}
		} catch (Exception e) {
			logger.error(getClassName() + ".getPropValues() - paramName=" + paramName, e);
			throw e;
		} finally {
		}
		return result;
	}
	abstract public T build() throws Exception;
	
}
