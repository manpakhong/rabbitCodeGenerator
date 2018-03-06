package hk.ebsl.mfms.manager;

import java.util.List;

import hk.ebsl.mfms.dto.AttendanceConfig;

public interface AttendanceConfigManager {
	public List<AttendanceConfig> getAttendanceConfigListByAccountKey(Integer accountKey);
}
