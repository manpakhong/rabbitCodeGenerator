package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.ToolManager;
import hk.ebsl.mfms.web.form.ToolForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ToolFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(ToolFormValidator.class);

	private ToolManager toolManager;

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support ToolForm
		return ToolForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Tool Form");

		ToolForm toolForm = (ToolForm) target;
		logger.debug("ToolForm Code: " + toolForm.getCode());
		logger.debug("ToolForm Name: " + toolForm.getName());
		logger.debug("ToolForm Desc: " + toolForm.getDesc());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");

		if (!errors.hasFieldErrors("code")) {
			// check name availability
			try {
				Tool existingTool = toolManager.getToolByCode(toolForm.getSiteKey(), toolForm.getCode(), false);

				if (existingTool != null && (toolForm.getKey() == null || !toolForm.getKey().equals(existingTool.getKey()))) {
					errors.rejectValue("code", "defect.code.exist");
				}
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("code", "defect.code.exist");
			}

		}

		if (!errors.hasFieldErrors("code")) {
			// code is not empty
			if (toolForm.getCode() != null) {
				if (toolForm.getCode().length() > codeMaxLength) {
					errors.rejectValue("code", "defect.code.exceeded", new Object[] { codeMaxLength }, null);
				}
			}
		}

		if (!errors.hasFieldErrors("name")) {
			if (toolForm.getName() != null) {
				if (toolForm.getName().length() > nameMaxLength) {
					errors.rejectValue("name", "defect.name.exceeded", new Object[] { nameMaxLength }, null);
				}
			}
		}

		if (!errors.hasFieldErrors("desc")) {
			if (toolForm.getDesc() != null) {
				if (toolForm.getDesc().length() > descMaxLength) {
					errors.rejectValue("desc", "defect.desc.exceeded", new Object[] { descMaxLength }, null);
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

	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
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
