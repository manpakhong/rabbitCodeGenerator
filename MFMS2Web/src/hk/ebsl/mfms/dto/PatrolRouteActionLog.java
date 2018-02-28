package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_patrolrouteactionlog")
public class PatrolRouteActionLog {
	
	@Id
	@Column(name = "pral_Key")
	@GeneratedValue
	private Integer key;
	
	@Column(name = "pral_ActionType")
	private String actionType;
	
	@Column(name = "pral_ActionDateTime")
	private Timestamp actionDateTime;
	
	@Column(name = "pral_PatrolPhotoKey")
	private Integer patrolPhotoKey;
	
	@Column(name = "pral_PatrolPhotoRemark")
	private String patrolPhotoRemark;
	
	@Column(name = "pral_RouteDefKey")
	private Integer routeDefKey;

	@Column(name = "pral_LastModifyBy")
	private Integer lastModifyBy;
	
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

	public Integer getPatrolPhotoKey() {
		return patrolPhotoKey;
	}

	public void setPatrolPhotoKey(Integer patrolPhotoKey) {
		this.patrolPhotoKey = patrolPhotoKey;
	}

	public String getPatrolPhotoRemark() {
		return patrolPhotoRemark;
	}

	public void setPatrolPhotoRemark(String patrolPhotoRemark) {
		this.patrolPhotoRemark = patrolPhotoRemark;
	}

	public Integer getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(Integer routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	
	
	public Integer getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(Integer lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Override
	public String toString() {
		return "PatrolRouteActionLog [key=" + key + ", actionType=" + actionType + ", actionDateTime=" + actionDateTime
				+ ", patrolPhotoKey=" + patrolPhotoKey + ", patrolPhotoRemark=" + patrolPhotoRemark + ", routeDefKey="
				+ routeDefKey + ", lastModifyBy=" + lastModifyBy + "]";
	}

	
	
}
