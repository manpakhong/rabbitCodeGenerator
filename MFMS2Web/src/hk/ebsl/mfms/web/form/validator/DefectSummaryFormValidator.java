package hk.ebsl.mfms.web.form.validator;

import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.web.form.DefectListForm;
import hk.ebsl.mfms.web.form.DefectStatusSummaryForm;
import hk.ebsl.mfms.web.form.ProblemCodeForm;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DefectSummaryFormValidator implements Validator {

	public final static Logger logger = Logger.getLogger(DefectSummaryFormValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		// indicate this validator only support ProblemCodeForm
		return DefectStatusSummaryForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		DefectStatusSummaryForm defectStatusSummaryForm = (DefectStatusSummaryForm) target;

		if (null!=defectStatusSummaryForm.getFrom()) {
			if (defectStatusSummaryForm.getFrom().isEmpty() 
					&& (!defectStatusSummaryForm.getTo().isEmpty() || !defectStatusSummaryForm.getFrom().isEmpty())
					&& defectStatusSummaryForm.getTo().isEmpty())
				errors.rejectValue("from", "report.date.empty");
		}
	}

	public static Logger getLogger() {
		return logger;
	}

}
