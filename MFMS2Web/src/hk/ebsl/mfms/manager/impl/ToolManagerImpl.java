package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.ToolDao;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.ToolManager;

public class ToolManagerImpl implements ToolManager {

	public static final Logger logger = Logger.getLogger(ToolManagerImpl.class);

	private ToolDao toolDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Tool> getAllTool() throws MFMSException {

		logger.debug("getAllTool()");

		List<Tool> list = toolDao.getAllTool();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Tool> getToolBySiteKey(Integer siteKey) throws MFMSException {

		List<Tool> list = toolDao.getToolBySiteKey(siteKey);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Tool> searchTool(Integer siteKey, String code, String name) throws MFMSException {

		logger.debug("searchTool()[" + siteKey + "," + code + "," + name + "]");

		List<Tool> list = toolDao.searchTool(siteKey, code, name, "N");

		return list;
	}

	public void test() {

	}

	public ToolDao getToolDao() {
		return toolDao;
	}

	public void setToolDao(ToolDao toolDao) {
		this.toolDao = toolDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveTool(Tool tool) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("saveTool()[" + tool.getKey());
		logger.debug("saveTool()[" + tool.getSiteKey());
		logger.debug("saveTool()[" + tool.getCode());
		logger.debug("saveTool()[" + tool.getDeleted() + "]");

		toolDao.saveTool(tool);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Tool getToolByKey(Integer key) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("getToolByKey()[" + key + "]");

		Tool tool = toolDao.getToolByKey(key);

		return tool;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteToolByKey(Integer accountKey, Integer toolKey) throws MFMSException {

		logger.debug("deleteToolByKey()[" + accountKey + "," + toolKey + "]");

		Tool tool = toolDao.getToolByKey(toolKey);

		if (tool == null) {
			throw new MFMSException("Tool not found");

		}
		tool.setDeleted("Y");
		tool.setLastModifyBy(accountKey);
		tool.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		saveTool(tool);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Tool getToolByName(Integer siteKey, String name, boolean fetch) throws MFMSException {

		logger.debug("getToolByName()[" + siteKey + "," + name + "," + fetch + "]");

		Tool tool = toolDao.getToolByName(siteKey, name, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return tool;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Tool getToolByCode(Integer siteKey, String code, boolean fetch) throws MFMSException {

		logger.debug("getRoleByCode()[" + siteKey + "," + code + "," + fetch + "]");

		Tool tool = toolDao.getToolByCode(siteKey, code, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return tool;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Tool> searchToolByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		logger.debug("searchTool()[" + lastModifiedDate + "]");

		List<Tool> toolList = toolDao.searchTool(lastModifiedDate, offset, maxResults, siteKey);

		return toolList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = toolDao.theCountOfSearchResult(lastModifiedDate, siteKey);

		return theCountoftotalResult;
	}

}
