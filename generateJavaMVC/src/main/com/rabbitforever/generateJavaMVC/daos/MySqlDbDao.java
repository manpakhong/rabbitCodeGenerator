package com.rabbitforever.generateJavaMVC.daos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rabbitforever.generateJavaMVC.commons.RConnection;
import com.rabbitforever.generateJavaMVC.factories.DbUtilsFactory;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;
import com.rabbitforever.generateJavaMVC.utils.DbUtils;

public class MySqlDbDao {
	private DbUtilsFactory dbUtilsFactory;
	private DbUtils dbUtils;
	public MySqlDbDao() throws Exception
	{
		try {
			dbUtilsFactory = DbUtilsFactory.getInstanceOfDbUtilsFactory();
			dbUtils = dbUtilsFactory.getInstanceOfMySqlDbUtils();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<MetaDataField> getMetaDataList(String _database)
	{
		Connection conn = null;
		List<MetaDataField> metaDataFieldList = new ArrayList<MetaDataField>();

		try
		{
			
			conn = dbUtils.getConnection();
			
		    ResultSet rsColumns = null;
		    DatabaseMetaData meta = conn.getMetaData();
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
		}finally {
			try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		
		return metaDataFieldList;
	}
	
	public void getColumnName(String _database)
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
			e.printStackTrace();
		}
	}
	
	}
	public void testConnection(){
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
