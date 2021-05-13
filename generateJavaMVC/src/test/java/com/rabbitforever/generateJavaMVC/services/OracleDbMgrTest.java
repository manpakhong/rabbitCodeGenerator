package com.rabbitforever.generateJavaMVC.services;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class OracleDbMgrTest {

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
	public void testGetTableNameList() {
		try {
			OracleDbMgr oracleDbMgr = new OracleDbMgr();
			List<String> tableNameList = oracleDbMgr.getTableNameList();
			for (String tableName : tableNameList) {
				System.out.println(tableName);
			}
			Assert.assertTrue(tableNameList.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception!");
		}

	}

}
