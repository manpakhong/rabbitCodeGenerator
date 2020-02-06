package com.rabbitforever.generateJavaMVC.services;

import java.util.List;

import com.rabbitforever.generateJavaMVC.models.eos.MetaDataField;

public abstract class DbMgr {
	public abstract List<MetaDataField> getMetaDataList(String _database) throws Exception;
}
