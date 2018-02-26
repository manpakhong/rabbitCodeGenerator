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
@Table(name = "tbl_defectscheduleactionlog")
public class DefectScheduleActionLog {

	@Id
	@Column(name = "dsal_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "dsal_ActionType")
	private String actionType;

	@Column(name = "dsal_ActionDateTime")
	private Timestamp actionDateTime;

	@Column(name = "dsal_ActionBy")
	private Integer actionBy;

	@Column(name = "dsal_defectScheduleKey")
	private Integer defectScheduleKey;

	@Column(name = "dsal_SiteKey")
	private Integer siteKey;

	@Column(name = "dsal_Desc")
	private String desc;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dsal_ActionBy", insertable = false, updatable = false)
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

	public Integer getDefectScheduleKey() {
		return defectScheduleKey;
	}

	public void setDefectScheduleKey(Integer defectScheduleKey) {
		this.defectScheduleKey = defectScheduleKey;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

}
