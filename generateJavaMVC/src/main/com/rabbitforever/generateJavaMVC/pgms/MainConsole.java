package com.rabbitforever.generateJavaMVC.pgms;

import com.rabbitforever.generateJavaMVC.services.DaoGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.EoGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.FileArchieveMgr;
import com.rabbitforever.generateJavaMVC.services.OrmDaoGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.OrmServiceGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.ServiceGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.SoGenerateMgr;

public class MainConsole {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{

			String argTableName = "";
			if (args.length < 1)
			{
				argTableName = "ORD_ORDER_ITEM_CES,OP_CODE_DETAIL,OP_CODE,OP_COMPONENT,OP_COMPONENT_CONTROL";
//				argTableName = "OR_ANAESTHESIA,OR_ANAEST_OPERATION,BIL_PAYMENT_METHOD,BIL_PATIENTPKG_USED,ADM_PACKAGE,ADM_PACKAGE_DETAILS,ARC_ITEMPRICE,ORD_ORDER_ITEM_CES,ARC_ITMMAST,PAC_BEDTYPE,ADM_PAYOR_DETAILS,PAC_ROOM,PAC_BED,BIL_CHARGE_REQUEST_ERR,BIL_EPISODE_BILLING_SUMMARY,BIL_PATIENT_CHARGES,DL_PREGDELBABY,PAC_TRANSFER_STATUS,BIL_CODE,BIL_CODE_DETAIL,CT_LOC,PAC_TRANSFER_REASON,ARC_BILLGRP,ARC_BILLSUB,OEC_ORDERSTATUS,ADM_EPISODE,ORD_ORDER_ITEM_CES,ORD_ORDER_CES,ARC_ITMMAST,ADM_TRANSACTION,BIL_PATIENT_PKG_USED,BIL_PATIENT_PKG_ORDSET_USED,BIL_CHARGE_REQUEST";
			}
			else
			{
				argTableName = args[0];
			}
			
			String delimiter = ",";
			String temp[];
			temp = argTableName.split(delimiter);
			
			for(int i=0; i < temp.length; i++)
			{
				FileArchieveMgr fileArchieveMgr = new FileArchieveMgr();
				fileArchieveMgr.maintainFileArchieve();
				
				EoGenerateMgr voGeneratorMgr = new EoGenerateMgr(temp[i]);
				voGeneratorMgr.generateEo();
				
				SoGenerateMgr soGeneratorMgr = new SoGenerateMgr(temp[i]);
				soGeneratorMgr.generateSo();
				
	
				DaoGenerateMgr daoGeneratorMgr = new DaoGenerateMgr(temp[i]);
				daoGeneratorMgr.generateDao();
				
				ServiceGenerateMgr svrGeneratorMgr = new ServiceGenerateMgr(temp[i]);
				svrGeneratorMgr.generateService();
//				
				OrmDaoGenerateMgr ormDaoGeneratorMgr = new OrmDaoGenerateMgr(temp[i]);
				ormDaoGeneratorMgr.generateDao();
				
//				IDaoGenerateMgr idaoGeneratorMgr = new IDaoGenerateMgr(temp[i]);
//				idaoGeneratorMgr.generateDao();	
//				
				OrmServiceGenerateMgr ormServiceGenerateMgr = new OrmServiceGenerateMgr(temp[i]);
				ormServiceGenerateMgr.generateService();

//				
//				IServiceGenerateMgr isvrGeneratorMgr = new IServiceGenerateMgr(temp[i]);
//				isvrGeneratorMgr.generateService();
			}
			
//			MySqlDbDao mysqlDbDao = new MySqlDbDao();
//			mysqlDbDao.getColumnName(argTableName);			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
