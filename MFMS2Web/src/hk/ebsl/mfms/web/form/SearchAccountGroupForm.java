package hk.ebsl.mfms.web.form;

import java.util.ArrayList;
import java.util.List;

import hk.ebsl.mfms.dto.AccountGroup;

public class SearchAccountGroupForm {

	private Integer key;
	
	private Integer siteKey;
	
	private String name;
	
	private String desc;
	
	private Boolean deleteSuccess = false;
	
	private String referrerStr;
	
	private String deletedName;
	
	private Boolean canModify = false;
	
	private Boolean canRemove= false;

	private Boolean canGen = false;
	
	private List<AccountGroup> resultList = new ArrayList<AccountGroup>();

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<AccountGroup> getResultList() {
		return resultList;
	}

	public void setResultList(List<AccountGroup> resultList) {
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

	public String getReferrerStr() {
		return referrerStr;
	}

	public void setReferrerStr(String referrerStr) {
		this.referrerStr = referrerStr;
	}

	public Boolean getCanModify() {
		return canModify;
	}

	public void setCanModify(Boolean canModify) {
		this.canModify = canModify;
	}

	public Boolean getCanRemove() {
		return canRemove;
	}

	public void setCanRemove(Boolean canRemove) {
		this.canRemove = canRemove;
	}

	public Boolean getCanGen() {
		return canGen;
	}

	public void setCanGen(Boolean canGen) {
		this.canGen = canGen;
	}


}
