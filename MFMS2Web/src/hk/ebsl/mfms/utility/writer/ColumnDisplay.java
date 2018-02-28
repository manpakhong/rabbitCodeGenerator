package hk.ebsl.mfms.utility.writer;

import hk.ebsl.mfms.utility.ObjectUtil;
import hk.ebsl.mfms.utility.StringUtil;
import hk.ebsl.mfms.utility.function.Function;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;


public class ColumnDisplay 
{
	private static final String _EQUALS = "=";
	private static final String _QUOTATION = "'";
	private static final String _COLON = ":";
	private static final String _COMMA = ",";
	private static final String _OBJECT = "%";
	
	/**
	 * The condition prefix. 
	 * The condition currently only applies to image path.
	 */
	private static final String CONDITION_PREFIX = "?";
	
	/**
	 * Defines the key of the column header
	 */
	private String headingKey;
	
	/**
	 * Defines the bean property associate to this column
	 */
	private String property;
	
	/**
	 * Defines the key to retrieve value from the map, if property
	 * resolved a map.
	 */
	private String mapKey;
	
	/**
	 * Default result if the map returns NULL with mapKey.
	 */
	private String mapIfNull;
	
	/**
	 * Defines the masking to be applied to the result string.
	 */
	private String masking;
	
	/**
	 * Tells whether the property result string is a resource key.
	 */
	private boolean isStringKey = false;

	/**
	 * Defines the image path key if images are to be set to the column.
	 */
	private String imagePathKey;
	
	/**
	 * Defines the key prefix if the property result string is a key.
	 */
	private String keyPrefix;
	
	/**
	 * Defines the width of the column
	 */
	private int width;
	
	/**
	 * Defines if this column is a hidden column.
	 */
	private boolean hidden = false;
	
	/**
	 * Defines if this Column heading is allowable for edit.
	 */
	private boolean editable = true;
	
	/**
	 * Defines a function for processing the property value. Forward -> get property, Revert -> set property.
	 */
	private Function function;
	
	/**
	 * Constructors.
	 */
	public ColumnDisplay()
	{
		
	}
	public ColumnDisplay(String headingKey, String property)
	{
		this.headingKey = headingKey;
		this.property = property;
	}
	
	/**
	 * @return Returns the headingKey.
	 */
	public String getHeadingKey() {
		return headingKey;
	}

	/**
	 * @param headingKey The headingKey to set.
	 */
	public void setHeadingKey(String headingKey) {
		this.headingKey = headingKey;
	}

	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * This method writes the object columns to a file.
	 * 
	 * @param filepath
	 * @param objects
	 * @param columnHeadings
	 * @throws Exception 
	 */
	public static void toFile(
		ResourceBundle resourceFactory,
		String filepath,
		List objects,
		List<ColumnDisplay> columnHeadings,
		boolean asString) 
			throws Exception
	{
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File(filepath)));
		
		pw.println(StringUtils.join(getHeaders(resourceFactory, columnHeadings), _COMMA));
		
		if (objects != null)
		{
			for (int i=0; i<objects.size(); i++)
			{
				pw.println(StringUtils.join(getColumnFields(resourceFactory, objects.get(i), columnHeadings, asString), _COMMA));
				pw.flush();
			}
		}
		
		pw.close();
	}

	/**
	 * This method writes the object columns to a byte array.
	 * 
	 * @param resourceFactory
	 * @param filepath
	 * @param objects
	 * @param columnHeadings
	 * @throws FileNotFoundException
	 */
	public static byte[] toFile(
		ResourceBundle resourceFactory,
		List objects,
		List<ColumnDisplay> columnHeadings,
		boolean asString) 
			throws Exception
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(outputStream);
		
		pw.println(StringUtils.join(getHeaders(resourceFactory, columnHeadings), _COMMA));
		
		if (objects != null)
		{
			for (int i=0; i<objects.size(); i++)
			{
				pw.println(StringUtils.join(getColumnFields(resourceFactory, objects.get(i), columnHeadings, asString), _COMMA));
				pw.flush();
			}
		}
		
		pw.close();
		
		return outputStream.toByteArray();
	}
		
	private static String getResource(ResourceBundle resourceFactory, String key)
	{
		try
		{
			return resourceFactory.getString(key);
		} catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * This method returns an array of the column headings key.
	 * 
	 * @param columnHeadings
	 * @return
	 */
	public static String[] getHeaders(ResourceBundle resourceFactory, List<ColumnDisplay> columnHeadings)
	{
		String[] result = new String[columnHeadings.size()];
		for (int i=0; i<columnHeadings.size(); i++)
		{
			result[i] = 
				resourceFactory == null ? 
						columnHeadings.get(i).getHeadingKey() :
							columnHeadings.get(i).getHeadingKey() == null ? "" : resourceFactory.getString(columnHeadings.get(i).getHeadingKey());
		}

		return result;
	}
	
	/**
	 * This method returns an array of the column headings properties.
	 * 
	 * @param columnHeadings
	 * @return
	 */
	public static String[] getProperties(List<ColumnDisplay> columnHeadings)
	{
		String[] result = new String[columnHeadings.size()];
		for (int i=0; i<columnHeadings.size(); i++)
		{
			result[i] = columnHeadings.get(i).getProperty();
		}
		return result;
	}
	
	/**
	 * This method returns the resolved column values of the object.
	 * 
	 * @param object
	 * @param columnHeadings
	 * @return
	 * @throws Exception
	 */
	public static Object[] getColumnFields(
		ResourceBundle resourceFactory,
		Object object, 
		List<ColumnDisplay> columnHeadings,
		boolean asString)
			throws Exception
	{
		Object[] result = new Object[columnHeadings.size()];
		for (int i=0; i<columnHeadings.size(); i++)
		{
			result[i] = ObjectUtil.getProperty(object, columnHeadings.get(i).getProperty());
			if (result[i] instanceof java.util.Map && columnHeadings.get(i).getMapKey() != null)
			{
				java.util.Map map = (java.util.Map)result[i];
				result[i] = map.get(columnHeadings.get(i).getMapKey());
				if (result[i] == null) result[i] = columnHeadings.get(i).getMapIfNull();
			}
			if (asString) result[i] = StringUtil.toString(result[i]);
			if (columnHeadings.get(i).getFunction() != null)
			{
				if (!(result[i] instanceof String)) result[i] = StringUtil.toString(result[i]);
				result[i] = columnHeadings.get(i).getFunction().getResult((String)result[i]);
			}
			if (columnHeadings.get(i).getIsStringKey() && resourceFactory != null)
			{
				if (!(result[i] instanceof String)) result[i] = StringUtil.toString(result[i]);
				result[i] = getResource(resourceFactory,
								(columnHeadings.get(i).getKeyPrefix() == null ? "" : columnHeadings.get(i).getKeyPrefix())  
								+ result[i]);
			}
			if (columnHeadings.get(i).getMasking() != null)
			{
				if (!(result[i] instanceof String)) result[i] = StringUtil.toString(result[i]);
				result[i] = columnHeadings.get(i).getMaskedString(object, (String)result[i]);
			}
		}

		return result;
	}
		
	/**
	 * This method returns the property class of the property field defined in columnHeadings
	 * 
	 * @param objClass
	 * @param columnHeadings
	 * @return
	 * @throws Exception
	 */
	public static Class[] getColumnClasses(Class objClass, List<ColumnDisplay> columnHeadings)
		throws Exception
	{
		Object object = objClass.newInstance();
		Class[] result = new Class[columnHeadings.size()];
		for (int i=0; i<columnHeadings.size(); i++)
		{
			result[i] = PropertyUtils.getPropertyDescriptor(object, columnHeadings.get(i).getProperty()).getPropertyType();	
		}
		return result;
	}
	
	/**
	 * Return the masked string.
	 * 
	 * @param object
	 * @param string
	 * @return
	 * @throws Exception
	 */
	private String getMaskedString(Object object, String string)
		throws Exception
	{
		if (string == null || string.trim().equals("")) return string;
		
		if (masking != null)
		{
			String pattern = masking;
			if (masking.startsWith(CONDITION_PREFIX))
			{
				String tag = masking.substring(1, masking.indexOf(_EQUALS));
				String comparedString = masking.substring(masking.indexOf(_QUOTATION)+1);
				comparedString = comparedString.substring(0, comparedString.indexOf(_QUOTATION));
				
				Object property = ObjectUtil.getProperty(object, tag);
				
				String trueResult = masking.substring(masking.indexOf(_COLON)+1, masking.indexOf(_COMMA));
				String falseResult = masking.substring(masking.indexOf(_COMMA)+1);
				
				pattern = StringUtil.toString(property).equals(comparedString) ? trueResult : falseResult;
			}
			
			return StringUtil.replaceAll(pattern, _OBJECT, string);
		}
		
		return string;
	}
	
	/**
	 * @param object
	 * @param columnHeadings
	 * @return
	 * @throws Exception
	 */
	public static String[] getColumnImages(
		ResourceBundle resourceFactory,
		Object object, 
		List<ColumnDisplay> columnHeadings)
			throws Exception
	{
		String[] result = new String[columnHeadings.size()];
		for (int i=0; i<columnHeadings.size(); i++)
		{
			String path = columnHeadings.get(i).getImagePathKeyByObject(object);
			if (path != null && !"".equals(path))
			{
				result[i] = new String(resourceFactory.getString(path));
			}
		}

		return result;
	}

	/**
	 * @param object
	 * @return Returns the imagePathKey.
	 * @throws Exception
	 */
	public String getImagePathKeyByObject(Object object) 
		throws Exception
	{
		if (imagePathKey != null && imagePathKey.startsWith(CONDITION_PREFIX))
		{
			String statement = imagePathKey.substring(1);
			return getConditionString(statement, object);	
		}
		
		return imagePathKey;
	}
	
	/**
	 * This method gets the conditional result of the conditional statement.
	 * 
	 * @param statement
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String getConditionString(String statement, Object object)
		throws Exception
	{
		String tag = statement.substring(0, statement.indexOf(_EQUALS));
		
		String comparedString = statement.substring(statement.indexOf(_QUOTATION)+1);
		comparedString = comparedString.substring(0, comparedString.indexOf(_QUOTATION));
		
		Object property = ObjectUtil.getProperty(object, tag);
		
		String trueResult = statement.substring(statement.indexOf(_COLON)+1, statement.indexOf(_COMMA));
		String falseResult = statement.substring(statement.indexOf(_COMMA)+1);
		
		return StringUtil.toString(property).equals(comparedString) ? trueResult : falseResult;
	}
	
	public boolean getIsStringKey() {
		return isStringKey;
	}

	public void setIsStringKey(boolean isStringKey) {
		this.isStringKey = isStringKey;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	/**
	 * @return Returns the hidden.
	 */
	public boolean getHidden() {
		return hidden;
	}

	/**
	 * @param hidden The hidden to set.
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return Returns the editable.
	 */
	public boolean getEditable() {
		return editable;
	}

	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return Returns the imagePathKey.
	 */
	public String getImagePathKey() {
		return imagePathKey;
	}

	/**
	 * @param imagePathKey The imagePathKey to set.
	 */
	public void setImagePathKey(String imagePathKey) {
		this.imagePathKey = imagePathKey;
	}

	/**
	 * @return Returns the masking.
	 */
	public String getMasking() {
		return masking;
	}

	/**
	 * @param masking The masking to set.
	 */
	public void setMasking(String masking) {
		this.masking = masking;
	}

	/**
	 * @return
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param function
	 */
	public void setFunction(Function function) {
		this.function = function;
	}
	
	/**
	 * @return
	 */
	public String getMapKey() {
		return mapKey;
	}
	
	/**
	 * @param mapKey
	 */
	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
	
	/**
	 * @return
	 */
	public String getMapIfNull() {
		return mapIfNull;
	}
	
	/**
	 * @param mapIfNull
	 */
	public void setMapIfNull(String mapIfNull) {
		this.mapIfNull = mapIfNull;
	}

}
