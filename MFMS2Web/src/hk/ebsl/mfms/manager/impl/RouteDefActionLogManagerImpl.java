package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.RouteDefActionLogDao;
import hk.ebsl.mfms.dao.RouteDefDao;
import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteDefActionLog;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.RouteDefActionLogManager;

public class RouteDefActionLogManagerImpl implements RouteDefActionLogManager {

	public static final Logger logger = Logger.getLogger(RouteDefActionLogManagerImpl.class);

	private RouteDefActionLogDao routeDefActionLogDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RouteDefActionLog> searchRouteDefActionLog(Integer siteKey, Integer accountKey, Integer routeDefKey,
			Timestamp from, Timestamp to) throws MFMSException {

		List<RouteDefActionLog> list = routeDefActionLogDao.searchRouteDefActionLog(siteKey, accountKey, routeDefKey,
				from, to);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer saveRouteDefActionLog(RouteDefActionLog routeDefActionLog) throws MFMSException {
		// TODO Auto-generated method stub
		return routeDefActionLogDao.saveRouteDefActionLog(routeDefActionLog);

	}

	public void test() {

	}

	public RouteDefActionLogDao getRouteDefActionLogDao() {
		return routeDefActionLogDao;
	}

	public void setRouteDefActionLogDao(RouteDefActionLogDao routeDefActionLogDao) {
		this.routeDefActionLogDao = routeDefActionLogDao;
	}
}
