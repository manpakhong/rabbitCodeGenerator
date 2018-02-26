package hk.ebsl.mfms.utility;

import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;
import hk.ebsl.mfms.json.CalendarJSON;
import hk.ebsl.mfms.json.CalendarJSON.Frequency;
import hk.ebsl.mfms.json.CalendarJSON.Ranges;
import hk.ebsl.mfms.manager.PatrolScheduleManager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

public class CalendarEvent {

	private static final Logger logger = Logger.getLogger(CalendarEvent.class);

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private PatrolScheduleManager patrolScheduleManager;

	// public CalendarEvent (){
	//
	// }

	public List<PatrolSchedule> getEventByDateRange(int siteKey,
			Calendar startDate, Calendar endDate) {

		Calendar cloneStart = (Calendar) startDate.clone();
		cloneStart.set(Calendar.HOUR_OF_DAY, 0);
		cloneStart.set(Calendar.MINUTE, 0);
		cloneStart.set(Calendar.SECOND, 0);
		cloneStart.set(Calendar.MILLISECOND, 0);

		Calendar cloneEnd = (Calendar) endDate.clone();
		cloneEnd.set(Calendar.HOUR_OF_DAY, 0);
		cloneEnd.set(Calendar.MINUTE, 0);
		cloneEnd.set(Calendar.SECOND, 0);
		cloneEnd.set(Calendar.MILLISECOND, 0);

		List<PatrolSchedule> list = patrolScheduleManager
				.searchPatrolScheduleDate(siteKey,
						new Timestamp(cloneStart.getTimeInMillis()),
						new Timestamp(cloneEnd.getTimeInMillis()));

		List<PatrolSchedule> rtnList = new ArrayList<PatrolSchedule>();
		for (PatrolSchedule ps : list) {

			if (!checkSkipped(ps.getScheduleKey(), cloneStart, cloneEnd)) {
				rtnList.add(ps);
			}

		}

		return rtnList;
	}

	// time range in one day eg 1/1/2001 10:00:00 - 1/1/2001 15:00:00
	public Boolean isTheDayEvent(Calendar theStartDateTime,
			Calendar theEndDateTime, String frequencyStr,
			Timestamp scheduleStartDate, Timestamp scheduleEndDate,
			Timestamp scheduleTime) {

		if (scheduleEndDate == null) {
			// System.out.println("Null end date");

			Calendar maxEnd = Calendar.getInstance();
			maxEnd.add(Calendar.YEAR, 100);

			scheduleEndDate = new Timestamp(maxEnd.getTimeInMillis());
		}

		logger.debug("isTheDayEvent()[" + theStartDateTime + ","
				+ theEndDateTime + "," + frequencyStr + "," + scheduleStartDate
				+ "," + scheduleEndDate + "," + scheduleTime + "]");

		Frequency eventType = getEventType(frequencyStr);

		Boolean isValidEvent = false;

		Calendar now = theStartDateTime;

		Calendar eventStartDate = Calendar.getInstance();
		eventStartDate.setTime(new Date(scheduleStartDate.getTime()));

		int eventMonth = eventStartDate.get(Calendar.MONTH);
		int eventDay = eventStartDate.get(Calendar.DAY_OF_MONTH);

		System.out.println("eventMonth : " + eventMonth);

		switch (eventType) {

		case Once:

			isValidEvent = true;

			break;

		case Weekly:

			Frequency[] weekDay = getEventWeekDay(frequencyStr);

			for (Frequency f : weekDay) {
				int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);

				if ((f.getValue() + 1) == dayOfWeek) {
					isValidEvent = true;
					break;
				}

			}

			break;

		case Daily:

			isValidEvent = true;

			break;

		case Monthly:

			switch (now.get(Calendar.MONTH)) {

			case Calendar.FEBRUARY:
				if (eventDay > 28 && now.get(Calendar.DAY_OF_MONTH) == 28) {
					isValidEvent = true;
					break;
				}
			case Calendar.APRIL:
			case Calendar.JUNE:
			case Calendar.SEPTEMBER:
			case Calendar.NOVEMBER:
				if (eventDay > 30 && now.get(Calendar.DAY_OF_MONTH) == 30) {
					isValidEvent = true;
					break;
				}
			default:
				if ( eventDay == now.get(Calendar.DAY_OF_MONTH)) {
					isValidEvent = true;
				}
				break;
			}

			break;

		case Annually:

			if (eventMonth == Calendar.FEBRUARY && eventDay == 29) {

				GregorianCalendar cal = new GregorianCalendar();
				if (cal.isLeapYear(now.get(Calendar.YEAR))) {

					if (eventDay == now.get(Calendar.DAY_OF_MONTH)) {
						isValidEvent = true;
					}

				} else {

					if (now.get(Calendar.DAY_OF_MONTH) == 28) {
						isValidEvent = true;
					}
				}

			} else {

				if (eventMonth == now.get(Calendar.MONTH)
						&& eventDay == now.get(Calendar.DAY_OF_MONTH)) {

					isValidEvent = true;
				}

			}

			break;

		default:
			break;

		}

		if (isValidEvent) {

			isValidEvent = checkIsTimeValid(theStartDateTime, theEndDateTime,
					scheduleEndDate, scheduleTime);

		}

		//

		return isValidEvent;

	}

	public List<Ranges> getEventRanges(String frequencyStr,
			Timestamp scheduelStartDate, Timestamp scheduleEndDate,
			Timestamp scheduleTime) {

		List<Ranges> rtn = new ArrayList<Ranges>();

		// Calendar eventStartDate = Calendar.getInstance();
		// eventStartDate.setTime(new Date(scheduelStartDate.getTime()));
		// int eventMonth = eventStartDate.get(Calendar.MONTH);

		Calendar start = Calendar.getInstance();
		start.setTime(new Date(scheduelStartDate.getTime()));

		int eventDay = start.get(Calendar.DAY_OF_MONTH);

		Calendar end = Calendar.getInstance();
		end.setTime(new Date(scheduleEndDate.getTime()));

		GregorianCalendar cal = new GregorianCalendar();

		Frequency eventType = getEventType(frequencyStr);
		switch (eventType) {
		// case Once:
		// break;
		// case Daily:
		// break;
		// case Weekly:
		// break;
		case Monthly:

			if (eventDay > 28) {

				for (Calendar st = start; st.getTimeInMillis() <= end
						.getTimeInMillis(); st.add(Calendar.MONTH, 1)) {

					switch (st.get(Calendar.MONTH)) {

					case Calendar.FEBRUARY:
						if (cal.isLeapYear(st.get(Calendar.YEAR))) {
							st.set(Calendar.DAY_OF_MONTH, 29);
						} else {
							st.set(Calendar.DAY_OF_MONTH, 28);
						}

						break;

					case Calendar.APRIL:
					case Calendar.JUNE:
					case Calendar.SEPTEMBER:
					case Calendar.NOVEMBER:
						if (eventDay > 30)
							st.set(Calendar.DAY_OF_MONTH, 30);
						break;

					case Calendar.JANUARY:
					case Calendar.MARCH:
					case Calendar.MAY:
					case Calendar.JULY:
					case Calendar.AUGUST:
					case Calendar.OCTOBER:
					case Calendar.DECEMBER:
						st.set(Calendar.DAY_OF_MONTH, eventDay);
					default:
						break;

					}

					rtn.add(new CalendarJSON().new Ranges(timestampToString(
							new Timestamp(st.getTimeInMillis()), dateFormat),
							timestampToString(
									new Timestamp(st.getTimeInMillis()),
									dateFormat)));

				}

			} else {
				for (Calendar st = start; st.getTimeInMillis() <= end
						.getTimeInMillis(); st.add(Calendar.MONTH, 1)) {

					rtn.add(new CalendarJSON().new Ranges(timestampToString(
							new Timestamp(st.getTimeInMillis()), dateFormat),
							timestampToString(
									new Timestamp(st.getTimeInMillis()),
									dateFormat)));
				}
			}

			break;
		case Annually:

			if (start.get(Calendar.MONTH) == Calendar.FEBRUARY
					&& start.get(Calendar.DAY_OF_MONTH) > 28) {

				for (Calendar st = start; st.getTimeInMillis() <= end
						.getTimeInMillis(); st.add(Calendar.YEAR, 1)) {

					if (cal.isLeapYear(st.get(Calendar.YEAR))) {
						st.set(Calendar.DAY_OF_MONTH, 29);
					} else {
						st.set(Calendar.DAY_OF_MONTH, 28);
					}

					rtn.add(new CalendarJSON().new Ranges(timestampToString(
							new Timestamp(st.getTimeInMillis()), dateFormat),
							timestampToString(
									new Timestamp(st.getTimeInMillis()),
									dateFormat)));
				}

			} else {

				for (Calendar st = start; st.getTimeInMillis() <= end
						.getTimeInMillis(); st.add(Calendar.YEAR, 1)) {
					rtn.add(new CalendarJSON().new Ranges(timestampToString(
							new Timestamp(st.getTimeInMillis()), dateFormat),
							timestampToString(
									new Timestamp(st.getTimeInMillis()),
									dateFormat)));
				}
			}

			break;
		default:
			rtn.add(new CalendarJSON().new Ranges(timestampToString(
					scheduelStartDate, dateFormat), timestampToString(
					scheduleEndDate, dateFormat)));
			break;

		}

		return rtn;

	}

	private Boolean checkIsTimeValid(Calendar theStartDateTime,
			Calendar theEndDateTime, Timestamp scheduleEndDate,
			Timestamp scheduleTime) {

		Calendar endDate = Calendar.getInstance();
		endDate.setTime(new Date(scheduleEndDate.getTime()));

		Calendar time = Calendar.getInstance();
		time.setTime(new Date(scheduleTime.getTime()));

		time.set(theStartDateTime.get(Calendar.YEAR),
				theStartDateTime.get(Calendar.MONTH),
				theStartDateTime.get(Calendar.DATE));

		// System.out.println("Start : " + theStartDateTime.getTimeInMillis()
		// + " || End : " + theEndDateTime.getTimeInMillis()
		// + " || Time : " + time.getTimeInMillis());

		if (endDate.get(Calendar.HOUR_OF_DAY) == 0
				&& endDate.get(Calendar.MINUTE) == 0
				&& endDate.get(Calendar.SECOND) == 0) {

			if ((theEndDateTime.after(time) && theStartDateTime.before(time))
					|| (theStartDateTime.getTimeInMillis() == time
							.getTimeInMillis())) {

				return true;
			}

		} else {

			if (time.after(endDate)) {

				return false;
			} else {

				System.out.println("End date > time : End :"
						+ theEndDateTime.getTimeInMillis() + "|| start :"
						+ theStartDateTime.getTimeInMillis() + "||time :"
						+ time.getTimeInMillis());

				if ((theEndDateTime.getTimeInMillis() >= time.getTimeInMillis())
						&& (theStartDateTime.getTimeInMillis() <= time
								.getTimeInMillis())) {
					return true;
				}
			}

		}

		return false;

	}

	private Boolean checkSkipped(int scheduleKey, Calendar startDate,
			Calendar endDate) {
		List<PatrolSchedule> scheduleChildenList = this.patrolScheduleManager
				.searchPatrolScheduleChilden(scheduleKey);

		if (!scheduleChildenList.isEmpty()) {

			for (PatrolSchedule ps : scheduleChildenList) {
				if (ps.getSkippedStartDate().getTime() <= startDate
						.getTimeInMillis()
						&& ps.getSkippedEndDate().getTime() >= endDate
								.getTimeInMillis()) {

					return true;
				}
			}
		}

		return false;

	}

	public Frequency getEventType(String frequencyStr) {

		String[] frequency = frequencyStr.split("_");

		Frequency f = Frequency.fromInt(Integer.parseInt(frequency[0]));

		return f;
	}

	public Frequency[] getEventWeekDay(String frequencyStr) {

		String[] frequency = frequencyStr.split("_");

		Frequency f = Frequency.fromInt(Integer.parseInt(frequency[0]));

		switch (f) {
		case Once:
			return null;
		case Daily:
			return null;
		case Weekly:

			String[] weekDay = frequency[1].split(",");
			Frequency[] rtn = new Frequency[weekDay.length];

			if (weekDay != null && weekDay.length > 0) {

				for (int i = 0; i < weekDay.length; i++) {
					rtn[i] = Frequency.fromInt(Integer.parseInt(weekDay[i]));
				}

				return rtn;

			} else {
				return null;
			}
		case Monthly:
			return null;
		case Annually:
			return null;
		default:
			return null;

		}

	}

	private String timestampToString(Timestamp time, SimpleDateFormat format) {
		return format.format(new Date(time.getTime()));
	}

	// getter & setter
	public PatrolScheduleManager getPatrolScheduleManager() {
		return patrolScheduleManager;
	}

	public void setPatrolScheduleManager(
			PatrolScheduleManager patrolScheduleManager) {
		this.patrolScheduleManager = patrolScheduleManager;
	}

}
