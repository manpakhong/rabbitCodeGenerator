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

public class OrmServiceGenerateMgr {
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
	public OrmServiceGenerateMgr(String _tableName)throws Exception {

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
		String daoSuffix = "OrmDao";
		String helperSuffix = "OrmMgrHelper";
		String eoSuffix = "Eo";
		String daoClassName = null;
		String daoObjectName = null;
		String servicesDirName = null;
		String serviceObjectName = null;
		String classServiceSuffix="OrmMgr";
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
			sb.append("import org.apache.log4j.LogManager;\n");
			sb.append("import org.apache.log4j.Logger;\n");
			sb.append("import java.util.ArrayList;\n");
			sb.append("import java.util.List;\n");

			
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
			sb.append("\tprivate final Logger logger = LogManager.getLogger(getClassName());\n");
			sb.append("\tprivate " + serviceClassName + daoSuffix + " dao;\n");
			sb.append("\t//uncomment the line to use helper\n");
			sb.append("\t//private " + serviceClassName + helperSuffix + " helper;\n");
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
			
			
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"(Session session) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(session, false, null);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"()\", e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"(Session session, String connectionType) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(session, false, connectionType);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"() - connectionType=\" + connectionType, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			
			sb.append("\tpublic " + serviceClassName +  classServiceSuffix +"(Session session, Boolean closeConnectionFinally,  String connectionType) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tinit(session, closeConnectionFinally, connectionType);\n");
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"." + serviceClassName +  classServiceSuffix +"() - closeConnectionFinally=\" + closeConnectionFinally + \",connectionType=\" + connectionType, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			// init
			sb.append("\tpublic void init(Session session, Boolean closeConnectionFinally,  String connectionType) throws Exception{\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tif(connectionType == null){\n");
			sb.append("\t\t\t\tconnectionType = dbProperties.getConnectionType();\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tthis.session = session;\n");
			sb.append("\t\t\tdao = new " + serviceClassName + daoSuffix + "(session, closeConnectionFinally, connectionType);\n");
			sb.append("\t\t\t//uncomment the line to use helper\n");
			sb.append("\t\t\t//helper = new " + serviceClassName + helperSuffix + "(session);\n");
			
			sb.append("\t\t} catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \"init() - closeConnectionFinally=\" + closeConnectionFinally + \",connectionType=\" + connectionType, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end constructor\n");
			
			
			// ###############################
			// count Mgr
			// ###############################
			sb.append("\tpublic Long count(Object so) throws Exception{\n");
			sb.append("\t\tLong count = null;\n");
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
			sb.append("\t} // end read function\n");

			// ###############################
			// read Map Mgr
			// ###############################
			sb.append("\t// do necessary adjustment if the key is not Id, or delete the method\n");
			sb.append("\tpublic Map<Long," + serviceClassName + "Eo> " + "readMap(Object so) throws Exception{\n");
			sb.append("\t\tMap<Long, " + serviceClassName + "Eo> " + objClassName + "EoMap = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tList<" + serviceClassName + "Eo> " + objClassName + "EoList = dao.read(so);\n");
			
			sb.append("\t\t\t\tif ( " + objClassName + "EoList != null){\n");
			sb.append("\t\t\t\t\t" + objClassName + "EoMap = new HashMap<Long, " + serviceClassName + "Eo>();\n");
			sb.append("\t\t\t\t\tfor (" + serviceClassName + "Eo " + objClassName +"Eo : " + objClassName + "EoList ){\n");
			sb.append("\t\t\t\t\t\tLong id = " + objClassName + "Eo.getId();\n");
			sb.append("\t\t\t\t\t\t" + objClassName + "EoMap.put(id, " + objClassName + "Eo);\n");
			sb.append("\t\t\t\t\t} // end for\n");
			sb.append("\t\t\t\t}\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".readMap() - so=\" + so, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\treturn " + objClassName + "EoMap;\n");
			sb.append("\t} // end readMap function\n");
			
			
			// ###############################
			// create Mgr
			// ###############################
			sb.append("\tpublic void " + "create(" + serviceClassName + "Eo " +  "eo) throws Exception{\n");
//			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tdao.create(eo);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".create() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
//			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end create function\n");

			// ###############################
			// update Mgr
			// ###############################
			sb.append("\tpublic void " + "update(" + serviceClassName + "Eo " +  "eo) throws Exception{\n");
//			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tdao.update(eo);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".update() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
//			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end update function\n");

			// ###############################
			// delete statement
			// ###############################
			sb.append("\tpublic void " + "delete(" + serviceClassName + "Eo " +  "eo) throws Exception{\n");
//			sb.append("\t\tInteger noOfAffectedRow = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tdao.delete(eo);\n");
			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".delete() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
//			sb.append("\t\treturn noOfAffectedRow;\n");
			sb.append("\t} // end delete function\n");

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
