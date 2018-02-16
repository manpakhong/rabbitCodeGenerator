package com.rabbitforever.generateJavaMVC.utils;

import java.sql.Connection;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitforever.generateJavaMVC.bundles.DbProperties;
import com.rabbitforever.generateJavaMVC.factories.PropertiesFactory;

public class MySqlDbUtils extends DbUtils{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className = this.getClass().getName();
	private PropertiesFactory propertiesFactory;
	public MySqlDbUtils() throws Exception{
		try {
			propertiesFactory = PropertiesFactory.getInstanceOfPropertiesFactory();
			properties = (DbProperties) propertiesFactory.getInstanceOfMySqlDbProperties();	
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public IDatabaseConnection getDataBaseConnectionWithSchema() throws Exception {
		String schema = properties.getSchema();
		Connection jdbcConnection = getConnection();

		IDatabaseConnection dataBaseConnection = new DatabaseConnection(jdbcConnection, schema);

		DatabaseConfig dbConfig = dataBaseConnection.getConfig();

		// added this line to get rid of the warning
		dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
		return dataBaseConnection;
	}

	public IDatabaseConnection getDataBaseConnectionNoSchema() throws Exception {
		Connection jdbcConnection = getConnection();
		IDatabaseConnection dataBaseConnection = new DatabaseConnection(jdbcConnection);

		DatabaseConfig dbConfig = dataBaseConnection.getConfig();

		// added this line to get rid of the warning
		dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

		return dataBaseConnection;
	}
	public IDatabaseTester getJdbcDatabaseTester() throws Exception {
		IDatabaseTester databaseTester = null;
		try {
			String schema = properties.getSchema();
			String classForName = properties.getClassForName();
			String username = properties.getUsername();
			String password = properties.getPassword();
			String connectionString = properties.getConnectString();
			databaseTester = new JdbcDatabaseTester(classForName, connectionString, username, password);
		} catch (Exception e) {
			logger.error(className + ".getJdbcDatabaseTester() - ", e);
			throw e;
		}
		return databaseTester;
	}
	
}
