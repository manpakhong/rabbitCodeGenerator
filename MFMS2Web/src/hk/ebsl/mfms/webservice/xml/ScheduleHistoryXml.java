package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DefectSchedule")
public class ScheduleHistoryXml extends BaseXml {

	private Integer key;
	
	private Integer scheduleKey;

	private Long finishDateTime;

	private Long taskDateTime;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getScheduleKey() {
		return scheduleKey;
	}

	public void setScheduleKey(Integer scheduleKey) {
		this.scheduleKey = scheduleKey;
	}

	public Long getFinishDateTime() {
		return finishDateTime;
	}

	public void setFinishDateTime(Long finishDateTime) {
		this.finishDateTime = finishDateTime;
	}

	public Long getTaskDateTime() {
		return taskDateTime;
	}

	public void setTaskDateTime(Long taskDateTime) {
		this.taskDateTime = taskDateTime;
	}
	
	


}
