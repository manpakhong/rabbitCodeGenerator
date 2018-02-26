package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.web.form.UserAccountForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserAccountFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(UserAccountFormValidator.class);

	private UserAccountManager userAccountManager;

	private int maxLoginIdLength;

	private int maxNameLength;

	private int maxPasswordLength;

	private int maxEmailLength;

	private int maxTagIdLength;

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAccountForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		validate(target, errors, false);
	}

	public void validate(Object target, Errors errors, boolean updateOwn) {

		logger.debug("Validate User Account Form");

		UserAccountForm accountForm = (UserAccountForm) target;

		logger.debug("UserAccountForm loginId" + accountForm.getLoginId());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginId", "account.username.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "account.name.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "account.email.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNumber", "account.contact.number.required");

		if (accountForm.getKey() == null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "account.password.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "account.confirm.password.required");
		}

		if (!updateOwn) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "status", "account.status.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedRoleKey", "account.role.required");
		}

		// check Length
		if (!errors.hasFieldErrors("loginId")) {
			if (accountForm.getLoginId().length() > maxLoginIdLength) {
				errors.rejectValue("loginId", "account.username.max.length.exceeded", new Object[] { maxLoginIdLength },
						null);
			}

			String s = accountForm.getLoginId();
			Pattern p = Pattern.compile("[a-zA-Z0-9_-]");
			Matcher m = p.matcher(s);

			for (char c : s.toCharArray()) {
				if (!m.find()) {
					errors.rejectValue("loginId", "account.username.alphanumeric");
					break;
				}
			}
		}

		if (!errors.hasFieldErrors("password")) {
			if (accountForm.getPassword() != null && accountForm.getPassword().length() > maxPasswordLength) {
				errors.rejectValue("password", "account.password.max.length.exceeded",
						new Object[] { maxPasswordLength }, null);
			}
		}

		if (!errors.hasFieldErrors("email")) {
			if (accountForm.getEmail().length() > maxEmailLength) {
				errors.rejectValue("email", "account.email.max.length.exceeded", new Object[] { maxEmailLength }, null);
			}
		}

		if (!StringUtils.isEmpty(accountForm.getName())) {
			if (accountForm.getName().length() > maxNameLength) {
				errors.rejectValue("name", "account.name.max.length.exceeded", new Object[] { maxNameLength }, null);
			}
		}

		if (!errors.hasFieldErrors("contactNumber")) {
			// contact Number field is not null
			try {
				if (!StringUtils.isEmpty(accountForm.getContactCountryCode())) {
					Integer countryCode = Integer.parseInt(accountForm.getContactCountryCode());
					if (countryCode < 0) {
						logger.error("Contact Country Code is not positive integer");
						errors.rejectValue("contactNumber", "account.contact.number.integer");
					} else if (countryCode > Integer.MAX_VALUE) {
						logger.error("Contact Country Code > " + Integer.MAX_VALUE);
						errors.rejectValue("contactNumber", "account.contact.number.integer.max.exceeded",
								new Object[] { Integer.MAX_VALUE }, null);
					}
				}
				if (!StringUtils.isEmpty(accountForm.getContactAreaCode())) {
					Integer areaCode = Integer.parseInt(accountForm.getContactAreaCode());
					if (areaCode < 0) {
						logger.error("Contact Area Code is not positive integer");
						errors.rejectValue("contactNumber", "account.contact.number.integer");
					} else if (areaCode > Integer.MAX_VALUE) {
						logger.error("Contact Area Code > " + Integer.MAX_VALUE);
						errors.rejectValue("contactNumber", "account.contact.number.integer.max.exceeded",
								new Object[] { Integer.MAX_VALUE }, null);
					}
				}
				if (!StringUtils.isEmpty(accountForm.getContactNumber())) {
					// Long contactNumber =
					// Long.parseLong(accountForm.getContactNumber());
					// if (contactNumber <= 0) {
					// logger.error("Contact Number is not positive integer");
					// errors.rejectValue("contactNumber",
					// "account.contact.number.integer");
					// } else if (contactNumber > Long.MAX_VALUE) {
					// logger.error("Contact Number > " + Long.MAX_VALUE);
					// errors.rejectValue("contactNumber",
					// "account.contact.number.integer.max.exceeded",
					// new Object[] { Long.MAX_VALUE }, null);
					//
					// }

					if (accountForm.getContactNumber().length() > maxLoginIdLength)

						errors.rejectValue("contactNumber", "defect.contactTel.exceeded",
								new Object[] { maxLoginIdLength }, null);
					
					//check contact number format
					try{
						Long.parseLong(accountForm.getContactNumber());
					}catch(Exception ex){
						errors.rejectValue("contactNumber", "site.contact.number.integer.only");
					}
			}
				} catch (NumberFormatException nfe) {
				// some fields are not number
				// seems max value exceed error goes to here
				logger.error("Contact Number is not integer");
				errors.rejectValue("contactNumber", "site.contact.number.integer");
			}
		}

		if (!StringUtils.isEmpty(accountForm.getTagId())) {
			if (accountForm.getTagId().length() > maxTagIdLength) {
				errors.rejectValue("tagId", "account.tagId.max.length.exceeded", new Object[] { maxTagIdLength }, null);
			}
		}

		if (!errors.hasFieldErrors("currentPassword")) {
			if (accountForm.getConfirmPassword() != null && accountForm.getPassword() != null) {
				if (!accountForm.getConfirmPassword().isEmpty() && !accountForm.getPassword().isEmpty()) {
					try {
						UserAccount existingAccount = userAccountManager
								.getUserAccountByLoginId(accountForm.getLoginId(), false);
//						if (accountForm.getCurrentPassword() != null
//								&& !existingAccount.getPassword().equals(accountForm.getCurrentPassword())) {
							
							if (accountForm.getCurrentPassword() != null 	&& !(BCrypt.checkpw(accountForm.getCurrentPassword(), existingAccount.getPassword())) ) {

							errors.rejectValue("currentPassword", "account.current.password.incorrect");
						}

					} catch (MFMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("confirmPassword")) {
			// check password and confirm password
			if (accountForm.getConfirmPassword() != null && accountForm.getPassword() != null) {
				//if (!accountForm.getConfirmPassword().equals(accountForm.getPassword())) {
					
					if (!accountForm.getConfirmPassword().equals(accountForm.getPassword())) {
					errors.rejectValue("confirmPassword", "account.confirm.password.incorrect");
				}
			}
		}

		// check login Id availability
		if (!errors.hasFieldErrors("loginId")) {

			try {
				UserAccount existingAccount = userAccountManager.getUserAccountByLoginId(accountForm.getLoginId(),
						false);

				if (existingAccount != null
						&& (accountForm.getKey() == null || (!accountForm.getKey().equals(existingAccount.getKey())))) {
					errors.rejectValue("loginId", "account.username.exist");

				}

			} catch (MFMSException e) {
				// any error
				errors.rejectValue("loginId", "account.username.exist");
			}
		}

		// check email format

		if (!errors.hasFieldErrors("email")) {
			boolean validEmail = EmailValidator.getInstance().isValid(accountForm.getEmail());

			if (!validEmail) {
				errors.rejectValue("email", "account.email.invalid");
			}
		}
		

	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public int getMaxLoginIdLength() {
		return maxLoginIdLength;
	}

	public void setMaxLoginIdLength(int maxLoginIdLength) {
		this.maxLoginIdLength = maxLoginIdLength;
	}

	public int getMaxNameLength() {
		return maxNameLength;
	}

	public void setMaxNameLength(int maxNameLength) {
		this.maxNameLength = maxNameLength;
	}

	public int getMaxPasswordLength() {
		return maxPasswordLength;
	}

	public void setMaxPasswordLength(int maxPasswordLength) {
		this.maxPasswordLength = maxPasswordLength;
	}

	public int getMaxEmailLength() {
		return maxEmailLength;
	}

	public void setMaxEmailLength(int maxEmailLength) {
		this.maxEmailLength = maxEmailLength;
	}

	public int getMaxTagIdLength() {
		return maxTagIdLength;
	}

	public void setMaxTagIdLength(int maxTagIdLength) {
		this.maxTagIdLength = maxTagIdLength;
	}

}
