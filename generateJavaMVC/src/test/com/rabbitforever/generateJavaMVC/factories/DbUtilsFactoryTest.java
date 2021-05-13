package com.rabbitforever.generateJavaMVC.factories;

import java.sql.Connection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rabbitforever.generateJavaMVC.utils.DbUtils;

public class DbUtilsFactoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstanceOfMySqlDbUtils() {
		try {
			DbUtilsFactory dbUtilsFactory= DbUtilsFactory.getInstanceOfDbUtilsFactory();
			DbUtils dbUtils = dbUtilsFactory.getInstanceOfMySqlDbUtils();
			Connection connection = dbUtils.getConnection();
			Assert.assertNotNull(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
