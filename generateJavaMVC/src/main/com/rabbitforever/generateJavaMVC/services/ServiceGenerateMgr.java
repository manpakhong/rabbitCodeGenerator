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

public class ServiceGenerateMgr {
	private final Logger logger = Logger.getLogger(getClassName());
	private String tableName;
	private String serviceClassName;
	private String objClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	private String getClassName() {
		return this.getClass().getName();
	}
	
	private void init() throws Exception {
		try {
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
			objClassName = Misc.convertTableFieldsFormat2JavaPropertiesFormat(tableName);

		} catch (Exception e) {
			logger.error(this.getClass() + ".init() - ", e);
			throw e;
		}
	}
	public ServiceGenerateMgr(String _tableName)throws Exception {

		try {
			tableName = _tableName;
			init();
		} catch (Exception e) {
			logger.error(this.getClass() + ".ServiceGenerateMgr() - ", e);
			throw e;
		}

	} // end constructor
	public void generateService() throws Exception{
		generateService(null);
	}
	public void generateService(CompressFileDto compressFileDto) throws Exception{
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
		String daoSuffix = "JdbcDao";
		String eoSuffix = "Eo";
		String daoClassName = null;
		String daoObjectName = null;
		String servicesDirName = null;
		String serviceObjectName = null;
		String classServiceSuffix="JdbcMgr";
		FileWriter fstream = null;
		BufferedWriter out = null;
		PrintWriter pw = null;
		try {
			// Create file

			serviceClassName = Misc.convertTableNameFormat2ClassNameFormat(tableName);
			serviceObjectName = Misc.convertTableFieldsFormat2JavaPropertiesFormat(tableName);
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
			servicesDirName = sysProperties.getServicesDirName();
			systemRootDirectory = sysProperties.getSystemRootDirectory();

			String serviceFile = outputRootDirectory + "\\" + javaDirName + "\\" + systemRootDir + "\\" + servicesDirName + "\\"
					+ serviceClassName + classServiceSuffix +".java";

			// ################################################## begin writing
			// file
			StringBuilder sb = new StringBuilder();

			// --- package
			sb.append("package " + packageName + "." + servicesDirName + ";\n");
			
			// --- import
			sb.append("import org.apache.log4j.Logger;\n");

			
			// --- class
			sb.append("public class " + serviceClassName + classServiceSuffix +" extends ServiceBase");
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
			sb.append("\tprivate final Logger logger = Logger.getLogger(getClassName());\n");
			sb.append("\tprivate " + serviceClassName + daoSuffix + " dao;\n");

			// getClassName()
			sb.append("\tprivate String getClassName(){\n");
			sb.append("\t\treturn this.getClass().getName();\n");
			sb.append("\t}\n");
			
			// constructors
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"() throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(null, null, null);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"() \", e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");

			
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"(String connectionType) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(null, null, connectionType);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"() - connectionType=\" + connectionType, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"(Connection connection) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(connection, false, null);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"()\", e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"(Connection connection, String connectionType) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(connection, false, connectionType);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"() - connectionType=\" + connectionType, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"(Connection connection, Boolean closeConnectionFinally,  String connectionType) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(connection, closeConnectionFinally, connectionType);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"() - closeConnectionFinally=\" + closeConnectionFinally + \",connectionType=\" + connectionType, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			// init
			sb.append("\tpublic void init(Connection connection, Boolean closeConnectionFinally,  String connectionType) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tif(connectionType == null){\n");
			sb.append("\t\t\t\tconnectionType = dbProperties.getConnectionType();\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tthis.connection = connection;\n");
			sb.append("\t\t\tdao = new " + serviceClassName + "JdbcDao(connection, closeConnectionFinally, connectionType);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"init() - closeConnectionFinally=\" + closeConnectionFinally + \",connectionType=\" + connectionType, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			
			// ###############################
			// count Mgr
			// ###############################
			sb.append("\tpublic Integer count(Object so) throws Exception{\n");
			sb.append("\t\tInteger count = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tcount = dao.count(so);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".count() - so=\" + so, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\treturn count;\n");
			sb.append("\t} // end count function\n");
			
			// ###############################
			// get by id
			// ###############################
			sb.append("\tpublic " + serviceClassName + "Eo " + "getById(Long id) throws Exception{\n");
			sb.append("\t\tList<" + serviceClassName + "Eo> " + objClassName + "EoList = null;\n");
			sb.append("\t\t" + serviceClassName + "Eo " + objClassName + "Eo = null;\n");
			sb.append("\t\t" + serviceClassName + "So " + objClassName + "So = null;\n");
			sb.append("\t\ttry{\n");
			
			sb.append("\t\t\t"+ objClassName +"So = new " + serviceClassName + "So();\n");
			sb.append("\t\t\t"+ objClassName + "So.setId(id);\n");
			sb.append("\t\t\t" + objClassName + "EoList = dao.read(" + objClassName + "So);\n");
			
			sb.append("\t\t\tif(" + objClassName + "EoList != null && " + objClassName + "EoList.size() > 0){\n");
			sb.append("\t\t\t\t" + objClassName + "Eo = " + objClassName + "EoList.get(0);\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".getById() - id=\" + id, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\tfinally{\n");
			sb.append("\t\t\t" + objClassName + "So = null;\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn " + objClassName + "Eo;\n");
			sb.append("\t} // end getById function\n");
			
			// ###############################
			// read Mgr
			// ###############################
			sb.append("\tpublic List<" + serviceClassName + "Eo> " + "read(Object so) throws Exception{\n");
			sb.append("\t\tList<" + serviceClassName + "Eo> " + objClassName + "EoList = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\t" + objClassName + "EoList = dao.read(so);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".read() - so=\" + so, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\treturn " + objClassName + "EoList;\n");
			sb.append("\t} // end select function\n");

			// ###############################
			// create Mgr
			// ###############################
			sb.append("\tpublic Integer " + "create(" + serviceClassName + "Eo " +  "eo) throws Exception{\n");
			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tnoOfAffectedRow = dao.create(eo);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".create() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end create function\n");

			// ###############################
			// update Mgr
			// ###############################
			sb.append("\tpublic Integer " + "update(" + serviceClassName + "Eo " +  "eo) throws Exception{\n");
			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tnoOfAffectedRow = dao.update(eo);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".update() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end create function\n");

			// ###############################
			// delete statement
			// ###############################
			sb.append("\tpublic Integer " + "delete(" + serviceClassName + "Eo " +  "eo) throws Exception{\n");
			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tnoOfAffectedRow = dao.delete(eo);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".delete() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end create function\n");

			// ########## end class ##############################
			sb.append("} //end class\n");
			if (compressFileDto != null) {
				
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				pw = new PrintWriter(byteArrayOutputStream);
				pw.write(sb.toString());
				
				compressFileDto.setFileName(serviceClassName + classServiceSuffix + ".java");
				compressFileDto.setByteArrayOutputStream(byteArrayOutputStream);
			} else {
				fstream = new FileWriter(serviceFile);
				out = new BufferedWriter(fstream);
				out.write(sb.toString());
			}

			// ################################################## end writing
			// file
//			out.close();
		} catch (Exception e) {// Catch exception if any
			logger.error(this.getClass() + ".generateService() - ", e);
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
		System.out.println("Service is generated. : " + serviceClassName +  classServiceSuffix +".java");
	} // end generateService()

//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ServiceGenerateMgr daoGenerateMgr = new ServiceGenerateMgr("LACCCDTL");
//		daoGenerateMgr.generateService();
//	}

}
