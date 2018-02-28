<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">

<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker.css">
<script src="import/datetimepicker/moment.js"></script>
<script src="import/datetimepicker/bootstrap-datetimepicker.js"></script>


<script>
	$(document).ready(function() {
		$("#selectPatrolDate").load("ShowSelectReportDate.do");
		$("#btn_resultReport").hide();
	});

	function getStaffReport() {
		window.open("GetPatrolStaffReport.do",'_blank');
	}
	function getResultReport() {

		//var form = document.createElement("form");
		var form = document.getElementById("hidden_form");
		form.setAttribute("method", "post");
		form.setAttribute("target", "_blank");
		form.setAttribute("action", "GetPatrolResultReport.do");

		var type = $('input:radio[name="resultType"]:checked').val()
		var startDate = document.getElementById("startDatePicker").value;
		var endDate = document.getElementById("endDatePicker").value;
		var route = document.getElementById("route").value;
		var patrolStaff = document.getElementById("patrolStaff").value;

		json = JSON.stringify({
			type : type,
			startDate : startDate,
			endDate : endDate,
			route : route,
			patrolStaff : patrolStaff
		});

		
		var hiddenField = document.createElement("input");
		hiddenField.setAttribute("type", "hidden");
		hiddenField.setAttribute("name", "json");
		hiddenField.setAttribute("value", json);
		form.appendChild(hiddenField);
		
		//form.submit();
		$("#hidden_submit").click();
		
		//window.open("GetPatrolResultReport.do",'_blank');
		/*
		$.ajax({
			method : "POST",
			dataType: 'html',
			url : "GetPatrolResultReport.do",
			data : "",
			success : function(data) {
				console.log(data);
				window.open(data,'_blank');		
			}

		});
		 */

	}

	function changeAction(action) {
		if (action === "patrolStaff") {
			$("#btn_staffReport").show();
			$("#btn_resultReport").hide();
		}
		if (action === "patrolResult") {
			$("#btn_staffReport").hide();
			$("#btn_resultReport").show();
		}

	}
</script>



<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><a href="PatrolManagement.do"><i class="icon-icon_patrol_management_b"></i> <spring:message code="menu.patrolMgt"/></a></li>
			<li><a href="PatrolReport.do"> <spring:message code="menu.patrolMgt.report"/></a></li>
			<li class="active"> <spring:message code="menu.patrolMgt.report.staff"/></li>
		</ol>
	</section>
	<!-- Main content -->

	<section class="content">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="box">
						<div class="box-body">

							<!-- Nav tabs -->
							<ul class="nav nav-tabs" role="tablist">
								<li role="presentation" class="active" style="width: 50%;"><a
									href="#tab_patrolStaff" aria-controls="current" role="tab"
									data-toggle="tab" onclick="changeAction('patrolStaff')"><spring:message code="menu.patrolMgt.report.staff"/></a></li>
								<li role="presentation" style="width: 50%;"><a
									href="#tab_patrolResult" aria-controls="series" role="tab"
									data-toggle="tab" onclick="changeAction('patrolResult')"><spring:message code="menu.patrolMgt.report.result"/></a></li>
							</ul>


							<!-- Tab panes -->
							<div class="tab-content">
								<div role="tabpanel" class="tab-pane active"
									id="tab_patrolStaff">

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

								<div role="tabpanel" class="tab-pane" id="tab_patrolResult">

									<div class="row">
										<div class="col-lg-12">
											<div class="row">

												<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
													<label class="control-label form-control"
														style="border: none; white-space: nowrap;"><spring:message code="patrol.report.type"/></label>
												</div>


												<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
													<c:forEach items="${reportType}" var="reportType"
														varStatus="status">
														<label class="radio-inline"> <input type="radio"
															name="resultType"
															<c:if test="${status.index eq 0}"> checked="checked"</c:if>
															value="${reportType.key}" />${reportType.value}
														</label>
													</c:forEach>
												</div>
											</div>

											<div class="row">

												<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
													<label class="control-label form-control"
														style="border: none; white-space: nowrap;"><spring:message code="patrol.report.date"/>:
													</label>
												</div>
												<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8"
													id="selectPatrolDate">

													<!-- load view_selectPatrolDate.jsp -->
												</div>


											</div>
											<div class="row">

												<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
													<label class="control-label form-control"
														style="border: none; white-space: nowrap;"><spring:message code="patrol.report.route"/>:
													</label>
												</div>
												<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">

													<form:select path="route" multiple="false"
														cssClass="form-control" id="route" onchange="">
														<form:options items="${route}" />
													</form:select>
												</div>

											</div>
											<div class="row">

												<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
													<label class="control-label form-control"
														style="border: none; white-space: nowrap;"><spring:message code="patrol.report.staff"/></label>
												</div>
												<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
													<form:select path="patrolStaff" multiple="false"
														cssClass="form-control" id="patrolStaff" onchange="">
														<form:options items="${patrolStaff}" />
													</form:select>
												</div>

											</div>
										</div>

									</div>

								</div>
							</div>
						</div>
						<div class="box-footer">
							<button class="btn btn-primary" id="btn_resultReport"
								onclick="getResultReport()"><spring:message code="button.submit"/></button>
							<button class="btn btn-primary" onclick="getStaffReport()"
								id="btn_staffReport"><spring:message code="button.submit"/></button>
						</div>
					</div>
				</div>
			</div>
		
	</section>

<form id="hidden_form">
<input id="hidden_submit" type="submit" style="visibility:hidden;">
</form>

</div>

<script>  
  menu_toggle("sub_pm");
  menu_toggle("sub_pm_report");
  menu_select("patrolStaff");
</script>

