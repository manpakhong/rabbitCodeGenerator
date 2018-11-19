package com.rabbitforever.generateJavaMVC.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.commons.JavaOracle;
import com.rabbitforever.generateJavaMVC.commons.Misc;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;
import com.rabbitforever.generateJavaMVC.policies.SystemParams;

public class SoGenerateMgr {

	private String tableName;
	private String soClassName;
	private String objClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	public SoGenerateMgr(String _tableName) {
		try {
			tableName = _tableName;
			soClassName = tableName;

			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
			objClassName = Misc
					.convertTableFieldsFormat2JavaPropertiesFormat(tableName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end constructor

	public void generateVo() {
		try {
			// Create file
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
			String sosDirName = null;
			String systemRootDir = null;
			String eoSuffix = "Eo";
			String soSuffix = "So";
			soClassName =
			Misc.convertTableNameFormat2ClassNameFormat(tableName);
			outputRootDirectory = sysProperties.getOutputRootDirectory();
			modelsDirName = sysProperties.getModelsDirName();
			eosDirName = sysProperties.getEosDirName();
			sosDirName = sysProperties.getSosDirName();
			javaDirName = sysProperties.getJavaDirName();
			systemRootDir = sysProperties.getSystemRootDirectory();
			factoriesDirName = sysProperties.getFactoriesDirName();
			factoriesBuilderDirName = sysProperties.getFactoriesBuilderDirName();
			bundlerDirName = sysProperties.getBundleDirName();
			packageName = sysProperties.getPackageName();
			javaDirName = sysProperties.getJavaDirName();
			systemRootDirectory = sysProperties.getSystemRootDirectory();
			
			String soFile = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDir + "\\" + modelsDirName + "\\"
					+ sosDirName + "\\" + soClassName + soSuffix + ".java";

			FileWriter fstream = new FileWriter(soFile);
			BufferedWriter out = new BufferedWriter(fstream);
			// ################################################## begin writing file
			StringBuilder sb = new StringBuilder();

			sb.append("package " + packageName + "." +  modelsDirName + ".sos;\n");
			
			// --- class
			sb.append("public class " + soClassName + soSuffix +  " extends " + soClassName + eoSuffix + " implements So\n");
			sb.append("{\n");

			String database = this.sysProperties.getDatabase();

			DbMgr dbMgr = null;
			if (database.equals(SysProperties.DATABASE_MYSQL)) {
				dbMgr = new MySqlDbMgr();
			}else if (database.equals(SysProperties.DATABASE_ORACLE)){
				dbMgr = new OracleDbMgr();
			}
			List<MetaDataField> metaDataFieldList = new ArrayList<MetaDataField>();
			metaDataFieldList = dbMgr.getMetaDataList(tableName);

			sb.append("\tprotected Date createDateTimeFrom;\n");
			sb.append("\tprotected Date createDateTimeTo;\n");
			sb.append("\tprotected Date lastModifyDateTimeFrom;\n");
			sb.append("\tprotected Date lastModifyDateTimeTo;\n");

			sb.append("\t@Override\n");
			sb.append("\tpublic void setCreateDateTimeFrom(Date createDateTimeFrom){\n");
			sb.append("\t\tthis.createDateTimeFrom = createDateTimeFrom;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic void setCreateDateTimeTo(Date createDateTimeTo){\n");
			sb.append("\t\tthis.createDateTimeTo = createDateTimeTo;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic void setLastModifyDateTimeFrom(Date lastModifyDateTimeFrom){\n");
			sb.append("\t\tthis.lastModifyDateTimeFrom = lastModifyDateTimeFrom;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic void setLastModifyDateTimeTo(Date lastModifyDateTimeTo){\n");
			sb.append("\t\tthis.lastModifyDateTimeTo = lastModifyDateTimeTo;\n");
			sb.append("\t}\n");
			
			
			
			sb.append("\t@Override\n");
			sb.append("\tpublic Date getCreateDateTimeFrom(){\n");
			sb.append("\t\treturn this.createDateTimeFrom;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic Date getCreateDateTimeTo(){\n");
			sb.append("\t\treturn this.createDateTimeTo;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic Date getLastModifyDateTimeFrom(){\n");
			sb.append("\t\treturn this.lastModifyDateTimeFrom;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic Date getLastModifyDateTimeTo(){\n");
			sb.append("\t\treturn this.lastModifyDateTimeTo;\n");
			sb.append("\t}\n");
			
			
			sb.append("}\n");
			out.write(sb.toString());

			// ################################################## end writing file
			out.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		} // end try ... catch ...

		System.out.println("So is generated. : " + soClassName + "So.java");
	} // end generateVo()
} // end VoGenerateMgr
