package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
@Entity
@Table(name = "tbl_attendanceconfig")
@JsonRootName("")
public class AttendanceConfig {
	public final static String CODE_FUNC_WCI = "FUNC_WCI";
	public final static String CODE_FUNC_WCO = "FUNC_WCO";
	public final static String CODE_FUNC_WLO = "FUNC_WLO";
	public final static String CODE_FUNC_WLI = "FUNC_WLI";
	public final static String CODE_FUNC_WOI = "FUNC_WOI";
	public final static String CODE_FUNC_WOO = "FUNC_WOO";
	public final static String CODE_REM_LOC_W = "REM_LOC_W";
	
	@Id
	@Column(name="atdt_Key")
	@GeneratedValue
	private Integer key;
	@Column(name="atdc_Platform")
	private String platform;
	@Column(name="atdc_Code")
	private String code;
	@Column(name="atdc_Desc")
	private String desc;
	@Column(name="atdc_Value")
	private String value;
	@Column(name="atdc_Datatype")
	private String dataType;
	@Column(name="atdc_Unit")
	private String unit;
	@Column(name="atdc_AccountKey")
	private Integer accountKey;
	@Column(name="atdc_CreateBy")
	private String createBy;
	@Column(name="atdc_CreateDateTime")
	private Timestamp createDateTime;
	@Column(name="atdc_LastModifyBy")
	private Integer lastModifyBy;
	@Column(name="atdc_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	@Column(name="atdc_Deleted")
	private String deleted;
	public Integer getKey() {
		return key;
	}
	public void setKey(Integer key) {
		this.key = key;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getAccountKey() {
		return accountKey;
	}
	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
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
	
	
	
}
