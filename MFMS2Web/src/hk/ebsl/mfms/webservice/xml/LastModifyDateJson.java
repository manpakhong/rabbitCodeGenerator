package hk.ebsl.mfms.webservice.xml;

public class LastModifyDateJson {

	public long accountGroupLastModifyDate;
	
	public long responisbleLastModifyDate;
	
	public long LastModifyDate;
	
	public Integer siteKey;
	
	public Integer offset;
	
	public Integer maxResult;
	
	public long getLastModifyDate() {
		return LastModifyDate;
	}

	public void setLastModifyDate(long LastModifyDate) {
		this.LastModifyDate = LastModifyDate;
	}

	public long getAccountGroupLastModifyDate() {
		return accountGroupLastModifyDate;
	}

	public void setAccountGroupLastModifyDate(long accountGroupLastModifyDate) {
		this.accountGroupLastModifyDate = accountGroupLastModifyDate;
	}

	public long getResponisbleLastModifyDate() {
		return responisbleLastModifyDate;
	}

	public void setResponisbleLastModifyDate(long responisbleLastModifyDate) {
		this.responisbleLastModifyDate = responisbleLastModifyDate;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}


}
