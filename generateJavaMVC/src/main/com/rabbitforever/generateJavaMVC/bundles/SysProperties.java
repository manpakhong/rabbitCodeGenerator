package com.rabbitforever.generateJavaMVC.bundles;
public class SysProperties extends PropertiesBase{
	public static final String LANG_EN = PropertiesBase.LANG_EN;
	public static final String LANG_TCHI = PropertiesBase.LANG_TCHI;
	public static final String DATABASE_MYSQL = "mysql";
	public static final String DATABASE_ORACLE = "oracle";
	public SysProperties() {
		super();
}
	public SysProperties(String lang) {
		super(lang);
}
	private String database;
	private String packageName;
	private String outputRootDirectory;
	private String systemRootDirectory;
	private String javaDirName;
	private String modelsDirName;
	private String eosDirName;
	private String servicesDirName;
	private String daosDirName;
	private String sosDirName;

	private String tablePrefix;
	private String bundleDirName;
	private String factoriesDirName;
	private String factoriesBuilderDirName;
	private String utilsDirName;
	
	
	public String getUtilsDirName() {
		return utilsDirName;
	}
	public void setUtilsDirName(String utilsDirName) {
		this.utilsDirName = utilsDirName;
	}
	public String getSosDirName() {
		return sosDirName;
	}
	public void setSosDirName(String sosDirName) {
		this.sosDirName = sosDirName;
	}
	public void setDatabase(String database){
		this.database = database;
	}
	public String getDatabase(){
		return this.database;
	}
	public void setPackageName(String packageName){
		this.packageName = packageName;
	}
	public String getPackageName(){
		return this.packageName;
	}
	public void setOutputRootDirectory(String outputRootDirectory){
		this.outputRootDirectory = outputRootDirectory;
	}
	public String getOutputRootDirectory(){
		return this.outputRootDirectory;
	}
	public void setSystemRootDirectory(String systemRootDirectory){
		this.systemRootDirectory = systemRootDirectory;
	}
	public String getSystemRootDirectory(){
		return this.systemRootDirectory;
	}
	public void setJavaDirName(String javaDirName){
		this.javaDirName = javaDirName;
	}
	public String getJavaDirName(){
		return this.javaDirName;
	}
	public void setModelsDirName(String modelsDirName){
		this.modelsDirName = modelsDirName;
	}
	public String getModelsDirName(){
		return this.modelsDirName;
	}
	public void setEosDirName(String eosDirName){
		this.eosDirName = eosDirName;
	}
	public String getEosDirName(){
		return this.eosDirName;
	}
	public void setServicesDirName(String servicesDirName){
		this.servicesDirName = servicesDirName;
	}
	public String getServicesDirName(){
		return this.servicesDirName;
	}
	public void setDaosDirName(String daosDirName){
		this.daosDirName = daosDirName;
	}
	public String getDaosDirName(){
		return this.daosDirName;
	}
	public void setTablePrefix(String tablePrefix){
		this.tablePrefix = tablePrefix;
	}
	public String getTablePrefix(){
		return this.tablePrefix;
	}
	public void setBundleDirName(String bundleDirName){
		this.bundleDirName = bundleDirName;
	}
	public String getBundleDirName(){
		return this.bundleDirName;
	}
	public void setFactoriesDirName(String factoriesDirName){
		this.factoriesDirName = factoriesDirName;
	}
	public String getFactoriesDirName(){
		return this.factoriesDirName;
	}
	public void setFactoriesBuilderDirName(String factoriesBuilderDirName){
		this.factoriesBuilderDirName = factoriesBuilderDirName;
	}
	public String getFactoriesBuilderDirName(){
		return this.factoriesBuilderDirName;
	}
}
