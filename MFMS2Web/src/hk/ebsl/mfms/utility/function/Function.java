/**
 * @author pklam
 *
 * Created Date: 2006¦~11¤ë16¤é
 *
 * Copyright 2006 E-Business Technology Institute. All Rights Reserved.
 * 
 * This software is the proprietary information of E-Business Technology Institute.  
 * Use is subject to license terms.
 * 
 */
package hk.ebsl.mfms.utility.function;

public abstract class Function 
{

	/**
	 * This method returns the result after the object undergoes
	 * the function.
	 * 
	 * @param object
	 * @return
	 */
	public abstract String getResult(String object);
	
	/**
	 * This method returns the reverted value of the function.
	 * 
	 * @param object
	 * @return
	 */
	public abstract String revert(String object);
}
