package com.rabbitforever.generateJavaMVC.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.rabbitforever.generateJavaMVC.bundles.SysProperties;
import com.rabbitforever.generateJavaMVC.commons.Misc;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;

public class OrmDaoGenerateMgr {

	private String tableName;
	private String className;
	private String objClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	public OrmDaoGenerateMgr(String _tableName) {


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
		String daoSuffix = "OrmDao";
		String eoSuffix = "Eo";
		String soSuffix = "So";
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
			sb.append("import java.util.ArrayList;\n");
			sb.append("import java.util.Date;\n");
			sb.append("import java.util.List;\n");
			
			sb.append("import javax.persistence.criteria.CriteriaBuilder;\n");
			sb.append("import javax.persistence.criteria.CriteriaQuery;\n");
			sb.append("import javax.persistence.criteria.Predicate;\n");
			sb.append("import javax.persistence.criteria.Root;\n");
			
			sb.append("import org.hibernate.Session;\n");
			sb.append("import org.hibernate.SessionFactory;\n");
			sb.append("import org.hibernate.Transaction;\n");
			sb.append("import org.hibernate.query.Query;\n");
			sb.append("import org.slf4j.Logger;\n");
			sb.append("import org.slf4j.LoggerFactory;\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			sb.append("import org.springframework.stereotype.Repository;\n");
			sb.append("import com.rabbitforever.gamblehub.interceptors.AuditInterceptor;\n");
			sb.append("import com.rabbitforever.gamblehub.models.sos.OrderedBy;\n");

			
			// --- class
			sb.append("public class " + daoClassName + daoSuffix + " extends DaoBase" + "<" + daoClassName + eoSuffix + ">");
			sb.append("{\n");

			MySqlDbMgr oracleDbMgr = new MySqlDbMgr();
			List<MetaDataField> metaDataFieldList = new ArrayList<MetaDataField>();
			metaDataFieldList = oracleDbMgr.getMetaDataList(tableName);

			// properties
			sb.append("\tprivate final Logger logger = LoggerFactory.getLogger(getClassName());\n");
//			sb.append("\tprivate static DbUtils mySqlDbUtils;\n");
//			sb.append("\tprivate static DbUtilsFactory dbUtilsFactory;\n");
			
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
			sb.append("\t\tCriteriaBuilder builder = null;\n");
			sb.append("\t\tCriteriaQuery<" + daoClassName + eoSuffix + "> query = null;\n");
			sb.append("\t\tTransaction trans = null;\n");
			sb.append("\t\tRoot<" + daoClassName + eoSuffix + "> root = null;\n");
			sb.append("\t\tQuery<" + daoClassName + eoSuffix + "> q = null;\n");
			sb.append("\t\tList<Predicate> predicateList = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tsession = sessionFactory.getCurrentSession();\n");
			sb.append("\t\t\t" + daoClassName + soSuffix + " " + daoObjectName + soSuffix + " = (" + daoClassName + soSuffix + ") so;"+ "\n");
			sb.append("\t\t\ttrans = session.getTransaction();\n");
			sb.append("\t\t\ttrans.begin();\n");
			sb.append("\t\t\tbuilder = session.getCriteriaBuilder();\n");
			sb.append("\t\t\tquery = builder.createQuery(" + daoClassName + eoSuffix + ".class);\n");
			sb.append("\t\t\troot = query.from("+ daoClassName + eoSuffix + ".class);\n");
	
			
			// loop if so.getXXX() != null
			for (int i = 0; i < metaDataFieldList.size(); i++) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField = metaDataFieldList.get(i);
				String upperFirstCharAttributeName = Misc.upperStringFirstChar(Misc.convertTableFieldsFormat2JavaPropertiesFormat(metaDataField.getColumnName()));
				sb.append("\t\t\tif(" + daoObjectName + "So.get" + upperFirstCharAttributeName);
				sb.append("() != null){\n");
				sb.append("\t\t\t\tif (predicateList == null) {\n");
				sb.append("\t\t\t\t\tpredicateList = new ArrayList<Predicate>();\n");
				sb.append("\t\t\t\t}\n");
				sb.append("\t\t\t\tPredicate predicate = builder.equal(root.get(\"" + metaDataField.getColumnName() + "\"), " + daoObjectName + soSuffix + ".get" + upperFirstCharAttributeName+ "());\n");

				sb.append("\t\t\t}\n");
			}
			sb.append("\t\t\tif (predicateList != null) {\n");
			sb.append("\t\t\t\tquery.select(root).where(predicateList.toArray(new Predicate[] {}));\n");
			sb.append("\t\t\t} else {\n");
			sb.append("\t\t\t\tquery.select(root);\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tList<OrderedBy> orderedByList = " + daoObjectName + soSuffix + ".getOrderedByList();\n");
			sb.append("\t\t\tif (orderedByList != null){\n");
			sb.append("\t\t\t\tfor (OrderedBy orderedBy: orderedByList) {\n");
			sb.append("\t\t\t\t\tString dataField = orderedBy.getDataField();\n");
			sb.append("\t\t\t\t\tif (orderedBy.getIsAsc()) {\n");
			sb.append("\t\t\t\t\t\tquery.orderBy(builder.desc(root.get(dataField)));\n");
			sb.append("\t\t\t\t\t} else {\n");
			sb.append("\t\t\t\t\t\tquery.orderBy(builder.desc(root.get(dataField)));\n");
			sb.append("\t\t\t\t\t}\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tq = session.createQuery(query);\n");
			sb.append("\t\t\t" + daoObjectName  + eoSuffix +"List = q.getResultList();\n");
			sb.append("\t\t\tfor (" + daoClassName + eoSuffix + " " + daoObjectName + eoSuffix + ":" + daoObjectName + eoSuffix + "List){\n");
			sb.append("\t\t\t\tlogger.debug(" + daoObjectName + eoSuffix + ".getResult());\n");
			sb.append("\t\t\t}\n");
			
			

			
			
			sb.append("\t\t}catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".read() - so=\" + so, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif(trans != null){\n");
			sb.append("\t\t\t\ttrans.commit();\n");
			sb.append("\t\t\t\ttrans = null;\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn " + daoObjectName + eoSuffix + "List;\n");
			sb.append("\t} // end select function\n");
				
			// ###############################
			// create function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic Integer " + "create(" + daoClassName +  eoSuffix + " eo) throws Exception{\n");
			sb.append("\t\tTransaction trans = null;\n");
			sb.append("\t\tInteger id = null;\n");
			sb.append("\t\ttry{\n");
			

			sb.append("\t\t\tsession = sessionFactory.withOptions().interceptor(new AuditInterceptor()).openSession();\n");
			sb.append("\t\t\ttrans = session.getTransaction();\n");
			sb.append("\t\t\ttrans.begin();\n");
			
			sb.append("\t\t\teo.setCreatedBy(\"\");\n");
			sb.append("\t\t\teo.setCreateDate(new Date());\n");
			sb.append("\t\t\teo.setUpdatedBy(\"\");\n");
			sb.append("\t\t\teo.setUpdateDate(new Date());\n");
			sb.append("\t\t\tsession.save(eo);\n");
			sb.append("\t\t\ttrans.commit();\n");
			sb.append("\t\t\tid = eo.getId();\n");

			
			sb.append("\t\t}catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".create() - eo=\" + eo, e);\n");
			sb.append("\t\t\ttrans.rollback();\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif(session != null){\n");
			sb.append("\t\t\t\tsession.close();\n");
			sb.append("\t\t\t\tsession = null;\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\treturn id;\n");
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
			sb.append("\t\t\tpreparedStatement = connection.prepareStatement(UPDATE_SQL);\n");
			
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
			sb.append("\t\t\tpreparedStatement = connection.prepareStatement(DELETE_SQL);\n");
			
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
		OrmDaoGenerateMgr daoGenerateMgr = new OrmDaoGenerateMgr("LACCCDTL");
		daoGenerateMgr.generateDao();
	}

}
