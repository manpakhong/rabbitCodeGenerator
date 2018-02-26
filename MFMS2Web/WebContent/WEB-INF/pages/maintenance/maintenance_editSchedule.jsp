<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<jsp:include page="../common/left.jsp" />
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">

<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/moment.js"></script>
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>

<link rel='stylesheet' href='import/fullcalendar/fullcalendar.css' />
<script src='import/fullcalendar/fullcalendar.js'></script>
<link rel="stylesheet" type="text/css"
	href="import/duallistbox/bootstrap-duallistbox.css">
<script src="import/duallistbox/jquery.bootstrap-duallistbox.js"></script>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchMaintenanceSchedule')"
	var="searchMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createMaintenanceSchedule')"
	var="createMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyMaintenanceSchedule')"
	var="modifyMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeMaintenanceSchedule')"
	var="removeMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.maintenanceSchedule')"
	var="maintenanecPrivilege" />

<style>
.modal {
	text-align: center;
	padding: 0 !important;
}

.modal:before {
	content: '';
	display: inline-block;
	height: 100%;
	vertical-align: middle;
	margin-right: -4px;
}

.modal-dialog {
	display: inline-block;
	text-align: left;
	vertical-align: middle;
}
</style>

<c:choose>
	<c:when test="${defectScheduleForm.isSeries}">
		<c:set var="isSeries" value="true" />
		<c:set var="label" value="button.editSeries" />
		<c:set var="url" value="DoModifySeriesSchedule.do" />
		<c:set var="resetUrl" value="ModifySeriesSchedule.do" />
		<c:set var="deleteButton" value="button.deleteSeries" />
	</c:when>
	<c:otherwise>
		<c:set var="isSeries" value="false" />
		<c:set var="label" value="button.editCurrent" />
		<c:set var="url" value="DoModifyCurrentSchedule.do" />
		<c:set var="resetUrl" value="ModifyCurrentSchedule.do" />
		<c:set var="deleteButton" value="button.skipCurrent" />
	</c:otherwise>
</c:choose>


<script type="text/javascript">


var selectedStaffJson = '';
var eventEditStaffJson = '';

$(document).ready(function(){
	
	eventEditStaffJson = '${eventEditStaffJson}';
	
	
	$.ajax({
		method : "POST",
		url : "ShowStaffSelected.do",
		data : "edit=false&selectedStaffJson=" + eventEditStaffJson
				+ "&eventEditStaffJson=" + eventEditStaffJson,
		success : function(data) {

			$('#view_selectedStaff').html(data);

		}
	});
	
	$.ajax({
		method : "POST",
		url : "PatrolAssignSelectStaff.do",
		data : {"edit":false, "selectedStaffJson" : eventEditStaffJson, 
				"eventEditStaffJson" : eventEditStaffJson , "extraId": ""},
		success : function(data) {
			$('#selectedStaff').html(data);
			
			$("#accountDiv").removeClass("col-lg-4 col-md-4 col-sm-4 col-xs-4").addClass("col-lg-2 col-md-2 col-sm-2 col-xs-2");
			$("#noIn").removeClass("col-lg-8 col-md-8 col-sm-8 col-xs-8").addClass("col-lg-10 col-md-10 col-sm-10 col-xs-10");
			$("#accountLabel").attr('class', 'control-label').append("*");
		}
	});
	
	$('#view_searchedStaff').load("ShowStaffSearched.do?privilege=${maintenanecPrivilege}");
	
	$('#view_accountName').on('input', function(e) {
		submitStaffSearch();
	});
	
});


function refreshAccount() {
		console.log("#eventModal");
		$.ajax({
			method : "POST",
			url : "PatrolAssignSelectStaff.do",
			data : {"edit":false, "selectedStaffJson" : selectedStaffJson,
					 "eventEditStaffJson" : eventEditStaffJson, "extraId":""},
			success : function(data) {
				
				$('#selectedStaff').html(data);
				
				$("#staffSelected").val(selectedStaffJson);
				
				$("#accountDiv").removeClass("col-lg-4 col-md-4 col-sm-4 col-xs-4").addClass("col-lg-2 col-md-2 col-sm-2 col-xs-2");
				$("#noIn").removeClass("col-lg-8 col-md-8 col-sm-8 col-xs-8").addClass("col-lg-10 col-md-10 col-sm-10 col-xs-10");
				$("#accountLabel").attr('class', 'control-label').append("*");
			}
		});

	
}

function submitStaffSearch() {

	var roleKey = $('#view_accountRole').val();
	var accountName = document.getElementById('view_accountName').value;

	var privilege = '${maintenanecPrivilege}';

	$.ajax({
		method : 'POST',
		data : "accountName=" + accountName + "&accountRoleKey=" + roleKey
				+ "&privilege=" + privilege,
		url : "SubmitStaffSearch.do",
		success : function(data) {
			$('#view_searchedStaff').html(data);
		}
	})

}

function deleteCurrentSchedule(){
	
	console.log("deleteCurrentSchedule");
	$("#defectScheduleForm").attr("action", "DoDeleteSchedule.do");
	$("#defectScheduleForm").submit();
	
}

function submitClearedStaffSearch(){
	
}

function slideUpThePrompt(){	
}

</script>

<div class="content-wrapper">
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_maintenance_schedule_b"></i>
				<spring:message code="${label}" /></li>
		</ol>
	</section>
	<section class="content">
		<div class="row">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="box">
					<form:form id="defectScheduleForm" cssClass="form-horizontal"
						commandName="defectScheduleForm" method="post" action="${url}">
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<form:hidden path="staffSelected" id="staffSelected" />

						<form:hidden path="parentStart" />
						<form:hidden path="parentEnd" />
						<div class="box-body">
							<div class="modal-header">
								<h3 class="modal-title">
									<spring:message code="${label}" />
								</h3>
							</div>
							<div class="box-body">
								<div
									class="<spring:bind path="locationKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="defect.location" /></label>
									<div class="col-sm-10">
										<div class="panel panel-default">
											<div class="panel-heading">
												<label class="control-label"> <spring:message
														code="defect.location.select" />
												</label> <label id="locationName" class="control-label"><c:out
														value="${selectedLocation}" /></label>
											</div>
<script>
	function onLocationChange(inputKey, inputCode, inputName)
	{
		event.stopPropagation();
		var locationName = ' : ' + inputCode + ' - ' + inputName;

		$('#locationName').text(locationName);

		$
				.getJSON(
						'getEquipmentByLocation.do',
						{
							locationKey : inputKey,
							ajax : 'true'
						},
						function(data) {
							var html = '<option value=""><spring:message
								code="common.pleaseSelect" /></option>';
							var len = data.length;
							for (var i = 0; i < len; i++) {
								html += '<option value="' + data[i].key + '">'
										+ data[i].code + ' - ' + data[i].name + '</option>';
							}
							html += '</option>';
							$('#equipment').html(html);

						});

	}
</script>
											<ul class="list-group locationtree" cssClass="form-control"
												id="radioButtonLocationtree">
												<template:radioButtonLocationTree
													node="${defectScheduleForm.availableLocationTree}" />
											</ul>
										</div>
									</div>
								</div>

								<div class="col-xs-12">
									<div
										class="row form-group <spring:bind path="selectedAccountKeyList">
											<c:if test="${status.error}">has-error</c:if></spring:bind>"
										id="selectedStaff">
										<!-- load assignSchedule/view_assign_selectStaff.jsp -->
									</div>
								</div>
								<div
									class="form-group <spring:bind path="description"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="defect.description" />*</label>
									<div class="col-sm-10">
										<spring:message code="defect.description"
											var="defectDescription" />
										<form:textarea path="description" cssClass="form-control"
											placeholder="${defectDescription}" />
									</div>
								</div>
								<div
									class="<spring:bind path="equipmentKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="defect.equipment" /></label>
									<div class="col-sm-10">
										<form:select path="equipmentKey" cssClass="form-control"
											id="equipment" items="${equipmentList}">
										</form:select>
									</div>
								</div>
								<label class="col-sm-2 control-label"><spring:message
										code="defect.tools" /></label>
								<div class="col-sm-10">
									<form:select path="toolKey" cssClass="form-control"
										items="${toolList}">
									</form:select>
								</div>

								<div
									class="form-group <spring:bind path="frequency"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="patrol.schedule.frequency" />*</label>
									<div class="col-sm-10" style="padding: 8px 21px;">
										<form:radiobutton path="frequency" value="101" id="once"
											onclick="return false" />
										<spring:message code="schedule.frequency.Once" />
										<c:choose>
											<c:when test="${isSeries}">
												<form:radiobutton path="frequency" value="102"
													onclick="return false" />
												<spring:message code="schedule.frequency.Daily" />
												<form:radiobutton path="frequency" value="103" id="weekly"
													onclick="return false" />
												<spring:message code="schedule.frequency.Weekly" />
												<form:radiobutton path="frequency" value="104"
													onclick="return false" />
												<spring:message code="schedule.frequency.Monthly" />
												<form:radiobutton path="frequency" value="105"
													onclick="return false" />
												<spring:message code="schedule.frequency.Annually" />
											</c:when>
										</c:choose>
									</div>
									<div id="weekDay"
										class="form-group <spring:bind path="weekDays"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-2 control-label"><spring:message
												code="schedule.frequency.Weekly" />*</label>
										<div class="col-sm-10" style="padding: 1px 21px;">
											<form:checkbox path="weekDays" value="1"
												onclick="return false" />
											<spring:message code="schedule.frequency.Monday" />
											<form:checkbox path="weekDays" value="2"
												onclick="return false" />
											<spring:message code="schedule.frequency.Tuesday" />
											<form:checkbox path="weekDays" value="3"
												onclick="return false" />
											<spring:message code="schedule.frequency.Wednesday" />
											<form:checkbox path="weekDays" value="4"
												onclick="return false" />
											<spring:message code="schedule.frequency.Thursday" />
											<form:checkbox path="weekDays" value="5"
												onclick="return false" />
											<spring:message code="schedule.frequency.Friday" />
											<form:checkbox path="weekDays" value="6"
												onclick="return false" />
											<spring:message code="schedule.frequency.Saturday" />
											<form:checkbox path="weekDays" value="0"
												onclick="return false" />
											<spring:message code="schedule.frequency.Sunday" />
										</div>
										<script>
										
										</script>
									</div>
								</div>

								<div
									class="form-group <spring:bind path="startDate"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="patrol.schedule.date.start" />*</label>
									<div class="col-sm-10"
										<spring:message code="patrol.schedule.date.start" var="from"/>>
										<form:input id="startDate" path="startDate"
											cssClass="form-control" placeholder="${from}" readonly="true"
											style="background-color: #fff;" />
									</div>
								</div>
								<div id='endDateDiv'
									class="form-group <spring:bind path="endDate"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="patrol.schedule.date.end" /></label>
									<div class="col-sm-10"
										<spring:message code="patrol.schedule.date.end" var="to"/>>
										<form:input id="endDate" path="endDate"
											cssClass="form-control" placeholder="${to}" readonly="true"
											style="background-color: #fff;" />
									</div>
								</div>
								
							<div class="form-group <spring:bind path="startTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label" style="padding-right: 0px; padding-left: 0px; left: 15px;">
								<spring:message code="Task Start Time" />*</label>
								<div class="col-sm-10" <spring:message code="Task Start Time" var="timeFrom"/>>
									<form:input id="startTime" path="startTime" cssClass="form-control" placeholder="${timeFrom}" readonly="true"
										style="background-color: #fff;" data-date="1899-12-31"/>
								</div>
							</div>
							<div class="form-group <spring:bind path="endTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label" style="padding-right: 0px;">
								<spring:message code="Task End Time" /></label>
								<div class="col-sm-10" <spring:message code="Task End Time" var="timeTo"/>>
									<form:input id="endTime" path="endTime" cssClass="form-control" placeholder="${timeTo}" readonly="true"
										style="background-color: #fff;" data-date="1899-12-31"/>
								</div>
							</div>

								<div
									class="form-group <spring:bind path="time"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="patrol.schedule.time" /></label>
									<div class="col-sm-8"
										style="padding-left: 21px; padding-top: 6px;">
										<form:checkbox id="fullDay" path="isFullDay" value='true' />
										<spring:message code="schedule.fullDay" />
									</div>
									<!--  
									<div class="col-sm-offset-2 col-sm-10">
										<spring:message code="patrol.schedule.time" var="time" />
										<form:input id="time" path="time" cssClass="form-control"
											placeholder="${time}" readonly="true"
											style="background-color: #fff;" />
									</div>
									-->
								</div>


								<div
									class="form-group <spring:bind path="remarks"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<div
										class="<spring:bind path="remarks"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-2 control-label"><spring:message
												code="defect.remarks" /></label>
										<div class="col-sm-10">
											<spring:message code="defect.remarks" var="remarks" />
											<form:textarea path="remarks" cssClass="form-control"
												placeholder="${remarks}" />
										</div>
									</div>
								</div>
							</div>

							<div class="box-footer">

								<a onclick="showLoading()" class="btn btn-primary "
									href="DefectSchedule.do"><spring:message
										code="button.back.to.search" /></a>

								<c:if
									test="${currRole.grantedPrivilegeCodeList.contains(modifyMaintenanceSchedule)}">
									<button onclick="showLoading()" type="submit"
										style="float: right" class="btn btn-primary ">
										<spring:message code="button.submit" />
									</button>
								</c:if>


								<c:if
									test="${currRole.grantedPrivilegeCodeList.contains(removeMaintenanceSchedule)}">

									<c:choose>
										<c:when test="${isSeries}">
											<a href="#" style="float: right"
												data-name="<c:out value="${defectScheduleForm.description }"/>"
												data-href="<c:url value="DoDeleteSeries.do"><c:param name="key" value="${defectScheduleForm.key}"/></c:url>"
												data-toggle="modal" data-target="#confirmDeleteModal"
												class="btn btn-danger "><spring:message
													code="button.deleteSeries" /></a>
										</c:when>
										<c:otherwise>
										<!--  
											<a href="#" style="float: right"
												data-name="<c:out value="${defectScheduleForm.description }"/>"
												data-href="<c:url value="DoDeleteSchedule.do"><c:param name="key" value="${defectScheduleForm.key}"/><c:param name="start" value="${defectScheduleForm.parentStart}"/></c:url>"
												data-toggle="modal" data-target="#confirmDeleteModal"
												class="btn btn-danger "> <spring:message
													code="button.skipCurrent" /></a>
													-->
													
													<button type="button" style="float:right;" class="btn btn-danger" onclick="deleteCurrentSchedule()">
													<spring:message code="button.skipCurrent" /></button>
													
										</c:otherwise>
									</c:choose>

								</c:if>

								<!--  
								<a onclick="showLoading()" style="float: right"
									href="<c:url value="${resetUrl}"><c:param name="key" value="${defectScheduleForm.key}"/><c:param name="currentStart" value="${defectScheduleForm.parentStart}"/><c:param name="currentEnd" value="${defectScheduleForm.parentEnd}"/></c:url>"
									class="btn btn-primary "><spring:message
										code="button.reset" /></a>
-->
								<a onclick="showLoading()" style="float: right"
									href="javascript:window.location.href=window.location.href"
									class="btn btn-primary "><spring:message
										code="button.reset" /></a>

							</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
</div>


<!-- Modal selectStaffModal -->
<div id="eventModal"></div>
<div class="modal fade" id="selectStaffModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" id="myModalLabel">
					<spring:message code="patrol.schedule.select.account" />
				</h3>
			</div>
			<div class="modal-body">
				<!-- Body/Content for Modal -->
				<div class="row">
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<div class="row form-group">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.schedule.account.name" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<input class="form-control patrol_input" id="view_accountName"
									type="text" value="" />
							</div>
						</div>
					</div>
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<div class="row form-group">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<label class="control-label" style="border: none"><spring:message
										code="patrol.schedule.account.role" /></label>
							</div>
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
								<form:select path="accountRole" cssClass="form-control"
									multiple="false" id="view_accountRole"
									onclick="submitStaffSearch()">
									<form:options items="${accountRole}" />
								</form:select>
							</div>
						</div>
					</div>
				</div>
				<br>
				<div class="row">
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<div id="view_searchedStaff" class="panel panel-default">
							<!-- load assignSchedule/view_assign_account_searched.jsp -->
						</div>
					</div>
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<div id="view_selectedStaff" class="panel panel-default">
							<!-- load assignSchedule/view_assign_account_selected.jsp -->
						</div>
					</div>
				</div>
				<!--  End of Body/Content for Modal -->

				<div class="row">
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<button type="button" id="leftBtn"
							class="btn btn-default btn-block"
							style="margin-bottom: 0; margin-left: 0px;"
							onclick="addAllSelectedStaff();">
							<i class="fa fa-chevron-right"></i> <i
								class="fa fa-chevron-right"></i>
						</button>
					</div>
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<button type="button" id="rightBtn"
							class="btn btn-default btn-block"
							style="margin-bottom: 0; margin-left: 0px;"
							onclick="dropAllSelectedStaff();">
							<i class="fa fa-chevron-left"></i> <i class="fa fa-chevron-left"></i>
						</button>
					</div>
				</div>

			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="refreshAccount();">
					<spring:message code="button.ok" />
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="ACBack2Previous();">
					<spring:message code="button.cancel" />
				</button>
			</div>
		</div>
	</div>
</div>

<!-- Confirm Modal -->

<div id="confirmDeleteModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<spring:message code="schedule.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="schedule.confirm.delete.message" />
				</p>
				<br />
				<p id="confirmName"></p>
			</div>
			<div class="modal-footer">
				<a class="btn btn-danger btn-ok" id="confirmButton"
					onclick="showLoading()" style="margin-bottom: 0px"><spring:message
						code="${deleteButton}" /></a>
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0px">
					<spring:message code="button.close" />
				</button>
			</div>
		</div>
	</div>
</div>

<script>
	// for confirm delete
	$('#confirmDeleteModal').on(
			'show.bs.modal',
			function(e) {
				// put link on modal
				$(this).find('#confirmButton').attr('href',
						$(e.relatedTarget).data('href'));
				// put name on modal
				var str = $(e.relatedTarget).data('name');
				$('#confirmName').text(str);
			});
</script>


<script type="text/javascript">
	menu_select("maintenanceSchedule");
	
	//$("#endDateDiv").hide();	

	/*if (${defectScheduleForm.isFullDay}) {
		$("#time").hide();
	} else 
		$("#time").show();*/
	
	if (${defectScheduleForm.isWeekDay}) {
		$("#weekDay").show();
	} else 
		$("#weekDay").hide();
	
	if (${defectScheduleForm.frequency !=101}) {
		$("#endDateDiv").show();
	} else {
		//$("#endDateDiv").hide();
	}

	var userAccountBox1 = $("#userAccountBox1").bootstrapDualListbox({});

	$(function() {
		$('input[type="radio"]').click(function() {
//			if ($("#weekly").is(':checked')) {
//				$("#weekDay").show();
//			} else {
//				$('input:checkbox').removeAttr('checked');
//				$("#weekDay").hide();
//			}
			
			
//			if ($("#once").is(':checked')) {
				//$("#endDateDiv").hide();
//			} else {
				
//				if($("#startDate").val().length>0)
//					$("#endDate").datetimepicker("setStartDate",$("#startDate").val());
					
//					if($("#endDate").val().length>0)
//					$("#startDate").datetimepicker("setEndDate",$("#endDate").val());		
				
//				$("#endDateDiv").show();
//			}
			
		});

		$('#fullDay').change(function() {
			if ($("#fullDay").prop("checked")) {
				//$("#time").hide();
				//$("#time").val('');
				
				/*
//					var endDateM = moment($('#endDate').val(), "YYYY-MM-DD HH:mm");
//					endDateM.set('h',23);
//					endDateM.set('m',59);
//					$('#endDate').val(endDateM.format("YYYY-MM-DD HH:mm"));
//				}
				
//				if($('#startDate').val() != ""){										
//					var startDateM = moment($('#startDate').val(), "YYYY-MM-DD HH:mm");
//					startDateM.set('h',00);
//					startDateM.set('m',00);
//					$('#startDate').val(startDateM.format("YYYY-MM-DD HH:mm"));
				
//				}
				
//				$('#startDate').datetimepicker('remove');
//				$('#endDate').datetimepicker('remove');
				
//				setFullDayPicker();

				if($('#endTime').val() != ""){
					var endDateM = moment($('#endTime').val(), "HH:mm");
					endDateM.set('h',23);
					endDateM.set('m',59);
					console.log(endDateM);
					$('#endTime').val(endDateM.format("HH:mm"));
				}
				
				if($('#startTime').val() != ""){

					var startDateM = moment($('#startTime').val(), "HH:mm");
					startDateM.set('h',00);
					startDateM.set('m',00);
					$('#startTime').val(startDateM.format("HH:mm"));
				
				}*/
				
				//$('#startDate').datetimepicker('remove');
				//$('#endDate').datetimepicker('remove');
				
				//if($('#endTime').val() != ""){
					var endDateM = moment();
					endDateM.set('h',23);
					endDateM.set('m',59);
					console.log(endDateM);
					$('#endTime').val(endDateM.format("HH:mm"));
				//}
				
				//if($('#startTime').val() != ""){

					var startDateM = moment();
					startDateM.set('h',00);
					startDateM.set('m',00);
					$('#startTime').val(startDateM.format("HH:mm"));
				
				//}
				
				$('#startTime').datetimepicker('remove');
				$('#endTime').datetimepicker('remove');
				
				//setFullDayPicker();
			} else {
				//$("#time").show();			
//				$('#startDate').datetimepicker('remove');
				//$('#startDate').datetimepicker('remove');
				//$('#endDate').datetimepicker('remove');
				
				//setNotFullDayPicker();
				$('#endTime').val("");
				$('#startTime').val("");
				setDayPicker();
			}
		});
		
		$("#once, #daily, #weekly, #monthly, #yearly").change(function(){
			if ($("#once").prop("checked")) {
				if($("#startDate").val().length>0){
					$("#startDate").datetimepicker("update", $("#startDate").val());
					$("#endDate").datetimepicker("update", $("#startDate").val());
				}
				$("#startDate").datetimepicker("setEndDate", null);
				$("#endDate").datetimepicker("remove");
			} else{
			//re-initiate the datetimepicker
			$('#endDate').datetimepicker({
				format : 'yyyy-mm-dd',
				startDate : '+0d',
				minView : 2,
				clearBtn:  1,
				todayBtn : 1,
				autoclose : true,
				startView : 2
			});
			$("#endDate").datetimepicker("setStartDate", $("#startDate").val());
			$("#startDate").datetimepicker("setEndDate", $("#endDate").val());
			setDayPicker();
			}
		});
	});

	$(function() {
		setDayPicker();
		$("#endTime").datetimepicker("setStartDate", $("#startTime").val());
		$("#startTime").datetimepicker("setEndDate", $("#endTime").val());
		
		if ($("#fullDay").prop("checked")) {
			$('#startTime').datetimepicker('remove');
			$('#endTime').datetimepicker('remove');	
		}
		
		if ($("#once").prop("checked")) {
			if($("#startDate").val().length>0){
				$("#startDate").datetimepicker("update", $("#startDate").val());
				$("#endDate").datetimepicker("update", $("#startDate").val());
			}
			$("#startDate").datetimepicker("setEndDate", null);
			$("#endDate").datetimepicker("remove");
		}
//		setNotFullDayPicker();
		
/*
			$('#time').datetimepicker({
			format : 'hh:ii',
			startView : 1,
			maxView : 1,
			autoclose : true,
			keyboardNavigation : false
			
//		}).on("show", function(ev){
//			$(".table-condensed th").attr("style","visibility : hidden")	
		
//		}).on("changeDate", function(ev) {

//			$("#fullDay").prop('checked', false);
//			$(".table-condensed th").attr("style","visibility : hidden");
//		});
		
//		if($("#startDate").val().length>0)
//			$("#endDate").datetimepicker("setStartDate",$("#startDate").val());
			
//			if($("#endDate").val().length>0)
//			$("#startDate").datetimepicker("setEndDate",$("#endDate").val());		
		*/
	});
	
	
	function setFullDayPicker(){
		
		$('#startDate').datetimepicker({
			format : 'yyyy-mm-dd 00:00',
			minView : 2,
			clearBtn:  1,
			todayBtn : 1,
			startDate : '+0d',
			autoclose : true,
			startView : 2
			
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : visible")	
			
			
		}).on("changeDate", function(ev) {
			
			var endDateM = moment($('#startDate').val(), "YYYY-MM-DD HH:mm");
			$('#endDate').val(endDateM.format("YYYY-MM-DD 23:59"));
			
			var fullDate = $("#startDate").val();
			$("#endDate").datetimepicker("setStartDate", fullDate);
			
			$(this).datetimepicker('hide');

		});
		$('#endDate').datetimepicker({
			format : 'yyyy-mm-dd 23:59',
			startDate : '+0d',
			minView : 2,
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev) {
			var fullDate = $("#endDate").val();
				$("#startDate").datetimepicker("setEndDate", fullDate);
				
				$(this).datetimepicker('hide');
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : visible");		
		});
	}
	
	function setNotFullDayPicker(){
	
		$('#startDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii',
			minView : 0,
			clearBtn:  1,
			todayBtn : 1,
			startDate : '+0d',
			autoclose : true,
			startView : 2
			
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : visible")	
			
			
		}).on("changeDate", function(ev) {
			
			var endDateM = moment($('#startDate').val(), "YYYY-MM-DD HH:mm");
			endDateM.add(1, 'h');
			
			if($("#once").is(':checked')){
				$('#endDate').val(endDateM.format("YYYY-MM-DD HH:mm"));
			} 
			var fullDate = $("#startDate").val();
			$("#endDate").datetimepicker("setStartDate", fullDate);

			$(this).datetimepicker('hide');
		});
		$('#endDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii',
			startDate : '+0d',
			minView : 0,
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev) {
			var fullDate = $("#endDate").val();
				$("#startDate").datetimepicker("setEndDate", fullDate);
				
				$(this).datetimepicker('hide');
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : visible");		
		});
	}

	function setDayPicker(){
		$('#startDate').datetimepicker({
			format : 'yyyy-mm-dd',
			minView : 2,
			clearBtn:  1,
			todayBtn : 1,
			startDate : '+0d',
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev) {
			
			var endDateM = moment($('#startDate').val(), "YYYY-MM-DD");
			endDateM.add(1, 'h');
			var fullDate = $("#startDate").val();
			if($("#once").is(':checked')){
				
				console.log("chevked");
				$('#endDate').val(endDateM.format("YYYY-MM-DD"));
				
				if($("#startDate").val().length==0){
					$("#endDate").val('');
				}
				$("#endDate").datetimepicker("setStartDate", fullDate);
				$("#endDate").datetimepicker("remove");
				
			}else{
			
			if($("#startDate").val().length==0){
				$("#endDate").val('');
			}

			$("#endDate").datetimepicker("setStartDate", fullDate);
			
			}
			$(this).datetimepicker('hide');
			
		}).on("show", function(){
			$(".table-condensed th").attr("style","visibility : visible");
			$(".today").css("display","show");
		});
		
		$('#endDate').datetimepicker({
			format : 'yyyy-mm-dd',
			startDate : '+0d',
			minView : 2,
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev) {
			var fullDate = $("#endDate").val();
			$("#startDate").datetimepicker("setEndDate", fullDate);
			
			$(this).datetimepicker('hide');
			
		}).on("show", function(){
			$(".table-condensed th").attr("style","visibility : visible");
			$(".today").css("display","show");
		});
		
		$('#startTime').datetimepicker({
			format : 'hh:ii',
			startView : 1,
			maxView : 1,
			clearBtn:  1,
			autoclose:true,
			keyboardNavigation : false
		}).on("changeDate", function(ev) {
			
			var fullDate = $("#startTime").val();
			var endDateM = moment($('#startTime').val(), "HH:mm").clone();
			endDateM.add(1, 'h');
			
			if($("#once").is(':checked')){
				//$('#endTime').val(endDateM.format("HH:mm"));				
				$("#endTime").datetimepicker('update', endDateM.format("HH:mm"));				
				if($("#startTime").val().length==0){
					$("#endTime").val('');
				}
			} 
			
			
			$("#endTime").datetimepicker("setStartDate", fullDate);
			//$(this).datetimepicker('hide');
			
		}).on("show", function(){
			$(".table-condensed th").attr("style","visibility : hidden");
			$(".clear").attr("style","visibility : visible");
			$(".today").css("display","none");
		});

		$('#endTime').datetimepicker({
			format : 'hh:ii',
			startView : 1,
			maxView : 1,
			clearBtn:  1,
			autoclose:true,
			keyboardNavigation : false
		}).on("changeDate", function(ev) {
			var fullDate = $("#endTime").val();
			$("#startTime").datetimepicker("setEndDate", fullDate);
			
			//$(this).datetimepicker('hide');
			
		}).on("show", function(){
			$(".table-condensed th").attr("style","visibility : hidden");
			$(".clear").attr("style","visibility : visible");
			$(".today").css("display","none");
		});
	}
	
	$(document).ready(function() {
		$('.locationtree ul').hide();
		$('.locationtree li').hide();
		$('.locationtree li:first').show();
		/*
		$('.locationtree li i').on('click', function(e) {
			var $container = $(this.parentNode.parentNode).find('> ul');
			if ($container.is(":visible")) {
				$container.hide('fast');
				// change arrow
				$(this).removeClass('fa-angle-down').addClass('fa-angle-left');
			} else {
				$container.show('fast');
				// change arrow
				$(this).removeClass('fa-angle-left').addClass('fa-angle-down');
			}
			var children = $(this.parentNode.parentNode).find('> ul > li');
			for (var i = 0; i < children.length; i++) {
				var $el = $(children[i]);
				if ($el.is(":visible")) {
					$el.hide('fast');
				} else {
					$el.show('fast');
				}
			}
			e.stopPropagation();
		});
		*/
		$('.locationtree li').on('click', function(e) {
			console.log(e);
			var container = $(this).find('> ul');
			//console.log(container);
			if (container.is(":visible")) {
				container.hide('fast');
				// change arrow
				$(this).find('> div i').removeClass('fa-angle-down').addClass('fa-angle-left');
				//console.log($(this).find('> i'));
			} else {
				container.show('fast');
				// change arrow
				$(this).find('> div i').removeClass('fa-angle-left').addClass('fa-angle-down');
				//console.log($(this).find('> i'));
			}
			var children = $(this).find('> ul > li');
			for (var i = 0; i < children.length; i++) {
				var el = $(children[i]);
				if (el.is(":visible")) {
					el.hide('fast');
				} else {
					el.show('fast');
				}
			}
			e.stopPropagation();
		});
	});
	
	function timeToLong(dateString) {
		
		var dt = Date.parse('today');
		if( dateString != ""){
			dt = Date.parse(dateString);
			return dt.getTime();
		} else return 0;

	}
	
	function getTimeString(priority, dateString) {
		var dt = Date.parse('today');
		var ndt = Date.parse('today');
		if( dateString != ""){
			dt = Date.parse(dateString);
		}
		
		switch (priority) {
		case 0:
			ndt.setTime(dt.getTime());
			break;
		}
		var y = ndt.getFullYear();
		var M = ndt.getMonth() + 1;
		var d = ndt.getDate();
		var h = ndt.getHours();
		var m = ndt.getMinutes();
		
		M = M > 9 ? M : "0" + M;
		d = d > 9 ? d : "0" + d;
		var time = y + "-" + M + "-" + d;

		
		return time;
	}
	
</script>


