package com.rabbitforever.generateJavaMVC.services;

import java.util.List;

import com.rabbitforever.generateJavaMVC.daos.OracleDbDao;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;

public class OracleDbMgr extends DbMgr{
	private OracleDbDao oracleDbDao;
	public OracleDbMgr() throws Exception
	{
		try {
		oracleDbDao = new OracleDbDao();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	} // end constructor
	public List<MetaDataField> getMetaDataList(String _database)
	{
		return oracleDbDao.getMetaDataList(_database);
	} // end getMetaData()
} // end class
