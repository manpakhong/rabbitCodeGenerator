package com.rabbitforever.generateJavaMVC.services;

import java.util.List;

import com.rabbitforever.generateJavaMVC.daos.MySqlDbDao;
import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;

public class MySqlDbMgr {
	private MySqlDbDao mysqlDbDao;
	public MySqlDbMgr() throws Exception
	{
		try {
			mysqlDbDao = new MySqlDbDao();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	} // end constructor
	public List<MetaDataField> getMetaDataList(String _database)
	{
		return mysqlDbDao.getMetaDataList(_database);
	} // end getMetaData()
} // end class
