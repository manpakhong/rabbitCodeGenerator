package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.CauseCodeManager;
import hk.ebsl.mfms.web.form.CauseCodeForm;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CauseCodeFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(CauseCodeFormValidator.class);

	private CauseCodeManager causeCodeManager;

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support CauseCodeForm
		return CauseCodeForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate CauseCode Form");

		CauseCodeForm causeCodeForm = (CauseCodeForm) target;
		logger.debug("CauseCodeForm Code: " + causeCodeForm.getCode());
		logger.debug("CauseCodeForm Name: " + causeCodeForm.getName());
		logger.debug("CauseCodeForm Desc: " + causeCodeForm.getDesc());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");

		if (!errors.hasFieldErrors("code")) {
			// check name availability
			try {
				CauseCode existingCauseCode = causeCodeManager.getCauseCodeByCode(causeCodeForm.getSiteKey(),
						causeCodeForm.getCode(), false);

				if (existingCauseCode != null
						&& (causeCodeForm.getKey() == null || !causeCodeForm.getKey().equals(existingCauseCode.getKey()))) {
					errors.rejectValue("code", "defect.code.exist");
				}
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("code", "defect.code.exist");
			}

		}

		if (!errors.hasFieldErrors("code")) {
			// code is not empty
			if(causeCodeForm.getCode()!=null){
			if (causeCodeForm.getCode().length() > codeMaxLength) {
				errors.rejectValue("code", "defect.code.exceeded", new Object[] { codeMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("name")) {
			if(causeCodeForm.getName()!=null){
			if (causeCodeForm.getName().length() > nameMaxLength) {
				errors.rejectValue("name", "defect.name.exceeded", new Object[] { nameMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("desc")) {
			if(causeCodeForm.getDesc()!=null){
			if (causeCodeForm.getDesc().length() > descMaxLength) {
				errors.rejectValue("desc", "defect.desc.exceeded", new Object[] { descMaxLength }, null);
			}}
		}

	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public CauseCodeManager getCauseCodeManager() {
		return causeCodeManager;
	}

	public void setCauseCodeManager(CauseCodeManager causeCodeManager) {
		this.causeCodeManager = causeCodeManager;
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
