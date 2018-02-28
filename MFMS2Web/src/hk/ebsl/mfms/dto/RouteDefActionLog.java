package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_routedefactionlog")
public class RouteDefActionLog {

	@Id
	@Column(name = "rdal_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "rdal_ActionType")
	private String actionType;

	@Column(name = "rdal_ActionDateTime")
	private Timestamp actionDateTime;

	@Column(name = "rdal_SiteKey")
	private Integer siteKey;

	@Column(name = "rdal_RouteDefKey")
	private Integer routeDefKey;

	@Column(name = "rdal_ActionBy")
	private Integer actionBy;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rdal_ActionBy", insertable = false, updatable = false)
	private UserAccount account;

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

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public Integer getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(Integer routeDefKey) {
		this.routeDefKey = routeDefKey;
	}

	public Integer getActionBy() {
		return actionBy;
	}

	public void setActionBy(Integer actionBy) {
		this.actionBy = actionBy;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

}
