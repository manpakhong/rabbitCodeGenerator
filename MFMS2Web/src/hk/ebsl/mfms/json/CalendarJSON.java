package hk.ebsl.mfms.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarJSON {

	public enum Frequency {
		Monday(1), Tuesday(2), Wednesday(3), Thursday(4), Friday(5), Saturday(6), Sunday(0), Once(101), 
		Daily(102), Weekly(103), Monthly(104), Annually(105);

		private int value;
		private static final Map<Integer, Frequency> frequencyMap = new HashMap<Integer, Frequency>();

		Frequency(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		static {
			for (Frequency freq : Frequency.values()) {
				frequencyMap.put(freq.value, freq);
			}
		}

		public static Frequency fromInt(int i) {
			Frequency type = frequencyMap.get(Integer.valueOf(i));
			if (type == null)
				return null;
			return type;
		}

	}

	private String id;
	private String title;
	private String start; // Time HH:SS
	private String end;// Time HH:SS
	private Boolean allDay;
	private String url; // click event
	private String className;// cssClass
	private Boolean editable = false;
	private Boolean startEditable = false;
	private Boolean durationEditable = false;
	private Boolean overlap = false;
	private String constraint;
	private Integer[] dow; // Day 0-7 0=Sunday
	private List<Ranges> ranges; // Date YYYY-MM-DD

	private String color; // eg : #ff0000
	private String backgroundColor;
	private String borderColor;
	private String textColor;
	
//	private Boolean forceEventDuration = false;
//	private String duration;

	private List<ExcludeDays> excludedDates;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Boolean getStartEditable() {
		return startEditable;
	}

	public void setStartEditable(Boolean startEditable) {
		this.startEditable = startEditable;
	}

	public Boolean getDurationEditable() {
		return durationEditable;
	}

	public void setDurationEditable(Boolean durationEditable) {
		this.durationEditable = durationEditable;
	}

	public Boolean getOverlap() {
		return overlap;
	}

	public void setOverlap(Boolean overlap) {
		this.overlap = overlap;
	}

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	public Integer[] getDow() {
		return dow;
	}

	public void setDow(Integer[] dow) {
		this.dow = dow;
	}

	public List<Ranges> getRanges() {
		return ranges;
	}

	public void setRanges(List<Ranges> ranges) {
		this.ranges = ranges;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public List<ExcludeDays> getExcludedDates() {
		return excludedDates;
	}

	public void setExcludedDates(List<ExcludeDays> excludedDates) {
		this.excludedDates = excludedDates;
	}

//	public Boolean getForceEventDuration() {
//		return forceEventDuration;
//	}
//
//	public void setForceEventDuration(Boolean forceEventDuration) {
//		this.forceEventDuration = forceEventDuration;
//	}



	public class Ranges {
		private String start;
		private String end;

		public Ranges(String start, String end) {
			super();
			this.start = start + " 00:00:00";
			this.end = end + " 23:59:59";

		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

	}

	public class ExcludeDays {
		private String start;
		private String end;

		public ExcludeDays(String start, String end) {
			super();
			this.start = start + " 00:00:00";
			this.end = end + " 23:59:59";
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

	}

	public static String encodeFrequency(Integer frequency, List<Integer> weekDay) {

		if (frequency != null) {

			String rtn = "";
			rtn = frequency + "";
			if (Frequency.Weekly.getValue() == frequency) {
				if (weekDay != null) {
					for (int i = 0; i < weekDay.size(); i++) {
						if (i == 0) {
							rtn += "_";
						} else {
							rtn += ",";
						}
						rtn += weekDay.get(i);
					}
				}
			}

			return rtn;

		} else
			return "";

	}

	public static Integer getDecodedFrequency(String frequencyStr) {

		String[] frequency = frequencyStr.split("_");

		return Integer.parseInt(frequency[0]);
		
	}

	public static Integer[] getDecodedWeekDay(String frequencyStr) {

		String[] frequency = frequencyStr.split("_");

		Frequency f = Frequency.fromInt(Integer.parseInt(frequency[0]));

		switch (f) {
		case Once:
			return null;
		case Daily:
			return null;
		case Weekly:

			String[] weekDay = frequency[1].split(",");

			if (weekDay != null && weekDay.length > 0) {
				Integer[] weekDayInt = new Integer[weekDay.length];
				for (int i = 0; i < weekDay.length; i++) {
					weekDayInt[i] = Integer.parseInt(weekDay[i]);
				}

				return weekDayInt;

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

}
