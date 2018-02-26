package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.StatusAccessMode;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.StatusAccessModeManager;
import hk.ebsl.mfms.web.form.RoleForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class RoleFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(RoleFormValidator.class);

	private RoleManager roleManager;

	private int maxNameLength;

	private int maxDescriptionLength;

	private StatusAccessModeManager statusAccessModeManager;

	private String defaultSiteAdminRoleName;

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support SiteForm
		return RoleForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		logger.debug("Validate Role Form");

		RoleForm roleForm = (RoleForm) target;
		logger.debug("RoleForm Name: " + roleForm.getName());
		logger.debug("RoleForm Desc: " + roleForm.getDesc());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "role.name.required");
		// ValidationUtils.rejectIfEmptyOrWhitespace(errors, "desc",
		// "role.description.required");

		if (!errors.hasFieldErrors("name")) {
			// name is not empty
			if (roleForm.getName().length() > maxNameLength) {
				errors.rejectValue("name", "role.name.max.length.exceeded", new Object[] { maxNameLength }, null);
			}
		}

		if (!errors.hasFieldErrors("name") && roleForm.getKey() != null) {

			try {
				Role originalRole = roleManager.getRoleByKey(roleForm.getKey(), false);

				if (originalRole != null) {
					if (defaultSiteAdminRoleName.equals(originalRole.getName())
							&& !roleForm.getName().equals(originalRole.getName())) {
						// name change is not allowed for default site admin
						// role
						errors.rejectValue("name", "role.name.cannot.be.changed");
					}
				} else {

				}
			} catch (MFMSException e) {
				// any error
				e.printStackTrace();
			}
		}

		if (!errors.hasFieldErrors("name")) {
			// check name availability
			try {
				Role existingRole = roleManager.getRoleByName(roleForm.getSiteKey(), roleForm.getName(), false);

				if (existingRole != null
						&& (roleForm.getKey() == null || !roleForm.getKey().equals(existingRole.getKey()))) {
					errors.rejectValue("name", "role.name.exist");
				}
			} catch (MFMSException e) {
				// any error
				errors.rejectValue("name", "role.name.exist");
			}

		}

		if (!errors.hasFieldErrors("desc")) {
			// Description field is not null
			if (null != roleForm.getDesc() && roleForm.getDesc().length() > maxDescriptionLength) {
				errors.rejectValue("desc", "role.description.max.length.exceeded",
						new Object[] { maxDescriptionLength }, null);
			}
		}
		if(roleForm.getModeKey()!=null){
		if (roleForm.getModeKey() != -1) {
			logger.debug("Check mode key");
			try {
				StatusAccessMode sam = statusAccessModeManager.getStatusAccessModeByModeKey(roleForm.getModeKey());
				if (sam == null) {
					errors.rejectValue("modeKey", "role.status.access.mode.not.exist");
				}
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errors.rejectValue("modeKey", "role.status.access.mode.not.exist");
			}
		}
		}

	}

	public int getMaxNameLength() {
		return maxNameLength;
	}

	public void setMaxNameLength(int maxNameLength) {
		this.maxNameLength = maxNameLength;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public int getMaxDescriptionLength() {
		return maxDescriptionLength;
	}

	public void setMaxDescriptionLength(int maxDescriptionLength) {
		this.maxDescriptionLength = maxDescriptionLength;
	}

	public StatusAccessModeManager getStatusAccessModeManager() {
		return statusAccessModeManager;
	}

	public void setStatusAccessModeManager(StatusAccessModeManager statusAccessModeManager) {
		this.statusAccessModeManager = statusAccessModeManager;
	}

	public String getDefaultSiteAdminRoleName() {
		return defaultSiteAdminRoleName;
	}

	public void setDefaultSiteAdminRoleName(String defaultSiteAdminRoleName) {
		this.defaultSiteAdminRoleName = defaultSiteAdminRoleName;
	}

}
