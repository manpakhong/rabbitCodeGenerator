<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">

<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/moment.js"></script>
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>


<script>
	$(document).ready(function() {
		//$("#selectPatrolDate").load("ShowSelectReportDate.do");
		//$("#btn_resultReport").hide();
	});

	function getStaffReport() {

		window.open("GetPatrolStaffReport.do?exportType="
				+ $("input[name='exportType']:checked").val(), '_blank');
	}
	function resetReport() {
		$('input:radio[name="exportType"][value="pdf"]').click();
	}
</script>



<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><a href="PatrolManagement.do"><i
					class="icon-icon_patrol_management_b"></i> <spring:message code="menu.patrolMgt" /></a></li>
			<li><a href="PatrolReport.do"> <spring:message
						code="menu.patrolMgt.report" /></a></li>
			<li class="active"><spring:message
					code="menu.patrolMgt.report.staff" /></li>
		</ol>
	</section>
	<!-- Main content -->

	<section class="content">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="box">
						<div class="box-body">

							<div role="tabpanel" id="tab_patrolStaff">

								<div class="row">
									<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
										<label class="control-label form-control"
											style="border: none; white-space: nowrap;"><spring:message
												code="patrol.report.staff.type.label" /></label>
									</div>
									<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
										<div class="radio-inline">
											<label> <input type="radio" name="staffType"
												id="staffTypeRadio1" value="staff" checked="true"> <spring:message
													code="patrol.report.staff.type" />
											</label>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
										<label class="control-label form-control"
											style="border: none; white-space: nowrap;"><spring:message
												code="common.fileFormat" /></label>
									</div>
									<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
										<div class="radio-inline">
											<label> <input type="radio" name="exportType"
												id="blankRadio2" value="pdf" checked="true"> PDF
											</label>
										</div>
										<!-- 
										<div class="radio-inline">
											<label> <input type="radio" name="exportType"
												id="blankRadio1" value="xls">Excel
											</label>
										</div>
										 -->
									</div>
								</div>

								<!--  
									<div class="row">
										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
											<label class="control-label form-control"
												style="border: none; white-space: nowrap;">Patrol Staff </label>
										</div>
										<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
											<select></select>
										</div>
									</div>
									-->
							</div>
							<div class="box-footer">
							<button class="btn btn-primary" id="btn_resultReport_reset"
								onclick="resetReport()" style="float: right">
								<spring:message code="button.reset" />
							</button>
							<button class="btn btn-primary" onclick="getStaffReport()"
								id="btn_staffReport" style="float: right">
								<spring:message code="button.submit" />
							</button>

						</div>
						</div>
						
					</div>

				</div>
			</div>
	<form id="hidden_form">
	<input id="hidden_submit" type="submit" style="visibility: hidden;">
</form>
</div>
</section>

</div>

<script>
	menu_toggle("sub_pm");
	menu_toggle("sub_pm_report");
	menu_select("patrolStaff");
</script>

