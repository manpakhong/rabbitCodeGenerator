package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.web.form.LocationForm;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class LocationFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(LocationFormValidator.class);

	private LocationManager locationManager;

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	private MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support LocationForm
		return LocationForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Location Form");

		LocationForm locationForm = (LocationForm) target;
		if (!errors.hasFieldErrors("parentKey")) {
			try {
				if (locationForm.getParentKey() == null
						&& locationManager.getLocationsBySiteKey(locationForm.getSiteKey()).size() > 0) {
					try {

						if ((locationForm.getKey() != null
								&& locationManager.getLocationByKey(locationForm.getKey()).getLevelKey() != 1)) {

							errors.rejectValue("parentKey", "defect.parent.required");
						}

						if (locationForm.getKey() == null) {
							errors.rejectValue("parentKey", "defect.parent.required");
						}

					} catch (MFMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");

		if (!errors.hasFieldErrors("code")) {
			// check name availability
			try {
				Location existingLocation = locationManager.getLocationByCode(locationForm.getSiteKey(),
						locationForm.getCode(), false);
				if (existingLocation != null && (locationForm.getKey() == null
						|| (!locationForm.getKey().equals(existingLocation.getKey())))) {
					errors.rejectValue("code", "defect.code.exist");
				}
			} catch (MFMSException e) {
			}
		}

		if (!errors.hasFieldErrors("code")) {
			// code is not empty
			if (locationForm.getCode().length() > codeMaxLength) {
				errors.rejectValue("code", "defect.code.exceeded", new Object[] { codeMaxLength }, null);
			}
		}

		if (!errors.hasFieldErrors("name")) {
			if(locationForm.getName()!=null){
			if (locationForm.getName().length() > nameMaxLength) {
				errors.rejectValue("name", "defect.name.exceeded", new Object[] { nameMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("desc")) {
			if(locationForm.getDesc()!=null){
			if (locationForm.getDesc().length() > descMaxLength) {
				errors.rejectValue("desc", "defect.desc.exceeded", new Object[] { descMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("tagId")) {
			if(locationForm.getTagId()!=null){
			if (locationForm.getTagId().length() > maxLength) {
				errors.rejectValue("tagId", "defect.tagId.exceeded", new Object[] { maxLength }, null);
			}}
		}
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
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
