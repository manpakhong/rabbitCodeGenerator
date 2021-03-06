package com.rabbitforever.generateJavaMVC.services;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.commons.Misc;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.models.dtos.CompressFileDto;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;

public class SoGenerateMgr {
	private TypeMappingMgr typeMappingMgr;
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
			typeMappingMgr = new OracleTypeMappingMgr();
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end constructor
	public void generateSo() throws Exception{
		generateSo(null);
	}
	public void generateSo(CompressFileDto compressFileDto) throws Exception{
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
			String sosDirName = null;
			String systemRootDir = null;
			String eoSuffix = "Eo";
			String soSuffix = "So";
			
			String daoObjectName = null;
			String daoClassName = null;
			
			
			daoClassName = 	Misc.convertTableNameFormat2ClassNameFormat(tableName);
			daoObjectName = Misc.lowerStringFirstChar(daoClassName);
			soClassName = Misc.convertTableNameFormat2ClassNameFormat(tableName);
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


			// ################################################## begin writing file
			StringBuilder sb = new StringBuilder();

			sb.append("package " + packageName + "." +  modelsDirName + ".sos;\n");
			
			sb.append("import java.util.ArrayList;\n");
			sb.append("import java.util.Date;\n");
			sb.append("import java.util.List;\n");
			
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

//			sb.append("\tprotected Date createDatetimeFrom;\n");
//			sb.append("\tprotected Date createDatetimeTo;\n");
//			sb.append("\tprotected Date updateDatetimeFrom;\n");
//			sb.append("\tprotected Date updateDatetimeTo;\n");
			sb.append("\tprotected Integer firstResult;\n");
			sb.append("\tprotected Integer lastResult;\n");
			sb.append("\tprotected List<OrderedBy> orderedByList;\n");

//			sb.append("\t@Override\n");
//			sb.append("\tpublic void setCreateDatetimeFrom(Date createDatetimeFrom){\n");
//			sb.append("\t\tthis.createDatetimeFrom = createDatetimeFrom;\n");
//			sb.append("\t}\n");
//			
//			sb.append("\t@Override\n");
//			sb.append("\tpublic void setCreateDatetimeTo(Date createDatetimeTo){\n");
//			sb.append("\t\tthis.createDatetimeTo = createDatetimeTo;\n");
//			sb.append("\t}\n");
//			
//			sb.append("\t@Override\n");
//			sb.append("\tpublic void setUpdateDatetimeFrom(Date updateDatetimeFrom){\n");
//			sb.append("\t\tthis.updateDatetimeFrom = updateDatetimeFrom;\n");
//			sb.append("\t}\n");
//			
//			sb.append("\t@Override\n");
//			sb.append("\tpublic void setUpdateDatetimeTo(Date updateDatetimeTo){\n");
//			sb.append("\t\tthis.updateDatetimeTo = updateDatetimeTo;\n");
//			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic void setFirstResult(Integer firstResult){\n");
			sb.append("\t\tthis.firstResult = firstResult;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic void setLastResult(Integer lastResult){\n");
			sb.append("\t\tthis.lastResult = lastResult;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic void setOrderedByList(List<OrderedBy> orderedByList){\n");
			sb.append("\t\tthis.orderedByList = orderedByList;\n");
			sb.append("\t}\n");		
			
			sb.append("\t@Override\n");
			sb.append("\tpublic void addOrderedBy(OrderedBy orderedBy){\n");
			sb.append("\t\tif(this.orderedByList == null){\n");
			sb.append("\t\t\tthis.orderedByList = new ArrayList<OrderedBy>();\n");
			sb.append("\t\t}\n");
			sb.append("\t\tthis.orderedByList.add(orderedBy);\n");
			sb.append("\t}\n");	
			
//			sb.append("\t@Override\n");
//			sb.append("\tpublic Date getCreateDatetimeFrom(){\n");
//			sb.append("\t\treturn this.createDatetimeFrom;\n");
//			sb.append("\t}\n");
//			
//			sb.append("\t@Override\n");
//			sb.append("\tpublic Date getCreateDatetimeTo(){\n");
//			sb.append("\t\treturn this.createDatetimeTo;\n");
//			sb.append("\t}\n");
//			
//			sb.append("\t@Override\n");
//			sb.append("\tpublic Date getUpdateDatetimeFrom(){\n");
//			sb.append("\t\treturn this.updateDatetimeFrom;\n");
//			sb.append("\t}\n");
//			
//			sb.append("\t@Override\n");
//			sb.append("\tpublic Date getUpdateDatetimeTo(){\n");
//			sb.append("\t\treturn this.updateDatetimeTo;\n");
//			sb.append("\t}\n");
			
			
			// loop pcount field name
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
				
				if (nullable == 1) {
					isNullable = false;
				} else {
					isNullable = true;
				}
								
				if (columnName.equals("ID") 
						) {
					
					sb.append("\tprotected List<Long> idList;\n");
					
					sb.append("\tpublic void setIdList(List<Long> idList){\n");
					sb.append("\t\tthis.idList = idList;\n");
					sb.append("\t}\n");
					
					
					sb.append("\tpublic List<Long> getIdList(){\n");
					sb.append("\t\treturn this.idList;\n");
					sb.append("\t}\n");

				}
				
				if (typeString == null) {
					boolean isDebug = true;
				}
				
				if (typeString.equals("Date")
//						&& 
//						(!columnName.contains("CREATE_DATE") &&
//						 !columnName.contains("UPDATE_DATE")
//						)
						) {
					sb.append("\tprotected Date " + javaPropertiesFormat + "From;\n");
					sb.append("\tprotected Date " + javaPropertiesFormat + "To;\n");
					
					sb.append("\tpublic void set" + upperPropertiesFormat +"From(Date "  + javaPropertiesFormat + "From){\n");
					sb.append("\t\tthis." + javaPropertiesFormat + "From = " + javaPropertiesFormat + "From;\n");
					sb.append("\t}\n");
					
					
					sb.append("\tpublic Date get" + upperPropertiesFormat + "From(){\n");
					sb.append("\t\treturn this."  +  javaPropertiesFormat + "From;\n");
					sb.append("\t}\n");
					
					
					sb.append("\tpublic void set" + upperPropertiesFormat +"To(Date "  + javaPropertiesFormat + "To){\n");
					sb.append("\t\tthis." + javaPropertiesFormat + "To = " + javaPropertiesFormat + "To;\n");
					sb.append("\t}\n");
					
					
					sb.append("\tpublic Date get" + upperPropertiesFormat + "To(){\n");
					sb.append("\t\treturn this."  +  javaPropertiesFormat + "To;\n");
					sb.append("\t}\n");
					
					
					if (columnName.contains("EFFECTIVE_DATE_TO") ) {
						sb.append("\tprotected Date effectiveDateBetween;\n");
						
						sb.append("\tpublic void setEffectiveDateBetween(Date effectiveDateBetween){\n");
						sb.append("\t\tthis.effectiveDateBetween = effectiveDateBetween;\n");
						sb.append("\t}\n");
						
						
						sb.append("\tpublic Date getEffectiveDateBetween(){\n");
						sb.append("\t\treturn this.effectiveDateBetween;\n");
						sb.append("\t}\n");
						
					}
					
				}
			}
			
			
			
			
			
			sb.append("\t@Override\n");
			sb.append("\tpublic Integer getFirstResult(){\n");
			sb.append("\t\treturn this.firstResult;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic Integer getLastResult(){\n");
			sb.append("\t\treturn this.lastResult;\n");
			sb.append("\t}\n");
			
			sb.append("\t@Override\n");
			sb.append("\tpublic List<OrderedBy> getOrderedByList(){\n");
			sb.append("\t\treturn this.orderedByList;\n");
			sb.append("\t}\n");
			
			sb.append("}\n");
			if (compressFileDto != null) {
				
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				pw = new PrintWriter(byteArrayOutputStream);
				pw.write(sb.toString());
				
				compressFileDto.setFileName(soClassName + soSuffix + ".java");
				compressFileDto.setByteArrayOutputStream(byteArrayOutputStream);
			} else {
				fstream = new FileWriter(soFile);
				out = new BufferedWriter(fstream);
								out.write(sb.toString());
			}

			// ################################################## end writing file

		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
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
		System.out.println("So is generated. : " + soClassName + "So.java");
	} // end generateVo()
} // end VoGenerateMgr
