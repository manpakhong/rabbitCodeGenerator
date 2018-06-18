package com.rabbitforever.generateJavaMVC.pgms;

import com.rabbitforever.generateJavaMVC.services.DaoGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.EoGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.FileArchieveMgr;
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
				argTableName = "gbl_about,gbl_authorities,gbl_big_small,gbl_role,gbl_user";
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
				voGeneratorMgr.generateVo();
				
				SoGenerateMgr soGeneratorMgr = new SoGenerateMgr(temp[i]);
				soGeneratorMgr.generateVo();
				
	
				DaoGenerateMgr daoGeneratorMgr = new DaoGenerateMgr(temp[i]);
				daoGeneratorMgr.generateDao();
//				
//				IDaoGenerateMgr idaoGeneratorMgr = new IDaoGenerateMgr(temp[i]);
//				idaoGeneratorMgr.generateDao();	
//				
//				
				ServiceGenerateMgr svrGeneratorMgr = new ServiceGenerateMgr(temp[i]);
				svrGeneratorMgr.generateService();
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
