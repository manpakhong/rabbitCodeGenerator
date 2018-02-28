package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "tbl_defectemail")
public class DefectEmail {
	
	public static final String FAILED = "F";
	
	public static final String SUCCESS = "S";
	
	@Id
	@Column(name = "de_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "de_DefectKey")
	private Integer defectKey;

	@Column(name = "de_Email")
	private String email;

	@Column(name = "de_Type")
	private String type;

	@Column(name = "de_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "de_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "de_Result")
	private String result;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getDefectKey() {
		return defectKey;
	}

	public void setDefectKey(Integer defectKey) {
		this.defectKey = defectKey;
	}	

	public Timestamp getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Timestamp getLastModifyDateTime() {
		return lastModifyDateTime;
	}

	public void setLastModifyDateTime(Timestamp lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


}
