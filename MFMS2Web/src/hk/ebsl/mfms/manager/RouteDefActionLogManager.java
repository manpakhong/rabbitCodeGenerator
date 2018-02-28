package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteDefActionLog;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface RouteDefActionLogManager {
	public List<RouteDefActionLog> searchRouteDefActionLog(Integer siteKey, Integer accountKey, Integer routeDefKey,
			Timestamp from, Timestamp to) throws MFMSException;

	public Integer saveRouteDefActionLog(RouteDefActionLog routeDefActionLog) throws MFMSException;

}
