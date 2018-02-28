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
@Table(name = "tbl_patrolscheduleactionlog")
public class PatrolScheduleActionLog {

	@Id
	@Column(name = "psal_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "psal_ActionType")
	private String actionType;

	@Column(name = "psal_ActionDateTime")
	private Timestamp actionDateTime;

	@Column(name = "psal_ActionBy")
	private Integer actionBy;

	@Column(name = "psal_PatrolScheduleKey")
	private Integer patrolScheduleKey;

	@Column(name = "psal_SiteKey")
	private Integer siteKey;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "psal_ActionBy", insertable = false, updatable = false)
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

	public Integer getActionBy() {
		return actionBy;
	}

	public void setActionBy(Integer actionBy) {
		this.actionBy = actionBy;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public Integer getPatrolScheduleKey() {
		return patrolScheduleKey;
	}

	public void setPatrolScheduleKey(Integer patrolScheduleKey) {
		this.patrolScheduleKey = patrolScheduleKey;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

}
