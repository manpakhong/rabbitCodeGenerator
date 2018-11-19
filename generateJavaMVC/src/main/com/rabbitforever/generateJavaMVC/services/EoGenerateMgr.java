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

public class EoGenerateMgr {

	private String tableName;
	private String eoClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	public EoGenerateMgr(String _tableName) {
		try {
			tableName = _tableName;
			eoClassName = tableName;

			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
			
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
			String systemRootDir = null;
			String eoSuffix = "Eo";
			eoClassName =
			Misc.convertTableNameFormat2ClassNameFormat(tableName);
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
			systemRootDirectory = sysProperties.getSystemRootDirectory();
			
			String eoFile = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDir + "\\" + modelsDirName + "\\"
					+ eosDirName + "\\" + eoClassName + eoSuffix + ".java";

			FileWriter fstream = new FileWriter(eoFile);
			BufferedWriter out = new BufferedWriter(fstream);
			// ################################################## begin writing file
			StringBuilder sb = new StringBuilder();

			sb.append("package " + packageName + "." +  modelsDirName + ".eos;\n");
			
			// --- class
			sb.append("public class " + eoClassName + eoSuffix +  "\n");
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
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);

				sb.append("protected String " + 
						Misc.lowerStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
								) +
						";\n");
			} // end for (int i = 0; i < metaDataFieldList.size(); i++)

			sb.append("}\n");
			out.write(sb.toString());

			// ################################################## end writing file
			out.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
		} // end try ... catch ...

		System.out.println("Eo is generated. : " + eoClassName + "Eo.java");
	} // end generateVo()
} // end VoGenerateMgr
