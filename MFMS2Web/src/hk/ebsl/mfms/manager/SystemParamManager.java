package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.SystemParam;
import hk.ebsl.mfms.exception.MFMSException;

import java.util.List;

public interface SystemParamManager {

	public List<SystemParam> getAllSystemParam() throws MFMSException;

	public SystemParam getSystemParamByKey(String id, Integer siteKey) throws MFMSException;

	public String getSystemParamValueByKey(String id, Integer siteKey) throws MFMSException;

}
