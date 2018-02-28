package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.FailureClassManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.ProblemCodeManager;
import hk.ebsl.mfms.manager.StatusManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.report.template.object.DefectListReport;
import hk.ebsl.mfms.report.template.object.DefectStatusSummaryReport;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class DefectDao extends BaseDao {

	@Autowired
	private LocationManager locationManager;

	@Autowired
	private ProblemCodeManager problemCodeManager;

	@Autowired
	private FailureClassManager failureClassManager;

	@Autowired
	private StatusManager statusManager;

	@Autowired
	private UserAccountManager userAccountManager;

	public static final Logger logger = Logger.getLogger(DefectDao.class);

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	@SuppressWarnings("unchecked")
	public List<Defect> getAllDefect() {

		return getDefaultDefectCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public synchronized String getNextDefectCode(Integer siteKey) {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Defect.class);
		criteria.add(Restrictions.eq("siteKey", siteKey));
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("key"));

		if (criteria.list().size() != 0) {

			Defect d = (Defect) criteria.list().get(criteria.list().size() - 1);

			int nextCode = Integer.parseInt(d.getCode()) + 1;

			return Integer.toString(nextCode);
		} else
			return Integer.toString(1);
	}

	@SuppressWarnings("unchecked")
	public boolean hasLocation(Integer locationKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (locationKey != null) {
			criteria.add(Restrictions.eq("locationKey", locationKey));
		}

		List<Defect> list = criteria.list();

		if (list.size() > 0)
			return true;
		else
			return false;

	}

	@SuppressWarnings("unchecked")
	public boolean hasCauseCode(Integer causeCodeKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (causeCodeKey != null) {
			criteria.add(Restrictions.eq("causeCodeKey", causeCodeKey));
		}

		List<Defect> list = criteria.list();

		if (list.size() > 0)
			return true;
		else
			return false;

	}

	@SuppressWarnings("unchecked")
	public boolean hasTool(Integer toolKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (toolKey != null) {
			criteria.add(Restrictions.eq("toolKey", toolKey));
		}

		List<Defect> list = criteria.list();

		if (list.size() > 0)
			return true;
		else
			return false;

	}

	@SuppressWarnings("unchecked")
	public boolean hasEquipment(Integer equipmentKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (equipmentKey != null) {
			criteria.add(Restrictions.eq("equipmentKey", equipmentKey));
		}

		List<Defect> list = criteria.list();

		if (list.size() > 0)
			return true;
		else
			return false;

	}

	@SuppressWarnings("unchecked")
	public Defect getDefectByKey(Integer key) {
		Criteria criteria = getDefaultDefectCriteria();
		criteria.add(Restrictions.eq("key", key));
		criteria.add(Restrictions.eq("deleted", "N"));

		List<Defect> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public int searchDefectCount(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer accountKey,
			List<Integer> privilegeLocationKey, Integer groupKey) {
		
		Criteria criteria = getDefaultDefectCriteria();

		criteria.createAlias("location", "l");

		criteria.add(Restrictions.eq("deleted", "N"));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		if(groupKey != null){
			criteria.add(Restrictions.eq("assignedGroupKey", groupKey));
		}

		if (accountKey != null) {
			criteria.add(Restrictions.eq("assignedAccountKey", accountKey));
		}

		if (code != null && !code.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.eq("code", DaoUtil.escape(code)));
		}

		if (desc != null && !desc.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("desc", DaoUtil.escape(desc),
					MatchMode.ANYWHERE));
		}

		if (locationKey != null) {
			// case insensitive
			// criteria.add(Restrictions.eq("locationKey", locationKey));

			Location l = getLocation(locationKey);

			criteria.add(Restrictions.or(
					Restrictions.eq("l.chain", l.getChain()),
					Restrictions.like("l.chain", l.getChain() + ",%")));

		}

		if (status != null && !status.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("statusID", DaoUtil.escape(status),
					MatchMode.ANYWHERE));
		}

		if (priority != null) {
			criteria.add(Restrictions.eq("priority", priority));
		}

		if (failureClassKey != null) {
			criteria.add(Restrictions.eq("failureClassKey", failureClassKey));
		}

		if (problemCodeKey != null) {
			criteria.add(Restrictions.eq("problemCodeKey", problemCodeKey));
		}
		if (causeCodeKey != null) {
			criteria.add(Restrictions.eq("causeCodeKey", causeCodeKey));
		}

		// List<Defect> list = criteria.list();
//		if (accountGroupAccount != null){
//			if (accountGroupAccount.isEmpty()) {
//				criteria.add(Restrictions.in("assignedAccountKey",
//						new Integer[] { -1 }));
//			} else {
//				criteria.add(Restrictions.in("assignedAccountKey",
//						accountGroupAccount));
//			}
//		}
		if (privilegeLocationKey != null)
			criteria.add(Restrictions.in("locationKey", privilegeLocationKey));

		return ((Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
		
		
	}

	@SuppressWarnings("unchecked")
	public int searchDefectCount(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer accountKey,
			List<Integer> accountGroupAccount,
			List<Integer> privilegeLocationKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (accountKey != null) {
			criteria.add(Restrictions.eq("assignedAccountKey", accountKey));
		}

		if (code != null && !code.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.eq("code", DaoUtil.escape(code)));
		}

		if (desc != null && !desc.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("desc", DaoUtil.escape(desc),
					MatchMode.ANYWHERE));
		}

		if (locationKey != null) {
			
			criteria.createAlias("location", "l");
			// case insensitive
			// criteria.add(Restrictions.eq("locationKey", locationKey));

			Location l = getLocation(locationKey);

			criteria.add(Restrictions.or(
					Restrictions.eq("l.chain", l.getChain()),
					Restrictions.like("l.chain", l.getChain() + ",%")));

		}

		if (status != null && !status.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("statusID", DaoUtil.escape(status),
					MatchMode.ANYWHERE));
		}

		if (priority != null) {
			criteria.add(Restrictions.eq("priority", priority));
		}

		if (failureClassKey != null) {
			criteria.add(Restrictions.eq("failureClassKey", failureClassKey));
		}

		if (problemCodeKey != null) {
			criteria.add(Restrictions.eq("problemCodeKey", problemCodeKey));
		}
		if (causeCodeKey != null) {
			criteria.add(Restrictions.eq("causeCodeKey", causeCodeKey));
		}

		// List<Defect> list = criteria.list();
		if (accountGroupAccount != null)
			if (accountGroupAccount.isEmpty()) {
				criteria.add(Restrictions.in("assignedAccountKey",
						new Integer[] { -1 }));
			} else {
				criteria.add(Restrictions.in("assignedAccountKey",
						accountGroupAccount));
			}
		if (privilegeLocationKey != null && !privilegeLocationKey.isEmpty())
			criteria.add(Restrictions.in("locationKey", privilegeLocationKey));

		return ((Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();

	}

	
	public List<Defect> searchDefect(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer accountKey, Integer groupKey){
		
		
		Criteria criteria = getDefaultDefectCriteria();

		criteria.createAlias("location", "l");

		criteria.add(Restrictions.eq("deleted", "N"));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		if(groupKey != null){
			criteria.add((Restrictions.eq("assignedGroupKey",groupKey)));
		}

		if (accountKey != null) {
			criteria.add(Restrictions.eq("assignedAccountKey", accountKey));
		}

		if (code != null && !code.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.eq("code", DaoUtil.escape(code)));
		}

		if (desc != null && !desc.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("desc", DaoUtil.escape(desc),
					MatchMode.ANYWHERE));
		}

		if (locationKey != null) {
			// case insensitive
			// criteria.add(Restrictions.eq("locationKey", locationKey));

			Location l = getLocation(locationKey);

			criteria.add(Restrictions.or(
					Restrictions.eq("l.chain", l.getChain()),
					Restrictions.like("l.chain", l.getChain() + ",%")));

		}

		if (status != null && !status.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("statusID", DaoUtil.escape(status),
					MatchMode.ANYWHERE));
		}

		if (priority != null) {
			criteria.add(Restrictions.eq("priority", priority));
		}

		if (failureClassKey != null) {
			criteria.add(Restrictions.eq("failureClassKey", failureClassKey));
		}

		if (problemCodeKey != null) {
			criteria.add(Restrictions.eq("problemCodeKey", problemCodeKey));
		}
		if (causeCodeKey != null) {
			criteria.add(Restrictions.eq("causeCodeKey", causeCodeKey));
		}

		List<Defect> list = criteria.list();

		return list;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Defect> searchDefect(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer accountKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.createAlias("location", "l");

		criteria.add(Restrictions.eq("deleted", "N"));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (accountKey != null) {
			criteria.add(Restrictions.eq("assignedAccountKey", accountKey));
		}

		if (code != null && !code.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.eq("code", DaoUtil.escape(code)));
		}

		if (desc != null && !desc.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("desc", DaoUtil.escape(desc),
					MatchMode.ANYWHERE));
		}

		if (locationKey != null) {
			// case insensitive
			// criteria.add(Restrictions.eq("locationKey", locationKey));

			Location l = getLocation(locationKey);

			criteria.add(Restrictions.or(
					Restrictions.eq("l.chain", l.getChain()),
					Restrictions.like("l.chain", l.getChain() + ",%")));

		}

		if (status != null && !status.isEmpty()) {
			// case insensitive
			criteria.add(Restrictions.ilike("statusID", DaoUtil.escape(status),
					MatchMode.ANYWHERE));
		}

		if (priority != null) {
			criteria.add(Restrictions.eq("priority", priority));
		}

		if (failureClassKey != null) {
			criteria.add(Restrictions.eq("failureClassKey", failureClassKey));
		}

		if (problemCodeKey != null) {
			criteria.add(Restrictions.eq("problemCodeKey", problemCodeKey));
		}
		if (causeCodeKey != null) {
			criteria.add(Restrictions.eq("causeCodeKey", causeCodeKey));
		}

		List<Defect> list = criteria.list();

		return list;

	}

	@SuppressWarnings("unchecked")
	public int searchDefectListCount(Integer siteKey, Timestamp from,
			Timestamp to, String code, Integer accKey, Integer locationKey,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, Integer equipmentKey, Integer priority,
			String callFrom, String status, Integer accountKey,
			List<Integer> accountGroupAccount) throws MFMSException {

		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<Status> statusList = statusManager.getAllStatus();

		List<UserAccountRole> siteUserAccountRoleList = userAccountManager
				.searchUserAccountRole(siteKey, null, null, null, null);

		for (UserAccountRole ar : siteUserAccountRoleList) {

			// do not add duplicate if any
			boolean exist = false;
			for (UserAccount a : accountList) {
				if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
					exist = true;
				}
			}
			if (!exist) {
				accountList.add(ar.getUserAccount());
			}
		}

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();

		for (Status s : statusList)
			statusMap.put(s.getStatusId(), s.getName());

		for (UserAccount acc : accountList)
			accountMap.put(acc.getKey(), acc.getLoginId());

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (from != null && to != null) {

			criteria.add(Restrictions.between("issueDateTime", from, to));
		}

		if (!code.isEmpty() || code == null) {
			// case insensitive
			criteria.add(Restrictions.eq("code", DaoUtil.escape(code)));
		}

		if (locationKey != null) {
			// case insensitive
			// criteria.add(Restrictions.eq("locationKey", locationKey));

			Location l = getLocation(locationKey);

			criteria.createAlias("location", "l").add(
					Restrictions.ilike("l.chain", l.getChain(),
							MatchMode.ANYWHERE));

		}

		if (accKey != null) {
			// case insensitive
			criteria.add(Restrictions.eq("assignedAccountKey", accKey));
		}

		if (!status.isEmpty() || status == null) {
			// case insensitive
			criteria.add(Restrictions.ilike("statusID", DaoUtil.escape(status),
					MatchMode.ANYWHERE));
		}

		if (priority != null) {
			criteria.add(Restrictions.eq("priority", priority));
		}

		if (failureClassKey != null) {
			criteria.add(Restrictions.eq("failureClassKey", failureClassKey));
		}

		if (problemCodeKey != null) {
			criteria.add(Restrictions.eq("problemCodeKey", problemCodeKey));
		}
		if (causeCodeKey != null) {
			criteria.add(Restrictions.eq("causeCodeKey", causeCodeKey));
		}

		if (equipmentKey != null) {
			criteria.add(Restrictions.eq("equipmentKey", equipmentKey));
		}
		
		if(accountGroupAccount!=null){
			criteria.add(Restrictions.in("assignedAccountKey", accountGroupAccount));
		}

		List<Defect> list = criteria.list();

		List<Defect> filteredFullList = new ArrayList<Defect>();

		List<Integer> privilegedLocationKeyList = locationManager
				.getPrivilegedLocationKeyList(siteKey, accountKey);

		if (privilegedLocationKeyList != null) {
			criteria.add(Restrictions.in("locationKey",
					privilegedLocationKeyList));
		}

		// for (Defect d : list) {
		//
		// if (privilegedLocationKeyList.contains(d.getLocationKey())) {
		//
		// filteredFullList.add(d);
		// }
		// }

		// List<DefectListReport> aa = new ArrayList<DefectListReport>();
		//
		// for (Defect d : filteredFullList) {
		//
		// DefectListReport a = new DefectListReport();
		//
		// a.setFieldCode(d.getCode());
		//
		// if (convertTimestampToString(d.getIssueDateTime()) != null)
		// a.setFieldIssueDate(convertTimestampToString(d
		// .getIssueDateTime()));
		// else
		// a.setFieldIssueDate(" - ");
		//
		// a.setFieldFailureClass(d.getFailureClass().getCode());
		// a.setFieldProblemCode(d.getProblemCode().getCode());
		// a.setFieldLocation(d.getLocation().getCode());
		// a.setFieldDesc(d.getDesc());
		// a.setFieldCallFrom(d.getCallFrom());
		// a.setFieldContactNumber(d.getContactName());
		//
		// if (d.getAssignedAccountKey() != null)
		// a.setFieldAssignedAccount(accountMap.get(d
		// .getAssignedAccountKey()));
		// else
		// a.setFieldAssignedAccount(" - ");
		//
		// a.setFieldPriority(d.getPriority().toString());
		//
		// if (convertTimestampToString(d.getTargetFinishDateTime()) != null)
		// a.setFieldTargetFinishDate(convertTimestampToString(d
		// .getTargetFinishDateTime()));
		// else
		// a.setFieldTargetFinishDate(" - ");
		//
		// if (convertTimestampToString(d.getActualFinishDateTime()) != null)
		// a.setFieldActualFinishDate(convertTimestampToString(d
		// .getActualFinishDateTime()));
		// else
		// a.setFieldActualFinishDate(" - ");
		//
		// a.setFieldMeetKpi(d.getMeetKpi());
		//
		// if (!d.getMeetKpi().contains("-")) {
		// Long time = d.getActualFinishDateTime().getTime()
		// - d.getTargetFinishDateTime().getTime();
		// a.setFieldDeviation("" + time / 1000 / 60);
		// } else
		// a.setFieldDeviation(" - ");
		//
		// a.setFieldRemark(d.getRemarks());
		//
		// String s = statusMap.get((String) d.getStatusID());
		// a.setFieldStatus(s == null ? "-" : s);
		//
		// aa.add(a);
		// }

		return ((Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();

	}

	@SuppressWarnings("unchecked")
	public List<DefectListReport> searchDefectList(Integer siteKey,
			Timestamp from, Timestamp to, String code, Integer accKey,
			Integer locationKey, Integer failureClassKey,
			Integer problemCodeKey, Integer causeCodeKey, Integer equipmentKey,
			Integer priority, String callFrom, String status, Integer accountKey, Integer groupKey)
			throws MFMSException {

		
		
		
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<Status> statusList = statusManager.getAllStatus();

		List<UserAccountRole> siteUserAccountRoleList = userAccountManager
				.searchUserAccountRole(siteKey, null, null, null, null);

		for (UserAccountRole ar : siteUserAccountRoleList) {

			// do not add duplicate if any
			boolean exist = false;
			for (UserAccount a : accountList) {
				if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
					exist = true;
				}
			}
			if (!exist) {
				accountList.add(ar.getUserAccount());
			}
		}

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();

		for (Status s : statusList)
			statusMap.put(s.getStatusId(), s.getName());

		for (UserAccount acc : accountList)
			accountMap.put(acc.getKey(), acc.getLoginId());

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (from != null && to != null) {

			criteria.add(Restrictions.between("issueDateTime", from, to));
		}

		if (!code.isEmpty() || code == null) {
			// case insensitive
			criteria.add(Restrictions.eq("code", DaoUtil.escape(code)));
		}

		if (locationKey != null) {
			// case insensitive
			// criteria.add(Restrictions.eq("locationKey", locationKey));

			Location l = getLocation(locationKey);

			criteria.createAlias("location", "l").add(
					Restrictions.ilike("l.chain", l.getChain(),
							MatchMode.ANYWHERE));

		}

		if (accKey != null) {
			// case insensitive
			criteria.add(Restrictions.eq("assignedAccountKey", accKey));
		}
		
		if(groupKey != null){
			criteria.add(Restrictions.eq("assignedGroupKey", groupKey));
		}

		if (!status.isEmpty() || status == null) {
			// case insensitive
			criteria.add(Restrictions.ilike("statusID", DaoUtil.escape(status),
					MatchMode.ANYWHERE));
		}

		if (priority != null) {
			criteria.add(Restrictions.eq("priority", priority));
		}

		if (failureClassKey != null) {
			criteria.add(Restrictions.eq("failureClassKey", failureClassKey));
		}

		if (problemCodeKey != null) {
			criteria.add(Restrictions.eq("problemCodeKey", problemCodeKey));
		}
		if (causeCodeKey != null) {
			criteria.add(Restrictions.eq("causeCodeKey", causeCodeKey));
		}

		if (equipmentKey != null) {
			criteria.add(Restrictions.eq("equipmentKey", equipmentKey));
		}

		List<Defect> list = criteria.list();

		List<Defect> filteredFullList = new ArrayList<Defect>();

		List<Integer> privilegedLocationKeyList = locationManager
				.getPrivilegedLocationKeyList(siteKey, accountKey);

		for (Defect d : list) {

			if (privilegedLocationKeyList.contains(d.getLocationKey())) {

				filteredFullList.add(d);
			}
		}

		List<DefectListReport> aa = new ArrayList<DefectListReport>();

		for (Defect d : filteredFullList) {

			DefectListReport a = new DefectListReport();

			a.setFieldCode(d.getCode());

			if (convertTimestampToString(d.getIssueDateTime()) != null)
				a.setFieldIssueDate(convertTimestampToString(d
						.getIssueDateTime()));
			else
				a.setFieldIssueDate(" - ");

			a.setFieldFailureClass(d.getFailureClass().getCode());
			a.setFieldProblemCode(d.getProblemCode().getCode());
			a.setFieldLocation(d.getLocation().getCode());
			a.setFieldDesc(d.getDesc());
			a.setFieldCallFrom(d.getCallFrom());
			a.setFieldContactNumber(d.getContactName());

			if (d.getAssignedAccountKey() != null)
				a.setFieldAssignedAccount(accountMap.get(d
						.getAssignedAccountKey()));
			else
				a.setFieldAssignedAccount(" - ");

			a.setFieldPriority(d.getPriority().toString());

			if (convertTimestampToString(d.getTargetFinishDateTime()) != null)
				a.setFieldTargetFinishDate(convertTimestampToString(d
						.getTargetFinishDateTime()));
			else
				a.setFieldTargetFinishDate(" - ");

			if (convertTimestampToString(d.getActualFinishDateTime()) != null)
				a.setFieldActualFinishDate(convertTimestampToString(d
						.getActualFinishDateTime()));
			else
				a.setFieldActualFinishDate(" - ");

			a.setFieldMeetKpi(d.getMeetKpi());

			if (d.getMeetKpi() != null && !d.getMeetKpi().contains("-")) {
				Long time = d.getActualFinishDateTime().getTime()
						- d.getTargetFinishDateTime().getTime();
				a.setFieldDeviation("" + time / 1000 / 60);
			} else
				a.setFieldDeviation(" - ");

			a.setFieldRemark(d.getRemarks());

			String s = statusMap.get((String) d.getStatusID());
			a.setFieldStatus(s == null ? "-" : s);

			aa.add(a);
		}

		return aa;

	}

	@SuppressWarnings("unchecked")
	public List<Defect> searchDefectByStatusTargetStartDateTime(String status,
			Timestamp targetStartFrom, Timestamp targetStartTo) {
		logger.debug("searchDefectByTargetStartDateTime()[" + status + ","
				+ targetStartFrom + "," + targetStartTo + "]");

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (status != null) {
			// case insensitive
			criteria.add(Restrictions.ilike("statusID", DaoUtil.escape(status),
					MatchMode.ANYWHERE));
		}

		if (targetStartFrom != null) {
			criteria.add(Restrictions
					.ge("targetStartDateTime", targetStartFrom));
		}
		if (targetStartTo != null) {
			criteria.add(Restrictions.le("targetStartDateTime", targetStartTo));
		}

		List<Defect> list = criteria.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Defect> searchDefectByAssignedTargetStartDateTime(
			Integer assignedAccountKey, Timestamp targetStartFrom,
			Timestamp targetStartTo) {
		logger.debug("searchDefectByTargetStartDateTime()["
				+ assignedAccountKey + "," + targetStartFrom + ","
				+ targetStartTo + "]");

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.eq("deleted", "N"));

		if (assignedAccountKey != null) {
			criteria.add(Restrictions.eq("assignedAccountKey",
					assignedAccountKey));
		}

		if (targetStartFrom != null) {
			criteria.add(Restrictions
					.ge("targetStartDateTime", targetStartFrom));
		}
		if (targetStartTo != null) {
			criteria.add(Restrictions.le("targetStartDateTime", targetStartTo));
		}

		List<Defect> list = criteria.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Defect> searchGroupDefectByAssignedTargetStartDateTime(
			Integer assignedGroupKey, Timestamp targetStartFrom,
			Timestamp targetStartTo) {
		logger.debug("searchGroupDefectByAssignedTargetStartDateTime()["
				+ assignedGroupKey + "," + targetStartFrom + ","
				+ targetStartTo + "]");

		Criteria criteria = getDefaultDefectCriteria();

		if (assignedGroupKey != null) {
			criteria.add(Restrictions.eq("assignedGroupKey", assignedGroupKey));
		}

		if (targetStartFrom != null) {
			criteria.add(Restrictions
					.ge("targetStartDateTime", targetStartFrom));
		}
		if (targetStartTo != null) {
			criteria.add(Restrictions.le("targetStartDateTime", targetStartTo));
		}

		List<Defect> list = criteria.list();

		return list;
	}

	public synchronized Integer saveDefect(Defect defect) {
		logger.debug("saveDefect()");
		Session currentSession = getSession();
		currentSession.saveOrUpdate(defect);

		return defect.getKey();
	}

	@SuppressWarnings("unchecked")
	public Defect getDefectByCode(Integer siteKey, String code, String deleted) {

		logger.debug("getDefectByCode()[" + siteKey + "," + code + "]");

		Criteria criteria = getDefaultDefectCriteria().add(
				Restrictions.eq("siteKey", siteKey)).add(
				Restrictions.eq("code", code));

		criteria.add(Restrictions.eq("deleted", "N"));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<Defect> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public List<Defect> getDefectByLastModifyDateAndSiteKey(Timestamp time,
			Integer siteKey, Integer accountKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.gt("lastModifyDateTime", time));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (accountKey != null) {
			criteria.add(Restrictions.eq("assignedAccountKey", accountKey));
		}

		List<Defect> list = criteria.list();

		return list;

	}

	@SuppressWarnings("unchecked")
	public List<Defect> getDefectByDelta(Timestamp time, Integer siteKey,
			Integer accountKey, Integer[] groupKey) {

		Criteria criteria = getDefaultDefectCriteria();

		criteria.add(Restrictions.gt("lastModifyDateTime", time));

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (groupKey != null && accountKey != null) {
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.eq("assignedAccountKey", accountKey))
					.add(Restrictions.in("assignedGroupKey", groupKey)));

		} else {

			if (accountKey != null) {
				criteria.add(Restrictions.eq("assignedAccountKey", accountKey));
			}

			if (groupKey != null) {
				criteria.add(Restrictions.in("assignedGroupKey", groupKey));
			}
		}

		List<Defect> list = criteria.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<DefectStatusSummaryReport> searchDefectStatusSummary(
			Integer siteKey, Timestamp from, Timestamp to, Integer accountKey,
			String[] statusMapValue, Integer locationKey,
			Integer failureClassKey, Integer problemCodeKey,
			List<Integer> privilegeLocationKey) throws MFMSException {

		List<Integer> privilegedLocationKeyList = locationManager
				.getPrivilegedLocationKeyList(siteKey, accountKey);

		List<Location> locationList = locationManager
				.getLocationsBySiteKey(siteKey);
		List<ProblemCode> problemCodeList = problemCodeManager
				.searchProblemCode(siteKey, null, null, null, null);
		List<FailureClass> failureClassList = failureClassManager
				.searchFailureClass(siteKey, null, null);
		List<Status> statusList = statusManager.getAllStatus();
		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<String, String> statusSeqMap = new LinkedHashMap<String, String>();

		for (Location l : locationList)
			locationMap.put(l.getKey(), l.getCode() + " - " + l.getName());

		for (ProblemCode pc : problemCodeList)
			problemCodeMap
					.put(pc.getKey(), pc.getCode() + " - " + pc.getName());

		for (FailureClass fc : failureClassList)
			failureClassMap.put(fc.getKey(),
					fc.getCode() + " - " + fc.getName());

		for (Status s : statusList)
			statusMap.put(s.getStatusId(), s.getName());

		for (Status s : statusList)
			statusSeqMap.put(s.getStatusId(), s.getSequence().toString());

		ProjectionList projectionList = Projections.projectionList();
		Criteria criteria = getDefaultDefectCriteria();

		criteria.createAlias("location", "l");

		projectionList.add(Projections.groupProperty("locationKey"));
		projectionList.add(Projections.groupProperty("failureClassKey"));
		projectionList.add(Projections.groupProperty("problemCodeKey"));
		projectionList.add(Projections.groupProperty("priority"));
		projectionList.add(Projections.groupProperty("statusID"));
		projectionList.add(Projections.rowCount());

		if (locationKey != null) {
			// case insensitive
			// criteria.add(Restrictions.eq("locationKey", locationKey));

			Location l = getLocation(locationKey);

			criteria.add(Restrictions.or(
					Restrictions.eq("l.chain", l.getChain()),
					Restrictions.like("l.chain", l.getChain() + ",%")));

		}

		if (failureClassKey != null) {
			criteria.add(Restrictions.eq("failureClassKey", failureClassKey));
		}

		if (problemCodeKey != null) {
			criteria.add(Restrictions.eq("problemCodeKey", problemCodeKey));
		}

		if (convertTimestampToString(from) != null
				&& convertTimestampToString(to) != null) {
			criteria.add(Restrictions.between("issueDateTime", from, to));
		} else if (convertTimestampToString(from) != null
				&& convertTimestampToString(to) == null) {
			criteria.add(Restrictions.gt("issueDateTime", from));
		} else if (convertTimestampToString(from) == null
				&& convertTimestampToString(to) != null) {
			criteria.add(Restrictions.lt("issueDateTime", to));
		}

		criteria.setProjection(projectionList);
		criteria.add(Restrictions.eq("deleted", "N"));
		criteria.add(Restrictions.eq("siteKey", siteKey));
		if (statusMapValue.length > 0)
			criteria.add(Restrictions.in("statusID", statusMapValue));

		if (privilegeLocationKey != null)
			criteria.add(Restrictions.in("locationKey", privilegeLocationKey));

		List<DefectStatusSummaryReport> aa = new ArrayList<DefectStatusSummaryReport>();

		List<Object[]> list = criteria.list();

		for (Object[] arr : list) {

			if (privilegedLocationKeyList.contains((Integer) arr[0])) {

				String location = locationMap.get((Integer) arr[0]);

				String failureClass = failureClassMap.get((Integer) arr[1]);

				String problemCode = problemCodeMap.get((Integer) arr[2]);

				Integer priority = (Integer) arr[3];

				String status = statusMap.get((String) arr[4]);

				String statusSeq = statusSeqMap.get((String) arr[4]);

				Long count = (Long) arr[5];

				DefectStatusSummaryReport d = new DefectStatusSummaryReport();

				d.setFieldLocation(location);
				d.setFieldFailureClass(failureClass);
				d.setFieldProblemCode(problemCode);
				d.setFieldPriority(priority.toString());
				d.setFieldStatus(status == null ? "-" : status);
				d.setFieldStatusSeq(statusSeq == null ? "-" : statusSeq);
				d.setFieldNumberOfWorkOrder(count.toString());

				aa.add(d);
			}
		}

		// for (DefectStatusSummaryReport d : aa)
		// logger.debug("*** [ " + d.getFieldStatus() + ", " +
		// d.getFieldLocation() + ", " + d.getFieldFailureClass()
		// + ", " + d.getFieldProblemCode() + ", " + d.getFieldPriority() +
		// d.getFieldNumberOfWorkOrder()
		// + " ]");

		return aa;

	}
	
	@SuppressWarnings("unchecked")
	public List<Defect> searchDefect(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Defect.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<Defect> defectList = criteria.list();

		return defectList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Defect.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}

	public String convertTimestampToString(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = sdf.format(ts);
			if (timeString.length() > 0)
				return timeString;
			else
				return null;
		} else
			return null;
	}

	private Criteria getDefaultDefectCriteria() {

		// default criteria for defect
		Session currentSession = getSession();
		return currentSession.createCriteria(Defect.class);
	}

	@SuppressWarnings("unchecked")
	public Location getLocation(Integer lKey) {

		Criteria criteria = locationCriteria();

		if (lKey != null) {
			criteria.add(Restrictions.eq("key", lKey));
		}

		List<Location> list = criteria.list();

		return list.get(0);

	}


	private Criteria locationCriteria() {

		// default criteria for defect
		Session currentSession = getSession();
		return currentSession.createCriteria(Location.class);
	}

	public ProblemCodeManager getProblemCodeManager() {
		return problemCodeManager;
	}

	public void setProblemCodeManager(ProblemCodeManager problemCodeManager) {
		this.problemCodeManager = problemCodeManager;
	}

	public FailureClassManager getFailureClassManager() {
		return failureClassManager;
	}

	public void setFailureClassManager(FailureClassManager failureClassManager) {
		this.failureClassManager = failureClassManager;
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	public void setStatusManager(StatusManager statusManager) {
		this.statusManager = statusManager;
	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

}
