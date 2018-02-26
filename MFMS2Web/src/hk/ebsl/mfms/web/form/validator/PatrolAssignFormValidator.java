package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.web.form.PatrolAssignForm;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PatrolAssignFormValidator implements  Validator {

	public final static Logger logger = Logger.getLogger(PatrolAssignFormValidator.class);

	@Override
	public boolean supports(Class<?> paramClass) {
		// TODO Auto-generated method stub
		return PatrolAssignForm.class.equals(paramClass);
	}

	@Override
	public void validate(Object paramObject, Errors paramErrors) {
		// TODO Auto-generated method stub
		
		PatrolAssignForm form = (PatrolAssignForm)paramObject;
		
//		ValidationUtils.rejectIfEmptyOrWhitespace(paramErrors, "routeCode", "patrol.route.name.empty");
//		ValidationUtils.rejectIfEmptyOrWhitespace(paramErrors, "routeName", "patrol.route.name.empty");
		
		//ValidationUtils.rejectIfEmpty(paramErrors, "emptyTest", "patrol.route.name.empty");
		
	}
}
