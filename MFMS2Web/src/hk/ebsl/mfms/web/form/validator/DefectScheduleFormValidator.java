package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.web.form.DefectScheduleForm;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class DefectScheduleFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(DefectScheduleFormValidator.class);

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		return DefectScheduleForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		DefectScheduleForm defectScheduleForm = (DefectScheduleForm) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "defect.description.required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "equipmentKey", "defect.equipment.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedAccountKeyList", "defect.account.required");
		if (!errors.hasFieldErrors("selectedAccountKeyList")) {
			if (defectScheduleForm.getSelectedAccountKeyList() == null) {
				errors.rejectValue("selectedAccountKeyList", "schedule.account.require");
			}
			
//			if (defectScheduleForm.getStaff() == null) {
//				errors.rejectValue("selectedAccountKeyList", "schedule.account.require");
//			}
		}

		if (!errors.hasFieldErrors("description")) {
			if (defectScheduleForm.getDescription().length() > descMaxLength) {
				errors.rejectValue("description", "defect.desc.exceeded", new Object[] { descMaxLength }, null);
			}
		}

		if (!errors.hasFieldErrors("startDate")) {

			if (defectScheduleForm.getStartDate() != null) {

				if (defectScheduleForm.getStartDate().isEmpty()) {
					errors.rejectValue("startDate", "startTime.required");
				}
			}
		}
		
		if (!errors.hasFieldErrors("startTime")) {

			if (defectScheduleForm.getStartTime() != null) {

				if (defectScheduleForm.getStartTime().isEmpty()) {
					errors.rejectValue("startTime", "startTime.required");
				}
			}
		}
		
//		if (!errors.hasFieldErrors("endDate")) {
//			if (defectScheduleForm.getEndDate() != null) {
//				if (defectScheduleForm.getEndDate().isEmpty() && defectScheduleForm.getFrequency() != 101) {
//					errors.rejectValue("endDate", "endDate.required");
//				}
//			}
//		}

		if (!errors.hasFieldErrors("frequency")) {
			if (defectScheduleForm.getFrequency() == null) {
				errors.rejectValue("frequency", "frequency.required");
			} else

			if (defectScheduleForm.getWeekDays() == null && defectScheduleForm.getFrequency() == 103) {
				errors.rejectValue("weekDays", "week.required");
			}
		}

		if (!errors.hasFieldErrors("time")) {
			if (defectScheduleForm.getTime() != null ) {
				if (defectScheduleForm.getTime().isEmpty()  && !defectScheduleForm.getIsFullDay()) {
					//errors.rejectValue("time", "time.required");
					defectScheduleForm.setTime("");
					defectScheduleForm.setIsFullDay(true);
				}
			}
		}

		if (!errors.hasFieldErrors("remarks")) {
			if (defectScheduleForm.getRemarks() != null) {
				if (defectScheduleForm.getRemarks().length() > descMaxLength) {
					errors.rejectValue("remarks", "defect.remarks.exceeded", new Object[] { descMaxLength }, null);
				}
			}
		}
		
		if (!errors.hasFieldErrors("remarks")) {
			if (defectScheduleForm.getRemarks() != null) {
				if (defectScheduleForm.getRemarks().length() > descMaxLength) {
					errors.rejectValue("remarks", "defect.remarks.exceeded", new Object[] { descMaxLength }, null);
				}
			}
		}
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getCodeMaxLength() {
		return codeMaxLength;
	}

	public void setCodeMaxLength(int codeMaxLength) {
		this.codeMaxLength = codeMaxLength;
	}

	public int getNameMaxLength() {
		return nameMaxLength;
	}

	public void setNameMaxLength(int nameMaxLength) {
		this.nameMaxLength = nameMaxLength;
	}

	public int getDescMaxLength() {
		return descMaxLength;
	}

	public void setDescMaxLength(int descMaxLength) {
		this.descMaxLength = descMaxLength;
	}

	public static Logger getLogger() {
		return logger;
	}

}
