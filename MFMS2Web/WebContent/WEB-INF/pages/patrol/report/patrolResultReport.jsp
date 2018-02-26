<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">


<script>
	$(document).ready(function() {
		$("#selectPatrolDate").load("ShowSelectReportDate.do");
		//$("#btn_resultReport").hide();
		$("#errorMsg").hide();
		$("#patrolStaffSelected").val(-1);
		$("#route").val(-1);
	});

	function getResultReport() {

		$("#errorMsg").hide();
		$(".has-error").removeClass("has-error");

		var startDate = document.getElementById("startDatePicker").value;
		var endDate = document.getElementById("endDatePicker").value;

		var hasError = false;
		var errorStr = "";
		if (startDate == "") {
			$("#startDatePicker").parent().addClass("has-error");
			hasError = true;
			if (errorStr == "") {
				errorStr += '<spring:message code="patrol.schedule.date.start.empty"/>';

			} else {
				errorStr += "<br/>"
						+ '<spring:message code="patrol.schedule.date.start.empty"/>';
			}
		}
		if (endDate == "") {
			$("#endDatePicker").parent().addClass("has-error");
			hasError = true;
			if (errorStr == "") {
				errorStr += '<spring:message code="patrol.schedule.date.end.empty"/>';
			} else {
				errorStr += "<br/>"
						+ '<spring:message code="patrol.schedule.date.end.empty"/>';
			}
		}

		if (hasError) {

			$("#errorMsgStr").html(errorStr);
			$("#errorMsg").show();
			return;
		}

		//var form = document.createElement("form");
		var form = document.getElementById("hidden_form");
		form.setAttribute("method", "post");
		form.setAttribute("target", "_blank");
		form.setAttribute("action", "GetPatrolResultReport.do");

		var type = $('input:radio[name="resultType"]:checked').val();
		var exportType = $('input:radio[name="exportType"]:checked').val();

		var route = document.getElementById("route").value;
		var patrolStaff = document.getElementById("patrolStaffSelected").value;
		
		var json = JSON.stringify({
			type : type,
			startDate : startDate,
			endDate : endDate,
			route : route,
			patrolStaff : patrolStaff,
			exportType : exportType
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
		$(hiddenField).remove();

	}

	function resetReport() {
		$("#errorMsg").hide();
		$(".has-error").removeClass("has-error");

		$('input:radio[name="resultType"][value="All"]').click();
		$('input:radio[name="exportType"][value="pdf"]').click();
		$("#endDatePicker").val("");
		$("#startDatePicker").val("");
		$("#patrolStaffSelected").val(-1);
		$("#route").val(-1);
		
		resetDate();

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
					code="menu.patrolMgt.report.result" /></li>
		</ol>
	</section>
	<!-- Main content -->

	<section class="content">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">


					<div class="box">

						<div id="errorMsg">
							<!-- load view_errorMsg.jsp -->

							<div class="form-group alert alert-danger" id="errorMsgStr"></div>

						</div>

						<div class="box-body">

							<!-- Tab panes -->


							<div class="row">
								<div class="col-lg-12">
									<div class="row">

										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
											<label class="control-label form-control"
												style="border: none; white-space: nowrap;"><spring:message
													code="patrol.report.type" /></label>
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


										<div id="selectPatrolDate">

											<!-- load view_selectPatrolDate.jsp -->
										</div>


									</div>
									<div class="row">

										<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
											<label class="control-label form-control"
												style="border: none; white-space: nowrap;"><spring:message
													code="patrol.report.route" /></label>
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
												style="border: none; white-space: nowrap;"><spring:message
													code="patrol.report.staff" /></label>
										</div>
										<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
											<form:select path="patrolStaff" multiple="false"
												cssClass="form-control" id="patrolStaffSelected" onchange="">
												<form:options items="${patrolStaff}" />
											</form:select>
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
													id="blankRadio2" value="pdf" checked="true">PDF
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
								</div>

							</div>
							<div class="box-footer">
							<button class="btn btn-primary" id="btn_resultReport_reset"
								onclick="resetReport()" style="float: right">
								<spring:message code="button.reset" />
							</button>

							<button class="btn btn-primary" id="btn_resultReport"
								onclick="getResultReport()" style="float: right">
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
	menu_select("patrolResult");
</script>

