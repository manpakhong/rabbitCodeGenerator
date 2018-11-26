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

public class DaoGenerateMgr {

	private String tableName;
	private String className;
	private String objClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	public DaoGenerateMgr(String _tableName) {


		try {
			tableName = _tableName;

			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
			
			objClassName = Misc
					.convertTableFieldsFormat2JavaPropertiesFormat(tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end constructor

	public void generateDao() {
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
		String daoClassName = null;
		String daoObjectName = null;
		try {
			// Create file

			daoClassName = 	Misc.convertTableNameFormat2ClassNameFormat(tableName);
			daoObjectName = Misc.lowerStringFirstChar(daoClassName);
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
			systemRootDirectory = sysProperties.getSystemRootDirectory();

			String daoFile = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDir + "\\" 
					+ daoDirName + "\\" + daoClassName + daoSuffix + ".java";

			FileWriter fstream = new FileWriter(daoFile);
			BufferedWriter out = new BufferedWriter(fstream);
			// ################################################## begin writing
			// file
			StringBuilder sb = new StringBuilder();
			sb.append("package " + packageName + "." + daoDirName + ";\n");
			
			// import
			sb.append("import java.sql.PreparedStatement;\n");
			sb.append("import java.sql.ResultSet;\n");
			sb.append("import java.util.ArrayList;\n");
			sb.append("import java.util.List;\n");
			sb.append("import org.slf4j.Logger;\n");
			sb.append("import org.slf4j.LoggerFactory;\n");
			
			// --- class
			sb.append("public class " + daoClassName + daoSuffix + " extends DaoBase" + "<" + daoClassName + eoSuffix + ">");
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

			// properties
			sb.append("\tprivate final Logger logger = LoggerFactory.getLogger(getClassName());\n");
//			sb.append("\tprivate static DbUtils mySqlDbUtils;\n");
//			sb.append("\tprivate static DbUtilsFactory dbUtilsFactory;\n");



			// ###############################
			// select statement
			// ###############################
			sb.append("\tprivate final String SELECT_SQL=\n");
			sb.append("\t\t\t\"select \" + \n");

			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);

				sb.append("\t\t\t\"");
				if (i > 0) {
					sb.append(",");
				}
				sb.append("" + metaDataField.getColumnName());
				sb.append(" \" + ");

				sb.append("\n");
			} // end for (int i = 0; i < metaDataFieldList.size(); i++)

			sb.append("\t\t\t\"from " + tableName + " \";\n");

			// ###############################
			// insert statement
			// ###############################
			sb.append("\tprivate final String INSERT_SQL=\n");
			sb.append("\t\t\t\"insert \" + \n");
			sb.append("\t\t\t\"into \" + \n");
			sb.append("\t\t\t\"" + tableName + " \" + \n");
			sb.append("\t\t\t\"( \" + \n");
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);

				sb.append("\t\t\t\"");
				if (i > 0) {
					sb.append(",");
				}
				sb.append("" + metaDataField.getColumnName());
				sb.append(" \" + ");

				sb.append("\n");
			} // end for (int i = 0; i < metaDataFieldList.size(); i++)
			sb.append("\t\t\t\") \" + \n");
			sb.append("\t\t\t\"values\" + \n");
			sb.append("\t\t\t\"( \" + \n");
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);

				sb.append("\t\t\t\"");
				if (i > 0) {
					sb.append(",");
				}
				sb.append("" + "?");
				sb.append(" \" + ");

				sb.append("\n");
			} // end for (int i = 0; i < metaDataFieldList.size(); i++)
			sb.append("\t\t\t\") \";\n");		
			
			// ###############################
			// update statement
			// ###############################
			sb.append("\tprivate final String UPDATE_SQL=\n");
			sb.append("\t\t\t\"update \" + \n");
			sb.append("\t\t\t\"" + tableName + " \" + \n");
			sb.append("\t\t\t\"set \" + \n");
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);

				sb.append("\t\t\t\"");
				if (i > 0) {
					sb.append(",");
				}
				sb.append("" + metaDataField.getColumnName());
				sb.append("= ? \" + ");

				sb.append("\n");
			} // end for (int i = 0; i < metaDataFieldList.size(); i++)

			sb.append("\t\t\t\"where xxx = ? \";\n");	
			
			// ###############################
			// delete statement
			// ###############################
			sb.append("\tprivate final String DELETE_SQL=\n");
			sb.append("\t\t\t\"delete \" + \n");
			sb.append("\t\t\t\"from \" + \n");
			sb.append("\t\t\t\"" + tableName + " \" + \n");
			sb.append("\t\t\t\"where xxx = ? \";\n");	
			
			// getClassName()
			sb.append("\tprivate String getClassName(){\n");
			sb.append("\t\treturn this.getClass().getName();\n");
			sb.append("\t}\n");
			
			// constructors
			sb.append("\tpublic " + daoClassName +  daoSuffix + "() throws Exception {\n");
			sb.append("\t\tsuper();\n");
			sb.append("\t}\n");
			
			sb.append("\tpublic " + daoClassName + daoSuffix + "(String connectionType) throws Exception {\n");
			sb.append("\t\tsuper(connectionType);\n");
			sb.append("\t}\n");
			
			// ###############################
			// read function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic List<" + daoClassName + eoSuffix + "> " + "read(Object so) throws Exception{\n");
			sb.append("\t\tList<" + daoClassName + eoSuffix + "> " + daoObjectName + eoSuffix + "List = null;\n");
			sb.append("\t\tStringBuilder whereSql = null;\n");
			sb.append("\t\tPreparedStatement preparedStatement = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tif (so instanceof " + daoClassName + "So == false) {\n");
			sb.append("\t\t\t\tthrow new Exception(\"so is not an instanceof " + daoClassName + "So\");\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\t" + daoClassName + "So " + daoObjectName + "So = (" + daoClassName + "So) so;\n");
			
			sb.append("\t\t\twhereSql = new StringBuilder();\n");
			sb.append("\t\t\tint wcount = 0;\n");
			
			// loop wcount field name
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);
				
				sb.append("\t\t\tif(" + daoObjectName + "So.get"
						+ Misc.upperStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
						));
				sb.append("() != null){\n");
				sb.append("\t\t\t\tif (wcount == 0) {\n");
				sb.append("\t\t\t\t\twhereSql.append(\"where \");\n");
				sb.append("\t\t\t\t}\n");
				sb.append("\t\t\t\t else if (wcount > 0) {\n");
				sb.append("\t\t\t\t\twhereSql.append(\"and \");\n");
				sb.append("\t\t\t\t}\n");
				sb.append("\t\t\t\twhereSql.append(\"" + metaDataField.getColumnName() + " = ? \");\n");
				sb.append("\t\t\t}\n");
			}

			// pcount
			sb.append("\t\t\tint pcount = 1;\n");
			sb.append("\t\t\tpreparedStatement = getConnection().prepareStatement(SELECT_SQL + whereSql.toString());\n");
			
			// loop pcount field name
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);
				
				sb.append("\t\t\tif(" + daoObjectName + "So.get"
						+ Misc.upperStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
						));
				sb.append("() != null){\n");
				sb.append("\t\t\t\tpreparedStatement.setString(pcount, " + daoObjectName + "So.get" + 
						Misc.upperStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
								)
				);
				sb.append("());\n");
				sb.append("\t\t\t\tpcount++;\n");
				sb.append("\t\t\t}\n");
			}
			sb.append("\t\t\tResultSet rs = preparedStatement.executeQuery();\n");
			sb.append("\t\t\twhile(rs.next()) {\n");
			sb.append("\t\t\t\tif (" + daoObjectName + eoSuffix + "List == null){\n");
			sb.append("\t\t\t\t\t" + daoObjectName + eoSuffix + "List = new ArrayList<" + daoClassName + eoSuffix + ">();\n");
			sb.append("\t\t\t\t}\n");
				
				sb.append("\t\t\t\t" + daoClassName + eoSuffix + " eo = new " + daoClassName + eoSuffix + "();\n");
			
				// loop while(rs.next() content....
				for (int i = 0; i < metaDataFieldList.size(); i++) {
					MetaDataField metaDataField = new MetaDataField();
					metaDataField = metaDataFieldList.get(i);
					sb.append("\t\t\t\tString " + 
							Misc
							.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
									.getColumnName())
							);
					sb.append(" = rs.getString(\"" +  
							metaDataField.getColumnName()
							+ "\");");
					sb.append("\n");
					sb.append("\t\t\t\teo.set" + 
							Misc.upperStringFirstChar(Misc
									.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
											.getColumnName())
									)
							);
					sb.append("(");
					sb.append(
							Misc
							.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
									.getColumnName())
							);
					sb.append(");\n");
					
				}
			sb.append("\t\t\t\t" + daoObjectName + eoSuffix + "List.add(eo);\n");
				
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".read() - so=\" + so, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif(preparedStatement != null){\n");
			sb.append("\t\t\t\tpreparedStatement.close();\n");
			sb.append("\t\t\t\tpreparedStatement = null;\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tif (connectionType.equals(CONNECTION_TYPE_JDBC)){\n");
			sb.append("\t\t\t\tif(connection != null) {\n");
			sb.append("\t\t\t\t\tconnection.close();\n");
			sb.append("\t\t\t\t\tconnection = null;\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn " + daoObjectName + eoSuffix + "List;\n");
			sb.append("\t} // end select function\n");
				
			// ###############################
			// create function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic Integer " + "create(" + daoClassName +  eoSuffix + " eo) throws Exception{\n");
			sb.append("\t\tPreparedStatement preparedStatement = null;\n");
			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			
			// pcount
			sb.append("\t\t\tint pcount = 1;\n");
			sb.append("\t\t\tpreparedStatement = getConnection().prepareStatement(INSERT_SQL);\n");
			
			// loop pcount field name
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);
				
				sb.append("\t\t\tif(eo.get"
						+ Misc.upperStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
						));
				sb.append("() != null){\n");
				sb.append("\t\t\t\tpreparedStatement.setString(pcount, eo.get" + 
						Misc.upperStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
								)
				);
				sb.append("());\n");
				sb.append("\t\t\t\tpcount++;\n");
				sb.append("\t\t\t}\n");
			}
			sb.append("\t\t\tnoOfAffectedRow = preparedStatement.executeUpdate();\n");
			sb.append("\t\t\tif (noOfAffectedRow.intValue() != 1) {\n");
			sb.append("\t\t\t\tthrow new Exception(\"insert failed! affectedRow=\" + noOfAffectedRow);\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".create() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif(preparedStatement != null){\n");
			sb.append("\t\t\t\tpreparedStatement.close();\n");
			sb.append("\t\t\t\tpreparedStatement = null;\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tif (connectionType.equals(CONNECTION_TYPE_JDBC)){\n");
			sb.append("\t\t\t\tif(connection != null) {\n");
			sb.append("\t\t\t\t\tconnection.close();\n");
			sb.append("\t\t\t\t\tconnection = null;\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end create function\n");			
			
			
			
			// ###############################
			// update function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic Integer " + "update(" + daoClassName +  eoSuffix + " eo) throws Exception{\n");
			sb.append("\t\tPreparedStatement preparedStatement = null;\n");
			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			
			// pcount
			sb.append("\t\t\tint pcount = 1;\n");
			sb.append("\t\t\tpreparedStatement = getConnection().prepareStatement(UPDATE_SQL);\n");
			
			// loop pcount field name
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);
				
//				sb.append("\t\t\tif(eo.get"
//						+ Misc.upperStringFirstChar(Misc
//								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
//										.getColumnName())
//						));
//				sb.append("() != null){\n");
				sb.append("\t\t\tpreparedStatement.setString(pcount, eo.get" + 
						Misc.upperStringFirstChar(Misc
								.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField
										.getColumnName())
								)
				);
				sb.append("());\n");
				sb.append("\t\t\tpcount++;\n");
//				sb.append("\t\t\t}\n");
			}
			sb.append("\t\t\tnoOfAffectedRow = preparedStatement.executeUpdate();\n");
			sb.append("\t\t\tif (noOfAffectedRow.intValue() != 1) {\n");
			sb.append("\t\t\t\tthrow new Exception(\"update failed! affectedRow=\" + noOfAffectedRow);\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".create() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif(preparedStatement != null){\n");
			sb.append("\t\t\t\tpreparedStatement.close();\n");
			sb.append("\t\t\t\tpreparedStatement = null;\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tif (connectionType.equals(CONNECTION_TYPE_JDBC)){\n");
			sb.append("\t\t\t\tif(connection != null) {\n");
			sb.append("\t\t\t\t\tconnection.close();\n");
			sb.append("\t\t\t\t\tconnection = null;\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end update function\n");	
			
			
			// ###############################
			// delete function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic Integer " + "delete(" + daoClassName +  eoSuffix + " eo) throws Exception{\n");
			sb.append("\t\tPreparedStatement preparedStatement = null;\n");
			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			
			// pcount
			sb.append("\t\t\tint pcount = 1;\n");
			sb.append("\t\t\tpreparedStatement = getConnection().prepareStatement(DELETE_SQL);\n");
			
			sb.append("\t\t\tif(eo.getXXX_key_XXX() != null){\n");
			sb.append("\t\t\t\tpreparedStatement.setInt(pcount, eo.getXXX_key_XXX());\n");
			sb.append("\t\t\t\tpcount++;\n");
			sb.append("\t\t\t}\n");
			
			
			sb.append("\t\t\tnoOfAffectedRow = preparedStatement.executeUpdate();\n");
			sb.append("\t\t\tif (noOfAffectedRow.intValue() != 1) {\n");
			sb.append("\t\t\t\tthrow new Exception(\"delete failed! affectedRow=\" + noOfAffectedRow);\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".delete() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif(preparedStatement != null){\n");
			sb.append("\t\t\t\tpreparedStatement.close();\n");
			sb.append("\t\t\t\tpreparedStatement = null;\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tif (connectionType.equals(CONNECTION_TYPE_JDBC)){\n");
			sb.append("\t\t\t\tif(connection != null) {\n");
			sb.append("\t\t\t\t\tconnection.close();\n");
			sb.append("\t\t\t\t\tconnection = null;\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end delete function\n");	
			
			// ########## end class ##############################
			sb.append("} //end class\n");
			out.write(sb.toString());

			// ################################################## end writing
			// file
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		} // end try ... catch ...
		  System.out.println("Dao is generated. : " + daoClassName + "Dao.java");		
	} // end generateDao()

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DaoGenerateMgr daoGenerateMgr = new DaoGenerateMgr("LACCCDTL");
		daoGenerateMgr.generateDao();
	}

}
