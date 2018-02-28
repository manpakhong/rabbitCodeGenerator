package hk.ebsl.mfms.web.form.validator;

import java.util.ArrayList;
import java.util.List;

import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.model.Patrol_Route;
import hk.ebsl.mfms.web.form.PatrolCreateForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PatrolCreateFormValidator implements Validator {

	public final static Logger logger = Logger
			.getLogger(PatrolCreateFormValidator.class);

	private RouteDefManager routeDefManager;

	private int maxCodeLength;
	private int maxNameLength;

	@Override
	public boolean supports(Class<?> paramClass) {
		// TODO Auto-generated method stub
		return PatrolCreateForm.class.equals(paramClass);
	}

	@Override
	public void validate(Object paramObject, Errors paramErrors) {
		// TODO Auto-generated method stub

		PatrolCreateForm form = (PatrolCreateForm) paramObject;

		List<Patrol_Route> list = null;
		
		
		if (form.getRouteCode() != null && !form.getRouteCode().trim().equals(""))
			list = routeDefManager.searchRoute(form.getSiteKey(), null, form.getRouteCode()
					.trim(), null);

		System.out.println("Vailadate Route Def key :"
				+ form.getSelectedLocationJson());

		ValidationUtils.rejectIfEmptyOrWhitespace(paramErrors, "routeCode",
				"patrol.route.code.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(paramErrors, "routeName",
				"patrol.route.name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(paramErrors,
				"selectedLocationJson", "patrol.route.location.empty");
		if (form.getSelectedLocationJson() != null) {
			if (form.getSelectedLocationJson().equals("[]")) {
				paramErrors.rejectValue("selectedLocationJson",
						"patrol.route.location.empty");
			}
		}

		if (form.getRouteCode() != null
				&& form.getRouteCode().length() > maxCodeLength) {
			paramErrors.rejectValue("routeCode",
					"patrol.route.code.max.length.exceeded",
					new Object[] { maxCodeLength }, null);
		}

		if (form.getRouteName() != null
				&& form.getRouteName().length() > maxNameLength) {
			paramErrors.rejectValue("routeName",
					"patrol.route.name.max.length.exceeded",
					new Object[] { maxNameLength }, null);
		}

		if (list != null && form.getRouteDefKey() == 0) {
			paramErrors
					.rejectValue("routeCode", "patrol.route.code.name.exist");
		}

	}

	public RouteDefManager getRouteDefManager() {
		return routeDefManager;
	}

	public void setRouteDefManager(RouteDefManager routeDefManager) {
		this.routeDefManager = routeDefManager;
	}

	public int getMaxCodeLength() {
		return maxCodeLength;
	}

	public void setMaxCodeLength(int maxCodeLength) {
		this.maxCodeLength = maxCodeLength;
	}

	public int getMaxNameLength() {
		return maxNameLength;
	}

	public void setMaxNameLength(int maxNameLength) {
		this.maxNameLength = maxNameLength;
	}

}
