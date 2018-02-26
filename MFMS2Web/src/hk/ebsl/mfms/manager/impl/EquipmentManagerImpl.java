package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.EquipmentDao;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.EquipmentManager;

public class EquipmentManagerImpl implements EquipmentManager {

	public static final Logger logger = Logger.getLogger(EquipmentManagerImpl.class);

	private EquipmentDao equipmentDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Equipment> getAllEquipment() throws MFMSException {

		logger.debug("getAllEquipment()");

		List<Equipment> list = equipmentDao.getAllEquipment();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public boolean hasEquipment(Integer locationKey) throws MFMSException {

		return equipmentDao.hasEquipment(locationKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Equipment> getEquipmentBySiteKey(Integer siteKey) throws MFMSException {

		List<Equipment> list = equipmentDao.getEquipmentBySiteKey(siteKey);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Equipment> searchEquipment(Integer siteKey, Integer locationKey, String code, String name)
			throws MFMSException {

		logger.debug("searchEquipment()[" + siteKey + "," + code + "," + name + "]");

		List<Equipment> list = equipmentDao.searchEquipment(siteKey, locationKey, code, name, "N");

		return list;
	}

	public void test() {

	}

	public EquipmentDao getEquipmentDao() {
		return equipmentDao;
	}

	public void setEquipmentDao(EquipmentDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveEquipment(Equipment equipment) throws MFMSException {
		equipmentDao.saveEquipment(equipment);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Equipment getEquipmentByKey(Integer key) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("getEquipmentByKey()[" + key + "]");

		Equipment equipment = equipmentDao.getEquipmentByKey(key);

		return equipment;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Equipment getEquipmentByName(Integer siteKey, String name, boolean fetch) throws MFMSException {

		logger.debug("getEquipmentByName()[" + siteKey + "," + name + "," + fetch + "]");

		Equipment equipment = equipmentDao.getEquipmentByName(siteKey, name, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return equipment;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Equipment getEquipmentByCode(Integer siteKey, String code, boolean fetch) throws MFMSException {

		logger.debug("getEquipmentByCode()[" + siteKey + "," + code + "," + fetch + "]");

		Equipment equipment = equipmentDao.getEquipmentByCode(siteKey, code, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return equipment;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteEquipmentByKey(Integer accountKey, Integer equipmentKey) throws MFMSException {

		logger.debug("deleteEquipmentByKey()[" + accountKey + "," + equipmentKey + "]");

		Equipment equipment = equipmentDao.getEquipmentByKey(equipmentKey);

		if (equipment == null) {
			throw new MFMSException("Equipment not found");

		}
		equipment.setDeleted("Y");
		equipment.setLastModifyBy(accountKey);
		equipment.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		saveEquipment(equipment);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Equipment> searchEquipmentByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		logger.debug("searchEquipment()[" + lastModifiedDate + "]");

		List<Equipment> equipmentList = equipmentDao.searchEquipment(lastModifiedDate, offset, maxResults, siteKey);

		return equipmentList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = equipmentDao.theCountOfSearchResult(lastModifiedDate, siteKey);

		return theCountoftotalResult;
	}

}
