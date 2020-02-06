package com.rabbitforever.generateJavaMVC.daos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rabbitforever.generateJavaMVC.bundles.DbProperties;
import com.rabbitforever.generateJavaMVC.commons.RConnection;
import com.rabbitforever.generateJavaMVC.factories.DbUtilsFactory;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;
import com.rabbitforever.generateJavaMVC.utils.DbUtils;

public class OracleDbDao {
	private final Logger logger = LogManager.getLogger(getClassName());
	private DbUtilsFactory dbUtilsFactory;
	private DbUtils dbUtils;
	private PropertiesFactory propertiesFactory;
	private DbProperties dbProperties;
	private String getClassName() {
		return this.getClass().getName();
	}
	public OracleDbDao() throws Exception
	{
		try {
			dbUtilsFactory = DbUtilsFactory.getInstanceOfDbUtilsFactory();
			dbUtils = dbUtilsFactory.getInstanceOfOracleDbUtils();
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			dbProperties = propertiesFactory.getInstanceOfDbProperties();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<MetaDataField> getMetaDataList(String _database) throws Exception
	{
		Connection conn = null;
		List<MetaDataField> metaDataFieldList = new ArrayList<MetaDataField>();

		try
		{
			String user = dbProperties.getUsername();
			String schema = dbProperties.getSchema();
			conn = dbUtils.getConnection();
			
		    ResultSet rsColumns = null;
		    DatabaseMetaData meta = conn.getMetaData();
		    
//		    ResultSet rsTables = meta.getTables(null,null,null,null);
//		    while (rsTables.next()) {
//		    	String tableName = rsTables.getString("TABLE_NAME");
//		    	String tbl = tableName;
//		    	if (tbl.equals("BIL_CHARGE_REQUEST")) {
//		    		String helo = tbl;
//		    		String a = helo;
//		    	}
//		    }
		    rsColumns = meta.getColumns(null, null, _database, null);
		    
		    while (rsColumns.next()) {
				MetaDataField metaDataField = new MetaDataField();
				metaDataField.setColumnName(rsColumns.getString("COLUMN_NAME"));
				metaDataField.setTypeName(rsColumns.getString("TYPE_NAME"));
				metaDataField.setColumnSize(rsColumns.getInt("COLUMN_SIZE"));
				metaDataField.setNullable(rsColumns.getInt("NULLABLE"));
				metaDataField.setOrdinalPosition(rsColumns.getInt("ORDINAL_POSITION"));

			    metaDataFieldList.add(metaDataField);		      
		    }

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		} finally {
				try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				logger.error(getClassName() + ".getMetaDataList() ", e);
				throw e;
			}
		}
		
		
		return metaDataFieldList;
	}
	public List<String> getTableNameList() throws Exception {
		Connection conn = null;
		List <String> tableNameList = null;
		try {
			String user = dbProperties.getUsername();
			String schema = dbProperties.getSchema();
			conn = dbUtils.getConnection();
			DatabaseMetaData md = conn.getMetaData();

			ResultSet rs = md.getTables(null, null, "%", new String[]{"TABLE","VIEW"});
			while (rs.next()) {
				if (tableNameList == null) {
					tableNameList = new ArrayList<String>();
				}
				String tableName = rs.getString("TABLE_NAME");
				if (!tableName.contains("$")) {
					tableNameList.add(tableName);
				}
			}
		} catch (Exception e) {
			logger.error(getClassName() + ".getTableNameList() ", e);
			throw e;
		}
		return tableNameList;
	}
	public void getColumnName(String _database) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = RConnection.getInstanceOfConnection("ORACLE");
			
		    ResultSet rsColumns = null;
		    DatabaseMetaData meta = conn.getMetaData();
		    rsColumns = meta.getColumns(null, null, _database, null);
		    
		    while (rsColumns.next()) {
		      String columnName = rsColumns.getString("COLUMN_NAME");
		      System.out.println("column name=" + columnName);
		      String columnType = rsColumns.getString("TYPE_NAME");
		      System.out.println("type:" + columnType);
		      int size = rsColumns.getInt("COLUMN_SIZE");
		      System.out.println("size:" + size);
		      int nullable = rsColumns.getInt("NULLABLE");
		      if (nullable == DatabaseMetaData.columnNullable) {
		        System.out.println("nullable true");
		      } else {
		        System.out.println("nullable false");
		      }
		      int position = rsColumns.getInt("ORDINAL_POSITION");
		      System.out.println("position:" + position);
		      
		    }				
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}finally {
			try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			logger.error(getClassName() + ".getMetaDataList() ", e);
			throw e;
		}
	}
	
	}
	public void testConnection() throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {

		    conn = RConnection.getInstanceOfConnection();	
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM ems_users");

		    // or alternatively, if you don't know ahead of time that
		    // the query will be a SELECT...

//		    if (stmt.execute("SELECT * FROM ems_users")) {
//		        rs = stmt.getResultSet();
//		        int test = 0;
//		    }
		    
		    rs = stmt.getResultSet();
		    
	        while (rs.next()) {
	        	int sid = rs.getInt("Sid");
	            String userNameEn =
	                rs.getString("User_Name_En");
	            
//	            int supplierID = rs.getInt("SUP_ID");
//	            float price = rs.getFloat("PRICE");
//	            int sales = rs.getInt("SALES");
//	            int total = rs.getInt("TOTAL");
	            System.out.println(
	                sid + "\t" + userNameEn);
	        }		    
		    

		    // Now do something with the ResultSet ....
		}
		catch (SQLException ex){
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
			logger.error(getClassName() + ".getMetaDataList() ", ex);
			throw ex;
		}
		finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}		
	}
} // end classs
