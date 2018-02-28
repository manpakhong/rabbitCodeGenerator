package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.ProblemCodeManager;
import hk.ebsl.mfms.web.form.ProblemCodeForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ProblemCodeFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(ProblemCodeFormValidator.class);

	private ProblemCodeManager problemCodeManager;

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

		ProblemCodeForm problemCodeForm = (ProblemCodeForm) target;

		if (!errors.hasFieldErrors("parentKey")) {

			if (problemCodeForm.getParentKey() == null) {
				errors.rejectValue("parentKey", "defect.failureClass.required");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");

		if (!errors.hasFieldErrors("code")) {
			// check name availability
			try {
				ProblemCode existingProblemCode = problemCodeManager.getProblemCodeByCode(problemCodeForm.getSiteKey(),
						problemCodeForm.getCode());

				if (existingProblemCode != null && (problemCodeForm.getKey() == null
						|| (!problemCodeForm.getKey().equals(existingProblemCode.getKey())))) {
					errors.rejectValue("code", "defect.code.exist");
				}
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("code", "defect.code.exist");
			}

		}

		if (!errors.hasFieldErrors("code")) {
			// code is not empty
			if(problemCodeForm.getCode()!=null){
			if (problemCodeForm.getCode().length() > codeMaxLength) {
				errors.rejectValue("code", "defect.code.exceeded", new Object[] { codeMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("name")) {
			if(problemCodeForm.getName()!=null){
			if (problemCodeForm.getName().length() > nameMaxLength) {
				errors.rejectValue("name", "defect.name.exceeded", new Object[] { nameMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("desc")) {
			if(problemCodeForm.getDesc()!=null){
			if (problemCodeForm.getDesc().length() > descMaxLength) {
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

	public ProblemCodeManager getProblemCodeManager() {
		return problemCodeManager;
	}

	public void setProblemCodeManager(ProblemCodeManager problemCodeManager) {
		this.problemCodeManager = problemCodeManager;
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
