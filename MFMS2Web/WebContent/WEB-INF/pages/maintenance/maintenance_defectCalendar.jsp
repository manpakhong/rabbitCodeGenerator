<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<jsp:include page="../common/left.jsp" />
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<%@ page contentType="text/html;charset=utf-8" %>
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">
<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>
<script src="import/datetimepicker/moment.js"></script>
<link rel="stylesheet" type="text/css"
	href="import/duallistbox/bootstrap-duallistbox.css">
<script src="import/duallistbox/jquery.bootstrap-duallistbox.js"></script>
<script src="import/datetimepicker/date.js"></script>
<!--  old version of fullcalendar 2.6
<link rel='stylesheet' href='import/fullcalendar/fullcalendar.css' />
<script src='import/fullcalendar/fullcalendar.js'></script>
<script src='import/fullcalendar/lang-all.js'></script>
-->
<!-- fullcalendar 3.4 -->
<link rel='stylesheet' href='import/fullcalendar-3.4.0/fullcalendar.css' />
<script src='import/fullcalendar-3.4.0/lib/moment.js'></script>
<script src='import/fullcalendar-3.4.0/fullcalendar.js'></script>
<script src='import/fullcalendar-3.4.0/locale-all.js'></script>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.maintenanceSchedule')"
	var="maintenanecPrivilege" />

<style>
.close {
	font-size: 50px;
	font-weight: bold;
	line-height: 18px;
	color: #000000;
	text-shadow: 0 1px 0 #ffffff;
	opacity: 0.4;
	filter: alpha(opacity = 20);
	text-decoration: none;
}

.fc-event-dot{
    display: inline-block;
    width: 20px;
    height: 20px;
    border-radius: 20px;
}
</style>

<script type="text/javascript">

<!--
var selectedStaffJson = '${selectedStaffJson}';
var eventEditStaffJson = '${eventEditStaffJson}';
-->
//for the color of the finished task
var backgroundColor_Completed = "#777"; //looks like orange
var backgroundColor_NotCompleted = "#F5A743"; //looks like deep dark grey
var fontColor = "black";
var fontColor4Completed = "white";
var selectedStaffJson = '';
var eventEditStaffJson = '';

$(document).ready(function(){
	
	selectedStaffJson = '${selectedStaffJson}';
	eventEditStaffJson = '${eventEditStaffJson}';
	
	$.ajax({
		method : "POST",
		url : "PatrolAssignSelectStaff.do",
		data : "edit=false&selectedStaffJson=" + selectedStaffJson
				+ "&eventEditStaffJson=" + eventEditStaffJson
				+ "&extraId=",
		success : function(data) {

			$('#selectedStaff').html(data);
			console.log("load selected staff");
			reDesignAccountDiv();
		}
	});
	
	
	$('#view_searchedStaff').load("ShowStaffSearched.do?privilege=${maintenanecPrivilege}");
	actionChange('series');
	$('#scheduleEventModal').on('hidden.bs.modal', function () {
		$("#rightNav").click()
	})
		//make prompt msg disappear
	    $(document).on('click', 'button, span, div.fc-content, i, a', function () {
	    	
			slideUpThePrompt();
			
	    });
	
});

function reDesignAccountDiv(){
	console.log("redesign account div");
	$("#noIn").attr('class', 'col-xs-10').removeAttr('style');
	$("#accountDiv").attr('class', '');
	$("#accountLabel").attr('class', 'col-sm-2 control-label').css( "border", "none").css("padding-right", "0px").append("*");
	
}

function refreshAccount() {
	if ($("#eventModal").hasClass('in')) {
		console.log("#eventModal");
		$.ajax({
			method : "POST",
			url : "PatrolAssignSelectStaff.do",
			data : "edit=true&selectedStaffJson=" + eventEditStaffJson
					+ "&eventEditStaffJson=" + eventEditStaffJson
					+ "&extraId=",
			success : function(data) {
				$('#modal_account').html(data);
				reDesignAccountDiv();
			}
		});

	} else {
		console.log("#not eventModal");
		$.ajax({
			method : "POST",
			url : "PatrolAssignSelectStaff.do",
			data : "edit=false&selectedStaffJson=" + selectedStaffJson
					+ "&eventEditStaffJson=" + eventEditStaffJson
					+ "&extraId=",
			success : function(data) {
				$("#staffSelected").val(selectedStaffJson);
				$('#selectedStaff').html(data);
				reDesignAccountDiv();
			}
		});

	}
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

</script>


<div class="content-wrapper">
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_maintenance_schedule_b"></i>
				<spring:message code="menu.defectMgt.schedule" /></li>
		</ol>
	</section>
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<form:form id="defectScheduleForm" cssClass="form-horizontal"
						commandName="defectScheduleForm" method="post"
						action="DoCreateDefectSchedule.do">
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<c:if test="${defectScheduleForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.create.success" />
							</div>
						</c:if>
						<c:if test="${defectScheduleForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.modify.success" />
							</div>
						</c:if>
						<c:if test="${defectScheduleForm.referrer == 'd'}">
							<div class="form-group alert alert-success">

								<spring:message code="menu.defectMgt.schedule" />
								<c:out value="${defectScheduleForm.deletedName}" />
								<spring:message code="has.been.deleted" />

							</div>
						</c:if>
						<c:if test="${defectScheduleForm.referrer == 's'}">
							<div class="form-group alert alert-success">

								<spring:message code="defect.the.event.is.completed" />

							</div>
						</c:if>
						<c:if test="${defectScheduleForm.referrer == 'n'}">
							<div class="form-group alert alert-danger">

								<spring:message code="defect.pass.event.cannot.deleted" />

							</div>
						</c:if>
						<c:if test="${defectScheduleForm.referrer == 'df'}">
							<div class="form-group alert alert-success">

								<spring:message code="defect.schedule.deletefuture.cannotdeletecurrent" />

							</div>
						</c:if>
						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<form:hidden path="staffSelected" id="staffSelected" />

						<spring:eval
							expression="@privilegeMap.getProperty('privilege.code.searchMaintenanceSchedule')"
							var="searchMaintenanceSchedule" />
						<spring:eval
							expression="@privilegeMap.getProperty('privilege.code.createMaintenanceSchedule')"
							var="createMaintenanceSchedule" />

						<div class="box-body">

							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)}">
								<c:if
									test="${currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
									<div class="col-lg-6">
								</c:if>
								<c:if
									test="${!currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
									<div class="col-lg-12">
								</c:if>
								<div id="calendar"></div>
								<!--  
								<button class="btn btn-primary" type="button" id="btn_export" onclick="exportToExcel();">
									<spring:message code="button.exportExcel" />
								</button>
								-->
								<div class="modal fade" id="eventModal" tabindex="-1"
									role="dialog" aria-labelledby="myModalLabel"></div>
						</div>
						</c:if>

						<c:if
							test="${currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)}">
								<div class="col-lg-6">
							</c:if>
							<c:if
								test="${!currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)}">
								<div class="col-lg-12">
							</c:if>
							<div class="modal-header">
								<h3 class="modal-title">
									<spring:message code="patrol.schedule.new" />
								</h3>
							</div>
							<div
								class="<spring:bind path="locationKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<br> <label class="col-sm-2 control-label"><spring:message
										code="defect.location" />*</label>
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
												node="${defectScheduleForm.availableLocationTree}"/>
										</ul>
										<script>
										$("#locationKey1")[0].click();
										</script>
									</div>
								</div>
							</div>
																	
									<div>						
										<div class="form-group <spring:bind path="selectedAccountKeyList">
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
							<div class="form-group <spring:bind path="equipmentKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
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
							<div class="form-group <spring:bind path="frequency"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-2 control-label"><spring:message
										code="patrol.schedule.frequency" />*</label>
										<div class="col-sm-10" style="padding: 8px 21px" >
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="101" id="once" />
									<spring:message code="schedule.frequency.Once" />
										</span>
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="102" id="daily"/>
									<spring:message code="schedule.frequency.Daily" />
										</span>
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="103" id="weekly" />
									<spring:message code="schedule.frequency.Weekly" />
										</span>
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="104" id="monthly"/>
											<spring:message code="schedule.frequency.Monthly" />	
										</span>		
										<span style="white-space: nowrap;"> 			
									<form:radiobutton path="frequency" value="105" id="yearly"/>
									<spring:message code="schedule.frequency.Annually" />
										</span>														
								</div>
								<div id="weekDay"
									class="form-group <spring:bind path="weekDays"><c:if test="${status.error}">has-error</c:if></spring:bind>">
											<label class="col-sm-2 control-label"><spring:message
											code="schedule.frequency.Weekly" />*</label>
											<div class="col-sm-10" style="padding: 8px 21px;">
											<div>
										<form:checkbox path="weekDays" value="1" />
										<spring:message code="schedule.frequency.Monday" />
											</div>
											<div>
										<form:checkbox path="weekDays" value="2" />
										<spring:message code="schedule.frequency.Tuesday" />
											</div>
											<div>
										<form:checkbox path="weekDays" value="3" />
										<spring:message code="schedule.frequency.Wednesday" />
											</div>
											<div>
										<form:checkbox path="weekDays" value="4" />
										<spring:message code="schedule.frequency.Thursday" />
											</div>
											<div>
										<form:checkbox path="weekDays" value="5" />
										<spring:message code="schedule.frequency.Friday" />
											</div>
											<div>
										<form:checkbox path="weekDays" value="6" />
										<spring:message code="schedule.frequency.Saturday" />
											</div>
											<div>
										<form:checkbox path="weekDays" value="0" />
										<spring:message code="schedule.frequency.Sunday" />
									</div>
											</div>
										</div>
									</div>
							
						    <div class="form-group <spring:bind path="startDate"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label"><spring:message code="patrol.schedule.date.start" />*</label>
								<div class="col-sm-10" <spring:message code="patrol.schedule.date.start" var="from"/>>
									<form:input id="startDate" path="startDate" cssClass="form-control" placeholder="${from}" readonly="true"
										style="background-color: #fff;" />
								</div>
							</div>
							<div id='endDateDiv' class="form-group <spring:bind path="endDate"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label"><spring:message code="patrol.schedule.date.end" /></label>
								<div class="col-sm-10" <spring:message code="patrol.schedule.date.end" var="to"/>>
									<form:input id="endDate" path="endDate" cssClass="form-control" placeholder="${to}" readonly="true"
										style="background-color: #fff;" />
								</div>
							</div>
							  
							 <div class="form-group <spring:bind path="startTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label" style="padding-right: 0px; padding-left: 0px; left: 15px;">
								<spring:message code="patrol.schedule.task.date.start" />*</label>
								<div class="col-sm-10" <spring:message code="patrol.schedule.task.date.start" var="timeFrom"/>>
									<form:input id="startTime" path="startTime" cssClass="form-control" placeholder="${timeFrom}" readonly="true"
										style="background-color: #fff;" data-date="1899-12-31"/>
								</div>
							</div>
							<div class="form-group <spring:bind path="endTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label" style="padding-right: 0px;">
								<spring:message code="patrol.schedule.task.date.end" /></label>
								<div class="col-sm-10" <spring:message code="patrol.schedule.task.date.end" var="timeTo"/>>
									<form:input id="endTime" path="endTime" cssClass="form-control" placeholder="${timeTo}" readonly="true"
										style="background-color: #fff;" data-date="1899-12-31"/>
								</div>
							</div>

							<div class="form-group <spring:bind path="time"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label"><spring:message
										code="patrol.schedule.time" /></label>
								<div class="col-sm-10"
									style="padding-left: 21px; padding-top: 6px;">
									<form:checkbox id="fullDay" path="isFullDay" value="true" />
									<spring:message code="schedule.fullDay" />
								</div>
								<!--  
								<div class=" col-sm-offset-4 col-sm-8">
									<spring:message code="patrol.schedule.time" var="time" />
									<form:input id="time" path="time" cssClass="form-control"
										placeholder="${time}" readonly="true"
										style="background-color: #fff;" />
								</div>
								-->
							</div>


							<div
																																																																							<div
								class="form-group <spring:bind path="remarks"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<div
									class="<spring:bind path="remarks"><c:if test="${status.error}">has-error</c:if></spring:bind>">
											<label class="col-sm-2 control-label"><spring:message
											code="defect.remarks" /></label>
											<div class="col-sm-10">
										<spring:message code="defect.remarks" var="defectRemarks" />
										<form:textarea path="remarks" cssClass="form-control"
											placeholder="${defectRemarks}" />
										<br>
									</div>
								</div>
							</div>
							<div class="modal-footer">

								<button onclick="showLoading()" type="submit"
									style="float: left;" class="btn btn-success">
									<spring:message code="button.addSchedule" />
								</button>

								<a onclick="showLoading()" href="DefectSchedule.do"
									class="btn btn-primary "><spring:message
										code="button.reset" /></a>

							</div>
				</div>
				</c:if>

			</div>
			</form:form>
			<form:form commandName="hidden_form" cssClass="form-horizontal" >
				<input id="hidden_submit" type="submit" style="visibility: hidden;">
			</form:form>
		</div>
</div>
</section>
</div>


<!-- Modal selectStaffModal -->
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
						<button type="button" class="btn btn-default btn-block"
							style="margin-bottom: 0; margin-left: 0px;"
							onclick="addAllSelectedStaff();">
							<i class="fa fa-chevron-right"></i> <i
								class="fa fa-chevron-right"></i>
						</button>
					</div>
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<button type="button" class="btn btn-default btn-block"
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
<!--  origin
<div id="modifyModal" class="modal fade">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body" style="text-align: center">
				<a onclick="showLoading()" id="editCurrentUrl" class="btn btn-primary btn-lg">
				<spring:message code="button.editCurrent" /></a> 
				<a onclick="showLoading()" id="editSeriesUrl" class="btn btn-primary btn-lg">
				<spring:message code="button.editSeries" /></a>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
-->
<div class="modal fade" id="scheduleEventModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
				<spring:message code="menu.defectMgt.schedule.defect" />
				</h4>
			</div>
			<div class="modal-body">
			
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="" style="width: 50%;">
				<a id="leftNav" href="#eventTab" aria-controls="current" role="tab" data-toggle="tab" onclick="actionChange('current')">
					<spring:message code="schedule.view.current" /></a></li>
				<li role="presentation" class="active" style="width: 50%;">
				<a id="rightNav" href="#eventTab" aria-controls="series" role="tab" data-toggle="tab" onclick="actionChange('series')">
					<spring:message code="schedule.view.series" /></a></li>
			</ul>
			
			<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="eventTab">
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.location" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mlocationName" readonly="true"> locationName </div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.location" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcLocationName" readonly="true"> locationName </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="menu.accountMgt.account" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<select class="form-control" id="maccount" size="2" readonly="true"> AccountID </select>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="menu.accountMgt.account" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<select class="form-control" id="mcAccount" size="2" readonly="true"> AccountID </select>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.description"/></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mdescription" readonly="true"> Description </div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.description"/></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcDescription" readonly="true"> Description </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.equipment" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mequipment" readonly="true"> Equipment </div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.equipment" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcEquipment" readonly="true"> Equipment </div>
					</div>
				</div>				
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.tools" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mtool" readonly="true"> Tool </div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.tools" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcTool" readonly="true"> Tool </div>
					</div>
				</div>
								
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.frequency" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mfrequency" readonly="true"> Frequency </div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.frequency" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcFrequency" readonly="true"> Frequency </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.date.start" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mstartDate" readonly="true"> Start Date </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.date.end" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mendDate" readonly="true"> End Date </div>
					</div>
				</div>
				<!-- for current -->
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.date.start" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcStartDate" readonly="true"> Start Date </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.date.end" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcEndDate" readonly="true"> End Date </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.task.date.start" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mstartTime" readonly="true"> Task Start Time </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.task.date.end" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mendTime" readonly="true"> Task End Time </div>
					</div>
				</div>

				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.task.date.start" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcStartTime" readonly="true"> Task Start Time </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="patrol.schedule.task.date.end" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcEndTime" readonly="true"> Task End Time </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="schedule.fullDay" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mfullDate" readonly="true"> Yes/No </div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="schedule.fullDay" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcFullDate" readonly="true"> Yes/No </div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.status" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mstatus" readonly="true"> Status </div>
					</div>
				</div>					
				
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.remarks" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mremarks" readonly="true"> Remarks </div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
						<label class="col-sm-10 control-label"><spring:message code="defect.remarks" /></label>
					</div>
					<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
						<div class="form-control" id="mcRemarks" readonly="true"> Remarks </div>
					</div>
				</div>																				
			</div></div></div>
			
			<div class="modal-footer">
				<div class="row">
				  <!--  
					<button type="button" style="display: inline-block; margin-bottom: 0px;" id="completedModalBtn" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#completedModal">
					<spring:message code="button.toCompleteEvent" /></button>
				-->
					<a href="javascript:void(0)" onclick="" id="completedModalBtn" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#completedModal" style = "margin-bottom: 0px;">
					<spring:message code="button.toCompleteEvent" /></a> 
					<a onclick="showLoading()" id="editCurrentUrl" class="btn btn-primary btn-lg">
					<spring:message code="button.editCurrent" /></a>
					<a onclick="showLoading()" id="editSeriesUrl" class="btn btn-primary btn-lg">
					<spring:message code="button.editSeries" /></a>
				</div>
			</div>
		</div>
	</div>
</div>
	
<!-- completed Modal -->
<div class="modal fade" id="completedModal" tabindex="-1" role="dialog" aria-labelledby="completedModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
					<h4 class="modal-title" id="completedModalLabel"><spring:message code="defect.complete.comfirmation"/></h4>
			</div>
		<div class="modal-body" id="completedModalBody"><spring:message code="defect.make.event.completed"/></div>
			<div class="modal-footer"> 
				<button type="button" class="btn btn-primary" style="margin-bottom: 0px;" data-dismiss="modal"><spring:message code="button.No"/></button>
				<button type="button" class="btn btn-primary" id="completedUrl" onclick="completeSchedule()"><spring:message code="button.Yes"/></button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	menu_select("maintenanceSchedule");
	$("#weekDay").hide();	
	//$("#endDateDiv").hide();	
	
	/*if (${defectScheduleForm.isFullDay}) {
		$("#time").hide();
	} else 
		$("#time").show();*/
	
	if (${defectScheduleForm.isWeekDay}) {
		$("#weekDay").show();
	} else 
		$("#weekDay").hide();
	
	/*if (${defectScheduleForm.frequency !=101}) {
		$("#endDateDiv").show();
	} else 
		$("#endDateDiv").hide();*/
	
	var userAccountBox1 = $("#userAccountBox1").bootstrapDualListbox({});

	$(function() {
		$('input[type="radio"]').click(function() {
			if ($("#weekly").is(':checked')) {
				$("#weekDay").show();
			} else {
				$('input:checkbox').removeAttr('checked');
				$("#weekDay").hide();
			}
			
			
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
				
//				setFullDayPicker();
				
			} else {
				//$("#time").show();
				
//				$('#startDate').datetimepicker('remove');
//				$('#endDate').datetimepicker('remove');
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
		
		//setNotFullDayPicker();
//		$('#time').datetimepicker({
//			format : 'hh:ii',
//			startView : 1,
//			maxView : 1,
//			pickDate: false,
//			autoclose : true,
//			keyboardNavigation : false
//		}).on("show", function(ev){
//			$(".table-condensed th").attr("style","visibility : hidden")
			
//		}).on("changeDate", function(ev) {
//			$("#fullDay").prop('checked', false);	
//		});
	
   		//if($("#startDate").val().length>0)
		//$("#endDate").datetimepicker("setStartDate",$("#startDate").val());
		
		//if($("#endDate").val().length>0)
		//$("#startDate").datetimepicker("setEndDate",$("#endDate").val());		
		
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
	
	
	var lang = "${pageContext.response.locale}";
	
	if(lang == 'zh_HK')
		lang = 'zh-tw'
		else 
			lang = 'en'	
		
	console.log("lang : " + lang);
				$(window).load(function() {
					// for the events which in the small box
					var eventMap_s=[];
					var mySet = new Set();
					
					$('#calendar').fullCalendar(
							{
								events : function(start, end, timezone, callback) {

									showLoading();
											
									$.ajax({
												method : "POST",
												url : "GetDefectSchedule.do",
												dataType : "json",
												success : function(data) {

													callback(data);													
													hideLoading();
													
												}
											});

								},							
								eventRender : function(event, element, view) {
									
									//console.log(event);
									
									//.toISOString() to convert time zone as UTC
									var today = new Date().toISOString().substring(0, 10);
									var eventDate = new Date(event.start).toISOString().substring(0, 10);

									//check past day
									
									if(today > eventDate){
										//for month view
										if($(element)[0].nodeName.toLowerCase()==="a"){
										$(element).css({"background-color" : backgroundColor_NotCompleted, "border":"1px solid" + backgroundColor_NotCompleted, "color" : fontColor});
										}
										//for week and day view
										if($(element)[0].nodeName.toLowerCase()==="tr"){
											$($(element).children()[0]).children().css({"background-color" : backgroundColor_NotCompleted, "border":"1px solid"+backgroundColor_NotCompleted});
										}
									}
									
									//console.log("event.id: " + event.id + "  event.titile: " + event.title);
									$(element).addClass("event_id_" + event.id + "_date_" + eventDate + "_").css("cursor","pointer");
									$(element).addClass('date'+eventDate);					
									mySet.add("event_id_"+event.id+"_date_"+new Date(event.start).toISOString().substring(0, 10)+"_");
									
									//control what events should be display.
									var canGen = false;		
									
									var eStart = event.start.toISOString();
									var eEnd;
									if(event.end!=null){									
									    eEnd = event.end.toISOString();
									} else {
										eEnd = null;
									}
//									var rStart = JSON.stringify(event.ranges).substring(11,30);
//									var rEnd = JSON.stringify(event.ranges).substring(39,58);
//									var eventRanges = JSON.stringify(event.ranges);
//									console.log("event title : " + event.title);
//									console.log("eStart : " + eStart);
//									console.log("eEnd : " + eEnd);
//									console.log("event.ranges :" + JSON.stringify(event.ranges));
//									console.log("event.ranges.start : " + rStart);
//									console.log("event.ranges.end : " + rEnd);

											//range has timediff
										eventS = event.start.clone()
										eventS.add(-8,"hours");
											
									if(event.end!=null){
										eventE = event.end.clone();
										eventE.add(-8,"hours");
										}
									if (event.ranges.filter(function(range) {
										//console.log(event);		
										if (event.end == null){ 
											return false;
										}						
											
											return (eventS.isBefore(range.end) && eventE.isAfter(range.start));
										}).length > 0) {
									
										canGen = true;
									}
									
									
									//console.log(event);
									//console.log(event.excludedDates);
									//console.log(JSON.stringify(event.excludedDates));
									if (event.excludedDates != null) {
										event.excludedDates.filter(function(excludedDate) {
											if (eventS.isSameOrBefore(excludedDate.end) && eventE.isSameOrAfter(excludedDate.start)) {
														canGen = false;
													}											
												});
									}

									return canGen;									
								},
								eventClick : function(calEvent, jsEvent, view) {
									//get the parent key if the event has. if not the key remain the same
									var parentKey = getScheduleEventParentKey(calEvent.id);
																
									if(calEvent.end!=null){
										var end = calEvent.end.format();

									} else{
										var end = "";
									}
									var editCurrentUrl = "ModifyCurrentSchedule.do?key=" + calEvent.id + "&currentStart=" + calEvent.start
                                    .format() + "&currentEnd=" + end;
									
									var editSeriesUrl = "ModifySeriesSchedule.do?key=" + parentKey + "&currentStart=" + calEvent.start
                                    .format() + "&currentEnd=" + end;
									
									var completedUrl = "key=" + calEvent.id + "&currentStart=" + calEvent.start
	                                    .format() + "&currentEnd=" + end;
									
									$("#editCurrentUrl").attr("href",editCurrentUrl);
									$("#editSeriesUrl").attr("href",editSeriesUrl);
									$("#completedUrl").attr("value", completedUrl);
//									$('#modifyModal').modal('show')
//									console.log("check currentEnd : " + end);
									$.ajax({
										method : "POST",
										url : "GetScheduleEvent_1.do",
										data : "scheduleKey=" + calEvent.id + "&currentStart=" + calEvent.start.format() + "&currentEnd=" + end,
										success : function(data) {
												var obj = JSON.parse(data.substring(1,data.length-1));
												
												setInfo(obj);
												
												$('#scheduleEventModal').modal('show');

										}
									});

															
								},
								
								dayClick : function(date, jsEvent, view) {
									//console.log(date);
								},
								locale: lang,
								columnFormat: 'ddd D/M',
								contentHeight: 685,
								//defaultDate:'2017-06-30',
								listDayFormat: 'dddd',
								listDayAltFormat: 'MMM Do YYYY',
                                defaultView: 'listDay',
                                allDayDefault: true,
                                displayEventTime: false,
                                timezone : 'Asia/Hong_Kong',
                    			noEventsMessage : "No events to display",
                                header: {
                    				left: 'title',
                    				right: 'month,listWeek,listDay,prev,next,today',
                    			},
                    			
								views : {
									month: { eventLimit: 3 },								
								},
																
								eventLimitClick :  function(cellInfo){

													for(i=0;i<cellInfo.segs.length;i++){
														var eve = cellInfo.segs[i];

														eventMap_s.push("event_id_"+eve['event'].id+"_date_"+new Date(eve['event'].start).toISOString().substring(0, 10)+"_");
													}
													
													setTimeout(function(){ 
														
														CompletedEventChangeColor(eventMap_s);
														
														}, 0);
													
													return "popover";
								},
								
								eventAfterRender : function( event, element, view ) { 
								
								},
								
								eventAfterAllRender : function(view){

									if($("#notification").length==0){
										setNotificationHeader();
									}
									//if there is no event in the date, hide the export button
									if($(".fc-list-empty-wrap2").length>0){
										$("#btn_export").hide();
									} else{
										$("#btn_export").show();
									}
									
									var today = new Date().toISOString().substring(0,10)
//									console.log(today);
									
									
									if($(".fc-listWeek-button").attr('class').search('active')>0 ){
										$(".date"+today).css('background-color','rgb(252, 248, 227)');
									} else{
										$(".date"+today).css('background-color','');
									}
									
									if($(".fc-listDay-button").attr('class').search('active')>0 && $(".fc-list-heading").attr('data-date')==today){
										$('.fc-scroller').css('background-color','rgb(252, 248, 227)');
									} else{
										$('.fc-scroller').css('background-color','');
									}
									
									//please try yo put the below codes at last.
									var identify = Array.from(mySet);
//									console.log("eventAfterAllRender (Array): " + identify);
//									console.log("eventAfterAllRender (Array.length): " + identify.length);
									localStorage.setItem("set", identify);
									if(identify.length>0){
									CompletedEventChangeColor(identify);
									}
									mySet.clear();
								}								
							});
					
					
			$(".fc-toolbar .fc-left").attr("style", "cursor:pointer");

			$(".fc-toolbar .fc-left").append("<input id='hidden_datepicker' type='button' style='visibility: hidden; position:absolute;'/>")
			$(".fc-toolbar .fc-left").append("<input id='hidden_datepicker_month' type='button' style='visibility: hidden; position:absolute;'/>")
			
			dateSelector();
			monthSelector();

			$(".fc-toolbar .fc-left h2").click(
					function() {

						var topVar = $(".fc-toolbar .fc-left").position().top;
						var leftVar = $(".fc-toolbar .fc-left").position().left;
						
					if($(".fc-list-table").length>0 || $(".fc-list-empty").length>0){
						$("#hidden_datepicker").css('top', topVar);
						$("#hidden_datepicker").css('left', leftVar);
						$("#hidden_datepicker").datetimepicker('show');
					} else{
						$("#hidden_datepicker_month").css('top', topVar);
						$("#hidden_datepicker_month").css('left', leftVar);
						$("#hidden_datepicker_month").datetimepicker('show');
					}				
				});
			});		
	
	//select month only
	function monthSelector(){
		var dtp2 = $("#hidden_datepicker_month").datetimepicker({
			bootcssVer : 3,
			format : 'yyyy-mm-dd',
			minView : 3,
			startView : 3,
			autoclose : true,
		});	
		dtp2.on("show", function(ev) {

		}).on("changeDate", function(ev) {

				$('#calendar').fullCalendar('gotoDate', moment(ev.date.valueOf()).format("YYYY-MM-DD"));
		});
	}
	
	//select month 1st, then choose date		
	function dateSelector(){
		var dtp = $("#hidden_datepicker").datetimepicker({
			bootcssVer : 3,
			format : 'yyyy-mm-dd',
			minView : 2,
			startView : 3,
			autoclose : true,
		});	
		dtp.on("show", function(ev) {

		}).on("changeDate", function(ev) {

				$('#calendar').fullCalendar('gotoDate', moment(ev.date.valueOf()).format("YYYY-MM-DD"));
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
	
	function getScheduleEventParentKey(key){
		var parentKey = "";
		$.ajax({
			method : "POST",
			async: false,
			url : "GetScheduleEventParentKey.do",
			data : {scheduleKey : key},
			success : function(data){
				parentKey = data;
			}
		});
		return parentKey;
	}

	function checkFinish(event){
		var result = "";
		$.ajax({
			method : "POST",
			async: false,
			url : "CheckFinish.do",
			data: {event : event},
			success : function(data) {

				result = data;
			}
		});
		return result;
	}

	
	function CompletedEventChangeColor(event){
		//console.log("CompletedEventChangeColor event : " + event);
		var resultStr = checkFinish(event);
		
		if(resultStr.length>0){

			for(i=0;i<resultStr.length;i++){
				//for month view, change the event background color
				 $("a."+resultStr[i]).each(function(){
					 
					$(this).css({"background-color" : backgroundColor_Completed, "border":"2px solid " + backgroundColor_Completed , "color": fontColor4Completed});
					//$(this).css("background-color",backgroundColor_Completed);
				 });
				 //for week and day view, change the bullet color
				 $("."+resultStr[i]).each(function(){

						$($(this).children()[0]).children().css({"background-color" : backgroundColor_Completed, "border":"1px solid" + backgroundColor_Completed});
						
					 });
			}
		}	
	}
	
	function setInfo(obj){
		for(key in obj){
		
			 var _select = $('<select>');
		
//			 console.log("setInfo() : " + key + " : " + obj[key]);
			 
			 var content = obj[key];

			if(content!=null){
				
				if(content instanceof Array){					
					for(x in content){
//						console.log("content["+x+"] : " + content[x]);
						_select.append($('<option var=' + x + '></option>').html(content[x]));						
						$("#m" + key).html(_select.html()).css("padding-left", "3px");
					}
					
				} else {
					if(key=="description" || key=="cDescription"){
						$("#mdescription").replaceWith('<input class="form-control" id="mdescription" readonly></input>');
						$("#mcDescription").replaceWith('<input class="form-control" id="mcDescription" readonly></input>');
						$("#mdescription, #mcDescription").val(content);
						
					}else if(key=="remarks" || key=="cRemarks"){
						$("#mremarks").replaceWith('<input class="form-control" id="mremarks" readonly></input>');
						$("#mcRemarks").replaceWith('<input class="form-control" id="mcRemarks" readonly></input>');
						$("#mremarks, #mcRemarks").val(content);
					} else{
				 		 $("#m" + key).html(content);
					}
				}				
			} else{
				 $("#m" + key).html(" ");
			}
		}
	}
	
	//the header with 3 bullets
	function setNotificationHeader(){
		$(".fc-view-container").
		before( "<div class = 'row' id = 'notification' style='text-align: right; margin-bottom: 10px;'><div class='fc-event-dot' id = 'b1'/><div class='fc-event-dot' id = 'b2'/><div class='fc-event-dot' id = 'b3'/></div>" );
		
		$("#b1").after("<div style='display: inline;'> <spring:message code='defectSchedule.blueBullet.meaning'/> </div>");
		$("#b2").after("<div style='display: inline;'> <spring:message code='defectSchedule.greenBullet.meaning'/> </div>");
		$("#b3").after("<div style='display: inline;'> <spring:message code='defectSchedule.yellowBullet.meaning'/> </div>");
		

		$("#notification div").css("vertical-align", "middle");
		$("#b2").css("background-color",backgroundColor_Completed);
		$("#b3").css("background-color",backgroundColor_NotCompleted);
		$(".fc-header-toolbar").css("margin-bottom", "0px");
		
		$(".fc-month-button").html("<spring:message code='defectSchedule.month'/>");
		$(".fc-listWeek-button").html("<spring:message code='defectSchedule.week'/>");
		$(".fc-listDay-button").html("<spring:message code='defectSchedule.day'/>");
		$(".fc-today-button").html("<spring:message code='defectSchedule.today'/>");
	}
	
	function exportToExcel() {
		
		var events = localStorage.getItem("set");
		var month = new Date($('#calendar').fullCalendar('getDate')).toISOString().substring(0,7);
		var form = document.getElementById("hidden_form");
		form.setAttribute("method", "post");
		form.setAttribute("target", "_blank");
		form.setAttribute("action", "eventExportResult.do?events=" + events + "&month=" + month);

		//form.submit();
		$("#hidden_submit").click();
	}
	
	function completeSchedule() {
		var data = $("#completedUrl").val()
		console.log("the value I want : " + data);
		$.ajax({
			method : "POST",
			url : "completeSchedule.do",
			data: {data : data},
			//dataType:'json',
			success : function(data) {

				$("#mstatus").html(data);
				
				window.location.href = "DefectSchedule.do?r=s";
				}			
		});
	}
	
	function actionChange(action) {
		if (action === "current") {
			//button
			$("#editCurrentUrl").show();
			$("#editSeriesUrl").hide();
			$("#completedModalBtn").show();
			//console.log("$('#mstatus').html() : " + $('#mstatus').html());
			var status = '<spring:message code="defect.completed"/>';
			console.log("status : " + status);
			if($("#mstatus").html()===status){
				$("#completedModalBtn").attr('disabled',true).attr('data-toggle',"").css({"background-color": backgroundColor_Completed, "border":"1px solid" + backgroundColor_Completed});
				$("#editCurrentUrl").attr('disabled',true).attr('data-toggle',"").css({"background-color": backgroundColor_Completed, "border":"1px solid" + backgroundColor_Completed});
				$("#editCurrentUrl").attr('onclick','').attr('href','javascript:void(0)');
			} else{
				$("#completedModalBtn").attr('disabled',false).attr('data-toggle',"modal").css({"background-color": "", "border":""});
				$("#editCurrentUrl").attr('disabled',false).attr('data-toggle',"modal").css({"background-color": "", "border":""});
			}
			
			//current date
			$("#mcLocationName").parent().parent().show();
			$("#mcAccount").parent().parent().show();
			$("#mcDescription").parent().parent().show();
			$("#mcEquipment").parent().parent().show();
			$("#mcTool").parent().parent().show();
			$("#mcFrequency").parent().parent().show();
			$("#mcStartDate").parent().parent().show();
			$("#mcEndDate").parent().parent().show();
			$("#mcStartTime").parent().parent().show();
			$("#mcEndTime").parent().parent().show();
			$("#mcFullDate").parent().parent().show();
			$("#mcRemarks").parent().parent().show();	
			$("#mstatus").parent().parent().show();
			//series date
			$("#mlocationName").parent().parent().hide();
			$("#maccount").parent().parent().hide();
			$("#mdescription").parent().parent().hide();
			$("#mequipment").parent().parent().hide();
			$("#mtool").parent().parent().hide();
			$("#mfrequency").parent().parent().hide();
			$("#mstartDate").parent().parent().hide();
			$("#mendDate").parent().parent().hide();
			$("#mstartTime").parent().parent().hide();
			$("#mendTime").parent().parent().hide();
			$("#mfullDate").parent().parent().hide();
			$("#mremarks").parent().parent().hide();
		} else {
			if (action === "series") {
			//button
			$("#editCurrentUrl").hide();
			$("#editSeriesUrl").show();
			$("#completedModalBtn").hide();
			//current date
			$("#mcLocationName").parent().parent().hide();
			$("#mcAccount").parent().parent().hide();
			$("#mcDescription").parent().parent().hide();
			$("#mcEquipment").parent().parent().hide();
			$("#mcTool").parent().parent().hide();
			$("#mcFrequency").parent().parent().hide();
			$("#mcStartDate").parent().parent().hide();
			$("#mcEndDate").parent().parent().hide();
			$("#mcStartTime").parent().parent().hide();
			$("#mcEndTime").parent().parent().hide();
			$("#mcFullDate").parent().parent().hide();
			$("#mcRemarks").parent().parent().hide();
			//series date
			$("#mlocationName").parent().parent().show();
			$("#maccount").parent().parent().show();
			$("#mdescription").parent().parent().show();
			$("#mequipment").parent().parent().show();
			$("#mtool").parent().parent().show();
			$("#mfrequency").parent().parent().show();
			$("#mstartDate").parent().parent().show();
			$("#mendDate").parent().parent().show();
			$("#mstartTime").parent().parent().show();
			$("#mendTime").parent().parent().show();
			$("#mfullDate").parent().parent().show();
			$("#mremarks").parent().parent().show();
			$("#mstatus").parent().parent().hide();
			}
		}
	}
	
	function slideUpThePrompt(){	
		 $( ".alert" ).slideUp( 800 );
	}
</script>


