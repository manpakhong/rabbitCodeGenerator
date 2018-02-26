package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.exception.MFMSException;
import java.sql.Timestamp;
import java.util.List;

public interface EquipmentManager {

	public List<Equipment> getAllEquipment() throws MFMSException;

	public List<Equipment> getEquipmentBySiteKey(Integer siteKey) throws MFMSException;

	public List<Equipment> searchEquipment(Integer siteKey, Integer locationKey, String code, String name)
			throws MFMSException;

	public void saveEquipment(Equipment equipment) throws MFMSException;

	public Equipment getEquipmentByKey(Integer key) throws MFMSException;

	public Equipment getEquipmentByName(Integer key, String name, boolean fetch) throws MFMSException;

	public Equipment getEquipmentByCode(Integer key, String code, boolean fetch) throws MFMSException;

	public void deleteEquipmentByKey(Integer accountKey, Integer toolKey) throws MFMSException;

	public boolean hasEquipment(Integer locationKey) throws MFMSException;

	public List<Equipment> searchEquipmentByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);
}
