package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.EquipmentManager;
import hk.ebsl.mfms.web.form.EquipmentForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EquipmentFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(EquipmentFormValidator.class);

	private EquipmentManager equipmentManager;

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	
	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support EquipmentForm
		return EquipmentForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Equipment Form");

		EquipmentForm equipmentForm = (EquipmentForm) target;
		logger.debug("EquipmentForm Code: " + equipmentForm.getCode());
		logger.debug("EquipmentForm Name: " + equipmentForm.getName());
		logger.debug("EquipmentForm Desc: " + equipmentForm.getDesc());
		
		if (!errors.hasFieldErrors("locationKey")) {

			if (equipmentForm.getLocationKey() == null) {
				//errors.rejectValue("locationKey", "error.required", new Object[] { "equipment.location" }, null);
				errors.rejectValue("locationKey", "defect.location.required");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");

		if (!errors.hasFieldErrors("code")) {
			// check name availability
			try {
				Equipment existingEquipment = equipmentManager.getEquipmentByCode(equipmentForm.getSiteKey(), equipmentForm.getCode(), false);

				if (existingEquipment != null 
						&& (equipmentForm.getKey() == null || !equipmentForm.getKey().equals(existingEquipment.getKey()))) {
					errors.rejectValue("code", "defect.code.exist");
				}
				
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("code", "defect.code.exist");
			}

		}

		if (!errors.hasFieldErrors("code")) {
			// code is not empty
			if(equipmentForm.getCode()!=null){
			if (equipmentForm.getCode().length() > codeMaxLength) {
				errors.rejectValue("code", "defect.code.exceeded", new Object[] { codeMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("name")) {
			if(equipmentForm.getName()!=null){
			if (equipmentForm.getName().length() > nameMaxLength) {
				errors.rejectValue("name", "defect.name.exceeded", new Object[] { nameMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("desc")) {
			if(equipmentForm.getDesc()!=null){
			if (equipmentForm.getDesc().length() > descMaxLength) {
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

	public EquipmentManager getEquipmentManager() {
		return equipmentManager;
	}

	public void setEquipmentManager(EquipmentManager equipmentManager) {
		this.equipmentManager= equipmentManager;
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
