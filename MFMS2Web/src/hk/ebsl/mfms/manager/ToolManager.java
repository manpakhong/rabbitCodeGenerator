package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface ToolManager {

	public List<Tool> getAllTool() throws MFMSException;

	public List<Tool> getToolBySiteKey(Integer siteKey) throws MFMSException;

	public List<Tool> searchTool(Integer siteKey, String code, String name) throws MFMSException;

	public void saveTool(Tool tool) throws MFMSException;

	public Tool getToolByKey(Integer key) throws MFMSException;

	public Tool getToolByName(Integer key, String name, boolean fetch) throws MFMSException;

	public Tool getToolByCode(Integer key, String code, boolean fetch) throws MFMSException;

	public void deleteToolByKey(Integer accountKey, Integer toolKey) throws MFMSException;

	public List<Tool> searchToolByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);

}
