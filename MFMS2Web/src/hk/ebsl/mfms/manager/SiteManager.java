package hk.ebsl.mfms.manager;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;

public interface SiteManager {

	public Site getSiteByKey(Integer key) throws MFMSException;
	
	public Site getSiteByName(String name) throws MFMSException;
	
	public void saveSite(Site site) throws MFMSException;
	
	public List<Site> searchSite(String name, String address, Integer contactCountryCode, Integer contactAreaCode, Integer contactNumber, String deleted) throws MFMSException;
	
	public void createSite(Site site, Role adminRole, UserAccount admin) throws MFMSException;
	
	public void deleteSiteByKey(Integer accountKey, Integer siteKey) throws MFMSException;
	
	public List<Site> searchSiteByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);
	
	public Integer searchResultCount(Timestamp lastModifiedDate);
}
