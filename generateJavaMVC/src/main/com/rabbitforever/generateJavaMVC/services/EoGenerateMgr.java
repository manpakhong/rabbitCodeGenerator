package com.rabbitforever.generateJavaMVC.services;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.commons.Misc;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.models.dtos.CompressFileDto;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;

public class EoGenerateMgr {
	private final Logger logger = Logger.getLogger(getClassName());
	private String tableName;
	private String eoClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	private TypeMappingMgr typeMappingMgr;
	
	private String getClassName(){
		return this.getClass().getName();
	}
	
	public EoGenerateMgr(String _tableName) throws Exception {
		try {
			tableName = _tableName;
			init();
			
		} catch (Exception e) {
			logger.error(this.getClass() + ".EoGenerateMgr() - ", e);
			throw e;
		}

	} // end constructor

	private void init() throws Exception{
		try {
			typeMappingMgr = new OracleTypeMappingMgr();
			eoClassName = tableName;
	
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
		} catch (Exception e) {
			logger.error(this.getClass() + ".init() - ", e);
			throw e;
		}
	}

	public void generateEo(CompressFileDto compressFileDto) throws Exception {
		FileWriter fstream = null;
		BufferedWriter out = null;		
		PrintWriter pw = null;
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


			// ################################################## begin writing file
			StringBuilder sb = new StringBuilder();

			sb.append("package " + packageName + "." +  modelsDirName + ".eos;\n");
			
			sb.append("import java.util.Date;\n");
			sb.append("import javax.persistence.Column;\n");
			sb.append("import javax.persistence.Entity;\n");
			sb.append("import javax.persistence.GeneratedValue;\n");
			sb.append("import javax.persistence.GenerationType;\n");
			sb.append("import javax.persistence.Id;\n");
			sb.append("import javax.persistence.SequenceGenerator;\n");
			sb.append("import javax.persistence.Table;\n");
			sb.append("import javax.persistence.Temporal;\n");
			sb.append("import javax.persistence.TemporalType;\n");
			
			// --- class
			sb.append("@Entity\n");
			sb.append("@Table(name = \"" + tableName + "\")\n");
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
			// --------------> generate attributies
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);
				String databaseFieldTypeName = metaDataField.getTypeName();
				String typeString = typeMappingMgr.mappingTo(databaseFieldTypeName);
				
				// for debug
				if (databaseFieldTypeName.contains("TIMESTAMP")) {
					logger.info("Timestamp");
				}
				sb.append("\tprotected " + typeString + " " +
						Misc.lowerStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
								) +
						";\n");
			} // end for (int i = 0; i < metaDataFieldList.size(); i++)

			
			
			// --------------> generate getter and setter
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);
				String columnName = metaDataField.getColumnName();
				String databaseFieldTypeName = metaDataField.getTypeName();
				String typeString = typeMappingMgr.mappingTo(databaseFieldTypeName);
				Integer columnSize = metaDataField.getColumnSize();
				Integer nullable = metaDataField.getNullable();
				Boolean isNullable = null;
				
				String javaPropertiesFormat = Misc.lowerStringFirstChar(Misc
						.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
								.getColumnName()));
				String upperPropertiesFormat = Misc.upperStringFirstChar(Misc
						.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
								.getColumnName()));
				
				if (nullable.equals(1)) {
					isNullable = true;
				} else {
					isNullable = false;
				}
				
				if (columnName.equals("ID")) {
					if (!isNullable) {
						sb.append("\t@Id\n");
						sb.append("\t@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"" + tableName + "_ID_SEQ" +  "\")\n");
						sb.append("\t@SequenceGenerator(sequenceName = \"" + tableName + "_ID_SEQ\", allocationSize = 1, name=\"" + tableName + "_ID_SEQ" + "\")\n");
					}
				}
				
				// for debug
				if (databaseFieldTypeName.contains("TIMESTAMP")) {
					logger.info("Timestamp");
				}
				
				
				// ------------> getter hibernateHeader
				if (databaseFieldTypeName.contains("TIMESTAMP")) {
					sb.append("\t@Temporal(TemporalType.TIMESTAMP)\n");
				}
				
				sb.append("\t@Column(name = \"" + columnName +"\", ");

				
				if (databaseFieldTypeName.contains("TIMESTAMP")) {
					sb.append("columnDefinition = \"DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\", ");
				}
				
				if (typeString != null) {
					if (typeString.contains("String")) {
						sb.append("length = " + columnSize + ",");
					}
				
				}
				
				if (isNullable) {
					sb.append("nullable = true)");

				} else {
					if (columnName.equals("ID")) {
						sb.append("columnDefinition = \"NUMBER\", ");
					}
					sb.append("nullable = false)");
				}
				
				sb.append("\n");
				
				// getter
				sb.append("\tpublic " + typeString + " get" + upperPropertiesFormat +
						"(){\n");
				sb.append("\t\treturn " + javaPropertiesFormat + ";\n");
				sb.append("\t}\n");
				
				// setter
				sb.append("\tpublic void set" + upperPropertiesFormat +
						"(" + typeString + " " +  javaPropertiesFormat + "){\n");
				
				sb.append("\t\tthis." + javaPropertiesFormat + " = " + javaPropertiesFormat + ";\n");
				sb.append("\t}\n");
			} // end for (int i = 0; i < metaDataFieldList.size(); i++)

			sb.append("}\n");
			
			if (compressFileDto != null) {
				
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				pw = new PrintWriter(byteArrayOutputStream);
				pw.write(sb.toString());
				
				compressFileDto.setFileName(eoClassName + eoSuffix + ".java");
				compressFileDto.setByteArrayOutputStream(byteArrayOutputStream);
			} else {
				fstream = new FileWriter(eoFile);
				out = new BufferedWriter(fstream);
				out.write(sb.toString());
			}


			// ################################################## end writing file

		} catch (Exception e) {// Catch exception if any
			logger.error(this.getClass() + ".generateEo() - ", e);
			throw e;
		} // end try ... catch ...
		finally {
			if (out != null) {
				out.close();
				out = null;
			}
			if (fstream != null) {
				fstream.close();
				fstream = null;
			}
			if (pw != null) {
				pw.close();
				pw = null;
			}
		}

		System.out.println("Eo is generated. : " + eoClassName + "Eo.java");
	} // end generateVo()

	public void generateEo() throws Exception {
		generateEo(null);
	}
} // end VoGenerateMgr
