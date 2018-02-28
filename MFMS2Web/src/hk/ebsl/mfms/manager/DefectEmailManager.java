package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.DefectEmail;
import hk.ebsl.mfms.exception.MFMSException;

import java.util.List;

public interface DefectEmailManager {

	public List<DefectEmail> searchDefectEmail(Integer defectKey, String email, String type, String result) throws MFMSException;
	
	public void saveDefectEmail(DefectEmail defectEmail) throws MFMSException;
}
