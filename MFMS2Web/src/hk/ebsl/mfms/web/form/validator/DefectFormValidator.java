package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.utility.DateUtil;
import hk.ebsl.mfms.utility.FileBucket;
import hk.ebsl.mfms.web.form.DefectForm;

import java.sql.Timestamp;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class DefectFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(DefectFormValidator.class);

	private DefectManager defectManager;

	private int maxPictureSize;

	private int maxVideoSize;

	private int maxLength;

	private int codeMaxLength;

	private int nameMaxLength;

	private int descMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support DefectForm
		return DefectForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Defect Form");

		DefectForm defectForm = (DefectForm) target;
		Integer MB = 1024 * 1024;

		FileBucket file = null;

		if (!errors.hasFieldErrors("locationKey")) {

			if (defectForm.getLocationKey() == null) {
				errors.rejectValue("locationKey", "defect.location.required");
				
			}
		}

		if (!errors.hasFieldErrors("failureClassKey")) {

			if (defectForm.getFailureClassKey() == null) {
				errors.rejectValue("failureClassKey", "defect.failureClass.required");
			}
		}

		if (!errors.hasFieldErrors("problemCodeKey")) {

			if (defectForm.getProblemCodeKey() == null) {
				errors.rejectValue("problemCodeKey", "defect.problemCode.required");
			}
		}
		
		//for test
		if (!errors.hasFieldErrors("assignedGroupKey")) {

			if (defectForm.getAssignedGroupKey() == null) {
				errors.rejectValue("assignedGroupKey", "defect.assignedGroup.required");
			}
		}
		
//		if (!errors.hasFieldErrors("assignedAccountKey")) {
//			if (defectForm.getAssignedAccountKey() == null ) {
//				errors.rejectValue("assignedAccountKey", "defect.assignedAccount.required");
//			}
//		}
		

		if (!errors.hasFieldErrors("contactEmail")) {
			if (defectForm.getContactEmail() != null) {
				if (!defectForm.getContactEmail().isEmpty()) {
					boolean validEmail = EmailValidator.getInstance().isValid(defectForm.getContactEmail());

					if (!validEmail) {
						errors.rejectValue("contactEmail", "account.email.invalid");
					}
				}
			}
		}

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "defect.code.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "desc", "defect.description.required");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactName", "defect.contactName.required");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactTel", "defect.contactTel.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "targetStartDateTime", "defect.targetStartDate.required");
		
		if(defectForm.getStatusID().equals("C"))
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "actualStartDateTime", "defect.actualStartDate.required");

		if (!defectForm.getTargetStartDateTime().isEmpty() && !defectForm.getTargetFinishDateTime().isEmpty()) {
			try {
				Timestamp startTime = DateUtil.convertStringToTimestamp(defectForm.getTargetStartDateTime());
				Timestamp endTime = DateUtil.convertStringToTimestamp(defectForm.getTargetFinishDateTime());
				
				if (endTime.before(startTime)) {
					errors.rejectValue("targetStartDateTime", "defect.targetFinish.before.targetStart");
				}
			} catch (Exception e) {
				
			}
		}

		if (!defectForm.getActualStartDateTime().isEmpty() && !defectForm.getActualFinishDateTime().isEmpty()) {
			try {
				Timestamp startTime = DateUtil.convertStringToTimestamp(defectForm.getActualStartDateTime());
				Timestamp endTime = DateUtil.convertStringToTimestamp(defectForm.getActualFinishDateTime());
				
				if (endTime.before(startTime)) {
					errors.rejectValue("actualStartDateTime", "defect.actualFinish.before.actualStart");
				}
			} catch (Exception e) {
				
			}
		}
		
//		if (!errors.hasFieldErrors("code")) {
//			// code is not empty
//			if (defectForm.getCode().length() > maxLength) {
//				errors.rejectValue("code", "error.code.exceeded", new Object[] { maxLength }, null);
//			}
//
//			try {
//				Defect existingDefect = defectManager.getDefectByCode(defectForm.getSiteKey(), defectForm.getCode(),
//						false);
//
//				if (existingDefect != null
//						&& (defectForm.getKey() == null || (!defectForm.getKey().equals(existingDefect.getKey())))) {
//					errors.rejectValue("code", "defect.code.exist");
//				}
//			} catch (MFMSException e) {
//				// any error
//				errors.rejectValue("code", "defect.code.exist");
//			}
//		}

		if (!errors.hasFieldErrors("desc")) {
			if (defectForm.getDesc().length() > descMaxLength) {
				errors.rejectValue("desc", "defect.desc.exceeded", new Object[] { descMaxLength }, null);
			}
		}

		if (!errors.hasFieldErrors("contactName")) {
			if (defectForm.getContactName().length() > nameMaxLength) {
				errors.rejectValue("contactName", "defect.contactName.exceeded", new Object[] { nameMaxLength }, null);
			}
		}

		if (!errors.hasFieldErrors("contactTel")) {
			if (defectForm.getContactTel().length() > codeMaxLength) {
				errors.rejectValue("contactTel", "defect.contactTel.exceeded", new Object[] { codeMaxLength }, null);
			}
		}

		if (!errors.hasFieldErrors("contactEmail")) {

			if (defectForm.getContactEmail() != null) {

				if (defectForm.getContactEmail().length() > nameMaxLength) {
					errors.rejectValue("contactEmail", "defect.contactEmail.exceeded", new Object[] { nameMaxLength },
							null);
				}
			}

		}
		if (defectForm.getEmergencyTel() != null) {
			if (!errors.hasFieldErrors("emergencyTel")) {
				if (defectForm.getEmergencyTel().length() > codeMaxLength) {
					errors.rejectValue("emergencyTel", "defect.emergencyTel.exceeded", new Object[] { codeMaxLength },
							null);
				}
			}
		}

		if (defectForm.getRemarks() != null) {
			if (!errors.hasFieldErrors("remarks")) {
				if (defectForm.getRemarks().length() > descMaxLength) {
					errors.rejectValue("remarks", "defect.remarks.exceeded", new Object[] { descMaxLength }, null);
				}
			}
		}

		Boolean maxName = false;

		Boolean maxDesc = false;

		Boolean maxSize = false;

		Boolean invalidFormat = false;
		if (defectForm.getPhotoRemain() >= 1) {

			if (defectForm.getPicture1().getFile() != null) {
				if (defectForm.getPicture1().getFile().getSize() > 0) {
					file = defectForm.getPicture1();
					if (file.getFile().getSize() / MB > maxPictureSize) {
						maxSize = true;
					}

					if (file.getFile().getOriginalFilename().length() > nameMaxLength) {
						maxName = true;
					}

					if (file.getDesc().length() > descMaxLength) {
						maxDesc = true;
					}

					logger.debug("-------" + file.getFile().getContentType());

					if (!file.getFile().getContentType().contains("image")) {
						invalidFormat = true;
					}

				}
			}
		}
		if (defectForm.getPhotoRemain() >= 2) {
			if (defectForm.getPicture2().getFile() != null) {
				if (defectForm.getPicture2().getFile().getSize() > 0) {
					file = defectForm.getPicture2();
					if (file.getFile().getSize() / MB > maxPictureSize) {
						maxSize = true;
					}

					if (file.getFile().getOriginalFilename().length() > nameMaxLength) {
						maxName = true;
					}

					if (file.getDesc().length() > descMaxLength) {
						maxDesc = true;
					}

					if (!file.getFile().getContentType().contains("image")) {
						invalidFormat = true;
					}

				}
			}
		}
		if (defectForm.getPhotoRemain() >= 3) {
			if (defectForm.getPicture3().getFile() != null) {
				if (defectForm.getPicture3().getFile().getSize() > 0) {
					file = defectForm.getPicture3();
					if (file.getFile().getSize() / MB > maxPictureSize) {
						maxSize = true;
					}

					if (file.getFile().getOriginalFilename().length() > nameMaxLength) {
						maxName = true;
					}

					if (file.getDesc().length() > descMaxLength) {
						maxDesc = true;
					}

					if (!file.getFile().getContentType().contains("image")) {
						invalidFormat = true;
					}
				}
			}
		}

		if (defectForm.getPhotoRemain() >= 4) {
			if (defectForm.getPicture4().getFile() != null) {
				if (defectForm.getPicture4().getFile().getSize() > 0) {
					file = defectForm.getPicture4();
					if (file.getFile().getSize() / MB > maxPictureSize) {
						maxSize = true;
					}
					if (file.getFile().getOriginalFilename().length() > nameMaxLength) {
						maxName = true;
					}

					if (file.getDesc().length() > descMaxLength) {
						maxDesc = true;
					}

					if (!file.getFile().getContentType().contains("image")) {
						invalidFormat = true;
					}
				}
			}
		}

		if (defectForm.getPhotoRemain() >= 5) {

			if (defectForm.getPicture5().getFile() != null) {
				if (defectForm.getPicture5().getFile().getSize() > 0) {
					file = defectForm.getPicture5();
					if (file.getFile().getSize() / MB > maxPictureSize) {
						maxSize = true;
					}
					if (file.getFile().getOriginalFilename().length() > nameMaxLength) {
						maxName = true;
					}

					if (file.getDesc().length() > descMaxLength) {
						maxDesc = true;
					}

					if (!file.getFile().getContentType().contains("image")) {
						invalidFormat = true;
					}
				}
			}
		}

		file = defectForm.getVideo();
		if (defectForm.getVideoRemain() >= 1) {
			if (file.getFile() != null) {
				if (file.getFile().getSize() > 0) {
					if (file.getFile().getSize() / MB > maxVideoSize) {
						errors.rejectValue("key", "defect.videoSize.exceeded", new Object[] { maxVideoSize }, null);
					}

					if (file.getFile().getOriginalFilename().length() > nameMaxLength) {
						maxName = true;
					}

					if (file.getDesc().length() > descMaxLength) {
						maxDesc = true;
					}

					if (!file.getFile().getContentType().contains("video")) {
						invalidFormat = true;
					}
				}
			}
		}

		if (maxName)
			errors.rejectValue("key", "defect.fileName.exceeded", new Object[] { nameMaxLength }, null);
		if (maxDesc)
			errors.rejectValue("key", "defect.fileDesc.exceeded", new Object[] { descMaxLength }, null);
		if (maxSize)
			errors.rejectValue("key", "defect.pictureSize.exceeded", new Object[] { maxPictureSize }, null);
		if (invalidFormat)
			errors.rejectValue("key", "defect.file.invaildFormat");

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

	public int getMaxPictureSize() {
		return maxPictureSize;
	}

	public void setMaxPictureSize(int maxPictureSize) {
		this.maxPictureSize = maxPictureSize;
	}

	public int getMaxVideoSize() {
		return maxVideoSize;
	}

	public void setMaxVideoSize(int maxVideoSize) {
		this.maxVideoSize = maxVideoSize;
	}

}
