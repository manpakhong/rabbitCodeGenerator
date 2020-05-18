package com.rabbitforever.generateJavaMVC.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String className = this.getClass().getName();

	
	
	public FileUtils() throws Exception{


	}


	public void traverseDir(String fileInString, List<File> fileList){
		try {
			File file = new File(fileInString);
			if (file != null){
				traverseDir(file, fileList);
			}
		} catch (Exception e) {
			logger.error(className + ".traverseDir()", e);
			throw e;
		} finally{

		}
	}
	public void traverseDir(File dir, List<File> fileList){
		try {
			for (File fileEntry: dir.listFiles()){
		        if (fileEntry.isDirectory()) {
		        	traverseDir(fileEntry, fileList);
		        } else {
		            fileList.add(fileEntry);
		            
		        }
			}
		} catch (Exception e) {
			logger.error(className + ".traverseDir()", e);
			throw e;
		} finally{

		}
	}

	
	public void writeText2File(String content, String fileName) throws Exception{
		File file = null;
		OutputStreamWriter outputStreamWriter = null;
		FileOutputStream fileOutputStream = null;
	
		BufferedWriter bw = null;
		try {
			
			file = new File(fileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			bw = new BufferedWriter(outputStreamWriter);
			bw.write(content);
			bw.close();


		} catch (Exception e) {
			logger.error(className + ".writeText2File() - content=" + content + ",fileName=" + fileName, e);
			throw e;
		} finally{
			if (file != null){
				file = null;
			}
			if (fileOutputStream != null){
				fileOutputStream.close();
				fileOutputStream = null;
			}
			if (outputStreamWriter != null){
				outputStreamWriter.close();
				outputStreamWriter = null;
			}

		}
	}
	
	public String getDirPath(File file) throws Exception{
		String dirPath = null;
		try {
			File parentDir = file.getParentFile();
			String parentDirString = parentDir.getParent();
			dirPath = parentDirString;
		} catch (Exception e) {
        	logger.error(className + ".getDirPath() - filePath=" + file.getAbsolutePath(), e);
        	throw e;
		}
		return dirPath;
	}
	public void copyFile(Path sourceFilePath, Path targetFilePath) throws Exception {

		try {
			copyFile(sourceFilePath, targetFilePath, null);
		} catch (Exception e) {
        	logger.error(className + ".copyFile() - ",e);

        	throw e;
		} finally{

		}
	}
	public void copyFile(Path sourceFilePath, Path targetFilePath, List<File> exceptionFileList) throws Exception {
		File targetFile = null;
		try {
			targetFile = targetFilePath.toFile();
			String parentPath = getDirPath(targetFile);
			createDirectoryIfNotExisted(parentPath);
			Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
        	logger.error(className + ".copyFile() - ",e);
        	if (exceptionFileList != null){
        		exceptionFileList.add(targetFile);
        	}
//        	throw e;
        	
		} finally{
			if (targetFile != null){
				targetFile = null;
			}
		}
	}
	public String getCurrentDriveLetter() throws Exception{
		String driveLetter = null;
		try {
			File currentPath = new File(".");
			String path = currentPath.getCanonicalPath();
			
			String patternString = "[a-zA-Z]\\:";
			List<String> matchStringList = CommonUtils.regMatch(path, patternString);
			if (matchStringList != null){
				driveLetter = matchStringList.get(0);
			}
			
		} catch (Exception e) {
        	logger.error(className + ".getCurrentDrive() - ",e);
        	throw e;
		}
		return driveLetter;
	}
	public boolean isDirExisted(String directoryName) throws Exception {
		boolean isDirExisted = false;
		File theDir = null;
		try {
			  theDir = new File(directoryName);
			  
			  if (theDir.exists()){
				  isDirExisted = true;
			  }

		} catch (Exception e) {
			logger.error(className + ".isDirExisted() - directoryName=" + directoryName, e);
		} finally{
			if (theDir != null){
				theDir = null;
			}
		}
		return isDirExisted;
	}
	
	
	public void createDirectoryIfNotExisted(String directoryName) throws Exception {
		File theDir = null;
		try {
			  theDir = new File(directoryName);
			  
			  if (!theDir.exists()){
			    theDir.mkdirs();
			  }


		} catch (Exception e) {
			logger.error(className + ".createDirectoryIfNotExisted() - directoryName=" + directoryName, e);
			throw e;
		} finally{
			if (theDir != null){
				theDir = null;
			}
		}
	}
	
	public List<String> readFromFile(String fileName) throws Exception {
		BufferedReader br = null;
		List<String> stringList = new ArrayList<String>();
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) {
				stringList.add(sCurrentLine);
			}

		} catch (IOException e) {
			logger.error(className + ".readFromFile() - fileName=" + fileName, e);
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return stringList;
	}
}
