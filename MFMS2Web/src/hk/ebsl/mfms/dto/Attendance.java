package hk.ebsl.mfms.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "tbl_Attendance")
@JsonRootName("")
public class Attendance implements Serializable{
	@Id
	@Column(name="atd_Key")
	@GeneratedValue
	private Integer key;
	@Column(name="atd_AccountKey")
	private Integer accountKey;
	@Column(name="atd_LoginID")
	private String loginId;
	@Column(name="atd_Platform")
	private String platform;
	@Column(name="atd_ActionTypeCode")
	private String actionTypeCode;
	@Column(name="atd_ActionDateTime")
	private Timestamp actionDateTime;
	@Column(name="atd_CreateBy")
	private Integer createBy;
	@Column(name="atd_CreateDateTime")
	private Timestamp createDateTime;
	@Column(name="atd_LastModifyBy")
	private Integer lastModifyBy;
	@Column(name="atd_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	@Column(name="atd_Deleted")
	private String deleted;
	@Column(name="atd_RefAttendanceInfo")
	private String refAttendanceInfo;
	public Integer getKey() {
		return key;
	}
	public void setKey(Integer key) {
		this.key = key;
	}
	public Integer getAccountKey() {
		return accountKey;
	}
	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getActionTypeCode() {
		return actionTypeCode;
	}
	public void setActionTypeCode(String actionTypeCode) {
		this.actionTypeCode = actionTypeCode;
	}
	public Timestamp getActionDateTime() {
		return actionDateTime;
	}
	public void setActionDateTime(Timestamp actionDateTime) {
		this.actionDateTime = actionDateTime;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	public Timestamp getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}
	public Integer getLastModifyBy() {
		return lastModifyBy;
	}
	public void setLastModifyBy(Integer lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
	public Timestamp getLastModifyDateTime() {
		return lastModifyDateTime;
	}
	public void setLastModifyDateTime(Timestamp lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getRefAttendanceInfo() {
		return refAttendanceInfo;
	}
	public void setRefAttendanceInfo(String refAttendanceInfo) {
		this.refAttendanceInfo = refAttendanceInfo;
	}
	
}
