package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.CauseCodeManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.web.form.CauseCodeForm;
import hk.ebsl.mfms.web.form.ForgetPasswordForm;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ForgetPasswordFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(ForgetPasswordFormValidator.class);

	private UserAccountManager userAccountManager;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support CauseCodeForm
		return ForgetPasswordForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		ForgetPasswordForm forgetPasswordForm = (ForgetPasswordForm) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "loginId", "account.username.required");

		if (!errors.hasFieldErrors("loginId")) {

			UserAccount existingAccount = null;
			try {
				existingAccount = userAccountManager.getUserAccountByLoginId(forgetPasswordForm.getLoginId(), false);
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (existingAccount == null) {
				errors.rejectValue("loginId", "account.not.exist");
			}

		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "account.email.required");

		if (!errors.hasFieldErrors("email")) {
			boolean validEmail = EmailValidator.getInstance().isValid(forgetPasswordForm.getEmail());

			if (!validEmail) {
				errors.rejectValue("email", "account.email.invalid");
			}
		}

		if (!errors.hasFieldErrors("email") && !errors.hasFieldErrors("loginId")) {

			UserAccount existingAccount = null;
			try {
				existingAccount = userAccountManager.getUserAccountByLoginId(forgetPasswordForm.getLoginId(), false);
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!existingAccount.getEmail().equals(forgetPasswordForm.getEmail()))
				errors.rejectValue("email", "account.email.notMatch");
		}

	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public static Logger getLogger() {
		return logger;
	}

}
