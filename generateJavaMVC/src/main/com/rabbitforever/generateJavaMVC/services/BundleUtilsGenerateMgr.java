package com.rabbitforever.generateJavaMVC.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.commons.JavaOracle;
import com.rabbitforever.generateJavaMVC.commons.Misc;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;
import com.rabbitforever.generateJavaMVC.policies.SystemParams;

public class BundleUtilsGenerateMgr {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className = this.getClass().getName();
	private String fileName;
	private String objLowerFirstCharClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	private List<String> lineList;
	
	public BundleUtilsGenerateMgr(List<String> lineList, String fileName) throws Exception{
		try {
			this.fileName = fileName.replace(".properties", "");
			this.lineList = lineList;
			objLowerFirstCharClassName = "";
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
		} catch (Exception e) {
			logger.error(className + ".BundleUtilsGenerateMgr()", e);
		}
	}

	public void generateBundleUtils() {
		String outputRootDirectory = null;
		String projectFolderRoot = null;
		String phpSysConfigRoot = null;
		String packageName = null;
		String factoriesDirName = null;
		String factoriesBuilderDirName = null;
		String bundlerDirName = null;
		String javaDirName = null;
		String systemRootDirectory = null;
		String upperFirstPropertiesName = null;
		String lowerFirstPropertiesName = null;
		String modelsDirName = null;
		String eosDirName = null;
		String daoDirName = null;
		String systemRootDir = null;
		String daoSuffix = "Dao";
		String eoSuffix = "Eo";
		String bundleSuffix = "Bundle";
		String utilsSuffix = "Utils";
		String propertiesSuffix = "Properties";
		String bundleUtilsSuffix = bundleSuffix + utilsSuffix;
		String utilsClassName = null;
		String daoObjectName = null;
		String utilsDirName = null;
		String utilsObjectName = null;
		try {
			// Create file
			objLowerFirstCharClassName = Misc.convertBundleNameFormat2ClassNameFormat(fileName);
			String objUpperFirstCharClassName = Misc.upperStringFirstChar(objLowerFirstCharClassName);
			utilsClassName = 	objUpperFirstCharClassName;
			utilsObjectName = objLowerFirstCharClassName;
			outputRootDirectory = sysProperties.getOutputRootDirectory();
			modelsDirName = sysProperties.getModelsDirName();
			eosDirName = sysProperties.getEosDirName();
			javaDirName = sysProperties.getJavaDirName();
			systemRootDir = sysProperties.getSystemRootDirectory();
			factoriesDirName = sysProperties.getFactoriesDirName();
			factoriesBuilderDirName = sysProperties.getFactoriesBuilderDirName();
			bundlerDirName = sysProperties.getBundleDirName();
			packageName = sysProperties.getPackageName();
			javaDirName = sysProperties.getJavaDirName();
			daoDirName = sysProperties.getDaosDirName();
			utilsDirName = sysProperties.getUtilsDirName();
			systemRootDirectory = sysProperties.getSystemRootDirectory();

			String bunndleUtilsFile = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDir + "\\" 
					+ utilsDirName + "\\" + utilsClassName + bundleUtilsSuffix + ".java";


			FileWriter fstream = new FileWriter(bunndleUtilsFile);
			BufferedWriter out = new BufferedWriter(fstream);
			// ################################################## begin writing
			// file
			StringBuilder sb = new StringBuilder();
			sb.append("package " + packageName + "." + utilsDirName + ";\n");
			
			// import
			sb.append("import org.slf4j.Logger;\n");
			sb.append("import org.slf4j.LoggerFactory;\n");
			
			// --- class
			sb.append("public class " + utilsClassName + bundleUtilsSuffix + " extends BundlesUtils" + "<" + utilsClassName + propertiesSuffix + ">");
			sb.append("{\n");
			sb.append("\t\tprivate String className;\n");


			// properties
			sb.append("\tprivate final Logger logger = LoggerFactory.getLogger(getClassName());\n");
//			sb.append("\tprivate static DbUtils mySqlDbUtils;\n");
//			sb.append("\tprivate static DbUtilsFactory dbUtilsFactory;\n");


			sb.append("\tpublic " + utilsClassName + bundleUtilsSuffix  + "(String fileName) throws Exception {\n");
			sb.append("\t\tsuper(fileName);\n");
			sb.append("\t}\n");
			
			// getClassName()
			sb.append("\tprivate String getClassName(){\n");
			sb.append("\t\tif(className == null){\n");
			sb.append("\t\t\tclassName = this.getClass().getName();\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn className;\n");
			sb.append("\t}\n");
			

			

			
			// ###############################
			// getProperties() statement
			// ###############################
			sb.append("\tpublic " + utilsClassName + propertiesSuffix + " getProperties() throws Exception{\n");
			sb.append("\t\t" + utilsClassName + propertiesSuffix + " " + utilsObjectName + propertiesSuffix + " = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\t" + utilsObjectName + propertiesSuffix + " = new " + utilsClassName + propertiesSuffix + "();\n");

			
			// member variables - EN
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line, Misc.LANG_EN);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_EN);
				String propertyOriginalString = Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line,
						Misc.LANG_EN);
				if (propertyString != null) {
					sb.append("\t\t\tString " + propertyString + "=getPropValues(\"" + propertyOriginalString +"\");\n");
					sb.append("\t\t\t" + utilsObjectName + propertiesSuffix + ".set" + Misc.upperStringFirstChar(propertyString) + "(" + propertyString + ");\n");
				}
			}
			// member variables - TC
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line, Misc.LANG_TC);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_TC);
				String propertyOriginalString = Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line,
						Misc.LANG_TC);
				if (propertyString != null) {
					sb.append("\t\t\tString " + propertyString + "=getPropValues(\"" + propertyOriginalString +"\");\n");
					sb.append("\t\t\t" + utilsObjectName + propertiesSuffix + ".set" + Misc.upperStringFirstChar(propertyString) + "(" + propertyString + ");\n");
				}
			}

			// methods - no language specification
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line);
				String propertyOriginalString = Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line);
				if (propertyString != null) {
					sb.append("\t\t\tString " + propertyString + "=getPropValues(\"" + propertyOriginalString +"\");\n");
					sb.append("\t\t\t" + utilsObjectName + propertiesSuffix + ".set" + Misc.upperStringFirstChar(propertyString) + "(" + propertyString + ");\n");
				}
			}
			

			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".getProperties()\", e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn " + utilsObjectName + propertiesSuffix + ";\n");
			sb.append("\t}\n");


			
			// ########## end class ##############################
			sb.append("} //end class\n");
			out.write(sb.toString());

			// ################################################## end writing
			// file
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		} // end try ... catch ...
		  System.out.println("BundleUtils is generated. : " + utilsClassName + "BundleUtils.java");		
	} // end generateDao()



}
