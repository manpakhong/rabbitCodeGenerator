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

public class IServiceGenerateMgr {

	private String tableName;
	private String voClassName;
	private String objClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	
	public IServiceGenerateMgr(String _tableName) throws Exception {
		tableName = _tableName;
		voClassName = tableName;
		propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
		sysProperties = propertiesFactory.getInstanceOfSysProperties();
		objClassName = Misc
				.convertTableFieldsFormat2JavaPropertiesFormat(tableName);
	} // end constructor

	public void generateService() {
		try {
			// Create file

			voClassName = tableName;

			String voFile = SystemParams.OUTPUT_ROOT_DIRECTORY
					+ "\\" + SystemParams.PROJECT_FOLDER_ROOT +"\\" + SystemParams.SERVICE_I_DIR_NAME + "\\" + voClassName + "Service.java";

			FileWriter fstream = new FileWriter(voFile);
			BufferedWriter out = new BufferedWriter(fstream);
			// ################################################## begin writing
			// file
			StringBuilder sb = new StringBuilder();

			// --- Interface
			sb.append("public Interface " + voClassName + "Service\n");
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

			// select function
			sb.append("public List<" + voClassName + "> " + "selectAll();\n");

			// insert function
			sb.append("public " + voClassName + " insert(" + voClassName + " " + objClassName +");\n");

			// update function
			sb.append("public " + voClassName + " update(" + voClassName + " " + objClassName +");\n");
			// delete function
			sb.append("public int delete(" + voClassName + " " + objClassName +");\n");
			

				
			// ########## end class ##############################
			sb.append("} //end interface\n");
			out.write(sb.toString());

			// ################################################## end writing
			// file
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		} // end try ... catch ...
		  System.out.println("Service Interface is generated. : " + voClassName + "Service.java");		
	} // end generateDao()

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		IServiceGenerateMgr serviceGenerateMgr = new IServiceGenerateMgr("LACCCDTL");
		serviceGenerateMgr.generateService();
	}

}
