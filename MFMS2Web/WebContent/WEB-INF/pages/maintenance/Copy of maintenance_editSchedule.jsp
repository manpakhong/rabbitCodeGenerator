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
<!--
var selectedStaffJson = '${selectedStaffJson}';
var eventEditStaffJson = '${eventEditStaffJson}';
//-->

var selectedStaffJson = '';
var eventEditStaffJson = '${eventEditStaffJson}';

$(document).ready(function(){
	
	$.ajax({
		method : "POST",
		url : "PatrolAssignSelectStaff.do",
		data : "edit=false&selectedStaffJson=" + eventEditStaffJson
				+ "&eventEditStaffJson=" + eventEditStaffJson,
		success : function(data) {

			$('#selectedStaff').html(data);

		}
	});
	
	$('#view_searchedStaff').load(
	"ShowStaffSearched.do?privilege=${maintenanecPrivilege}");
	
});


function refreshAccount() {
	if ($("#eventModal").hasClass('in')) {
		console.log("#eventModal");
		$.ajax({
			method : "POST",
			url : "PatrolAssignSelectStaff.do",
			data : "edit=true&selectedStaffJson=" + eventEditStaffJson
					+ "&eventEditStaffJson=" + eventEditStaffJson,
			success : function(data) {
				$('#modal_account').html(data);
			}
		});

	} else {
		console.log("#not eventModal");
		$.ajax({
			method : "POST",
			url : "PatrolAssignSelectStaff.do",
			data : "edit=false&selectedStaffJson=" + selectedStaffJson
					+ "&eventEditStaffJson=" + eventEditStaffJson,
			success : function(data) {
				$("#staffSelected").val(selectedStaffJson);
				$('#selectedStaff').html(data);
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
				<spring:message code="${label}" /></li>
		</ol>
	</section>
	<section class="content">
		<div class="row">
			<div class="col-lg-offset-2  col-lg-8 col-md-12 col-sm-12 col-xs-12">
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
						<form:hidden path="staffSelected" id="staffSelected"/>
						
						
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
											<ul class="list-group locationtree" cssClass="form-control"
												id="radioButtonLocationtree">
												<template:radioButtonLocationTree
													node="${defectScheduleForm.availableLocationTree}" />
											</ul>
										</div>
									</div>
								</div>
								<div
									class="form-group col-sm-12 <spring:bind path="selectedAccountKeyList">
										<c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-6 control-label"><spring:message
											code="role.availableUsers" />*</label> <label
										class="col-sm-6 control-label"><spring:message
											code="role.assignedUsers" />*</label>
									<div class="col-lg-12 ">
										<form:select id="userAccountBox1"
											path="selectedAccountKeyList" size="7">
											<c:forEach var="account"
												items="${defectScheduleForm.userAccountList}">
												<form:option value="${account.key}">
													<c:choose>
														<c:when test="${fn:length(account.name) gt 0}">
															<c:out value="${account.loginId} - ${account.name}" />
														</c:when>
														<c:otherwise>
															<c:out value="${account.loginId}" />
														</c:otherwise>
													</c:choose>
												</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								
								<div class="col-xs-12">
												<div class="row form-group" id="selectedStaff">
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
								<label class="col-sm-2 control-label"><spring:message
										code="defect.equipment" /></label>
								<div class="col-sm-4">
									<form:select path="equipmentKey" cssClass="form-control"
										id="equipment" items="${equipmentList}">
									</form:select>
								</div>
								<label class="col-sm-2 control-label"><spring:message
										code="defect.tools" /></label>
								<div class="col-sm-4">
									<form:select path="toolKey" cssClass="form-control"
										items="${toolList}">
									</form:select>
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
								<div
									class="form-group <spring:bind path="frequency"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="patrol.schedule.frequency" />*</label>
									<div class="col-sm-10" style="padding: 8px 21px;">
										<form:radiobutton path="frequency" value="101" id="once" />
										<spring:message code="schedule.frequency.Once" />
										<c:choose>
											<c:when test="${isSeries}">

												<form:radiobutton path="frequency" value="102" />
												<spring:message code="schedule.frequency.Daily" />
												<form:radiobutton path="frequency" value="103" id="weekly" />
												<spring:message code="schedule.frequency.Weekly" />
												<form:radiobutton path="frequency" value="104" />
												<spring:message code="schedule.frequency.Monthly" />
												<form:radiobutton path="frequency" value="105" />
												<spring:message code="schedule.frequency.Annually" />
											</c:when>
										</c:choose>
									</div>
									<div id="weekDay"
										class="form-group <spring:bind path="weekDays"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-2 control-label"><spring:message
												code="schedule.frequency.Weekly" />*</label>
										<div class="col-sm-10" style="padding: 1px 21px;">
											<form:checkbox path="weekDays" value="1" />
											<spring:message code="schedule.frequency.Monday" />
											<form:checkbox path="weekDays" value="2" />
											<spring:message code="schedule.frequency.Tuesday" />
											<form:checkbox path="weekDays" value="3" />
											<spring:message code="schedule.frequency.Wednesday" />
											<form:checkbox path="weekDays" value="4" />
											<spring:message code="schedule.frequency.Thursday" />
											<form:checkbox path="weekDays" value="5" />
											<spring:message code="schedule.frequency.Friday" />
											<form:checkbox path="weekDays" value="6" />
											<spring:message code="schedule.frequency.Saturday" />
											<form:checkbox path="weekDays" value="0" />
											<spring:message code="schedule.frequency.Sunday" />
										</div>
									</div>
								</div>
								<div
									class="form-group <spring:bind path="time"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-sm-2 control-label"><spring:message
											code="patrol.schedule.time" /></label>
									<div class="col-sm-8"
										style="padding-left: 21px; padding-top: 6px;">
										<form:radiobutton id="fullDay" path="isFullDay" value='true' />
										<spring:message code="schedule.fullDay" />
									</div>
									<div class="col-sm-offset-2 col-sm-10">
										<spring:message code="patrol.schedule.time" var="time" />
										<form:input id="time" path="time" cssClass="form-control"
											placeholder="${time}" readonly="true"
											style="background-color: #fff;" />
									</div>
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
											<a href="#" style="float: right"
												data-name="<c:out value="${defectScheduleForm.description }"/>"
												data-href="<c:url value="DoDeleteSchedule.do"><c:param name="key" value="${defectScheduleForm.key}"/><c:param name="start" value="${defectScheduleForm.parentStart}"/></c:url>"
												data-toggle="modal" data-target="#confirmDeleteModal"
												class="btn btn-danger "> <spring:message
													code="button.skipCurrent" /></a>
										</c:otherwise>
									</c:choose>

								</c:if>

								<a onclick="showLoading()" style="float: right"
									href="<c:url value="${resetUrl}"><c:param name="key" value="${defectScheduleForm.key}"/><c:param name="currentStart" value="${defectScheduleForm.parentStart}"/><c:param name="currentEnd" value="${defectScheduleForm.parentEnd}"/></c:url>"
									class="btn btn-primary "><spring:message
										code="button.reset" /></a>


							</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>
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
	
	$("#endDateDiv").hide();	

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
	} else 
		$("#endDateDiv").hide();

	var userAccountBox1 = $("#userAccountBox1").bootstrapDualListbox({});

	$(function() {
		$('input[type="radio"]').click(function() {
			if ($("#weekly").is(':checked')) {
				$("#weekDay").show();
			} else {
				$('input:checkbox').removeAttr('checked');
				$("#weekDay").hide();
			}
			
			
			if ($("#once").is(':checked')) {
				$("#endDateDiv").hide();
			} else {
				
				if($("#startDate").val().length>0)
					$("#endDate").datetimepicker("setStartDate",$("#startDate").val());
					
					if($("#endDate").val().length>0)
					$("#startDate").datetimepicker("setEndDate",$("#endDate").val());		
				
				$("#endDateDiv").show();
			}
			
		});

		$('#fullDay').change(function() {
			if ($("#fullDay").prop("checked")) {
				//$("#time").hide();
				$("#time").val('');
			} else {
				//$("#time").show();
			}
		});
	});

	$(function() {
		$('#startDate').datetimepicker({
			format : 'yyyy-mm-dd',
			minView : 2,
			clearBtn:  1,
			todayBtn : 1,
			startDate : '+0d',
			autoclose : true,
			startView : 2
			
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : visible")	

		}).on("changeDate", function(ev) {

			if($("#once").is(':checked')){
				$('#endDate').val($('#startDate').val());
			} 
			
			var fullDate = $("#startDate").val()
			$("#endDate").datetimepicker("setStartDate", fullDate);
		});
		$('#endDate').datetimepicker({
			format : 'yyyy-mm-dd',
			startDate : '+0d',
			minView : 2,
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
			
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : visible")	
			
		}).on("changeDate", function(ev) {
			
			var fullDate = $("#endDate").val()
			
			$("#startDate").datetimepicker("setEndDate", fullDate);
			
		});
		$('#time').datetimepicker({
			format : 'hh:ii',
			startView : 1,
			maxView : 1,
			autoclose : true,
			keyboardNavigation : false
			
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : hidden")	
			
		}).on("changeDate", function(ev) {

			$("#fullDay").prop('checked', false);
			$(".table-condensed th").attr("style","visibility : hidden");
		});
		
		if($("#startDate").val().length>0)
			$("#endDate").datetimepicker("setStartDate",$("#startDate").val());
			
			if($("#endDate").val().length>0)
			$("#startDate").datetimepicker("setEndDate",$("#endDate").val());		
		
	});

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


