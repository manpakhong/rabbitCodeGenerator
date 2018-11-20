package com.rabbitforever.generateJavaMVC.pgms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rabbitforever.generateJavaMVC.services.BundleBuilderGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.BundleGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.BundleUtilsGenerateMgr;
import com.rabbitforever.generateJavaMVC.services.FileArchieveMgr;
import com.rabbitforever.generateJavaMVC.utils.FileUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

public class PropGenMainConsole {

	public static void main(String[] args) {
		try {
			FileArchieveMgr fileArchieveMgr = new FileArchieveMgr();
			fileArchieveMgr.maintainFileArchieve();
			
			FileUtils fileUtils = new FileUtils();
			String path = new File(".").getCanonicalPath();
			final String FILE_NAME_ROOT = "C:/svn/bill_engine/trunk/java/release2/billing-patient-charges/properties";
			String fileNamesString = null;
			List<String> fileNameStringList = null;
			boolean isUsingFileNameRoot = false;
			if (args.length < 1) {
				if (!isUsingFileNameRoot) {
					fileNamesString = "patient-charges.properties,db-cms.properties,db-datapool.properties";
				} else {
					List<File> fileList = new ArrayList<File>();
					fileUtils.traverseDir(FILE_NAME_ROOT, fileList);
					fileNameStringList = new ArrayList<String>();
					for (File file: fileList){
						String fileNameStringLoop = file.getName();
						fileNameStringList.add(fileNameStringLoop);
					}
					
				}
			} else {
				fileNamesString = args[0];
			}

			if (fileNamesString == null) {
				fileNamesString = "";
				int count = 0;
				for (String fileName: fileNameStringList){
					if (count > 0){
						fileNamesString += ",";
					}
					fileNamesString += fileName;
					count++;
				}
			}

			String[] fileNameStringArray = fileNamesString.split(",");

			fileNameStringList = Arrays.asList(fileNameStringArray);
			for (String fileName : fileNameStringList) {
				String fullPath = FILE_NAME_ROOT  + "/" + fileName;
				List<String> lineList = fileUtils.readFromFile(fullPath);
				BundleGenerateMgr bundleGenerateMgr = new BundleGenerateMgr(lineList, fileName);
				bundleGenerateMgr.generateBundle();
				
				BundleBuilderGenerateMgr bundleBuilderGenerateMgr = new BundleBuilderGenerateMgr(lineList, fileName);
				bundleBuilderGenerateMgr.generateBuilder();	
				
				BundleUtilsGenerateMgr bundleUtilsGenerateMgr = new BundleUtilsGenerateMgr(lineList, fileName);
				bundleUtilsGenerateMgr.generateBundleUtils();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
