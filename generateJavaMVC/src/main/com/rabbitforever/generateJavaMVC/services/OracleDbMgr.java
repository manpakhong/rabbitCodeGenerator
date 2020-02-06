package com.rabbitforever.generateJavaMVC.services;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rabbitforever.generateJavaMVC.commons.RConnection;
import com.rabbitforever.generateJavaMVC.daos.OracleDbDao;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;

public class OracleDbMgr extends DbMgr{
	private final Logger logger = LogManager.getLogger(getClassName());
	private OracleDbDao oracleDbDao;
	private String getClassName() {
		return this.getClass().getName();
	}
	public OracleDbMgr() throws Exception
	{
		try {
		oracleDbDao = new OracleDbDao();
		} catch (Exception e) {
			logger.error(getClassName() + ".OracleDbMgr() ", e);
			throw e;
		}
	} // end constructor
	
	public List<String> getTableNameList() throws Exception {

		List <String> tableNameList = null;
		try {
			tableNameList = oracleDbDao.getTableNameList();
		} catch (Exception e) {
			logger.error(getClassName() + ".getTableNameList() ", e);
			throw e;
		}
		return tableNameList;
	}
	public List<MetaDataField> getMetaDataList(String _database) throws Exception
	{
		List<MetaDataField> metaDataFieldList = null;
		try {
			metaDataFieldList = oracleDbDao.getMetaDataList(_database);

		} catch (Exception e) {
			logger.error(getClassName() + ".getMetaDataList() ", e);
			throw e;
		}
		return metaDataFieldList;
	} // end getMetaData()
} // end class
