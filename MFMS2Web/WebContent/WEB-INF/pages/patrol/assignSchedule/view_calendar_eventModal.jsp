<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>



<script type="text/javascript">
	eventEditStaffJson = '${eventEditStaffJson}';
	
	var currentEventEditStaffJson = '${currentEventEditStaffJson}';
	var parentEventEditStaffJson = '${parentEventEditStaffJson}';
	var edit_selectedStaffJson = currentEventEditStaffJson;
	var edit_parentSelectedStaffJson = parentEventEditStaffJson;
	var currentMode = "";

	$(document).ready(
			function() {
				$.ajax({
					method : "POST",
					url : "PatrolAssignSelectStaff.do",
					data : "edit=true&selectedStaffJson=" + currentEventEditStaffJson
							+ "&eventEditStaffJson=" + currentEventEditStaffJson
							+ "&extraId=",
					success : function(data) {
						$('#modal_account').html(data);
					}
				});
				
				$.ajax({
					method : "POST",
					url : "PatrolAssignSelectStaff.do",
					data : "edit=true&selectedStaffJson=" + parentEventEditStaffJson
							+ "&eventEditStaffJson=" + parentEventEditStaffJson
							+ "&extraId=_series",
					success : function(data) {
						$('#modal_series_account').html(data);
					}
				});
				
				//$("#modal_account").load("PatrolAssignSelectStaff.do?edit=true");
				displayModalWeekDay();
				checkIsDeleted();
				actionChange("series");
				
				var frequency = $("#modal_frequency").val();
				if (frequency === "${onceValue}") {

					$("#modal_endDate").prop('disabled', true);

				} else {
					$("#modal_endDate").prop('disabled', false);

				}
				
			
			});

	function actionChange(action) {

		currentMode = action;
		
		if (action === "current") {
			$("#btn_deleteSeries").hide();
			$("#btn_editSeries").hide();
			$("#row_seriesStart").hide();
			$("#row_seriesEnd").hide();
			$("#row_frequency").hide();
			$("#modal_series_account").hide();
			$("#row_seriesTime").hide();

			$("#btn_skipCurrent").show();
			$("#btn_editCurrent").show();
			$("#row_currentStart").show();
			$("#row_currentEnd").show();
			$("#modal_account").show();
			$("#row_currentTime").show();
			
			//selectedStaffJson= currentEventEditStaffJson;
			//eventEditStaffJson = currentEventEditStaffJson;
			checkIsDeleted();

		} else {
			if (action === "series") {
				$("#btn_deleteSeries").show();
				$("#btn_editSeries").show();
				$("#row_seriesStart").show();
				$("#row_seriesEnd").show();
				$("#row_frequency").show();
				$("#modal_series_account").show();
				$("#row_seriesTime").show();

				$("#btn_skipCurrent").hide();
				$("#btn_editCurrent").hide();
				$("#row_currentStart").hide();
				$("#row_currentEnd").hide();
				$("#modal_account").hide();
				$("#row_currentTime").hide();
				
				//selectedStaffJson= parentEventEditStaffJson;
				//eventEditStaffJson = parentEventEditStaffJson;
				checkIsDeleted();

			}
		}
	}

	$("#modal_startDate").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 2,
		startDate : '+0d',
		autoclose : true,
	}).on("show", function() {
		$(".table-condensed th").attr("style", "visibility : visible");

	}).on("changeDate", function(ev) {

		var fullDate = $("#modal_startDate").val()

		$("#modal_endDate").datetimepicker("setStartDate", fullDate);

		
		var startDateMoment = moment($("#modal_startDate").val());
		var endDateMoment = moment($("#modal_endDate").val());
		
		if(startDateMoment.isAfter(endDateMoment)){
			$("#modal_endDate").val(startDateMoment.format("YYYY-MM-DD"));
		}
		
		//if ($("modal_endDate").val() === "") {
		//	$("modal_endDate").val(fullDate);
		//}
	});

	$("#modal_endDate").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 2,
		startDate : '+0d',
		autoclose : true,
		todayBtn : false,
	}).on("show", function(ev) {
		$(".table-condensed th").attr("style", "visibility : visible");
	}).on("changeDate", function(ev) {

		var fullDate = $("#modal_startDate").val()

		$("#modal_startDate").datetimepicker("setEndDate", fullDate);
	
		var startDateMoment = moment($("#modal_startDate").val());
		var endDateMoment = moment($("#modal_endDate").val());
		
		if(startDateMoment.isAfter(endDateMoment)){
			$("#modal_startDate").val(endDateMoment.format("YYYY-MM-DD"));
		}
		
	});

	$("#modal_currentStartDate").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 2,
		startDate : '+0d',
		autoclose : true,
	}).on("show", function() {
		$(".table-condensed th").attr("style", "visibility : visible");

	}).on("changeDate", function(ev) {

		var fullDate = $("#modal_currentStartDate").val()

		$("#modal_currentEndDate").datetimepicker("setStartDate", fullDate);

		var startDateMoment = moment($("#modal_currentStartDate").val());
		var endDateMoment = moment($("#modal_currentEndDate").val());
		
		if(startDateMoment.isAfter(endDateMoment)){
			$("#modal_currentEndDate").val(startDateMoment.format("YYYY-MM-DD"));
		}
	});

	$("#modal_currentEndDate").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 2,
		startDate : '+0d',
		autoclose : true,
		todayBtn : false,
	}).on("show", function(ev) {
		$(".table-condensed th").attr("style", "visibility : visible");
	}).on("changeDate", function(ev) {

		var fullDate = $("#modal_startDate").val()

		$("#modal_currentStartDate").datetimepicker("setEndDate", fullDate);
		
		var startDateMoment = moment($("#modal_currentStartDate").val());
		var endDateMoment = moment($("#modal_currentEndDate").val());
		
		if(startDateMoment.isAfter(endDateMoment)){
			$("#modal_currentStartDate").val(endDateMoment.format("YYYY-MM-DD"));
		}
	});

	$("#modal_time").datetimepicker({
		format : 'hh:ii',
		startView : 1,
		maxView : 1,
		autoclose : true,
		keyboardNavigation : false
	}).on("show", function() {
		$(".table-condensed th").attr("style", "visibility : hidden");
	});
	
	$("#modal_current_time").datetimepicker({
		format : 'hh:ii',
		startView : 1,
		maxView : 1,
		autoclose : true,
		keyboardNavigation : false
	}).on("show", function() {
		$(".table-condensed th").attr("style", "visibility : hidden");
	});


	function displayModalWeekDay() {
		var frequency = $("#modal_frequency").val();
		if (frequency === "${weeklyValue}") {

			$("#modal_weekDay").show();

		} else {
			$("#modal_weekDay").hide();

		}
		
		if (frequency === "${onceValue}") {

			$("#modal_endDate").prop('disabled', true);

		} else {
			$("#modal_endDate").prop('disabled', false);

		}
		
	}

	function editCurrentEvent() {

		/*
		var startDateMoment = moment($("#modal_currentStartDate").val());
		var endDateMoment = moment($("#modal_currentEndDate").val());
		

		console.log("Start : "+startDateMoment);
		console.log("end : "+endDateMoment);
		
		if(startDateMoment.isAfter(endDateMoment)){
			$("#submitStatus").load();
			return;
		}*/
		//showCustomLoading();
		var staff = document.getElementById("staff_modal");

		var accountData = "";
		for (i = 0; i < staff.length; i++) {
			accountData += "&account=" + staff.options[i].value;
		}

		var jsonStr = $('#patrolAssignEditForm').serialize();

		$.ajax({
			method : "POST",
			url : "EditCurrentEvent.do",
			data : jsonStr+accountData,
			success : function(data) {
				//$("#submitStatus").html(data);
				$("#eventModal").modal("hide");
			
				setTimeout(function(){
					$("#successMsg").html(data);
					$("#successMsg").show();
				},500);
				//hideCustomLoading();
			}
		});
	}

	function skipCurrentEvent() {

		var jsonStr = $('#patrolAssignEditForm').serialize();

		$.ajax({
			method : "POST",
			url : "DeleteCurrentEvent.do",
			data : jsonStr,
			success : function(data) {
				console.log("check plx : " + data);
				$('#eventModal').modal('hide');

				$("#successMsg").load(
						'ShowSuccessMsg.do?msg='+data
								+ '&extraMsg=');
				$("#successMsg").show();
				//setTimeout(function(){ $('#view_calendar').load("ShowCalendar.do"); }, 800);
				
				//time out
				 //slideUpThePrompt();
			},
			error : function(data) {

			}
		});

	}
	function editSeriesEvent() {

		//showCustomLoading();
		var staff = document.getElementById("staff_modal_series");

		var accountData = "";
		for (i = 0; i < staff.length; i++) {
			accountData += "&account=" + staff.options[i].value;
		}

		var jsonStr = $('#patrolAssignEditForm').serialize();

		console.log(jsonStr);

		$.ajax({
			method : "POST",
			url : "EditSeriesEvent.do",
			data : jsonStr + accountData,
			success : function(data) {
				//$("#submitStatus").html(data);
				$("#eventModal").modal("hide");
				setTimeout(function(){
					$("#successMsg").html(data);
					$("#successMsg").show();
				},500);
				//hideCustomLoading();
			}
			
		});
	}
	function deleteSeriesEvent() {

		var jsonStr = $('#patrolAssignEditForm').serialize();

		$.ajax({
			method : "POST",
			url : "DeleteSeriesEvent.do",
			data : jsonStr,
			success : function(data) {
				console.log("check plx : " + data);
				
				$('#eventModal').modal('hide');

				
				$("#successMsg").load(
						'ShowSuccessMsg.do?msg='+data
								+ '&extraMsg=');
				$("#successMsg").show();
				//setTimeout(function(){ $('#view_calendar').load("ShowCalendar.do"); }, 800);
				//time out
				//slideUpThePrompt();
			},
			error : function(data) {

			}
		});

	}
	
	function checkIsDeleted(){
		var isDeleted = $("#isEventDeleted").val();
		if(isDeleted ==="Y"){
			//$("#btn_editSeries").hide();
			//$("#btn_deleteSeries").hide();
			$("#btn_editCurrent").hide();
			$("#btn_skipCurrent").hide();
			
		}
		
		var isParentDeleted = $("#isParentEventDeleted").val();
		if(isParentDeleted ==="Y"){
			$("#btn_editSeries").hide();
			$("#btn_deleteSeries").hide();
			$("#btn_editCurrent").hide();
			$("#btn_skipCurrent").hide();
			
		}
		
	}
	
	function clearEndDate(){
		$("#modal_endDate").val("");
	}
	
</script>

<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">
				<spring:message code="patrol.schedule.modify.title" />
			</h4>
		</div>
		<div class="modal-body">
			<div class="row" id="submitStatus"></div>
			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="" style="width: 50%;"><a
					href="#eventTab" aria-controls="current" role="tab"
					data-toggle="tab" onclick="actionChange('current')"><spring:message
							code="patrol.schedule.modify.current" /></a></li>
				<li role="presentation" class="active" style="width: 50%;"><a
					href="#eventTab" aria-controls="series" role="tab"
					data-toggle="tab" onclick="actionChange('series')"><spring:message
							code="patrol.schedule.modify.series" /></a></li>
			</ul>

			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="eventTab">
					<form:form id="patrolAssignEditForm" cssClass="form-horizontal"
						commandName="patrolAssignEditForm" method="post" action="">
						<form:hidden path="scheduleKey" />
						<form:hidden path="parentScheduleKey" />
						<form:hidden path="routeDefKey" id="edit_routeDefKey" />
						<form:hidden path="eventEditStaffJson"
							id="edit_eventEditStaffJson" value='${currentEventEditStaffJson}'/>
						<form:hidden path="parentEventEditStaffJson"
							id="edit_parentEventEditStaffJson" value='${parentEventEditStaffJson}'/>
						<input type="hidden" id="edit_selectedStaffJson" value='${currentEventEditStaffJson}'/>
						<input type="hidden" id="edit_parentSelectedStaffJson" value='${parentEventEditStaffJson}' />
						<form:hidden path="skippedStartDate" />
						<form:hidden path="skippedEndDate" />
						<form:hidden path="isDeleted" id="isEventDeleted"/>
						<form:hidden path="parentIsDeleted" id="isParentEventDeleted"/>
						

						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.route.name" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:input cssClass="form-control patrol_input"
									path="routeName[0]" id="modal_routeName" readonly="true" />
							</div>
						</div>
						
							<div class="row" id="modal_series_account">						
						
							<!-- load view_assign_selectDt -->
							<!-- <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;">Account Name
									</label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<input class="form-control patrol_input" id="view_accountName"
									type="text" value="" />
							</div> -->
						</div>
						
						
						<div class="row" id="modal_account">						
						
							<!-- load view_assign_selectDt -->
							<!-- <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;">Account Name
									</label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<input class="form-control patrol_input" id="view_accountName"
									type="text" value="" />
							</div> -->
						</div>
						
						
						
						<div class="row" id="row_seriesStart" style="display: none;">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.date.start" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:input cssClass="form-control dateTimePicker"
									path="parentStart" id="modal_startDate" readonly="true" />

							</div>
						</div>
						<div class="row" id="row_currentStart">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.date.start" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:input cssClass="form-control dateTimePicker"
									path="startDate" id="modal_currentStartDate" readonly="true" />

							</div>
						</div>
						<div class="row" id="row_seriesEnd" style="display: none;">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.date.end" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
									<form:input cssClass="form-control dateTimePicker"
										path="parentEnd" id="modal_endDate" readonly="true" />
							</div>
						</div>
						<div class="row" id="row_currentEnd">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.date.end" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:input cssClass="form-control dateTimePicker"
									path="endDate" id="modal_currentEndDate" readonly="true" />

							</div>
						</div>

						<div class="row" id="row_seriesTime">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.time" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:input cssClass="form-control dateTimePicker" path="parentTime"
									id="modal_time" readonly="true" />

							</div>
						</div>

						<div class="row" id="row_currentTime">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.time" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:input cssClass="form-control dateTimePicker" path="time"
									id="modal_current_time" readonly="true" />

							</div>
						</div>
						<div class="row" id="row_frequency" style="display: none;">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label form-control"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.frequency" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:select path="frequency" multiple="false"
									cssClass="form-control frequency" id="modal_frequency"
									onchange="displayModalWeekDay()">
									<form:options items="${frequency}" />
								</form:select>

								<div class="row" id="modal_weekDay" style="display: none;">
									<div class="col-lg-12">
										<c:forEach items="${weekDay}" var="weekDay">
											<div class="form-group col-lg-4 ">
												<div class="checkbox">
													<label> <form:checkbox path="weekDay"
															value="${weekDay.key}" />${weekDay.value}
													</label>
												</div>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default"
				style="margin-bottom: 0;" data-dismiss="modal">
				<spring:message code="button.close" />
			</button>
			<button type="button" class="btn btn-danger"
				style="margin-bottom: 0;" id="btn_skipCurrent" style="display:none"
				onclick="skipCurrentEvent()">
				<spring:message code="button.skipCurrent" />
			</button>
			<button type="button" class="btn btn-primary"
				style="margin-bottom: 0;" id="btn_editCurrent"
				onclick="editCurrentEvent()">
				<spring:message code="button.editCurrent" />
			</button>
			<button type="button" class="btn btn-danger"
				style="margin-bottom: 0; display: none;" id="btn_deleteSeries"
				onclick="deleteSeriesEvent()">
				<spring:message code="button.deleteSeries" />
			</button>
			<button type="button" class="btn btn-primary"
				style="margin-bottom: 0; display: none;" id="btn_editSeries"
				onclick="editSeriesEvent()">
				<spring:message code="button.editSeries" />
			</button>
		</div>
	</div>
</div>