package hk.ebsl.mfms.web.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.UserAccount;

public class DefectActionForm {

	private Integer key;

	private Integer siteKey;

	private String code;

	private String actionType;

	private Integer accountKey;

	private Integer accountGroupKey;

	private String actionDate;

	private String from;

	private String to;

	private Boolean readOnly;
	
	private Boolean canGen = false;

	private Boolean delete;

	private List<DefectActionLog> resultList = new ArrayList<DefectActionLog>();

	Map<String, String> actionTypeMap = new LinkedHashMap<String, String>();

	Map<Integer, String> dateMap = new LinkedHashMap<Integer, String>();

	private List<UserAccount> accountList = new ArrayList<UserAccount>();
	
	private Integer fullListSize = 0;
	
	public List<UserAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<UserAccount> accountList) {
		this.accountList = accountList;
	}

	public List<DefectActionLog> getResultList() {
		return resultList;
	}

	public void setResultList(List<DefectActionLog> resultList) {
		this.resultList = resultList;
	}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}

	public Integer getAccountGroupKey() {
		return accountGroupKey;
	}

	public void setAccountGroupKey(Integer accountGroupKey) {
		this.accountGroupKey = accountGroupKey;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public Map<String, String> getActionTypeMap() {
		return actionTypeMap;
	}

	public void setActionTypeMap(Map<String, String> actionTypeMap) {
		this.actionTypeMap = actionTypeMap;
	}

	public Map<Integer, String> getDateMap() {
		return dateMap;
	}

	public void setDateMap(Map<Integer, String> dateMap) {
		this.dateMap = dateMap;
	}

	public Boolean getCanGen() {
		return canGen;
	}

	public void setCanGen(Boolean canGen) {
		this.canGen = canGen;
	}

	public Integer getFullListSize() {
		return fullListSize;
	}

	public void setFullListSize(Integer fullListSize) {
		this.fullListSize = fullListSize;
	}

}
