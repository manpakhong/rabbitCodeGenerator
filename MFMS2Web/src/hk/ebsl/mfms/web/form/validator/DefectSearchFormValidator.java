package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.web.form.ProblemCodeForm;
import hk.ebsl.mfms.web.form.SearchDefectForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DefectSearchFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(DefectSearchFormValidator.class);

	private DefectManager defectManager;

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support ProblemCodeForm
		return ProblemCodeForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Search Defect Form");

		SearchDefectForm searchDefectForm = (SearchDefectForm) target;
		if (!errors.hasFieldErrors("key")) {
			if (searchDefectForm.getCode().trim().length() == 0 &&

					searchDefectForm.getDescription().trim().length() == 0 &&

					searchDefectForm.getStatus().trim().length() == 0 &&

					searchDefectForm.getLocationKey() == null &&

					searchDefectForm.getProblemCodeKey() == null &&

					searchDefectForm.getGroupKey() == null &&

					searchDefectForm.getAccountKey() == null &&

					searchDefectForm.getFailureClassKey() == null &&

					searchDefectForm.getCauseCodeKey() == null &&

					searchDefectForm.getPriority() == null) {
				errors.rejectValue("key", "defect.noInput");
			}
		}

	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public DefectManager getDefectManager() {
		return defectManager;
	}

	public void setDefectManager(DefectManager defectManager) {
		this.defectManager = defectManager;
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
