package hk.ebsl.mfms.utility;

import hk.ebsl.mfms.utility.function.Function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class ObjectUtil 
{
	private static final Logger logger = Logger.getLogger(ObjectUtil.class);
		
	/**
	 * This method returns a Calendar property from the object.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @return
	 */
	public static Calendar getCalendar(Object object, String propertyName)
	{
		try
		{
			Object propertyObject = PropertyUtils.getProperty(object, propertyName);
		
			if (propertyObject instanceof Calendar) return (Calendar)propertyObject;
			return null;
			
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
			
			return null;
		}
	}

	/**
	 * This method sets a Calendar property to the object.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @param value
	 */
	public static void setCalendar(Object object, String propertyName, Calendar value)
	{
		try
		{
			Object propertyValue = value;
			
			PropertyUtils.setProperty(object, propertyName, propertyValue);
			
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	
	/**
	 * This method returns a Boolean property from the object.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @return
	 */
	public static Boolean getBoolean(Object object, String propertyName)
	{
		try
		{
			Object propertyObject 
				= PropertyUtils.getProperty(object, propertyName);
			
			if (propertyObject instanceof Boolean) return (Boolean)propertyObject;
			return null;
			
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
			
			return null;
		}
	}

	/**
	 * This method sets a Boolean property to the object.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @param value
	 */
	public static void setBoolean(Object object, String propertyName, Boolean value)
	{
		try
		{
			Object propertyValue = value;
			
			PropertyUtils.setProperty(object, propertyName, propertyValue);
			
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
	
	/**
	 * This method gets a property from the object and return the string representation
	 * of property value.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @return
	 */
	public static String getString(Object object, String propertyName)
	{
		try
		{
			Object propertyObject 
				= PropertyUtils.getProperty(object, propertyName);
			
			return StringUtil.toString(propertyObject);
			
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
			
			return null;
		}
	}

	/**
	 * This method sets a String value property to the object.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @param value
	 * @param isRequired
	 * @param maxlength
	 * @param minlength
	 * @return
	 * @throws Exception
	 */
	public static void setObject(Object object, String propertyName, Function function, String value)
		throws Exception
	{
		if (propertyName == null) return;
		if (function != null) value = function.revert(value);
		Object propertyValue 
			= StringUtil.toObject(PropertyUtils.getPropertyDescriptor(object, propertyName).getPropertyType(), value);
		PropertyUtils.setProperty(object, propertyName, propertyValue);
	}
	
	/**
	 * This method gets a property from the object
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public static Object getProperty(Object object, String propertyName)
		throws Exception
	{
		Object propertyObject = object;
		String key = propertyName; 
			
		while (key.indexOf(".") != -1)
		{
			if (propertyObject != null)
			{
				
				propertyObject 
					= PropertyUtils.getProperty(propertyObject, propertyName.substring(0, propertyName.indexOf(".")));
			}
			
			key = propertyName.substring(propertyName.indexOf(".")+1);
		}
		
		if (propertyObject == null) return null;
		
		propertyObject 
			= PropertyUtils.getProperty(propertyObject, key);
		
		return propertyObject;
	}
	
	/**
	 * This method returns an Integer property from the object.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @return
	 */
	public static Integer getInteger(Object object, String propertyName)
	{
		try
		{
			Object propertyObject 
				= PropertyUtils.getProperty(object, propertyName);
		
			if (propertyObject instanceof Integer) return (Integer)propertyObject;
			if (propertyObject instanceof Long)
			{
				Long longValue = (Long)propertyObject;
				return new Integer(longValue.intValue());
			}
			
			return null;			
			
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
			
			return null;
		}
		
	}

	/**
	 * This method sets an Integer property to the object.
	 * 
	 * @param object
	 * @param propertyName
	 * @param function
	 * @param value
	 */
	public static void setInteger(Object object, String propertyName, Integer value)
	{
		try
		{
			Object propertyValue = value;
			
			PropertyUtils.setProperty(object, propertyName, propertyValue);
			
		} catch (Exception e)
		{
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
	}
		
	/**
	 * This method converts a data list to a list of object according to the specified
	 * objectClass.
	 * 
	 * @param dataList
	 * @param objectClass
	 * @return
	 * @throws Exception
	public static java.util.List dataList2ObjectList(
		DataList dataList, 
		Class objectClass)
			throws Exception
	{
		java.util.List<Object> result = new ArrayList<Object>();
		
		for (int i=0; i<dataList.sizeOfDataArray(); i++)
		{
			BaseObject object = (BaseObject)objectClass.newInstance();
			setData2BaseObject(dataList.getDataArray(i), object);
			result.add(object);
		}
		
		
		return result;
	}
	 */
	
	/**
	 * Set the properties of the object according to the data.
	 * 
	 * @param data
	 * @param object
	 * @throws Exception
	public static void setData2BaseObject(
		Data data, 
		BaseObject object)
			throws Exception
	{		
		setField2BaseObject(data, object);
		setSubdata2BaseObject(data, object);
		setList2BaseObject(data, object);
	}
	 */
	
	/**
	 * This method sets simple fields into object.
	 * 
	 * @param data
	 * @param object
	 * @throws Exception
	private static void setField2BaseObject(
		Data data, 
		BaseObject object)
			throws Exception
	{
		for (int i=0; i<data.sizeOfFieldArray(); i++)
		{
			Field field = data.getFieldArray(i);
			
			try
			{
				PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(object, field.getName());
				if (desc == null) continue;
					
				Class propertyClass = desc.getPropertyType();
				Object value = StringUtil.toObject(propertyClass, field.getValue());
				
				PropertyUtils.setProperty(object, field.getName(), value);
					
			} catch (Exception e)
			{
				logger.error(e.getMessage());
				logger.trace(e.getMessage(), e);
			}
		}
	}
	 */
		
	/**
	 * This method sets sub data of type BaseObject into object.
	 * 
	 * @param data
	 * @param object
	 * @throws Exception
	private static void setSubdata2BaseObject(
		Data data, 
		BaseObject object)
			throws Exception
	{
		for (int i=0; i<data.sizeOfSubdataArray(); i++)
		{
			String subdataName = data.getSubdataArray(i).getName();
				
			PropertyDescriptor des = PropertyUtils.getPropertyDescriptor(object, subdataName);

			Object property = des.getPropertyType().newInstance();
			setData2BaseObject(data.getSubdataArray(i), (BaseObject)property);
			
			PropertyUtils.setProperty(object, subdataName, property);
		}
	}
	 */

	/**
	 * This method sets list of objects into object.
	 * 
	 * @param data
	 * @param object
	 * @throws Exception
	private static void setList2BaseObject(
		Data data, 
		BaseObject object)
			throws Exception
	{
		for (int i=0; i<data.sizeOfSublistArray(); i++)
		{
			String listName = data.getSublistArray(i).getName();
			
			PropertyDescriptor des = PropertyUtils.getPropertyDescriptor(object, listName);
			
			if (des.getPropertyType() == java.util.List.class)
			{
				java.util.List<BaseObject> list = new ArrayList<BaseObject>();
				
				for (int j=0; j<data.getSublistArray(i).sizeOfDataArray(); j++)
				{
					Object subObject = Class.forName(data.getSublistArray(i).getDataArray(j).getEntityType()).newInstance();
					setField2BaseObject(data.getSublistArray(i).getDataArray(j), (BaseObject)subObject);
					list.add((BaseObject)subObject);	
				}
				
				PropertyUtils.setProperty(object, listName, list);
				
			} else if (des.getPropertyType() == java.util.Set.class)
			{
				java.util.Set<BaseObject> set = new HashSet<BaseObject>();
				
				for (int j=0; j<data.getSublistArray(i).sizeOfDataArray(); j++)
				{
					Object subObject = Class.forName(data.getSublistArray(i).getDataArray(j).getEntityType()).newInstance();
					setField2BaseObject(data.getSublistArray(i).getDataArray(j), (BaseObject)subObject);
					set.add((BaseObject)subObject);	
				}
				
				PropertyUtils.setProperty(object, listName, set);
			}
		}
	}
	 */
	
	/**
	 * Set the BaseObject to the Data object.
	 * 
	 * @param data
	 * @param id
	 * @param object
	 * @throws Exception
	public static void setBaseObject2Data(
		Data data, 
		String id,
		BaseObject object)
			throws Exception
	{
		if (id != null) data.setId(id);
		
		data.setEntityType(object.getClass().getName());
		
		PropertyDescriptor[] des = PropertyUtils.getPropertyDescriptors(object);
		
		for (int i=0; i<des.length; i++)
		{
			String name = des[i].getName();
				
			if ("class".equals(name)) 
						continue;
				
			Object value = PropertyUtils.getProperty(object, des[i].getName());
			
			if (value instanceof BaseObject)
			{
				Data subdata = data.addNewSubdata();
				subdata.setName(name);
				
				setBaseObject2Data(subdata, null, (BaseObject)value);
				
			} else if (value instanceof java.util.List)
			{
				addList(data, name, (java.util.List)value);
				
			} else if (value instanceof java.util.Set) 
			{
				addSet(data, name, (java.util.Set)value);
				
			} else
			{
				addField(data, name, value);
			}
		}
	}
	 */
	
	/**
	 * Add a list to the data.
	 * 
	 * @param data
	 * @param name
	 * @param list
	 * @throws Exception
	private static void addList(Data data, String name, java.util.List list)
		throws Exception
	{
		if (list != null)
		{
			Sublist dataList = data.addNewSublist();
			dataList.setName(name);
			
			for (int i=0; i<list.size(); i++)
			{
				setBaseObject2Data(dataList.addNewData(), null, (BaseObject)list.get(i));
			}
		}
	}
	 */
	
	/**
	 * Add a set to the data.
	 * 
	 * @param data
	 * @param name
	 * @param set
	 * @throws Exception
	private static void addSet(Data data, String name, java.util.Set set)
		throws Exception
	{
		if (set != null)
		{
			Sublist dataList = data.addNewSublist();
			dataList.setName(name);
			
			Iterator it = set.iterator();
			while (it.hasNext())
			{
				setBaseObject2Data(dataList.addNewData(), null, (BaseObject)it.next());
			}
		}
	}
	 */
	
	/**
	 * Add field to the data.
	 * 
	 * @param data
	 * @param name
	 * @param value
	private static void addField(Data data, String name, Object value)
	{
		if (value != null)
		{
			Field field = data.addNewField();
			field.setName(name);
			field.setValue(StringUtil.toString(value));
		}
	}	
	 */
	
	/**
	 * This method checks if the two objects are equal.
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean equal(Object obj1, Object obj2)
	{
		if (obj1 == null) return (obj2 == null);
		return obj1.equals(obj2);
	}
	
	/**
	 * This method save the object to the given file.
	 * 
	 * @param object
	 * @param prefix the file path prefix before the currentTimeMillis
	 * @param suffix the file name suffix after the currentTimeMillis
	 * @throws Exception
	 */
	public static String saveObject(Object object, String prefix, String suffix) 
		throws Exception
	{
		File file = null;
		String filename = null;
		do
		{
			filename = prefix + System.currentTimeMillis() + suffix;
			file = new File(filename);

		} while (file.exists());
		
		FileOutputStream outputStream = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeObject(object);
		out.flush();
		out.close();
		
		return filename;
	}
	
	/**
	 * @param afSrc
	 * @param acRtnCls
	 * @return
	 * @throws IOException if it fails to read from file, <code>afSrc</code>.
	 * @throws ClassNotFoundException class inside <code>afSrc</code> is not a valid
	 *                                class found in this application.
	 */
	public static <T> T loadObject(File afSrc, Class<T> acRtnCls) throws IOException, 
		ClassNotFoundException {
	T tRtn=null;
	FileInputStream fisSrc = null;
	ObjectInputStream oisSrc=null;
	
		try {
			fisSrc =new FileInputStream(afSrc);
		  oisSrc = new ObjectInputStream(fisSrc);
		  tRtn = (T)oisSrc.readObject();
		  
		} catch (IOException aioe_x) {
			logger.error(aioe_x.getMessage(), aioe_x);
			throw aioe_x;
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw e;
			
		} finally {
			try {
				if (oisSrc!=null) oisSrc.close();
				if (fisSrc!=null) fisSrc.close();
			} catch (IOException aioe_x2) {					
			}
		}
		return tRtn;
	}
	
}