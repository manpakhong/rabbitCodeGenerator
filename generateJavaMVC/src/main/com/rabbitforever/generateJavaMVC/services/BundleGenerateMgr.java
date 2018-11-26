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

public class BundleGenerateMgr {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className = this.getClass().getName();
	private String fileName;
	private String objLowerFirstCharClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	private List<String> lineList;

	public BundleGenerateMgr(List<String> lineList, String fileName) throws Exception{
		try {
			this.fileName = fileName.replace(".properties", "");
			this.lineList = lineList;
			objLowerFirstCharClassName = "";
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
		} catch (Exception e) {
			logger.error(className + ".BundleGenerateMgr()", e);
		}
	}

	public void generateBundle() throws Exception {
		String outputRootDirectory = null;
		String projectFolderRoot = null;
		String phpSysConfigRoot = null;
		String packageName = null;
		String factoriesDirName = null;
		String factoriesBuilderDirName = null;
		String bundlerDirName = null;
		String javaDirName = null;
		String systemRootDirectory = null;
		
		String bundleDirName = null;

		String propertiesClassSuffix = "Properties";
		try {
			outputRootDirectory = sysProperties.getOutputRootDirectory();
			// projectFolderRoot = sysProperties.getProjectFolderRoot();
			// phpSysConfigRoot = sysProperties.getPhpSysConfigRoot();
			// Create file
			bundleDirName = sysProperties.getBundleDirName();
			packageName = sysProperties.getPackageName();
			factoriesDirName = sysProperties.getFactoriesDirName();
			factoriesBuilderDirName = sysProperties.getFactoriesBuilderDirName();
			bundlerDirName = sysProperties.getBundleDirName();
			packageName = sysProperties.getPackageName();
			javaDirName = sysProperties.getJavaDirName();
			systemRootDirectory = sysProperties.getSystemRootDirectory();
			
			objLowerFirstCharClassName = Misc.convertBundleNameFormat2ClassNameFormat(fileName);
			String objUpperFirstCharClassName = Misc.upperStringFirstChar(objLowerFirstCharClassName);
			String fileFolder = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDirectory + "\\" + bundleDirName;
			String objFile = fileFolder + "\\" + objUpperFirstCharClassName + propertiesClassSuffix +".java";
			FileUtils fileUtils = new FileUtils();
			fileUtils.createDirectoryIfNotExisted(fileFolder);
			FileWriter fstream = new FileWriter(objFile);
			BufferedWriter out = new BufferedWriter(fstream);
			// ################################################## begin writing
			// file
			StringBuilder sb = new StringBuilder();
			sb.append("package " + packageName + "." + bundleDirName + ";\n");


			// --- class
			sb.append("public class " + objUpperFirstCharClassName + propertiesClassSuffix +" extends PropertiesBase");
			sb.append("{\n");

			sb.append("\tpublic static final String LANG_EN = PropertiesBase.LANG_EN;\n");
			sb.append("\tpublic static final String LANG_TCHI = PropertiesBase.LANG_TCHI;\n");
			
			sb.append("\tpublic " + objUpperFirstCharClassName + "Properties() {\n");
			sb.append("\t\tsuper();\n");
			sb.append("\t}\n");
			
			
			sb.append("\tpublic " + objUpperFirstCharClassName + "Properties(String lang) {\n");
			sb.append("\t\tsuper(lang);\n");
			sb.append("\t}\n");
			
			// member variables - No language specification
			for (String line : lineList) {
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line);
				if (propertyString != null) {
					sb.append("\tprivate String " + propertyString + ";\n");
				}
			}
			
			// member variables - EN
			for (String line : lineList) {
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_EN);
				if (propertyString != null) {
					sb.append("\tprivate String " + propertyString + ";\n");
				}
			}
			// member variables - TC
			for (String line : lineList) {
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_TC);
				if (propertyString != null) {
					sb.append("\tprivate String " + propertyString + ";\n");
				}
			}

			// methods - no language specification
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line);

				if (functionString != null) {
					// setter
					sb.append("\tpublic void set" + functionString + "(String " + propertyString + "){\n");
					sb.append("\t\tthis." + propertyString + " = " + propertyString + ";\n");
					sb.append("\t}\n");
					// getter
					sb.append("\tpublic String get" + functionString + "(){\n");
					sb.append("\t\treturn this." + propertyString + ";\n");
					sb.append("\t}\n");
				}
			}
			
			// methods - EN
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line, Misc.LANG_EN);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_EN);

				if (functionString != null) {
					// setter
					sb.append("\tpublic void set" + functionString + "(String " + propertyString + "){\n");
					sb.append("\t\tthis." + propertyString + " = " + propertyString + ";\n");
					sb.append("\t}\n");
					// getter
					sb.append("\tpublic String get" + functionString + "(){\n");
					sb.append("\t\treturn this." + propertyString + ";\n");
					sb.append("\t}\n");
				}
			}

			// methods - TC
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnFormat(line, Misc.LANG_TC);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesFormat(line, Misc.LANG_TC);

				if (functionString != null) {
					// setter
					sb.append("\tpublic void set" + functionString + "(String " + propertyString + "){\n");
					sb.append("\t\tthis." + propertyString + " = " + propertyString + ";\n");
					sb.append("\t}\n");
					// getter
					sb.append("\tpublic String get" + functionString + "(){\n");
					sb.append("\t\treturn this." + propertyString + ";\n");
					sb.append("\t}\n");
					"".equals("");
				}
			}

			// methods - autodetect language
			for (String line : lineList) {
				String functionString = Misc.convertBundleFieldsFormat2JavaFnNoLangFormat(line, Misc.LANG_EN);
				String propertyString = Misc.convertBundleFieldsFormat2JavaPropertiesNoLangFormat(line, Misc.LANG_EN);

				if (functionString != null) {
					String upperEnLang = Misc.upperStringFirstChar(Misc.LANG_EN);
					String upperTcLang = Misc.upperStringFirstChar(Misc.LANG_TC);
					// getter
					sb.append("\tpublic String get" + functionString + "(){\n");
					sb.append("\t\tString property = null;\n");
					sb.append("\t\tif(this.lang == null){\n");
					sb.append("\t\t\tthis.lang = LANG_EN;\n");
					sb.append("\t\t}\n");
					sb.append("\t\tif(this.lang.equals(LANG_TCHI)){\n");
					sb.append("\t\t\tproperty = this.get" + functionString + upperTcLang + "();\n");
					sb.append("\t\t} else\n");
					sb.append("\t\tif(this.lang.equals(LANG_EN)){\n");
					sb.append("\t\t\tproperty = this.get" + functionString + upperEnLang + "();\n");
					sb.append("\t\t} else {\n");
					sb.append("\t\t\tproperty = this.get" + functionString + upperEnLang + "();\n");
					sb.append("\t\t}\n");
					sb.append("\t\treturn property;\n");
					sb.append("\t}\n");
				}
			}

			sb.append("}\n"); // end class Function
			out.write(sb.toString());

			// ################################################## end writing
			// file
			out.close();
		} catch (Exception e) {// Catch exception if any
			logger.error(className + ".generateBundle()", e);
			throw e;
		} // end try ... catch ...
	}
}
