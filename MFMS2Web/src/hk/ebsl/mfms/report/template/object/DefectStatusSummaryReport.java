package hk.ebsl.mfms.report.template.object;

public class DefectStatusSummaryReport {

	private String FieldStatus = "";
	private String FieldFailureClass= "";
	private String FieldProblemCode= "";
	private String FieldLocation= "";
	private String FieldPriority= "";
	private String FieldNumberOfWorkOrder= "";
	private String FieldStatusSeq = "";

	public DefectStatusSummaryReport() {

	}
	
	
	public DefectStatusSummaryReport(String fieldStatus) {
		super();
		FieldStatus = fieldStatus;
	}

	public String getFieldStatus() {
		return FieldStatus;
	}

	public void setFieldStatus(String fieldStatus) {
		FieldStatus = fieldStatus;
	}

	public String getFieldFailureClass() {
		return FieldFailureClass;
	}

	public void setFieldFailureClass(String fieldFailureClass) {
		FieldFailureClass = fieldFailureClass;
	}

	public String getFieldProblemCode() {
		return FieldProblemCode;
	}

	public void setFieldProblemCode(String fieldProblemCode) {
		FieldProblemCode = fieldProblemCode;
	}

	public String getFieldLocation() {
		return FieldLocation;
	}

	public void setFieldLocation(String fieldLocation) {
		FieldLocation = fieldLocation;
	}

	public String getFieldPriority() {
		return FieldPriority;
	}

	public void setFieldPriority(String fieldPriority) {
		FieldPriority = fieldPriority;
	}

	public String getFieldNumberOfWorkOrder() {
		return FieldNumberOfWorkOrder;
	}

	public void setFieldNumberOfWorkOrder(String fieldNumberOfWorkOrder) {
		FieldNumberOfWorkOrder = fieldNumberOfWorkOrder;
	}


	public String getFieldStatusSeq() {
		return FieldStatusSeq;
	}


	public void setFieldStatusSeq(String fieldStatusSeq) {
		FieldStatusSeq = fieldStatusSeq;
	}

}
