package hk.ebsl.mfms.web.form.validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.web.form.SiteForm;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class SiteFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(SiteFormValidator.class);

	private UserAccountManager userAccountManager;

	private SiteManager siteManager;

	private int maxNameLength;

	private int maxAddressLength;

	private int maxDefaultAdminIdLength;

	private int maxDefaultAdminNameLength;

	private int maxDefaultAdminPassLength;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support SiteForm
		return SiteForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Site Form");

		SiteForm siteForm = (SiteForm) target;
		logger.debug("SiteForm Name: " + siteForm.getName());
		logger.debug("SiteForm Contact Number: " + siteForm.getContactNumber());
		logger.debug("SiteForm Address: " + siteForm.getAddress());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "site.name.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNumber", "site.contact.number.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "site.address.required");

		if (!errors.hasFieldErrors("name")) {
			// name is not empty
			if (siteForm.getName().length() > maxNameLength) {
				errors.rejectValue("name", "site.name.max.length.exceeded", new Object[] { maxNameLength }, null);
			}
		}

		if (!errors.hasFieldErrors("name")) {
			// check name availability
			try {

				Site existingSite = siteManager.getSiteByName(siteForm.getName());

				// site exist and not the current editing site
				if (existingSite != null && (siteForm.getKey() == null || !siteForm.getKey().equals(existingSite.getKey()))) {
					errors.rejectValue("name", "site.name.exist");
				}
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("name", "site.name.exist");
			}

		}

		if (!errors.hasFieldErrors("contactNumber")) {
			// contact Number field is not null
			try {
				if (!StringUtils.isEmpty(siteForm.getContactCountryCode())) {
					Integer countryCode = Integer.parseInt(siteForm.getContactCountryCode());
					if (countryCode < 0) {
						logger.error("Contact Country Code is not positive integer");
						errors.rejectValue("contactNumber", "site.contact.number.integer");
					} else if (countryCode > Integer.MAX_VALUE) {
						logger.error("Contact Country Code > " + Integer.MAX_VALUE);
						errors.rejectValue("contactNumber", "site.contact.number.integer.max.exceeded",
								new Object[] { Integer.MAX_VALUE }, null);
					}
				}
				if (!StringUtils.isEmpty(siteForm.getContactAreaCode())) {
					Integer areaCode = Integer.parseInt(siteForm.getContactAreaCode());
					if (areaCode < 0) {
						logger.error("Contact Area Code is not positive integer");
						errors.rejectValue("contactNumber", "site.contact.number.integer");
					} else if (areaCode > Integer.MAX_VALUE) {
						logger.error("Contact Area Code > " + Integer.MAX_VALUE);
						errors.rejectValue("contactNumber", "site.contact.number.integer.max.exceeded",
								new Object[] { Integer.MAX_VALUE }, null);
					}
				}
				if (!StringUtils.isEmpty(siteForm.getContactNumber())) {
					Long contactNumber = Long.parseLong(siteForm.getContactNumber());
					if (contactNumber < 0) {
						logger.error("Contact Number is not positive integer");
						errors.rejectValue("contactNumber", "site.contact.number.integer");
					} else if (contactNumber > Long.MAX_VALUE) {
						logger.error("Contact Number > " + Long.MAX_VALUE);
						errors.rejectValue("contactNumber", "site.contact.number.integer.max.exceeded",
								new Object[] { Long.MAX_VALUE }, null);
					}
				}
			} catch (NumberFormatException nfe) {
				// some fields are not number
				// seems max value exceed error goes to here
				logger.error("Contact Number is not integer");
				errors.rejectValue("contactNumber", "site.contact.number.integer");
			}
		}

		if (!StringUtils.isEmpty(siteForm.getAddress())) {
			// check address length
			if (siteForm.getAddress().length() > maxAddressLength) {
				errors.rejectValue("address", "site.address.max.length.exceeded", new Object[] { maxAddressLength },
						null);
			}
		}

		if (siteForm.getCreateDefaultAdmin()) {
			// validate create user form
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "defaultAdminId", "account.username.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "defaultAdminPass", "account.password.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "defaultAdminName", "account.name.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "defaultAdminConfirmPass",
					"account.confirm.password.required");

			// check Length
			if (!errors.hasFieldErrors("defaultAdminId")) {
				if (siteForm.getDefaultAdminId().length() > maxDefaultAdminIdLength) {
					errors.rejectValue("defaultAdminId", "account.username.max.length.exceeded",
							new Object[] { maxDefaultAdminIdLength }, null);
				}
			}

			if (!errors.hasFieldErrors("defaultAdminPass")) {
				if (siteForm.getDefaultAdminPass().length() > maxDefaultAdminPassLength) {
					errors.rejectValue("defaultAdminPass", "account.password.max.length.exceeded",
							new Object[] { maxDefaultAdminPassLength }, null);
				}
			}

			if (!StringUtils.isEmpty(siteForm.getDefaultAdminName())) {
				if (siteForm.getDefaultAdminName().length() > maxDefaultAdminNameLength) {
					errors.rejectValue("defaultAdminName", "account.name.max.length.exceeded",
							new Object[] { maxDefaultAdminNameLength }, null);
				}
			}

			if (!errors.hasFieldErrors("defaultAdminPass") && !errors.hasFieldErrors("defaultAdminConfirmPass")) {
				// check password and confirm password
				if (!siteForm.getDefaultAdminConfirmPass().equals(siteForm.getDefaultAdminPass())) {
					errors.rejectValue("defaultAdminConfirmPass", "account.confirm.password.incorrect");
				}
			}

			// check user name
			if (!errors.hasFieldErrors("defaultAdminId")) {

				String s = siteForm.getDefaultAdminId();
				Pattern p = Pattern.compile("[a-zA-Z0-9_-]");
				Matcher m = p.matcher(s);

				for (char c : s.toCharArray()) {
					if (!m.find()) {
						errors.rejectValue("defaultAdminId", "account.username.alphanumeric");
						break;
					}
				}

				try {
					UserAccount existingUser = userAccountManager.getUserAccountByLoginId(siteForm.getDefaultAdminId(),
							false);

					if (existingUser != null) {
						// user already exist
						errors.rejectValue("defaultAdminId", "account.username.exist");
					}
				} catch (MFMSException e) {
					// any error
					errors.rejectValue("defaultAdminId", "account.username.exist");
				}
			}
		}
	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public int getMaxNameLength() {
		return maxNameLength;
	}

	public void setMaxNameLength(int maxNameLength) {
		this.maxNameLength = maxNameLength;
	}

	public int getMaxAddressLength() {
		return maxAddressLength;
	}

	public void setMaxAddressLength(int maxAddressLength) {
		this.maxAddressLength = maxAddressLength;
	}

	public int getMaxDefaultAdminIdLength() {
		return maxDefaultAdminIdLength;
	}

	public void setMaxDefaultAdminIdLength(int maxDefaultAdminIdLength) {
		this.maxDefaultAdminIdLength = maxDefaultAdminIdLength;
	}

	public int getMaxDefaultAdminNameLength() {
		return maxDefaultAdminNameLength;
	}

	public void setMaxDefaultAdminNameLength(int maxDefaultAdminNameLength) {
		this.maxDefaultAdminNameLength = maxDefaultAdminNameLength;
	}

	public int getMaxDefaultAdminPassLength() {
		return maxDefaultAdminPassLength;
	}

	public void setMaxDefaultAdminPassLength(int maxDefaultAdminPassLength) {
		this.maxDefaultAdminPassLength = maxDefaultAdminPassLength;
	}

}
