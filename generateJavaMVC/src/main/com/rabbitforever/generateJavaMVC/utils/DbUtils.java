package com.rabbitforever.generateJavaMVC.utils;

import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitforever.generateJavaMVC.bundles.DbProperties;

public abstract class DbUtils {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className = this.getClass().getName();
	// public static final String DB_TYPE_MYSQL = "mysql";
	// public static final String DB_TYPE_DB2 = "db2";
	protected DbProperties properties;
	
	public Connection getConnection() throws Exception {
		Connection jdbcConnection = null;
		try {
			String connectionString = properties.getConnectString();
			String userName = properties.getUsername();
			String password = properties.getPassword();
			String schema = properties.getSchema();
			Class.forName(properties.getClassForName());
			jdbcConnection = DriverManager.getConnection(connectionString, userName, password);
			// jdbcConnection.setSchema(schema);
		} catch (Exception e) {
			logger.error(className + ".getConnection() - ", e);
			throw e;
		}
		return jdbcConnection;
	}

	// public IDatabaseConnection getWriteXmlConnection() throws Exception {
	// IDatabaseConnection dbConnection = getWriteXmlConnection(DB_TYPE_DB2);
	// return dbConnection;
	// }
	//
	// public IDatabaseConnection getWriteXmlConnection(String dbType) throws
	// Exception {
	// IDatabaseConnection dbConnection = null;
	// if (dbType.equals(DB_TYPE_DB2)){
	// dbConnection = getDataBaseConnectionNoSchema();
	// }
	// if (dbType.equals(DB_TYPE_MYSQL)){
	// dbConnection = getDataBaseConnectionNoSchema();
	// }
	// return dbConnection;
	// }
	//
	// public IDatabaseConnection getLoadXmlConnection() throws Exception {
	// IDatabaseConnection dbConnection = getLoadXmlConnection(DB_TYPE_DB2);
	// return dbConnection;
	// }
	// public IDatabaseConnection getLoadXmlConnection(String dbType) throws
	// Exception {
	// IDatabaseConnection dbConnection = null;
	// if (dbType.equals(DB_TYPE_DB2)){
	// dbConnection = getDataBaseConnectionWithSchema();
	// }
	// if (dbType.equals(DB_TYPE_MYSQL)){
	// dbConnection = getDataBaseConnectionNoSchema();
	// }
	// return dbConnection;
	// }

	public abstract IDatabaseConnection getDataBaseConnectionWithSchema() throws Exception;

	public abstract IDatabaseConnection getDataBaseConnectionNoSchema() throws Exception;

	public abstract IDatabaseTester getJdbcDatabaseTester() throws Exception;
}
