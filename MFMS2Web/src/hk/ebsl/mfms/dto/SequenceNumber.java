package hk.ebsl.mfms.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name="tbl_sequencenumber")
public class SequenceNumber implements Serializable{
	
	@Id
	@Column(name = "sn_Name")
	private String name;
	@Column(name = "sn_Value")
	private int value;
	@Column(name = "sn_Desc")
	private String desc;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
	
}
