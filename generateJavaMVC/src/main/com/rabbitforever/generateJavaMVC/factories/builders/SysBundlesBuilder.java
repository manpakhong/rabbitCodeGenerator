package com.rabbitforever.generateJavaMVC.factories.builders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
public class SysBundlesBuilder extends BundlesBuilder<SysProperties>{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className;
	public SysBundlesBuilder(String fileName) throws Exception{
		super(fileName);
	}
	private String getClassName(){
		if(className == null){
			className = this.getClass().getName();
		}
		return className;
	}
	@Override
	public SysProperties build() throws Exception{
		 SysProperties sysProperties= null;
		try{
			sysProperties = new SysProperties();
			String database = getPropValues("database");
			sysProperties.setDatabase(database);
			String packageName = getPropValues("package_name");
			sysProperties.setPackageName(packageName);
			String outputRootDirectory = getPropValues("output_root_directory");
			sysProperties.setOutputRootDirectory(outputRootDirectory);
			String systemRootDirectory = getPropValues("system_root_directory");
			sysProperties.setSystemRootDirectory(systemRootDirectory);
			String javaDirName = getPropValues("java_dir_name");
			sysProperties.setJavaDirName(javaDirName);
			String modelsDirName = getPropValues("models_dir_name");
			sysProperties.setModelsDirName(modelsDirName);
			String eosDirName = getPropValues("eos_dir_name");
			sysProperties.setEosDirName(eosDirName);
			String sosDirName = getPropValues("sos_dir_name");
			sysProperties.setSosDirName(sosDirName);
			String utilsDirName = getPropValues("utils_dir_name");
			sysProperties.setUtilsDirName(utilsDirName);
			String servicesDirName = getPropValues("services_dir_name");
			sysProperties.setServicesDirName(servicesDirName);
			
			String servicesHelpersDirName = getPropValues("services_helpers_dir_name");
			sysProperties.setServicesHelpersDirName(servicesHelpersDirName);
			
			String daosDirName = getPropValues("daos_dir_name");
			sysProperties.setDaosDirName(daosDirName);
			String tablePrefix = getPropValues("table_prefix");
			sysProperties.setTablePrefix(tablePrefix);
			String bundleDirName = getPropValues("bundle_dir_name");
			sysProperties.setBundleDirName(bundleDirName);
			String factoriesDirName = getPropValues("factories_dir_name");
			sysProperties.setFactoriesDirName(factoriesDirName);
			String factoriesBuilderDirName = getPropValues("factories_builder_dir_name");
			sysProperties.setFactoriesBuilderDirName(factoriesBuilderDirName);
		} catch (Exception e) {
			this.logger.error(getClassName() + ".build() - this.fileName=" + this.fileName, e);
			throw e;
		}
		return sysProperties;
	}
}
