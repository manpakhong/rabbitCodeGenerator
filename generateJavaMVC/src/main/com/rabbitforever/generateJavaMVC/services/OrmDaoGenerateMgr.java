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


public class OrmDaoGenerateMgr {
	private final Logger logger = Logger.getLogger(getClassName());
	private TypeMappingMgr typeMappingMgr;
	private String tableName;
	private String className;
	private String objClassName;
	private PropertiesFactory propertiesFactory;
	private SysProperties sysProperties;
	
	private String getClassName() {
		return this.getClass().getName();
	}
	
	public OrmDaoGenerateMgr(String _tableName) throws Exception {
		try {
			tableName = _tableName;
			init();
		} catch (Exception e) {
			logger.error(this.getClass() + ".OrmDaoGenerateMgr() - ", e);
			throw e;
		}

	} // end constructor

	private void init() throws Exception {
		try {
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			sysProperties = propertiesFactory.getInstanceOfSysProperties();
			objClassName = Misc.convertTableFieldsFormat2JavaPropertiesFormat(tableName);
			typeMappingMgr = new OracleTypeMappingMgr();
		} catch (Exception e) {
			logger.error(this.getClass() + ".init() - ", e);
			throw e;
		}
	}
	public void generateDao() throws Exception{
		generateDao(null);
	}
	public void generateDao(CompressFileDto compressFileDto) throws Exception {
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
		FileWriter fstream = null;
		BufferedWriter out = null;
		PrintWriter pw = null;
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
			sb.append("import javax.persistence.criteria.Order;\n");
			sb.append("import org.apache.log4j.Logger;\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			sb.append("import org.springframework.stereotype.Repository;\n");
			sb.append("import hk.org.hkbh.cms.outpatient.core.daos.orm.interceptors.AuditInterceptor;\n");
			sb.append("import hk.org.hkbh.cms.outpatient.core.models.sos.OrderedBy;\n");

			
			// --- class
			sb.append("public class " + daoClassName + daoSuffix + " extends OrmDaoBase" + "<" + daoClassName + eoSuffix + ">");
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
//			sb.append("\tprivate static DbUtils mySqlDbUtils;\n");
//			sb.append("\tprivate static DbUtilsFactory dbUtilsFactory;\n");
			
			sb.append("\tprivate final String SELECT_COUNT_SQL =\n");
			sb.append("\t\t\"select \" +\n");
			sb.append("\t\t\"count(0) as count_result \" +\n"); 
			sb.append("\t\t\"from "+ daoClassName + "Eo " + daoObjectName +" \";\n");
			
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
			
			sb.append("\tpublic " + daoClassName + daoSuffix + "(Session session) throws Exception {\n");
			sb.append("\t\tsuper(session);\n");
			sb.append("\t}\n");
			
			sb.append("\tpublic " + daoClassName + daoSuffix + "(Session session, String connectionType) throws Exception {\n");
			sb.append("\t\tsuper(session, connectionType);\n");
			sb.append("\t}\n");
			
			
			sb.append("\tpublic " + daoClassName + daoSuffix + "(Session session, Boolean closeConnectionFinally, String connectionType) throws Exception {\n");
			sb.append("\t\tsuper(session, closeConnectionFinally, connectionType);\n");
			sb.append("\t}\n");

			// ###############################
			// generateReadWhereStatement()
			// ###############################
			sb.append("\tpublic String generateReadWhereStatement(" + daoClassName + "So " + daoObjectName + "So) throws Exception {\n");
			sb.append("\t\tStringBuilder whereSql = new StringBuilder();\n");
			sb.append("\t\ttry {\n");
			sb.append("\t\t\tint wcount = 0;\n");
			
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
				
				
				sb.append("\t\t\tif(" + daoObjectName + "So.get" + upperPropertiesFormat);
				sb.append("() != null){\n");
				sb.append("\t\t\t\tif (wcount == 0) {\n");
				sb.append("\t\t\t\t\twhereSql.append(\"where \");\n");
				sb.append("\t\t\t\t}\n");
				sb.append("\t\t\t\t else if (wcount > 0) {\n");
				sb.append("\t\t\t\t\twhereSql.append(\"and \");\n");
				sb.append("\t\t\t\t}\n");
				sb.append("\t\t\t\twhereSql.append(\"" + daoObjectName + "." + javaPropertiesFormat + " = :" + javaPropertiesFormat +" \");\n");
				sb.append("\t\t\t\twcount++;\n");
				sb.append("\t\t\t}\n");
			}
			
			
			// ordered by
			sb.append("\t\t\tif(" + daoObjectName + "So.getOrderedByList() != null){\n");
			sb.append("\t\t\t\t\tList<OrderedBy> orderedByList = " + daoObjectName + "So.getOrderedByList();\n");
			sb.append("\t\t\t\t\tif (orderedByList.size() > 0) {\n");
			sb.append("\t\t\t\t\t\twhereSql.append(\"order by \");\n");
			sb.append("\t\t\t\t\t\tfor (int i=0; i < orderedByList.size(); i++) {\n");
			sb.append("\t\t\t\t\t\t\tif (i > 1) {\n");
			sb.append("\t\t\t\t\t\t\t\twhereSql.append(\", \");\n");
			sb.append("\t\t\t\t\t\t\t}\n");
			sb.append("\t\t\t\t\t\t\tOrderedBy orderedBy = orderedByList.get(i);\n");
			sb.append("\t\t\t\t\t\t\twhereSql.append(\""+ daoObjectName +".\" + orderedBy.getDataField() + \" \");\n");
			sb.append("\t\t\t\t\t\t\tif (orderedBy.getIsAsc()){\n");
			sb.append("\t\t\t\t\t\t\t\twhereSql.append(\"asc \");\n");
			sb.append("\t\t\t\t\t\t\t} else {\n");
			sb.append("\t\t\t\t\t\t\t\twhereSql.append(\"desc \");\n");
			sb.append("\t\t\t\t\t\t\t}\n");
			sb.append("\t\t\t\t\t\t} //endfor\n");
			sb.append("\t\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t} catch (Exception e) {\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".generateReadWhereStatement() - " + daoObjectName
					+ "So=\" + " + daoObjectName + "So, e);\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn whereSql.toString();\n");
			sb.append("\t}// end generateReadWhereStatement\n");
			
			
			
			// ###############################
			// generateQuery()
			// ###############################
			sb.append("\tpublic TypedQuery<" +  daoClassName + "Eo> generateQuery(" + daoClassName + "So " + daoObjectName + "So) throws Exception {\n");
			sb.append("\t\tList<Predicate> predicateList = null;\n");
			sb.append("\t\tCriteriaBuilder builder = null;\n");
			sb.append("\t\tCriteriaQuery<" + daoClassName + "Eo> query = null;\n");
			sb.append("\t\tQuery<" + daoClassName + "Eo> q = null;\n");
			sb.append("\t\tRoot<" + daoClassName + "Eo> root = null;\n");
			sb.append("\t\tTypedQuery<" + daoClassName + "Eo> typedQuery = null;\n");
			sb.append("\t\ttry {\n");
			sb.append("\t\t\tbuilder = session.getCriteriaBuilder();\n");
			sb.append("\t\t\tquery = builder.createQuery(" + daoClassName + "Eo.class);\n");
			sb.append("\t\t\troot = query.from(" + daoClassName + "Eo.class);\n");
			
			
			sb.append("\t\t\tif(" + daoObjectName + "So != null) {\n");

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
				
				
				sb.append("\t\t\t\tif(" + daoObjectName + "So.get" + upperPropertiesFormat);
				sb.append("() != null){\n");
				sb.append("\t\t\t\t\tif(predicateList == null) {\n");
				sb.append("\t\t\t\t\t\tpredicateList = new ArrayList<Predicate>();\n");
				sb.append("\t\t\t\t\t}\n");
				sb.append("\t\t\t\t\tPredicate predicate = builder.equal(root.get(\"" + javaPropertiesFormat + "\"), "+ daoObjectName +"So.get" + upperPropertiesFormat + "());\n");
				sb.append("\t\t\t\t\tpredicateList.add(predicate);\n");
				sb.append("\t\t\t\t}\n");
			}
			
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tif (predicateList != null) {\n");
			sb.append("\t\t\t\tquery.select(root).where(predicateList.toArray(new Predicate[] {}));\n");
			sb.append("\t\t\t}else{\n");
			sb.append("\t\t\t\tquery.select(root);\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tList<OrderedBy> orderedByList = " +daoObjectName + "So.getOrderedByList();\n");
			sb.append("\t\t\tList<Order> orderList = null;\n");
			
			// ordered by
			sb.append("\t\t\tif(" + daoObjectName + "So.getOrderedByList() != null){\n");
			sb.append("\t\t\t\torderList = new ArrayList<Order>();\n");
			sb.append("\t\t\t\tif (orderedByList.size() > 0) {\n");
			sb.append("\t\t\t\t\tfor (OrderedBy orderedBy: orderedByList) {\n");
			sb.append("\t\t\t\t\t\tString dataField = orderedBy.getDataField();\n");
			sb.append("\t\t\t\t\t\tif (orderedBy.getIsAsc()) {\n");
			sb.append("\t\t\t\t\t\t\torderList.add(builder.asc(root.get(dataField)));\n");
			sb.append("\t\t\t\t\t\t} else {\n");
			sb.append("\t\t\t\t\t\t\torderList.add(builder.desc(root.get(dataField)));\n");
			sb.append("\t\t\t\t\t\t}\n");
			sb.append("\t\t\t\t\t} //endfor\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tif (orderList != null) {\n");
			sb.append("\t\t\t\tquery.orderBy(orderList);\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tEntityManager entityManager  =session.getEntityManagerFactory().createEntityManager();\n");
			sb.append("\t\t\ttypedQuery = entityManager.createQuery(query);\n");
			
			sb.append("\t\t\tif (" +daoObjectName+ "So.getFirstResult() != null) {\n");
			sb.append("\t\t\t\ttypedQuery.setFirstResult("+ daoObjectName+"So.getFirstResult()) != null){\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tif (" +daoObjectName+ "So.getLastResult() != null) {\n");
			sb.append("\t\t\t\ttypedQuery.setMaxResult("+ daoObjectName+"So.getLastResult()) != null){\n");
			sb.append("\t\t\t}\n");
			
			
			sb.append("\t\t} catch (Exception e) {\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".generateReadWhereStatement() - " + daoObjectName
					+ "So=\" + " + daoObjectName + "So, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn typedQuery;\n");
			sb.append("\t}// end generateReadWhereStatement\n");
			
			// ###############################
			// count function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic Long count(Object so) throws Exception{\n");
			sb.append("\t\tQuery<Long> query = null;\n");
			sb.append("\t\tString whereSql = null;\n");
			sb.append("\t\tLong count = null;\n");
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tif (so instanceof " + daoClassName + "So == false) {\n");
			sb.append("\t\t\t\tthrow new Exception(\"so is not an instanceof " + daoClassName + "So\");\n");
			sb.append("\t\t\t}\n");

			sb.append("\t\t\t" + daoClassName + "So " + daoObjectName + "So = (" + daoClassName + "So) so;\n");

			sb.append("\t\t\twhereSql = generateReadWhereStatement(" + daoObjectName + "So);\n");

			
			sb.append("\t\t\tgetSession();\n");
			
			sb.append("\t\t\tif (!this.closeSessionFinally) {\n");
			sb.append("\t\t\t\tif (this.transaction == null) {\n");
			sb.append("\t\t\t\t\tthis.transaction = this.session.getTransaction();\n");
			sb.append("\t\t\t\t\tTransactionStatus transactionStatus = transaction.getStatus();\n");
			sb.append("\t\t\t\t\tthis.transaction.begin();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			// pcount
			sb.append("\t\t\tint pcount = 1;\n");
			sb.append("\t\t\tquery = session.createQuery(SELECT_COUNT_SQL + whereSql);\n");

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
				
				
				sb.append("\t\t\tif(" + daoObjectName + "So.get" + upperPropertiesFormat);
				sb.append("() != null){\n");

				sb.append("\t\t\t\tquery.setParameter(\"" + javaPropertiesFormat + "\", " + daoObjectName + "So.get" + upperPropertiesFormat + "());\n");

//				sb.append("());\n");
//				sb.append("\t\t\t\tpcount++;\n");
				sb.append("\t\t\t}\n");
			}
			sb.append("\t\t\tObject value = query.uniqueResult();\n");
			sb.append("\t\t\tcount = (Long) value;\n");
			
			sb.append("\t\t\tif (!this.closeSessionFinally){\n");
			sb.append("\t\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\t\ttransaction.commit();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");

			sb.append("\t\t}\n");
			sb.append("\t\tcatch (Exception e){\n");
			sb.append("\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\tthis.transaction.rollback();\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".count() - so=\" + so, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");
			sb.append("\t\tfinally {\n");

			
			sb.append("\t\t\treturnSession(session);\n");
			
			sb.append("\t\t}\n");
			sb.append("\t\treturn count;\n");
			sb.append("\t} // end select count function\n");
			
			// ###############################
			// read function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic List<" + daoClassName + eoSuffix + "> " + "read(Object so) throws Exception{\n");
			sb.append("\t\tList<" + daoClassName + eoSuffix + "> " + daoObjectName + eoSuffix + "List = null;\n");
			sb.append("\t\t" + daoClassName + "So " + daoObjectName + "So = null;\n");

			
			sb.append("\t\ttry{\n");
			sb.append("\t\t\tif (so instanceof " + daoClassName + "So == false) {\n");
			sb.append("\t\t\t\tthrow new Exception(\"so is not instance of " + daoClassName + "So\");\n");
			sb.append("\t\t\t} else {\n");
			sb.append("\t\t\t\t" + daoObjectName +"So = (" + daoClassName + "So) so;\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tgetSession();\n");
			
			
			sb.append("\t\t\tif (!this.closeSessionFinally) {\n");
			sb.append("\t\t\t\tif (this.transaction == null) {\n");
			sb.append("\t\t\t\t\tthis.transaction = this.session.getTransaction();\n");
			sb.append("\t\t\t\t\tTransactionStatus transactionStatus = transaction.getStatus();\n");
			sb.append("\t\t\t\t\tthis.transaction.begin();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			
			sb.append("\t\t\tTypedQuery<" + daoClassName + "Eo> q = " + "generateQuery(" + daoObjectName + "So);\n");
			sb.append("\t\t\t" + daoObjectName + "EoList = " + "q.getResultList();\n");
			
			sb.append("\t\t\tif (!this.closeSessionFinally){\n");
			sb.append("\t\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\t\ttransaction.commit();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}catch (Exception e){\n");
			sb.append("\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\tthis.transaction.rollback();\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".read() - so=\" + so, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\treturnSession(session);\n");
			sb.append("\t\t}\n");
			sb.append("\t\treturn " + daoObjectName + eoSuffix + "List;\n");
			sb.append("\t} // end select function\n");
				
			// ###############################
			// create function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic void " + "create(" + daoClassName +  eoSuffix + " eo) throws Exception{\n");


			sb.append("\t\ttry{\n");

			sb.append("\t\t\tgetSession();\n");
			
			
			sb.append("\t\t\tif (!this.closeSessionFinally) {\n");
			sb.append("\t\t\t\tif (this.transaction == null) {\n");
			sb.append("\t\t\t\t\tthis.transaction = this.session.getTransaction();\n");
			sb.append("\t\t\t\t\tTransactionStatus transactionStatus = transaction.getStatus();\n");
			sb.append("\t\t\t\t\tthis.transaction.begin();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tsession.save(eo);\n");
			
			sb.append("\t\t\tif (!this.closeSessionFinally){\n");
			sb.append("\t\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\t\ttransaction.commit();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}catch (Exception e){\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".create() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthis.transaction.rollback();\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif (this.transaction != null) {\n");
			sb.append("\t\t\t\tthis.transaction = null;\n");

			sb.append("\t\t\t}\n");
			sb.append("\t\t\treturnSession(session);\n");
			sb.append("\t\t}\n");
			sb.append("\t} // end create function\n");
			
			// ###############################
			// update function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic void " + "update(" + daoClassName +  eoSuffix + " eo) throws Exception{\n");


			sb.append("\t\ttry{\n");

			sb.append("\t\t\tgetSession();\n");
			
			
			sb.append("\t\t\tif (!this.closeSessionFinally) {\n");
			sb.append("\t\t\t\tif (this.transaction == null) {\n");
			sb.append("\t\t\t\t\tthis.transaction = this.session.getTransaction();\n");
			sb.append("\t\t\t\t\tTransactionStatus transactionStatus = transaction.getStatus();\n");
			sb.append("\t\t\t\t\tthis.transaction.begin();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");

			sb.append("\t\t\tsession.update(eo);\n");
			
			sb.append("\t\t\tif (!this.closeSessionFinally){\n");
			sb.append("\t\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\t\ttransaction.commit();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}catch (Exception e){\n");
			sb.append("\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\tthis.transaction.rollback();\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".update() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif (this.transaction != null) {\n");
			sb.append("\t\t\t\tthis.transaction = null;\n");

			sb.append("\t\t\t}\n");
			sb.append("\t\t\treturnSession(session);\n");
			sb.append("\t\t}\n");

			sb.append("\t} // end update function\n");
			
			
			// ###############################
			// delete function
			// ###############################
			sb.append("\t@Override\n");
			sb.append("\tpublic void " + "delete(" + daoClassName +  eoSuffix + " eo) throws Exception{\n");


			sb.append("\t\ttry{\n");

			sb.append("\t\t\tgetSession();\n");
			
			
			sb.append("\t\t\tif (!this.closeSessionFinally) {\n");
			sb.append("\t\t\t\tif (this.transaction == null) {\n");
			sb.append("\t\t\t\t\tthis.transaction = this.session.getTransaction();\n");
			sb.append("\t\t\t\t\tTransactionStatus transactionStatus = transaction.getStatus();\n");
			sb.append("\t\t\t\t\tthis.transaction.begin();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t\tsession.delete(eo);\n");
			
			sb.append("\t\t\tif (!this.closeSessionFinally){\n");
			sb.append("\t\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\t\ttransaction.commit();\n");
			sb.append("\t\t\t\t}\n");
			sb.append("\t\t\t}\n");
			
			sb.append("\t\t}catch (Exception e){\n");
			sb.append("\t\t\tif (this.transaction != null){\n");
			sb.append("\t\t\t\tthis.transaction.rollback();\n");
			sb.append("\t\t\t}\n");
			sb.append("\t\t\tlogger.error(getClassName() + \".delete() - eo=\" + eo, e);\n");
			sb.append("\t\t\tthrow e;\n");
			sb.append("\t\t} // end try ... catch\n");			
			sb.append("\t\tfinally {\n");
			sb.append("\t\t\tif (this.transaction != null) {\n");
			sb.append("\t\t\t\tthis.transaction = null;\n");

			sb.append("\t\t\t}\n");
			sb.append("\t\t\treturnSession(session);\n");
			sb.append("\t\t}\n");

			sb.append("\t} // end delete function\n");
			
			// ########## end class ##############################
			sb.append("} //end class\n");
			
			if (compressFileDto != null) {
				
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				pw = new PrintWriter(byteArrayOutputStream);
				pw.write(sb.toString());
				
				compressFileDto.setFileName(daoClassName + daoSuffix + ".java");
				compressFileDto.setByteArrayOutputStream(byteArrayOutputStream);
			} else {
				fstream = new FileWriter(daoFile);
				out = new BufferedWriter(fstream);
								out.write(sb.toString());
			}

			// ################################################## end writing
			// file

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			logger.error(this.getClass() + ".generateDao() - ", e);
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
		  System.out.println("Dao is generated. : " + daoClassName + "Dao.java");		
	} // end generateDao()

//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		OrmDaoGenerateMgr daoGenerateMgr = new OrmDaoGenerateMgr("LACCCDTL");
//		daoGenerateMgr.generateDao();
//	}

}
