package com.rabbitforever.generateJavaMVC.models.dtos;

import java.io.ByteArrayOutputStream;

public class CompressFileDto extends Dto {
	private String fileName;
	private ByteArrayOutputStream byteArrayOutputStream;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ByteArrayOutputStream getByteArrayOutputStream() {
		return byteArrayOutputStream;
	}
	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}
	
}
