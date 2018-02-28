package hk.ebsl.mfms.dto;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StatusFlowId implements Serializable { 
	@Column(name = "sf_StatusID")
	private String statusId;

	@Column(name = "sf_NextStatusID")
	private String nextStatusId;

	@Column(name = "sf_ModeKey")
	private Integer modeKey;

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getNextStatusId() {
		return nextStatusId;
	}

	public void setNextStatusId(String nextStatusId) {
		this.nextStatusId = nextStatusId;
	}

	public Integer getModeKey() {
		return modeKey;
	}

	public void setModeKey(Integer modeKey) {
		this.modeKey = modeKey;
	}
}
