package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.FailureClassManager;
import hk.ebsl.mfms.manager.ToolManager;
import hk.ebsl.mfms.web.form.FailureClassForm;
import hk.ebsl.mfms.web.form.ToolForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FailureClassFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(FailureClassFormValidator.class);

	private FailureClassManager failureClassManager;

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support FailureClassForm
		return FailureClassForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate FailureClass Form");

		FailureClassForm failureClassForm = (FailureClassForm) target;
		logger.debug("FailureClassForm Code: " + failureClassForm.getCode());
		logger.debug("FailureClassForm Name: " + failureClassForm.getName());
		logger.debug("FailureClassForm Desc: " + failureClassForm.getDesc());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");

		if (!errors.hasFieldErrors("code")) {
			// check name availability
			try {
				FailureClass existingFailureClass = failureClassManager
						.getFailureClassByCode(failureClassForm.getSiteKey(), failureClassForm.getCode(), false);

				if (existingFailureClass != null && (failureClassForm.getKey() == null
						|| (!failureClassForm.getKey().equals(existingFailureClass.getKey())))) {
					errors.rejectValue("code", "defect.code.exist");
				}
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("code", "defect.code.exist");
			}

		}
		if (!errors.hasFieldErrors("code")) {
			// code is not empty
			if(failureClassForm.getCode()!=null){
			if (failureClassForm.getCode().length() > codeMaxLength) {
				errors.rejectValue("code", "defect.code.exceeded", new Object[] { codeMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("name")) {
			if(failureClassForm.getName()!=null){
			if (failureClassForm.getName().length() > nameMaxLength) {
				errors.rejectValue("name", "defect.name.exceeded", new Object[] { nameMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("desc")) {
			if(failureClassForm.getDesc()!=null){
			if (failureClassForm.getDesc().length() > descMaxLength) {
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

	public FailureClassManager getFailureClassManager() {
		return failureClassManager;
	}

	public void setFailureClassManager(FailureClassManager failureClassManager) {
		this.failureClassManager = failureClassManager;
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
