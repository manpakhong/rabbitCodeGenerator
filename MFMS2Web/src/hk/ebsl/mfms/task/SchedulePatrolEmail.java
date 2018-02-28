package hk.ebsl.mfms.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.PatrolExpireTimeJSON;
import hk.ebsl.mfms.json.PatrolResultDisplayJSON;
import hk.ebsl.mfms.json.PatrolResultDisplayJSON.ScheduleStatus;
import hk.ebsl.mfms.manager.EmailManager;
import hk.ebsl.mfms.manager.PatrolScheduleManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.utility.CalendarEvent;

public class SchedulePatrolEmail {

	private CalendarEvent calendarEvent;
	private EmailManager emailManager;
	private SiteManager siteManager;

	SimpleDateFormat onlyTimeFormat = new SimpleDateFormat("HH:mm");
	SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	

	public void sendDailyPatrolEmail() {
		try {
			Calendar today = Calendar.getInstance();
//			today.set(Calendar.DAY_OF_MONTH, 31);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);

			Calendar current = Calendar.getInstance();
			//
			Calendar timeBeforeToSee = Calendar.getInstance();
			// timeBeforeToSee.add(Calendar.HOUR_OF_DAY, hourBefore);
//			today.set(Calendar.DAY_OF_MONTH, 31);
			timeBeforeToSee.set(Calendar.HOUR_OF_DAY, 23);
			timeBeforeToSee.set(Calendar.MINUTE, 59);
			timeBeforeToSee.set(Calendar.SECOND, 59);
			timeBeforeToSee.set(Calendar.MILLISECOND, 999);

			List<PatrolSchedule> list = calendarEvent.getEventByDateRange(-1,
					current, current);

			for (PatrolSchedule tmp : list) {

				Boolean isToday = calendarEvent.isTheDayEvent(today,
						timeBeforeToSee, tmp.getFrequency(),
						tmp.getScheduleStartDate(), tmp.getScheduleEndDate(),
						tmp.getScheduleTime());

				System.out.println("Is Today Schedule key : "+tmp.getScheduleKey() + "|| "+isToday);
				
				if (isToday) {

//					System.out.println("Is Today Schedule key : "+tmp.getScheduleKey());
					Calendar time = Calendar.getInstance();
					time.setTime(new Date(tmp.getScheduleTime().getTime()));

					current.set(Calendar.HOUR_OF_DAY,
							time.get(Calendar.HOUR_OF_DAY));
					current.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

					List<PatrolScheduleAccount> accountList = tmp
							.getScheduleAccount();

					String accountName = "";

					System.out.println("Account List size : "
							+ accountList.size());

					for (PatrolScheduleAccount account : accountList) {
						if (accountName.equals("")) {
							accountName += account.getUserAccount().getName();
						} else {
							accountName += ", "
									+ account.getUserAccount().getName();
						}
					}

					String templateId = "dailyPatrolSchedule";

					Site site = siteManager.getSiteByKey(accountList.get(0)
							.getPatrolSchedule().getSiteKey());

					Object[] param = new Object[] {
							site.getName(),
							onlyDateFormat.format(today.getTime()) + " "+
							onlyTimeFormat.format(new Date(accountList.get(0)
									.getPatrolSchedule().getScheduleTime()
									.getTime())),
							accountList.get(0).getPatrolSchedule()
									.getRouteDef().getName() };

					List<String> emailList = new ArrayList<String>();
					for (PatrolScheduleAccount acList : accountList) {
						emailList.add(acList.getUserAccount().getEmail());

					}

					String[] email = new String[emailList.size()];
					emailList.toArray(email);

					emailManager.sendTemplateAsync(templateId, site.getKey(),
							email, param);

				}
			}
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendExpiredPatrolEmail() {
		try {
			Calendar today = Calendar.getInstance();
			today.add(Calendar.DATE, -1);
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);

			Calendar current = Calendar.getInstance();
			//
			Calendar timeBeforeToSee = Calendar.getInstance();
			// timeBeforeToSee.add(Calendar.HOUR_OF_DAY, hourBefore);
			timeBeforeToSee.add(Calendar.DATE, -1);
			timeBeforeToSee.set(Calendar.HOUR_OF_DAY, 23);
			timeBeforeToSee.set(Calendar.MINUTE, 59);
			timeBeforeToSee.set(Calendar.SECOND, 59);
			timeBeforeToSee.set(Calendar.MILLISECOND, 999);

			List<PatrolSchedule> list = calendarEvent.getEventByDateRange(-1,
					current, current);

			for (PatrolSchedule tmp : list) {

				Boolean isToday = calendarEvent.isTheDayEvent(today,
						timeBeforeToSee, tmp.getFrequency(),
						tmp.getScheduleStartDate(), tmp.getScheduleEndDate(),
						tmp.getScheduleTime());

				if (isToday) {

					if (tmp.getLastAttendTime() != null && tmp.getLastAttendTime().getTime() > today
							.getTimeInMillis())
						continue;

					Calendar time = Calendar.getInstance();
					time.setTime(new Date(tmp.getScheduleTime().getTime()));

					current.set(Calendar.HOUR_OF_DAY,
							time.get(Calendar.HOUR_OF_DAY));
					current.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

					List<PatrolScheduleAccount> accountList = tmp
							.getScheduleAccount();

					String accountName = "";

					System.out.println("Account List size : "
							+ accountList.size());

					for (PatrolScheduleAccount account : accountList) {
						if (accountName.equals("")) {
							accountName += account.getUserAccount().getName();
						} else {
							accountName += ", "
									+ account.getUserAccount().getName();
						}
					}

					String templateId = "expiredPatrolSchedule";

					Site site = siteManager.getSiteByKey(accountList.get(0)
							.getPatrolSchedule().getSiteKey());

					Object[] param = new Object[] {
							site.getName(),
							onlyDateFormat.format(today.getTime()) + " "+
							onlyTimeFormat.format(new Date(accountList.get(0)
									.getPatrolSchedule().getScheduleTime()
									.getTime())),
							accountList.get(0).getPatrolSchedule()
									.getRouteDef().getName() };

					List<String> emailList = new ArrayList<String>();
					for (PatrolScheduleAccount acList : accountList) {
						emailList.add(acList.getUserAccount().getEmail());

					}
					String[] email = new String[emailList.size()];
					emailList.toArray(email);

					emailManager.sendTemplateAsync(templateId, site.getKey(),
							email, param);

				}
			}
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CalendarEvent getCalendarEvent() {
		return calendarEvent;
	}

	public void setCalendarEvent(CalendarEvent calendarEvent) {
		this.calendarEvent = calendarEvent;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

}
