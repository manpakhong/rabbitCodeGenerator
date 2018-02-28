package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.Site;

import java.util.List;

public class SearchSiteForm {
	
	private String name;
	
	private String address;
	
	/*
	private Integer contactCountryCode;
	
	private Integer contactAreaCode;
	
	private Integer contactNumber;
	*/
	
	
	
	private List<Site> resultList;
	
	private Boolean deleteSuccess = false;
	
	private String deletedName;

	private Boolean canGen = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/*
	public Integer getContactCountryCode() {
		return contactCountryCode;
	}

	public void setContactCountryCode(Integer contactCountryCode) {
		this.contactCountryCode = contactCountryCode;
	}

	public Integer getContactAreaCode() {
		return contactAreaCode;
	}

	public void setContactAreaCode(Integer contactAreaCode) {
		this.contactAreaCode = contactAreaCode;
	}

	public Integer getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Integer contactNumber) {
		this.contactNumber = contactNumber;
	}
	*/

	public List<Site> getResultList() {
		return resultList;
	}

	public void setResultList(List<Site> resultList) {
		this.resultList = resultList;
	}

	public Boolean getDeleteSuccess() {
		return deleteSuccess;
	}

	public void setDeleteSuccess(Boolean deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}

	public String getDeletedName() {
		return deletedName;
	}

	public void setDeletedName(String deletedName) {
		this.deletedName = deletedName;
	}

	public Boolean getCanGen() {
		return canGen;
	}

	public void setCanGen(Boolean canGen) {
		this.canGen = canGen;
	}
}
