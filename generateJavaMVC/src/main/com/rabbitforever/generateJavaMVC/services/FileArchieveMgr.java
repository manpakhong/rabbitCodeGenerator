package com.rabbitforever.generateJavaMVC.services;

import java.io.File;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.policies.SystemParams;

public class FileArchieveMgr {
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	public FileArchieveMgr(){
		try {
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // end constructor
	
	public void maintainFileArchieve()
	{
		try
		{
			String tempDirPath = sysProperties.getOutputRootDirectory();
			String outputRootDirectory = sysProperties.getOutputRootDirectory();
			String modelsDirName = sysProperties.getModelsDirName();
			String eosDirName = sysProperties.getEosDirName();
			String sosDirName = sysProperties.getSosDirName();
			String daosDirName = sysProperties.getDaosDirName();
			String servicesDirName = sysProperties.getServicesDirName();
			String javaDirName = sysProperties.getJavaDirName();
			String systemRootDirName = sysProperties.getSystemRootDirectory();
			String factoriesDirName = sysProperties.getFactoriesDirName();
			String factoriesBuilderDirName = sysProperties.getFactoriesBuilderDirName();
			String bundlerDirName = sysProperties.getBundleDirName();
			String utilsDirName = sysProperties.getUtilsDirName();
			File tempDir = new File(tempDirPath);
	
			// if the directory does not exist, create it
			if (!tempDir.exists())
			{
				System.out.println("creating directory: " + tempDirPath);
				tempDir.mkdir();
			}
			
			String javaDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName;
			File javaDir = new File(javaDirPath);
			if (!javaDir.exists())
			{
				System.out.println("creating directory: " + javaDirPath);
				javaDir.mkdir();
			}
			
			// java
			String javaDirCPath = outputRootDirectory + "\\" + javaDirName;
			File javaCDir = new File(javaDirCPath);
			if (!javaCDir.exists())
			{
				System.out.println("creating directory: " + javaCDir);
				javaCDir.mkdir();
			}		
			
			// systemRoot
			String sysDirName = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName;
			File sysDir = new File(sysDirName);
			if (!sysDir.exists())
			{
				System.out.println("creating directory: " + sysDir);
				sysDir.mkdir();
			}	
			
			
			// models
			String modelsDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\" + modelsDirName;
			File modelsDir = new File(modelsDirPath);
			if (!modelsDir.exists())
			{
				System.out.println("creating directory: " + modelsDirPath);
				modelsDir.mkdir();
			}		
			
			
			// eo
			String vosDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\" + modelsDirName + "\\" + eosDirName;
			File vosDir = new File(vosDirPath);
			if (!vosDir.exists())
			{
				System.out.println("creating directory: " + vosDirPath);
				vosDir.mkdir();
			}			
			
			// so
			String sosDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\" + modelsDirName + "\\" + sosDirName;
			File sosDir = new File(sosDirPath);
			if (!sosDir.exists())
			{
				System.out.println("creating directory: " + sosDirPath);
				sosDir.mkdir();
			}			
			
//			// service Interface
//			String servicesIDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\" + SystemParams.SERVICE_I_DIR_NAME;
//			File servicesIDir = new File(servicesIDirPath);
//			if (!servicesIDir.exists())
//			{
//				System.out.println("creating directory: " + servicesIDirPath);
//				servicesIDir.mkdir();
//			}					
			
			// service
			String servicesDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\" + servicesDirName;
			File servicesDir = new File(servicesDirPath);
			if (!servicesDir.exists())
			{
				System.out.println("creating directory: " + servicesDirPath);
				servicesDir.mkdir();
			}			
			
//			// dao Interface
//			String daosIDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\" + SystemParams.DAO_I_DIR_NAME;
//			File daosIDir = new File(daosIDirPath);
//			if (!daosIDir.exists())
//			{
//				System.out.println("creating directory: " + daosIDirPath);
//				daosIDir.mkdir();
//			}					
			
			
			// dao
			String daosDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\" + daosDirName;
			File daosDir = new File(daosDirPath);
			if (!daosDir.exists())
			{
				System.out.println("creating directory: " + daosDirPath);
				daosDir.mkdir();
			}				
			
			// builder
			String builderDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName + "\\" + factoriesDirName + "\\" +  factoriesBuilderDirName + "";
			File builderDir = new File(builderDirPath);
			if (!builderDir.exists())
			{
				System.out.println("creating directory: " + builderDir);
				builderDir.mkdir();
			}			
			
			// util
			String utilDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName + "\\" + utilsDirName + "";
			File utilDir = new File(utilDirPath);
			if (!utilDir.exists())
			{
				System.out.println("creating directory: " + utilDir);
				utilDir.mkdir();
			}			
			
//			// cmd
//			String cmdsDirPath = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirName +"\\cmds";
//			File cmdsDir = new File(cmdsDirPath);
//			if (!cmdsDir.exists())
//			{
//				System.out.println("creating directory: " + cmdsDirPath);
//				cmdsDir.mkdir();
//			}				
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	} // end maintainFileArchieve()
} // end class
