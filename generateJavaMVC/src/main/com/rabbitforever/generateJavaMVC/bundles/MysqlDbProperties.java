package com.rabbitforever.generateJavaMVC.bundles;

public class MysqlDbProperties extends PropertiesBase implements DbProperties{
	public static final String LANG_EN = PropertiesBase.LANG_EN;
	public static final String LANG_TCHI = PropertiesBase.LANG_TCHI;
	public MysqlDbProperties() {
		super();
}
	public MysqlDbProperties(String lang) {
		super(lang);
}
	private String connectionString;
	private String classForName;
	private String host;
	private String port;
	private String schema;
	private String username;
	private String password;
	private String systemSchema;
	public void setConnectionString(String connectionString){
		this.connectionString = connectionString;
	}
	public String getConnectionString(){
		return this.connectionString;
	}
	public void setClassForName(String classForName){
		this.classForName = classForName;
	}
	public String getClassForName(){
		return this.classForName;
	}
	public void setHost(String host){
		this.host = host;
	}
	public String getHost(){
		return this.host;
	}
	public void setPort(String port){
		this.port = port;
	}
	public String getPort(){
		return this.port;
	}
	public void setSchema(String schema){
		this.schema = schema;
	}
	public String getSchema(){
		return this.schema;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public String getUsername(){
		return this.username;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return this.password;
	}
	public void setSystemSchema(String systemSchema){
		this.systemSchema = systemSchema;
	}
	public String getSystemSchema(){
		return this.systemSchema;
	}
	@Override
	public String getConnectString(){
		String connString = "";
		connString = connectionString + host + ":" + port + "/" + schema;
		return connString;
	}

}
