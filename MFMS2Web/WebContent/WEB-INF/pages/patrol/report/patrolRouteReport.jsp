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
		//$("#btn_routeReport").hide();
		$("#errorMsg").hide();
		$("#patrolStaffSelected").val(-1);
		$("#route").val(-1);
	});

	function getRouteReport() {

		$("#errorMsg").hide();
		$(".has-error").removeClass("has-error");

		var hasError = false;
		var errorStr = "";

		if (hasError) {

			$("#errorMsgStr").html(errorStr);
			$("#errorMsg").show();
			return;
		}

		//var form = document.createElement("form");
		var form = document.getElementById("hidden_form");
		form.setAttribute("method", "post");
		form.setAttribute("target", "_blank");
		form.setAttribute("action", "GetPatrolRouteReport.do");

		var exportType = $('input:radio[name="exportType"]:checked').val();

		var route = document.getElementById("route").value;
		
		var json = JSON.stringify({
			route : route,
			exportType : exportType
		});

		var hiddenField = document.createElement("input");
		hiddenField.setAttribute("type", "hidden");
		hiddenField.setAttribute("name", "json");
		hiddenField.setAttribute("value", json);
		form.appendChild(hiddenField);

		//form.submit();
		$("#hidden_submit").click();

		//window.open("GetPatrolRouteReport.do",'_blank');
		/*
		$.ajax({
			method : "POST",
			dataType: 'html',
			url : "GetPatrolRouteReport.do",
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

		$('input:radio[name="exportType"][value="pdf"]').click();
		$("#route").val(-1);

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
					code="menu.patrolMgt.report.route" /></li>
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
							<button class="btn btn-primary" id="btn_routeReport_reset"
								onclick="resetReport()" style="float: right">
								<spring:message code="button.reset" />
							</button>

							<button class="btn btn-primary" id="btn_routeReport"
								onclick="getRouteReport()" style="float: right">
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
	menu_select("patrolRoute");
</script>

