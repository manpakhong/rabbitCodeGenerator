package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.web.form.DefectListForm;
import hk.ebsl.mfms.web.form.ProblemCodeForm;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DefectListFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(DefectListFormValidator.class);

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

		DefectListForm defectListForm = (DefectListForm) target;

		if (defectListForm.getCode().trim().length() == 0 && defectListForm.getFailureClassKey() == null
				&& defectListForm.getProblemCodeKey() == null && defectListForm.getTo().trim().length() == 0
				&& defectListForm.getFrom().trim().length() == 0 && defectListForm.getCauseCodeKey() == null
				&& defectListForm.getEquipmentKey() == null && defectListForm.getAssignedToKey() == null
				&& defectListForm.getPriority() == null && defectListForm.getLocationKey() == null
				&& defectListForm.getAccountGroupKey() == null && defectListForm.getStatus().trim().length() == 0
				&& defectListForm.getCallFrom().trim().length() == 0) {
			errors.rejectValue("key", "defect.noInput");
		}

		if (defectListForm.getFrom().isEmpty() && !defectListForm.getTo().isEmpty()
				|| !defectListForm.getFrom().isEmpty() && defectListForm.getTo().isEmpty())
			errors.rejectValue("from", "report.date.empty");

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
