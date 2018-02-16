package com.rabbitforever.generateJavaMVC.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.commons.Misc;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.utils.FileUtils;

public class BundleBuilderGenerateMgr {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className = this.getClass().getName();
	private String fileName;
	private String objLowerFirstCharClassName;
	private String objUpperFirstCharClassName;
	private SysProperties sysProperties;
	private PropertiesFactory propertiesFactory;
	private List<String> lineList;

	public BundleBuilderGenerateMgr(List<String> lineList, String fileName) {
		try {
			this.fileName = fileName.replace(".properties", "");
			this.lineList = lineList;
			objLowerFirstCharClassName = "";
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
		} catch (Exception e) {
			logger.error(className + ".BundleBuilderGenerateMgr()", e);
		}
	}

	public void generateBuilder() throws Exception {
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
		try {
			outputRootDirectory = sysProperties.getOutputRootDirectory();
			// projectFolderRoot = sysProperties.getProjectFolderRoot();
			// phpSysConfigRoot = sysProperties.getPhpSysConfigRoot();
			// Create file
			factoriesDirName = sysProperties.getFactoriesDirName();
			factoriesBuilderDirName = sysProperties.getFactoriesBuilderDirName();
			bundlerDirName = sysProperties.getBundleDirName();
			packageName = sysProperties.getPackageName();
			javaDirName = sysProperties.getJavaDirName();
			systemRootDirectory = sysProperties.getSystemRootDirectory();
			String objUpperFirstCharSystemPropertiesClassName = "SysProperties";
			String objLowerFirstCharSystemPropertiesObjName = Misc.lowerStringFirstChar(objUpperFirstCharSystemPropertiesClassName);
			objUpperFirstCharClassName = Misc.convertBundleNameFormat2ClassNameFormat(fileName);
			objLowerFirstCharClassName = Misc.lowerStringFirstChar(objUpperFirstCharClassName);
			upperFirstPropertiesName =  objUpperFirstCharClassName + "Properties";
			lowerFirstPropertiesName = objLowerFirstCharClassName + "Properties";
			String objUpperFirstCharClassName = Misc.upperStringFirstChar(objLowerFirstCharClassName);
			String fileFolder = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirectory + "\\" + factoriesDirName + "\\" +  factoriesBuilderDirName + "";
			String objFile = fileFolder + "\\" + objUpperFirstCharClassName + "BundlesBuilder.java";
			FileUtils fileUtils = new FileUtils();
			fileUtils.createDirectoryIfNotExisted(fileFolder);
			FileWriter fstream = new FileWriter(objFile);
			BufferedWriter out = new BufferedWriter(fstream);
			// ################################################## begin writing
			// file
			StringBuilder sb = new StringBuilder();
			sb.append("package " + packageName + "." + factoriesDirName + "." + factoriesBuilderDirName + ";\n");

			// --- header
			sb.append("import org.slf4j.Logger;\n");
			sb.append("import org.slf4j.LoggerFactory;\n");
			sb.append("import " + packageName + "." + bundlerDirName + "." + objUpperFirstCharSystemPropertiesClassName
					+ ";\n");

			// --- class
			sb.append("public class " + objUpperFirstCharClassName + "BundlesBuilder extends BundlesBuilder<"
					+ upperFirstPropertiesName + ">");
			sb.append("{\n");

			// member variables
			sb.append("\tprivate final Logger logger = LoggerFactory.getLogger(this.getClass());\n");
			sb.append("\tprivate String className;\n");

			// constructor
			sb.append("\tpublic " + objUpperFirstCharClassName + "BundlesBuilder(String fileName) throws Exception{\n");
			sb.append("\t\tsuper(fileName);\n");
			sb.append("\t}\n");

			// init
			sb.append("\tprivate String getClassName(){\n");
			sb.append("\t\tif(className == null){\n");
			sb.append("\t\t\tclassName = this.getClass().getName();\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn className;\n");
			sb.append("\t}\n");

			// buildBundle
			sb.append("\t@Override\n");
			sb.append("\tpublic " + upperFirstPropertiesName + " build() throws Exception{\n");
			sb.append("\t\t " + upperFirstPropertiesName + " "
					+ lowerFirstPropertiesName + "= null;\n");
			sb.append("\t\ttry{\n");

			sb.append("\t\t\t" + lowerFirstPropertiesName + " = new "
					+ upperFirstPropertiesName + "();\n");
			// member variables - EN
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line, Misc.LANG_EN);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_EN);
				String propertyOriginalString = Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line,
						Misc.LANG_EN);
				if (propertyString != null) {

					sb.append("\t\t\tString " + propertyString + " = getPropValues(\"" + propertyOriginalString
							+ "\");\n");
					sb.append("\t\t\t" + lowerFirstPropertiesName + ".set" + functionString + "(" + propertyString + ");\n");

				}

			}
			// member variables - TC
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line, Misc.LANG_TC);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_TC);
				String propertyOriginalString = Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line,
						Misc.LANG_TC);
				if (propertyString != null) {

					sb.append("\t\t\tString " + propertyString + " = getPropValues(\"" + propertyOriginalString
							+ "\");\n");
					sb.append("\t\t\t" + lowerFirstPropertiesName + ".set" + functionString + "(" + propertyString + ");\n");

				}
			}

			// member variables - no specific lang
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line);
				String propertyOriginalString = Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line);
				if (propertyString != null) {

					sb.append("\t\t\tString " + propertyString + " = getPropValues(\"" + propertyOriginalString
							+ "\");\n");
					sb.append("\t\t\t" + lowerFirstPropertiesName + ".set" + functionString + "(" + propertyString + ");\n");

				}
			}
			// // member variables - EN
			// for (String line: lineList){
			// String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line,
			// Misc.LANG_EN);
			// String propertyString =
			// Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_EN);
			// String propertyOriginalString =
			// Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line, Misc.LANG_EN);
			// if (propertyString != null){
			// sb.append("\t\t\t\tif(isset($" + propertyString + ")){\n");
			// sb.append("\t\t\t\t\t$bundleEo->set" + functionString + "(trim($" +
			// propertyString + "));\n");
			// sb.append("\t\t\t\t}\n");
			// }
			// }
			// // member variables - TC
			// for (String line: lineList){
			// String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line,
			// Misc.LANG_TC);
			// String propertyString =
			// Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_TC);
			// String propertyOriginalString =
			// Misc.convertBundleFieldsFormat2OriginalUnderScoreFormat(line, Misc.LANG_TC);
			// if (propertyString != null){
			// sb.append("\t\t\t\tif(isset($" + propertyString + ")){\n");
			// sb.append("\t\t\t\t\t$bundleEo->set" + functionString + "(trim($" +
			// propertyString + "));\n");
			// sb.append("\t\t\t\t}\n");
			// }
			// }
			//
			sb.append("\t\t} catch (Exception e) {\n");
			sb.append(
					"\t\t\tthis.logger.error(getClassName() + \".build() - this.fileName=\" + this.fileName, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn " +  lowerFirstPropertiesName + ";\n");
			sb.append("\t}\n");

			sb.append("}\n"); // end class Function
			out.write(sb.toString());

			// ################################################## end writing
			// file
			out.close();
		} catch (Exception e) {// Catch exception if any
			logger.error(className + ".generateBuilder()", e);
			throw e;
		} // end try ... catch ...
	}
}
