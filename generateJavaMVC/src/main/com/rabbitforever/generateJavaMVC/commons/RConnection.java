package com.rabbitforever.generateJavaMVC.commons;

import java.sql.Connection;
import java.sql.DriverManager;

public class RConnection {
	
	private static Connection connection;
	
	public static Connection getInstanceOfConnection(String str)
	{
		try
		{
			if (str.equals("ORACLE"))
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				String url = "jdbc:oracle:thin:@192.168.1.108:1521:andb1";
				connection = DriverManager.getConnection(url, "jboss", "jbTest");
			}
			
		}
		catch (Exception ex){
			ex.printStackTrace();
		}

		return connection;
	}
	
	public static Connection getInstanceOfConnection()
	{
		try {
			
			// test driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			if (null == connection)
			{
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rabbit_epayment?" +
	                    "user=root&password=hongmdavem");

			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}

		return connection;
	} // end getInstanceOfConnection
} // end class
