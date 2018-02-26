package hk.ebsl.mfms.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "tbl_PunchCard")
@JsonRootName("")
public class PunchCard {
	@Id
	@Column(name = "pc_Key")
	@GeneratedValue
	private Integer key;
	
	@Column(name = "pc_Name")
	private String Name;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	
	
	
}
