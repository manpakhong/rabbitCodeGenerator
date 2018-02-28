package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.exception.MFMSException;

import java.util.List;

public interface FailureClassManager {

	public List<FailureClass> getAllFailureClass() throws MFMSException;
	
	public List<FailureClass> getFailureClassBySiteKey(Integer siteKey) throws MFMSException;
	
	public List<FailureClass> searchFailureClass(Integer siteKey, String code, String name) throws MFMSException;
	
	public void saveFailureClass(FailureClass failureClass) throws MFMSException;
	
	public FailureClass getFailureClassByKey(Integer key) throws MFMSException;
	
	public FailureClass getFailureClassByName(Integer key, String name, boolean fetch) throws MFMSException;
	
	public FailureClass getFailureClassByCode(Integer key, String code, boolean fetch) throws MFMSException;

	public void deleteFailureClassByKey(Integer accountKey, Integer key) throws MFMSException;

}
