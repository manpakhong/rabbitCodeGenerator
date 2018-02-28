package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.web.form.AccountGroupForm;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AccountGroupFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(AccountGroupFormValidator.class);

	private AccountGroupManager accountGroupManager;

	private int nameMaxLength;

	private int descMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support ProblemCodeForm
		return AccountGroupForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Account Group Form");

		AccountGroupForm accountGroupForm = (AccountGroupForm) target;
		

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "defect.name.required");

		if (!errors.hasFieldErrors("name")) {
			// check name availability
			try {
				AccountGroup existingAccountGroup = accountGroupManager
						.getAccountGroupByName(accountGroupForm.getSiteKey(), accountGroupForm.getName());

				if (existingAccountGroup != null && (accountGroupForm.getKey() == null
						|| (!accountGroupForm.getKey().equals(existingAccountGroup.getKey())))) {
					logger.debug("***name exist");
					errors.rejectValue("name", "defect.name.exist");
				}
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("code", "defect.name.exist");
			}

		}

		if (!errors.hasFieldErrors("selectedResponsibleAccountKeyList")) {
			if (accountGroupForm.getSelectedResponsibleAccountKeyList() == null) {
				errors.rejectValue("selectedResponsibleAccountKeyList", "account.account.responsible.required");
				logger.debug("***responsible account required");
			}
		}
		
		
		
		if (!errors.hasFieldErrors("name")) {
			if(accountGroupForm.getName()!=null){
			if (accountGroupForm.getName().length() > nameMaxLength) {
				errors.rejectValue("name", "defect.name.exceeded", new Object[] { nameMaxLength }, null);
			}}
		}

		if (!errors.hasFieldErrors("desc")) {
			if(accountGroupForm.getDesc()!=null){
			if (accountGroupForm.getDesc().length() > descMaxLength) {
				errors.rejectValue("desc", "defect.desc.exceeded", new Object[] { descMaxLength }, null);
			}}
		}
		
	}


	public AccountGroupManager getAccountGroupManager() {
		return accountGroupManager;
	}

	public void setAccountGroupManager(AccountGroupManager accountGroupManager) {
		this.accountGroupManager = accountGroupManager;
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
