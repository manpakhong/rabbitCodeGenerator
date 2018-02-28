package hk.ebsl.mfms.utility.function;

import java.util.Iterator;
import java.util.Map;

public class KeyMapFunction 
	extends Function 
{
	/**
	 * A map which stores the key-value pair. 
	 */
	private Map map;
	
	/* (non-Javadoc)
	 * @see hk.com.etic.common.function.Function#getResult(java.lang.Object)
	 */
	public String getResult(String object) 
	{
		return (String)map.get(object);
	}

	/* (non-Javadoc)
	 * @see hk.com.etic.common.function.Function#revert(java.lang.Object)
	 */
	public String revert(String object) 
	{
		if (object == null) return null;
		
		Iterator it = map.keySet().iterator();
		while (it.hasNext())
		{
			Object key = it.next();
			Object value = map.get(key);
			if (object.trim().equals(value)) return (String)key;
		}

		return null;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
