package hk.ebsl.mfms.utility.writer;

import hk.ebsl.mfms.utility.StringUtil;
import hk.ebsl.mfms.utility.function.KeyMapFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter 
{
	private static Logger logger = Logger.getLogger(ExcelWriter.class.getName());
	
	private OutputStream out;
	private XSSFWorkbook xssfWorkbook;
	
	private ResourceBundle resourceFactory;
	
	public ExcelWriter(OutputStream out) 
		throws IOException
	{
		logger.info("Bound ExcelWriter to an OutputStream");
		
		this.out = out;
		
		xssfWorkbook = new XSSFWorkbook();
	}
	
	
	public ExcelWriter(String filePath) 
		throws IOException
	{
		logger.info("Bound ExcelWriter to ["+filePath+"]");
	
		out = new FileOutputStream(new File(filePath));
		xssfWorkbook = new XSSFWorkbook();
		
	}
	
	/**
	 * This method writes the object to a sheet in the excel with fields specified in column headings.
	 * 
	 * @param sheetNum
	 * @param rowNum
	 * @param object
	 * @param columnHeadings
	 * @throws Exception
	 */
	public void write(int sheetNum, String sheetName, int rowNum, Object object, List<ColumnDisplay> columnHeadings, boolean asString)
		throws Exception
	{
		Object[] values = ColumnDisplay.getColumnFields(resourceFactory, object, columnHeadings, asString);
		write(sheetNum, sheetName, rowNum, values);
	}

	/**
	 * This method writes the String array to a sheet in the excel.
	 * 
	 * @param sheetNum
	 * @param rowNum
	 * @param values
	 * @throws Exception
	 */
	public void write(int sheetNum, String sheetName, int rowNum, Object[] values)
		throws Exception
	{
		createSheet(sheetNum, sheetName);
		XSSFRow row = xssfWorkbook.getSheetAt(sheetNum).createRow(rowNum);

		for (int i=0; i<values.length; i++)
		{
			XSSFCell cell = row.createCell((short)i);
			if (values[i] == null) cell.setCellValue("");
			else
			{
				Class classes = values[i].getClass();
				if (classes == int.class || classes == Integer.class)
				{
					cell.setCellValue((Integer)values[i]);
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					
				} else if (classes == double.class || classes == Double.class)
				{
					cell.setCellValue(StringUtil.toString((Double)values[i]));
//					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//					
//					HSSFDataFormat format = hssfWorkbook.createDataFormat();
//					HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
//					cellStyle.setDataFormat(format.getFormat(StringUtil.getNumberFormat()));
//				    cell.setCellStyle(cellStyle);
				
				} else if (classes == long.class || classes == Long.class)
				{
					cell.setCellValue((Long)values[i]);
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					
				} else if (classes == Timestamp.class)
				{
					cell.setCellValue(StringUtil.toString((Timestamp)values[i]));
//					cell.setCellValue((Timestamp)values[i]);
//
//					HSSFDataFormat format = hssfWorkbook.createDataFormat();
//					HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
//					cellStyle.setDataFormat(format.getFormat(DateUtil.getTimeFormatter().toPattern()));
//				    cell.setCellStyle(cellStyle);
				    
				} else if (classes == Date.class)
				{
					cell.setCellValue(StringUtil.toString((Date)values[i]));
					
//					cell.setCellValue((Date)values[i]);
//				
//					HSSFDataFormat format = hssfWorkbook.createDataFormat();
//					HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
//					cellStyle.setDataFormat(format.getFormat(DateUtil.getDateFormatter().toPattern()));
//				    cell.setCellStyle(cellStyle);
				    
				} else
				{
					cell.setCellValue(StringUtil.toString(values[i]));
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
		}
	}
	
	/**
	 * This method writes the objects to a sheet in the excel with fields specified in column headings.
	 * 
	 * @param showTitle
	 * @param sheetNum
	 * @param sheetName
	 * @param objClass
	 * @param list
	 * @param columnHeadings
	 * @param asString
	 * @throws Exception
	 */
	
	public void write(boolean showTitle, int sheetNum, String sheetName, Class objClass, List list, List<ColumnDisplay> columnHeadings, boolean asString) 
		throws Exception
	{
		if (logger.isDebugEnabled()) logger.debug("Writing to sheet ["+sheetNum+"("+sheetName+")]; list.size="+list.size());
		
		createSheet(sheetNum, sheetName);
		
		int prefix = 0;
		if (showTitle) writeTitle(sheetNum, prefix++, columnHeadings);
		
		for (int i=0; i<list.size(); i++) write(sheetNum, sheetName, i+prefix, list.get(i), columnHeadings, asString);
	}
	
	/**
	 * This method writes the title row to sheet sheetNum at row rowNum.
	 *   
	 * @param sheetNum
	 * @param rowNum
	 * @param columnHeadings
	 */
	private void writeTitle(int sheetNum, int rowNum, List<ColumnDisplay> columnHeadings)
	{
		XSSFRow row = xssfWorkbook.getSheetAt(sheetNum).createRow(rowNum);
		String[] values = ColumnDisplay.getHeaders(resourceFactory, columnHeadings);
		for (int i=0; i<values.length; i++)
		{
			XSSFCell cell = row.createCell((short)i);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(values[i]);
			
			XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
			XSSFFont font = xssfWorkbook.createFont();
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);
		    cell.setCellStyle(cellStyle);
		}
	}
	
	/**
	 * This method checks if the sheetNum exists in the workbook. If no, it will create one.
	 * It will then set the sheet name to the sheet if sheetName != null.
	 * 
	 * @param sheetNum
	 * @param sheetName
	 */
	private void createSheet(int sheetNum, String sheetName)
	{
		while (xssfWorkbook.getNumberOfSheets() <= sheetNum) xssfWorkbook.createSheet(sheetName);
		if (sheetName != null) xssfWorkbook.setSheetName(sheetNum, sheetName);
	}
	
	/**
	 * @throws IOException
	 */
	public void flush() 
		throws IOException
	{
		xssfWorkbook.write(out);
		out.flush();
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void close() 
		throws IOException
	{
		flush();
		out.close();
	}


	public ResourceBundle getResourceFactory() {
		return resourceFactory;
	}


	public void setResourceFactory(ResourceBundle resourceFactory) {
		this.resourceFactory = resourceFactory;
	}

	/**
	 * Main.
	 * 
	 * @param argv
	 */
	public static void main(String[] argv)
	{
		try 
		{
			List<ColumnDisplay> columnHeadings = new ArrayList<ColumnDisplay>();
			
			ColumnDisplay c = new ColumnDisplay();
			c.setProperty("name");
			c.setHeadingKey("Name");
			columnHeadings.add(c);
			
			c = new ColumnDisplay();
			c.setProperty("count");
			c.setHeadingKey("Mark");
			columnHeadings.add(c);
			
			c = new ColumnDisplay();
			c.setProperty("status");
			c.setHeadingKey("Status");
			KeyMapFunction f = new KeyMapFunction();
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("A", "Active");
			map.put("I", "Inactive");
			f.setMap(map);
			c.setFunction(f);
			columnHeadings.add(c);
			
//			List<TestObj> list = new ArrayList<TestObj>();
//			list.add(new TestObj("John, '123' Lemon", 98, "A"));
//			list.add(new TestObj("Mary", 50, "I"));
//			list.add(new TestObj("Susan", 78, "A"));
//			writer.write(true, 0, list, columnHeadings);
//			writer.close();
//			
//			ExcelReader reader = new ExcelReader("c:/out.xls");
//			
//			List results = reader.read(0, 1, -1, TestObj.class, columnHeadings);
//			for (int i=0; i<results.size(); i++) System.out.println(results.get(i).toString());
//			reader.close();
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
