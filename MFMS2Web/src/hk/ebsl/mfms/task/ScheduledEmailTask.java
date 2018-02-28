package hk.ebsl.mfms.task;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectEmail;
import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.email.EmailTemplate;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.manager.AccountGroupResponsibleManager;
import hk.ebsl.mfms.manager.DefectEmailManager;
import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.manager.DefectScheduleManager;
import hk.ebsl.mfms.manager.EmailManager;
import hk.ebsl.mfms.manager.EquipmentManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.utility.CalendarEvent;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class ScheduledEmailTask {

	public static final Logger logger = Logger.getLogger(ScheduledEmailTask.class);

	private EmailManager emailManager;

	private DefectManager defectManager;

	private DefectScheduleManager defectScheduleManager;

	private EquipmentManager equipmentManager;

	private UserAccountManager userAccountManager;

	private DefectEmailManager defectEmailManager;

	private AccountGroupManager accountGroupManager;

	private AccountGroupResponsibleManager accountGroupResponsibleManager;

	private LocationManager locationManager;

	private SiteManager siteManager;

	private CalendarEvent calendarEvent;

	public static final String EXP_WO_TEMP_ID = "expiringWorkOrder";
	public static final String EXP_MGR_WO_TEMP_ID = "expiringGroupWorkOrder";
	public static final String DAILY_WO_TEMP_ID = "dailyWorkOrder";
	public static final String DAILY_WO_SUB_TEMP_ID = "dailyWorkOrderSub";
	public static final String DAILY_MGR_WO_TEMP_ID = "dailyGroupWorkOrder";
	public static final String DAILY_MGR_WO_SUB_TEMP_ID = "dailyGroupWorkOrderSub";
	public static final String DAILY_MS_TEMP_ID = "dailyMaintenanceSchedule";
	public static final String DAILY_MS_SUB_TEMP_ID = "dailyMaintenanceScheduleSub";
	public static final String DAILY_EXP_MS_TEMP_ID = "dailyExpiredMaintenanceSchedule";
	public static final String DAILY_EXP_MS_SUB_TEMP_ID = "dailyExpiredMaintenanceScheduleSub";

	private static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	// Send defect reminder to assigned person before 120 minutes of “Target
	// Start Dateþý
	public void emailExpiringWorkOrder() {

		logger.debug("emailExpiringWorkOrder");

		int expireTime = 120;

		String templateId = EXP_WO_TEMP_ID;

		try {
			List<Defect> list = defectManager.searchExpiringDefect(expireTime);

			logger.debug("Work Orders found: " + (list == null ? 0 : list.size()));

			for (Defect d : list) {

				if (!"C".equalsIgnoreCase(d.getStatusID())) {

					// not completed: send email to assigned person

					logger.debug("Work Order: " + d.getKey());

					Integer siteKey = d.getSiteKey();

					String failureClassCode = d.getFailureClass().getCode();
					String problemCode = d.getProblemCode().getCode();
					String locationCode = d.getLocation().getCode() + " - " + d.getLocation().getName();

					UserAccount assignedPerson = userAccountManager
							.getUserAccountByAccountKey(d.getAssignedAccountKey());

					if (assignedPerson == null) {
						logger.error("No assigned person.");
						continue;
					}

					if (assignedPerson.getEmail() == null || StringUtils.isEmpty(assignedPerson.getEmail())) {
						logger.error("NO email address of assigned person.");
						continue;
					}

					// format only if it is not null
					String issueDateStr = d.getIssueDateTime() == null ? ""
							: DATETIME_FORMATTER.format(d.getIssueDateTime());
					String targetFinishDateStr = d.getTargetFinishDateTime() == null ? ""
							: DATETIME_FORMATTER.format(d.getTargetFinishDateTime());

					Site site = siteManager.getSiteByKey(d.getSiteKey());

					Object[] param = new Object[] { d.getCode(), issueDateStr, failureClassCode, problemCode,
							d.getDesc(), locationCode, d.getContactName(), d.getContactTel(), assignedPerson.getName(),
							d.getPriority(), DATETIME_FORMATTER.format(d.getTargetStartDateTime()), targetFinishDateStr,
							d.getRemarks(), site.getName() };

					DefectEmail de = new DefectEmail();

					de.setDefectKey(d.getKey());
					de.setEmail(assignedPerson.getEmail() == null ? "" : assignedPerson.getEmail());
					de.setType(templateId);
					de.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
					de.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

					try {
						// check defect email status
						List<DefectEmail> deList = defectEmailManager.searchDefectEmail(d.getKey(), de.getEmail(),
								templateId, null);

						if (deList != null && deList.size() > 0) {

							logger.debug("Defect Email List size = ");

							// there is a defect email
							if (DefectEmail.SUCCESS.equals(deList.get(0).getResult())) {
								// email was sent successfully
								logger.debug("Defect Email was sent");
								continue;
							} else {
								// email was failed, use old info
								de.setKey(deList.get(0).getKey());
								de.setCreateDateTime(deList.get(0).getCreateDateTime());
							}
						} else {
							logger.debug("There is no defect email.");
						}

						// commented for testing

						logger.debug("****************** email : " + assignedPerson.getEmail());

						emailManager.sendTemplate(templateId, siteKey, new String[] { assignedPerson.getEmail() },
								param);
						// emailManager.sendTemplate(templateId, siteKey, new
						// String[] {"tlau@ebsl.hk"}, param);

						// sent
						logger.debug("Defect Email was sent");
						de.setResult(DefectEmail.SUCCESS);

					} catch (MFMSException e) {

						logger.error("Failed to send Defect Email: " + e.getMessage());

						de.setResult(DefectEmail.FAILED);

					}

					logger.debug("Going to save DefectMail");
					defectEmailManager.saveDefectEmail(de);
				} else {
					// completed: do nothing
				}
			}
		} catch (Exception | MFMSException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	// Send defect reminder to manager before 30 minutes of “Target Start Dateþý
	public void emailExpiringGroupWorkOrder() {

		logger.debug("emailExpiringGroupWorkOrder");

		int expireTime = 30;

		String templateId = EXP_MGR_WO_TEMP_ID;

		try {
			List<Defect> list = defectManager.searchExpiringDefect(expireTime);

			logger.debug("Work Orders found: " + (list == null ? 0 : list.size()));

			for (Defect d : list) {

				if (!"C".equalsIgnoreCase(d.getStatusID())) {

					// not completed: send email to assigned group manager

					logger.debug("Work Order: " + d.getKey());

					Integer siteKey = d.getSiteKey();

					String failureClassCode = d.getFailureClass().getCode();
					String problemCode = d.getProblemCode().getCode();
					String locationCode = d.getLocation().getCode() + " - " + d.getLocation().getName();

					UserAccount assignedPerson = userAccountManager
							.getUserAccountByAccountKey(d.getAssignedAccountKey());
					AccountGroup assignedGroup = accountGroupManager
							.getAccountGroupByAccountGroupKey(d.getAssignedGroupKey());

					if (assignedGroup == null) {
						logger.error("No assigned group.");
						continue;
					}

					List<AccountGroupResponsible> agrList = accountGroupResponsibleManager
							.searchAccountGroupResponsible(assignedGroup.getKey());

					if (agrList == null || agrList.size() == 0) {
						logger.error("Assigned group does not have responsible account.");
						continue;
					}

					
					for (AccountGroupResponsible agr : agrList) {
						// for each responsible account send email
						UserAccount responsiblePerson = userAccountManager
								.getUserAccountByAccountKey(agr.getAccountKey());

						logger.debug("Responsible person = " + responsiblePerson.getLoginId());
						if (responsiblePerson.getEmail() == null || StringUtils.isEmpty(responsiblePerson.getEmail())) {
							logger.error("NO email address of responsible person.");
							continue;
						}

						// format only if it is not null
						String issueDateStr = d.getIssueDateTime() == null ? ""
								: DATETIME_FORMATTER.format(d.getIssueDateTime());
						String targetFinishDateStr = d.getTargetFinishDateTime() == null ? ""
								: DATETIME_FORMATTER.format(d.getTargetFinishDateTime());

						Site site = siteManager.getSiteByKey(d.getSiteKey());

						Object[] param = new Object[] { d.getCode(), issueDateStr, failureClassCode, problemCode,
								d.getDesc(), locationCode, d.getContactName(), d.getContactTel(),
								assignedPerson.getName(), d.getPriority(),
								DATETIME_FORMATTER.format(d.getTargetStartDateTime()), targetFinishDateStr,
								d.getRemarks(), assignedGroup.getName(), site.getName() };

						DefectEmail de = new DefectEmail();

						de.setDefectKey(d.getKey());
						de.setEmail(responsiblePerson.getEmail() == null ? "" : responsiblePerson.getEmail());
						de.setType(templateId);
						de.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
						de.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

						try {
							// check defect email status

							logger.debug("****************** email : " + de.getEmail());

							List<DefectEmail> deList = defectEmailManager.searchDefectEmail(d.getKey(), de.getEmail(),
									templateId, null);

							if (deList != null && deList.size() > 0) {

								logger.debug("Defect Email List size = ");

								// there is a defect email
								if (DefectEmail.SUCCESS.equals(deList.get(0).getResult())) {
									// email was sent successfully
									logger.debug("Defect Email was sent");
									continue;
								} else {
									// email was failed, use old info
									de.setKey(deList.get(0).getKey());
									de.setCreateDateTime(deList.get(0).getCreateDateTime());
								}
							} else {
								logger.debug("There is no defect email for " + responsiblePerson.getEmail());
							}

							logger.debug("****************** email : " + responsiblePerson.getEmail());

							// commented for testing
							emailManager.sendTemplate(templateId, siteKey,
									new String[] { responsiblePerson.getEmail() }, param);
							// emailManager.sendTemplate(EXP_WO_TEMP_ID,
							// siteKey, new String[] {"tlau@ebsl.hk"}, param);

							// sent
							logger.debug("Defect Email was sent");
							de.setResult(DefectEmail.SUCCESS);

						} catch (MFMSException e) {

							logger.error("Failed to send Defect Email: " + e.getMessage());

							de.setResult(DefectEmail.FAILED);

						}

						logger.debug("Going to save DefectMail");
						defectEmailManager.saveDefectEmail(de);
					}
				} else {
					// completed: do nothing
				}
			}
		} catch (Exception | MFMSException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	// send daily work order email to assigned person
	public void emailDailyWorkOrder() {

		logger.debug("emailDailyWorkOrder");

		String templateId = DAILY_WO_TEMP_ID;

		String subTemplateId = DAILY_WO_SUB_TEMP_ID;

		Timestamp now = new Timestamp(System.currentTimeMillis());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Timestamp from = new Timestamp(calendar.getTimeInMillis());

		calendar.add(Calendar.DATE, 1);

		Timestamp to = new Timestamp(calendar.getTimeInMillis());

		try {

			List<UserAccount> accountList = userAccountManager.getAllUserAccount();

			for (UserAccount a : accountList) {

				// look for each account's assigned workorder
				List<Defect> list = defectManager.searchDefectByTargetStartDate(a.getKey(), from, to);

				logger.debug("Work Orders found: " + (list == null ? 0 : list.size()));

				StringBuilder contentBuilder = new StringBuilder();

				EmailTemplate subTemplate = emailManager.getEmailTemplateById(subTemplateId);

				MessageFormat mf = new MessageFormat(subTemplate.getTemplateString());

				// build content from sub template
				int count = 0;
				for (Defect d : list) {

					if ("N".equalsIgnoreCase(d.getStatusID()) || "I".equalsIgnoreCase(d.getStatusID())) {

						// new / in progress: send email to assigned person

						logger.debug("Work Order: " + d.getKey());

						String failureClassCode = d.getFailureClass().getCode();
						String problemCode = d.getProblemCode().getCode();
						String locationCode = d.getLocation().getCode() + " - " + d.getLocation().getName();

						UserAccount assignedPerson = userAccountManager
								.getUserAccountByAccountKey(d.getAssignedAccountKey());

						if (assignedPerson == null) {
							logger.error("No assigned person.");
							continue;
						}

						if (assignedPerson.getEmail() == null || StringUtils.isEmpty(assignedPerson.getEmail())) {
							logger.error("NO email address of assigned person.");
							continue;
						}

						// format only if it is not null
						String issueDateStr = d.getIssueDateTime() == null ? ""
								: DATETIME_FORMATTER.format(d.getIssueDateTime());
						String targetFinishDateStr = d.getTargetFinishDateTime() == null ? ""
								: DATETIME_FORMATTER.format(d.getTargetFinishDateTime());

						Site site = siteManager.getSiteByKey(d.getSiteKey());

						Object[] param = new Object[] { d.getCode(), issueDateStr, failureClassCode, problemCode,
								d.getDesc(), locationCode, d.getContactName(), d.getContactTel(),
								assignedPerson.getName(), d.getPriority(),
								DATETIME_FORMATTER.format(d.getTargetStartDateTime()), targetFinishDateStr,
								d.getRemarks(), site.getName() };

						String formattedString = mf.format(param);

						contentBuilder.append(formattedString);

						count++;
					} else {
						// else: do nothing
					}
				}

				if (count > 0) {

					logger.debug("User Id = " + a.getLoginId());
					logger.debug("contentBuilder = " + contentBuilder);

					// use global setting as the person can be cross site
					emailManager.sendTemplate(templateId, 1, new String[] { a.getEmail() },
							new Object[] { count, contentBuilder.toString() });
				}
			}

		} catch (Exception | MFMSException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	// send daily expired work order email to manager
	public void emailDailyExpiredWorkOrder() {

		logger.debug("emailDailyExpiredWorkOrder");

		String templateId = DAILY_MGR_WO_TEMP_ID;

		String subTemplateId = DAILY_MGR_WO_SUB_TEMP_ID;

		Timestamp now = new Timestamp(System.currentTimeMillis());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Timestamp to = new Timestamp(calendar.getTimeInMillis());

		calendar.add(Calendar.DATE, -1);

		Timestamp from = new Timestamp(calendar.getTimeInMillis());

		try {

			List<AccountGroup> groupList = accountGroupManager.getAllAccountGroup();

			for (AccountGroup g : groupList) {

				List<AccountGroupResponsible> agrList = accountGroupResponsibleManager
						.searchAccountGroupResponsible(g.getKey());

				if (agrList == null || agrList.size() == 0) {
					logger.error("Assigned group does not have responsible account.");
					continue;
				}

				// look for each account's assigned workorder
				List<Defect> list = defectManager.searchDefectByTargetStartDate(g.getKey(), from, to);

				logger.debug("Work Orders found: " + (list == null ? 0 : list.size()));

				StringBuilder contentBuilder = new StringBuilder();

				EmailTemplate subTemplate = emailManager.getEmailTemplateById(subTemplateId);

				MessageFormat mf = new MessageFormat(subTemplate.getTemplateString());

				// build content from sub template
				int count = 0;
				for (Defect d : list) {

					if (!"C".equalsIgnoreCase(d.getStatusID())) {

						// not completed: send email to assigned group manager
						logger.debug("Work Order: " + d.getKey());

						String failureClassCode = d.getFailureClass().getCode();
						String problemCode = d.getProblemCode().getCode();
						String locationCode = d.getLocation().getCode() + " - " + d.getLocation().getName();

						UserAccount assignedPerson = userAccountManager
								.getUserAccountByAccountKey(d.getAssignedAccountKey());
						String assignedName;

						if (assignedPerson == null) {
							logger.error("No assigned person.");
							assignedName = "";
						} else if (assignedPerson.getName() != null) {
							assignedName = assignedPerson.getName();
						} else {
							assignedName = "";
						}

						// format only if it is not null
						String issueDateStr = d.getIssueDateTime() == null ? ""
								: DATETIME_FORMATTER.format(d.getIssueDateTime());
						String targetFinishDateStr = d.getTargetFinishDateTime() == null ? ""
								: DATETIME_FORMATTER.format(d.getTargetFinishDateTime());

						Site site = siteManager.getSiteByKey(d.getSiteKey());

						Object[] param = new Object[] { d.getCode(), issueDateStr, failureClassCode, problemCode,
								d.getDesc(), locationCode, d.getContactName(), d.getContactTel(), assignedName,
								d.getPriority(), DATETIME_FORMATTER.format(d.getTargetStartDateTime()),
								targetFinishDateStr, d.getRemarks(), site.getName() };

						String formattedString = mf.format(param);

						contentBuilder.append(formattedString);

						count++;
					}
				}

				if (count > 0) {

					logger.debug("Group Key = " + g.getKey());
					logger.debug("contentBuilder = " + contentBuilder);

					// use global setting as the person can be cross site
					for (AccountGroupResponsible agr : agrList) {

						UserAccount responsiblePerson = userAccountManager
								.getUserAccountByAccountKey(agr.getAccountKey());

						if (responsiblePerson.getEmail() == null || StringUtils.isEmpty(responsiblePerson.getEmail())) {
							logger.error("NO email address of responsible person.");
							continue;
						}
						logger.debug("email = " + responsiblePerson.getEmail());

						logger.debug("****************** email : " + responsiblePerson.getEmail());

						emailManager.sendTemplate(templateId, 1, new String[] { responsiblePerson.getEmail() },
								new Object[] { count, contentBuilder.toString() });
					}
				}
			}

		} catch (Exception | MFMSException e) {

			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	// send daily work order email to assigned person
	public void emailDailyMaintenanceSchedule() {

		logger.debug("emailDailyMaintenanceSchedule");

		String templateId = DAILY_MS_TEMP_ID;

		String subTemplateId = DAILY_MS_SUB_TEMP_ID;

		Timestamp now = new Timestamp(System.currentTimeMillis());

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(now);
		calendar2.setTime(now);

		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		Timestamp from = new Timestamp(calendar1.getTimeInMillis());

		calendar2.set(Calendar.HOUR_OF_DAY, 23);
		calendar2.set(Calendar.MINUTE, 59);
		calendar2.set(Calendar.SECOND, 59);
		calendar2.set(Calendar.MILLISECOND, 999);

		Timestamp to = new Timestamp(calendar2.getTimeInMillis());

		try {

			List<UserAccount> accountList = userAccountManager.getAllUserAccount();

			for (UserAccount a : accountList) {

				try {
					// look for each account's assigned maintenance schedule
					List<DefectSchedule> list = defectScheduleManager.searchAccountDefectSchedule(a.getKey(), from, to);
					// filter by exact occurrence
					if (list != null) {
						Iterator<DefectSchedule> it = list.iterator();
						while (it.hasNext()) {
							DefectSchedule ds = it.next();
							logger.debug("ds id = " + ds.getScheduleKey());
							logger.debug("ds freq = " + ds.getFrequency());
							Timestamp scheduleTime = ds.getScheduleTime();
							if (scheduleTime == null) {
								scheduleTime = new Timestamp(calendar1.getTimeInMillis());
							}
							boolean isToday = calendarEvent.isTheDayEvent(calendar1, calendar2, ds.getFrequency(),
									ds.getScheduleStartDate(), ds.getScheduleEndDate(), scheduleTime);
							logger.debug("isToday=" + isToday);
							if (!isToday) {
								it.remove();
							}
						}

						logger.debug("Maintenance Schedule found: " + list.size());

						StringBuilder contentBuilder = new StringBuilder();

						EmailTemplate subTemplate = emailManager.getEmailTemplateById(subTemplateId);

						MessageFormat mf = new MessageFormat(subTemplate.getTemplateString());

						// build content from sub template
						int count = 0;
						for (DefectSchedule d : list) {

							// send email to assigned person

							logger.debug("Defect Schedule: " + d.getScheduleKey());

							UserAccount assignedPerson = userAccountManager.getUserAccountByAccountKey(a.getKey());

							if (assignedPerson == null) {
								logger.error("No assigned person.");
								continue;
							}

							if (assignedPerson.getEmail() == null || StringUtils.isEmpty(assignedPerson.getEmail())) {
								logger.error("NO email address of assigned person.");
								continue;
							}

							String description = d.getDesc() == null ? "" : d.getDesc();
							Integer eqKey = d.getEquipmentKey();

							String equipmentStr = "";
							if (eqKey != null) {
								Equipment equipment = equipmentManager.getEquipmentByKey(eqKey);
								if (equipment != null) {
									equipmentStr = equipment.getName() == null ? "" : equipment.getName();
								}
							}

							Integer locationKey = d.getLocationKey();

							Location location = locationManager.getLocationByKey(locationKey);

							String locationCode = "";

							if (location != null && location.getCode() != null) {
								locationCode = location.getCode() + " - " + location.getName();
							}

							Date startDate;

							if (d.getScheduleTime() != null) {
								Calendar calendar3 = Calendar.getInstance();
								calendar3.setTime(d.getScheduleTime());
								calendar3.set(Calendar.YEAR, calendar1.get(Calendar.YEAR));
								calendar3.set(Calendar.MONTH, calendar1.get(Calendar.MONTH));
								calendar3.set(Calendar.DATE, calendar1.get(Calendar.DATE));

								startDate = calendar3.getTime();
							} else {
								startDate = calendar1.getTime();
							}

							Site site = siteManager.getSiteByKey(d.getSiteKey());

							Object[] param = new Object[] { description, equipmentStr, locationCode,
									DATETIME_FORMATTER.format(startDate), assignedPerson.getName(), d.getRemarks(),
									site.getName() };

							String formattedString = mf.format(param);

							contentBuilder.append(formattedString);

							count++;

						}

						if (count > 0) {

							logger.debug("User Id = " + a.getLoginId());
							logger.debug("contentBuilder = " + contentBuilder);

							// use global setting as the person can be cross
							// site
							
							emailManager.sendTemplate(templateId, 1, new String[] { a.getEmail() },
									new Object[] { count, contentBuilder.toString() });
						}
					} else {
						logger.debug("No Maintenance Schedule found. ");
					}
				} catch (Exception | MFMSException e) {
					logger.error(e.getMessage());
					logger.error(e);
					logger.error(e.getStackTrace());
					e.printStackTrace();
				}
			}

		} catch (Exception | MFMSException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	// send daily expired work order email to assigned person
	public void emailDailyExpMaintenanceSchedule() {

		logger.debug("emailDailyExpMaintenanceSchedule");

		String templateId = DAILY_EXP_MS_TEMP_ID;

		String subTemplateId = DAILY_EXP_MS_SUB_TEMP_ID;

		Timestamp now = new Timestamp(System.currentTimeMillis());

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(now);
		calendar2.setTime(now);

		// yesterday
		calendar1.add(Calendar.DATE, -1);
		calendar2.add(Calendar.DATE, -1);

		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		Timestamp from = new Timestamp(calendar1.getTimeInMillis());

		calendar2.set(Calendar.HOUR_OF_DAY, 23);
		calendar2.set(Calendar.MINUTE, 59);
		calendar2.set(Calendar.SECOND, 59);
		calendar2.set(Calendar.MILLISECOND, 999);

		Timestamp to = new Timestamp(calendar2.getTimeInMillis());

		try {

			List<UserAccount> accountList = userAccountManager.getAllUserAccount();

			for (UserAccount a : accountList) {

				try {
					// look for each account's assigned maintenance schedule
					List<DefectSchedule> list = defectScheduleManager.searchAccountDefectSchedule(a.getKey(), from, to);
					// filter by exact occurrence
					if (list != null) {
						Iterator<DefectSchedule> it = list.iterator();
						while (it.hasNext()) {
							DefectSchedule ds = it.next();

							Timestamp scheduleTime = ds.getScheduleTime();
							if (scheduleTime == null) {
								scheduleTime = new Timestamp(calendar1.getTimeInMillis());
							}
							boolean isYesterday = calendarEvent.isTheDayEvent(calendar1, calendar2, ds.getFrequency(),
									ds.getScheduleStartDate(), ds.getScheduleEndDate(), scheduleTime);

							// remove from list if not yesterday's event or
							// finish day is yesterday
							if (!isYesterday ||
							// after from and before to
									(ds.getFinishDateTime() != null && ds.getFinishDateTime().compareTo(from) >= 0
											&& ds.getFinishDateTime().compareTo(to) <= 0)) {
								it.remove();
							}
						}

						logger.debug("Maintenance Schedule found: " + list.size());

						StringBuilder contentBuilder = new StringBuilder();

						EmailTemplate subTemplate = emailManager.getEmailTemplateById(subTemplateId);

						MessageFormat mf = new MessageFormat(subTemplate.getTemplateString());

						// build content from sub template
						int count = 0;
						for (DefectSchedule d : list) {

							// send email to assigned person

							logger.debug("Defect Schedule: " + d.getScheduleKey());

							UserAccount assignedPerson = userAccountManager.getUserAccountByAccountKey(a.getKey());

							if (assignedPerson == null) {
								logger.error("No assigned person.");
								continue;
							}

							if (assignedPerson.getEmail() == null || StringUtils.isEmpty(assignedPerson.getEmail())) {
								logger.error("NO email address of assigned person.");
								continue;
							}

							String description = d.getDesc() == null ? "" : d.getDesc();
							Integer eqKey = d.getEquipmentKey();

							String equipmentStr = "";
							if (eqKey != null) {
								Equipment equipment = equipmentManager.getEquipmentByKey(eqKey);
								if (equipment != null) {
									equipmentStr = equipment.getName() == null ? "" : equipment.getName();
								}
							}

							Integer locationKey = d.getLocationKey();

							Location location = locationManager.getLocationByKey(locationKey);

							String locationCode = "";

							if (location != null && location.getCode() != null) {
								locationCode = location.getCode() + " - " + location.getName();
							}

							Date startDate;

							if (d.getScheduleTime() != null) {
								Calendar calendar3 = Calendar.getInstance();
								calendar3.setTime(d.getScheduleTime());
								calendar3.set(Calendar.YEAR, calendar1.get(Calendar.YEAR));
								calendar3.set(Calendar.MONTH, calendar1.get(Calendar.MONTH));
								calendar3.set(Calendar.DATE, calendar1.get(Calendar.DATE));

								startDate = calendar3.getTime();
							} else {
								startDate = calendar1.getTime();
							}

							Site site = siteManager.getSiteByKey(d.getSiteKey());

							Object[] param = new Object[] { description, equipmentStr, locationCode,
									DATETIME_FORMATTER.format(startDate), assignedPerson.getName(), d.getRemarks(),
									site.getName() };

							String formattedString = mf.format(param);

							contentBuilder.append(formattedString);

							count++;

						}

						if (count > 0) {

							logger.debug("User Id = " + a.getLoginId());
							logger.debug("contentBuilder = " + contentBuilder);

							// use global setting as the person can be cross
							// site

							logger.debug("****************** email : " + a.getEmail());

							emailManager.sendTemplate(templateId, 1, new String[] { a.getEmail() },
									new Object[] { count, contentBuilder.toString() });
						}
					} else {
						logger.debug("No Maintenance Schedule found. ");
					}
				} catch (Exception | MFMSException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}

		} catch (Exception | MFMSException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}
	
	

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public DefectManager getDefectManager() {
		return defectManager;
	}

	public void setDefectManager(DefectManager defectManager) {
		this.defectManager = defectManager;
	}

	public DefectScheduleManager getDefectScheduleManager() {
		return defectScheduleManager;
	}

	public void setDefectScheduleManager(DefectScheduleManager defectScheduleManager) {
		this.defectScheduleManager = defectScheduleManager;
	}

	public EquipmentManager getEquipmentManager() {
		return equipmentManager;
	}

	public void setEquipmentManager(EquipmentManager equipmentManager) {
		this.equipmentManager = equipmentManager;
	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public DefectEmailManager getDefectEmailManager() {
		return defectEmailManager;
	}

	public void setDefectEmailManager(DefectEmailManager defectEmailManager) {
		this.defectEmailManager = defectEmailManager;
	}

	public AccountGroupManager getAccountGroupManager() {
		return accountGroupManager;
	}

	public void setAccountGroupManager(AccountGroupManager accountGroupManager) {
		this.accountGroupManager = accountGroupManager;
	}

	public AccountGroupResponsibleManager getAccountGroupResponsibleManager() {
		return accountGroupResponsibleManager;
	}

	public void setAccountGroupResponsibleManager(AccountGroupResponsibleManager accountGroupResponsibleManager) {
		this.accountGroupResponsibleManager = accountGroupResponsibleManager;
	}

	public CalendarEvent getCalendarEvent() {
		return calendarEvent;
	}

	public void setCalendarEvent(CalendarEvent calendarEvent) {
		this.calendarEvent = calendarEvent;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

}
