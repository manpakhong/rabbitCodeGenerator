package hk.ebsl.mfms.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "tbl_accountactionlog")
public class AccountActionLog {

	@Id
	@Column(name = "aal_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "aal_ActionType")
	private String actionType;

	@Column(name = "aal_ActionDateTime")
	private Timestamp actionDateTime;

	@Column(name = "aal_Result")
	private String result;

	@Column(name = "aal_AccountId")
	private String accountId;
	
	@Column(name = "aal_SiteKey")
	private Integer siteKey;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Timestamp getActionDateTime() {
		return actionDateTime;
	}

	public void setActionDateTime(Timestamp actionDateTime) {
		this.actionDateTime = actionDateTime;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

}
