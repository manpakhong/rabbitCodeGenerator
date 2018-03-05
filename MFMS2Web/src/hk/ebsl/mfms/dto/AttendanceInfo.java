package hk.ebsl.mfms.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_AttendanceInfo")
public class AttendanceInfo implements Serializable{
	@Id
	@Column(name="atdi_Key")
	@GeneratedValue
	private Integer key;
	@Column(name="atdi_AttendanceKey")
	private Integer attendanceKey;
	@Column(name="atdi_IP")
	private String ip;
	@Column(name="atdi_Cookies")
	private String cookies;
	@Column(name="atdi_MACAddress")
	private String macAddress;
	@Column(name="atdi_IMEI")
	private String imei;
	@Column(name="atdi_MobileNumber")
	private String mobileNumber;
	@Column(name="atdi_GPSLatitude")
	private String gpsLatitude;
	@Column(name="atdi_GPSLongitude")
	private String gpsLongitude;
	@Column(name="atdi_GPSCaptureTime")
	private Timestamp gpsCaptureTime;
	@Column(name="atdi_LocationKey")
	private Integer locationKey;
	@Column(name="atdi_LocationCode")
	private String locationCode;
	@Column(name="atdi_LocationName")
	private String locationName;
	@Column(name="atdi_LocationTagID")
	private String locationTagId;
	@Column(name="atdi_RemarkLocationKey")
	private Integer remarkLocationKey;
	@Column(name="atdi_RemarkLocationKeyCode")
	private String remarkLocationKeyCode;
	@Column(name="atdi_RemarkLocationKeyName")
	private String remarkLocationKeyName;
	@Column(name="atdi_Remarks")
	private String remarks;
	@Column(name="atdi_CreateBy")
	private Integer createBy;
	@Column(name="atdi_CreateDateTime")
	private Timestamp createDateTime;
	@Column(name="atdi_LastModifyBy")
	private Integer lastModifyBy;
	@Column(name="atdi_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	@Column(name="atdi_Deleted")
	private String deleted;
	@Column(name="atdi_RefAttendance")
	private String refAttendance;
	@Column(name="atdi_AttendanceInfocol")
	private String attendanceInfocol;
	public Integer getAttendanceKey() {
		return attendanceKey;
	}
	public void setAttendanceKey(Integer attendanceKey) {
		this.attendanceKey = attendanceKey;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getGpsLatitude() {
		return gpsLatitude;
	}
	public void setGpsLatitude(String gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}
	public String getGpsLongitude() {
		return gpsLongitude;
	}
	public void setGpsLongitude(String gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}
	public Timestamp getGpsCaptureTime() {
		return gpsCaptureTime;
	}
	public void setGpsCaptureTime(Timestamp gpsCaptureTime) {
		this.gpsCaptureTime = gpsCaptureTime;
	}
	public Integer getLocationKey() {
		return locationKey;
	}
	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationTagId() {
		return locationTagId;
	}
	public void setLocationTagId(String locationTagId) {
		this.locationTagId = locationTagId;
	}
	public Integer getRemarkLocationKey() {
		return remarkLocationKey;
	}
	public void setRemarkLocationKey(Integer remarkLocationKey) {
		this.remarkLocationKey = remarkLocationKey;
	}
	public String getRemarkLocationKeyCode() {
		return remarkLocationKeyCode;
	}
	public void setRemarkLocationKeyCode(String remarkLocationKeyCode) {
		this.remarkLocationKeyCode = remarkLocationKeyCode;
	}
	public String getRemarkLocationKeyName() {
		return remarkLocationKeyName;
	}
	public void setRemarkLocationKeyName(String remarkLocationKeyName) {
		this.remarkLocationKeyName = remarkLocationKeyName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public String getRefAttendance() {
		return refAttendance;
	}
	public void setRefAttendance(String refAttendance) {
		this.refAttendance = refAttendance;
	}
	public String getAttendanceInfocol() {
		return attendanceInfocol;
	}
	public void setAttendanceInfocol(String attendanceInfocol) {
		this.attendanceInfocol = attendanceInfocol;
	}
	
}
