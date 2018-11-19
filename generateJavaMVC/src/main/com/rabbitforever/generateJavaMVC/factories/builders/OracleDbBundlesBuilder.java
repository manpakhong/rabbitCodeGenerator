package com.rabbitforever.generateJavaMVC.factories.builders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitforever.generateJavaMVC.bundles.MysqlDbProperties;
import com.rabbitforever.generateJavaMVC.bundles.OracleDbProperties;
import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
public class OracleDbBundlesBuilder extends BundlesBuilder<OracleDbProperties>{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className;
	public OracleDbBundlesBuilder(String fileName) throws Exception{
		super(fileName);
	}
	private String getClassName(){
		if(className == null){
			className = this.getClass().getName();
		}
		return className;
	}
	@Override
	public OracleDbProperties build() throws Exception{
		OracleDbProperties oracleDbProperties= null;
		try{
			oracleDbProperties = new OracleDbProperties();
			String connectionString = getPropValues("connection_string");
			oracleDbProperties.setConnectionString(connectionString);
			String classForName = getPropValues("class_for_name");
			oracleDbProperties.setClassForName(classForName);
			String host = getPropValues("host");
			oracleDbProperties.setHost(host);
			String port = getPropValues("port");
			oracleDbProperties.setPort(port);
			String schema = getPropValues("schema");
			oracleDbProperties.setSchema(schema);
			String username = getPropValues("username");
			oracleDbProperties.setUsername(username);
			String password = getPropValues("password");
			oracleDbProperties.setPassword(password);
			String systemSchema = getPropValues("system_schema");
			oracleDbProperties.setSystemSchema(systemSchema);
		} catch (Exception e) {
			this.logger.error(getClassName() + ".build() - this.fileName=" + this.fileName, e);
			throw e;
		}
		return oracleDbProperties;
	}
}
