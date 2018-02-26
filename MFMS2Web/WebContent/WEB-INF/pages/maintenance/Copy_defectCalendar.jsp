<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<jsp:include page="../common/left.jsp" />
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">
<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>
<script src="import/datetimepicker/moment.js"></script>
<link rel='stylesheet' href='import/fullcalendar/fullcalendar.css' />
<script src='import/fullcalendar/fullcalendar.js'></script>
<link rel="stylesheet" type="text/css"
	href="import/duallistbox/bootstrap-duallistbox.css">
<script src="import/duallistbox/jquery.bootstrap-duallistbox.js"></script>
<script src='import/fullcalendar/lang-all.js'></script>
<script src="import/datetimepicker/date.js"></script>

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
</style>


<script type="text/javascript">
$(document).ready(
		function() {
			//console.log("defaultViewStr : " + defaultViewStr);

			$('#eventModal').on('hidden.bs.modal', function(e) {
				$('#view_calendar').load("ShowCalendar.do");
			});
		});
<!--
var selectedStaffJson = '${selectedStaffJson}';
var eventEditStaffJson = '${eventEditStaffJson}';
-->

var selectedStaffJson = '';
var eventEditStaffJson = '';

$(document).ready(function(){
	
	selectedStaffJson = '${selectedStaffJson}';
	eventEditStaffJson = '${eventEditStaffJson}';
	
	$.ajax({
		method : "POST",
		url : "PatrolAssignSelectStaff.do",
		data : "edit=false&selectedStaffJson=" + selectedStaffJson
				+ "&eventEditStaffJson=" + eventEditStaffJson,
		success : function(data) {

			$('#selectedStaff').html(data);
			console.log("load selected staff");
			reDesignAccountDiv();
		}
	});
	
	
	$('#view_searchedStaff').load("ShowStaffSearched.do?privilege=${maintenanecPrivilege}");
	
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
					+ "&eventEditStaffJson=" + eventEditStaffJson,
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
					+ "&eventEditStaffJson=" + eventEditStaffJson,
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
							<div
								class="form-group <spring:bind path="frequency"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-2 control-label"><spring:message
										code="patrol.schedule.frequency" />*</label>
										<div class="col-sm-10" style="padding: 8px 21px" >
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="101" id="once" />
									<spring:message code="schedule.frequency.Once" />
										</span>
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="102" />
									<spring:message code="schedule.frequency.Daily" />
										</span>
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="103" id="weekly" />
									<spring:message code="schedule.frequency.Weekly" />
										</span>
										<span style="white-space: nowrap;"> 
									<form:radiobutton path="frequency" value="104" />
											<spring:message code="schedule.frequency.Monthly" />	
										</span>		
										<span style="white-space: nowrap;"> 			
									<form:radiobutton path="frequency" value="105" />
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
									<form:input id="endDate" path="endDate" cssClass="form-control"
										placeholder="${to}" readonly="true"
										style="background-color: #fff;" />
								</div>
							</div>

							<div
								class="form-group <spring:bind path="time"><c:if test="${status.error}">has-error</c:if></spring:bind>">
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
				<a onclick="showLoading()" id="editCurrentUrl"
					class="btn btn-primary btn-lg"><spring:message
						code="button.editCurrent" /></a> <a onclick="showLoading()"
					id="editSeriesUrl" class="btn btn-primary btn-lg"><spring:message
						code="button.editSeries" /></a>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<div class="modal fade" id="hahaModal">

<div id="testData"></div>
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
			
			
			if ($("#once").is(':checked')) {
				//$("#endDateDiv").hide();
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
				//$("#time").val('');
				
				if($('#endDate').val() != ""){
					var endDateM = moment($('#endDate').val(), "YYYY-MM-DD HH:mm");
					endDateM.set('h',23);
					endDateM.set('m',59);
					$('#endDate').val(endDateM.format("YYYY-MM-DD HH:mm"));
				}
				
				if($('#startDate').val() != ""){
					
					
					var startDateM = moment($('#startDate').val(), "YYYY-MM-DD HH:mm");
					startDateM.set('h',00);
					startDateM.set('m',00);
					$('#startDate').val(startDateM.format("YYYY-MM-DD HH:mm"));
				
				}
				
				$('#startDate').datetimepicker('remove');
				$('#endDate').datetimepicker('remove');
				
				setFullDayPicker();
				
			} else {
				//$("#time").show();
				
				$('#startDate').datetimepicker('remove');
				$('#endDate').datetimepicker('remove');
			
				setNotFullDayPicker();
			}
		});
	});

	$(function() {
		
		setNotFullDayPicker();
		
		$('#time').datetimepicker({
			format : 'hh:ii',
			startView : 1,
			maxView : 1,
			pickDate: false,
			autoclose : true,
			keyboardNavigation : false
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : hidden")
			
		}).on("changeDate", function(ev) {
			$("#fullDay").prop('checked', false);	
		});
	
   		if($("#startDate").val().length>0)
		$("#endDate").datetimepicker("setStartDate",$("#startDate").val());
		
		if($("#endDate").val().length>0)
		$("#startDate").datetimepicker("setEndDate",$("#endDate").val());		
		
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
	
	var lang = "${pageContext.response.locale}";
	
	if(lang == 'zh_HK')
		lang = 'zh-tw'
		else 
			lang = 'en'	
		
	
				$(window).load(function() {
					// for all the events in the calendar
					var eventMap=[];
					// for the events which in the small box
					var eventMap_s=[];
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
									
//									console.log("event.id: " + event.id + "  event.titile: " + event.title);
									//.toISOString() to convert time zone as UTC
									$(element).addClass("event_id_"+event.id+"_date_"+new Date(event.start).toISOString().substring(0, 10)+"_");
									
									// check all the event one by one
//									var eventDate = event.start+"";																		
//									var check = checkFinish(event.id, eventDate);
//									console.log("check: " + check);
//									if(check=="done"){
//										$(element).css("background-color", "red").css("border","1px red solid");
//									}	

									var canGen = false;
									if (event.ranges.filter(function(range) {
												
										if (event.end == null) 
											return false;
										
												return (event.start.isBefore(range.end) && event.end.isAfter(range.start));
											}).length > 0) {
										canGen = true;
									}
									//console.log(event);
									console.log(event.excludedDates);
									if (event.excludedDates != null) {
										event.excludedDates.filter(function(excludedDate) {
											if (event.start.isSameOrBefore(excludedDate.end) && event.end.isSameOrAfter(excludedDate.start)) {
														canGen = false;
													}											
												});
									}
									return canGen;
								},
								eventClick : function(calEvent, jsEvent, view) {
/*					
									var editCurrentUrl = "ModifyCurrentSchedule.do?key=" + calEvent.id + "&currentStart=" + calEvent.start
                                    .format() + "&currentEnd=" + calEvent.end.format();
									
									var editSeriesUrl = "ModifySeriesSchedule.do?key=" + calEvent.id + "&currentStart=" + calEvent.start
                                    .format() + "&currentEnd=" + calEvent.end.format();
									
									$("#editCurrentUrl").attr("href",editCurrentUrl);
									$("#editSeriesUrl").attr("href",editSeriesUrl);
									$('#modifyModal').modal('show')
*/									
									/*$.ajax({
												url : "EditSchedule.do?key=" + calEvent.id,
												dataType : "html",
												success : function(data) {
													window.location.href = "EditSchedule.do?key=" + calEvent.id;
												}
									})*/
									
									$.ajax({
										method : "POST",
										url : "GetScheduleEvent.do",
										dataType : "html",
										data : "scheduleKey=" + calEvent.id + "&currentStart=" + calEvent.start.format() + "&currentEnd=" + calEvent.end.format(),
										success : function(data) {
											$('#testData').html(data).promise().done(
															function() {
																$('#hahaModal').modal('show');
															});
										}
									})
																
								},
								
								dayClick : function(date, jsEvent, view) {
									
								},
								lang: lang,
								columnFormat: 'ddd D/M',
								contentHeight: 685,
                                defaultView: 'basicDay',
                                allDayDefault: true,
                                displayEventTime: false,
                                timezone: 'Asia/Hong_Kong',
                                header: {
                    				left: 'title',
                    				right: 'month,basicWeek,basicDay,prev,next,today'
                    			},
                    			
                    			eventLimit : true,
								views : {
									month : {eventLimit : 2}
								},
								//eventLimitClick :  "popover",
								eventLimitClick :  function(cellInfo){

													for(i=0;i<cellInfo.segs.length;i++){
														var eve = cellInfo.segs[i];
														//console.log("eve['event'].id : " + eve['event'].id);
														//console.log("eve['event'].start : " + new Date(eve['event'].start).toISOString().substring(0, 10));

														eventMap_s.push("event_id_"+eve['event'].id+"_date_"+new Date(eve['event'].start).toISOString().substring(0, 10)+"_");
													}
													
													//console.log("eventMap_s : " + eventMap_s);
													
													setTimeout(function(){ 
														
														becomeRed(eventMap_s);
														
														}, 0);
													
													return "popover";
								},
								eventAfterRender : function( event, element, view ) { 
									// loop									
									eventMap.push("event_id_"+event.id+"_date_"+new Date(event.start).toISOString().substring(0, 10)+"_");									
									//console.log("event_id_" + event.id);
									//console.log("_date_" + new Date(event.start).toISOString().substring(0, 10)+"_");
									
								},
								eventAfterAllRender : function(view){

									becomeRed(eventMap);
									
											
								}
								
							});
			
					
			$(".fc-toolbar .fc-left").attr("style", "cursor:pointer");

			$(".fc-toolbar .fc-left").append("<input id='hidden_datepicker' type='button' style='visibility: hidden; position:absolute;'/>")

			$("#hidden_datepicker").datetimepicker({
				bootcssVer : 3,
				format : 'yyyy-mm-dd',
				minView : 3,
				startView : 3,
				autoclose : true,
			}).on("show", function(ev) {
				//$(".table-condensed th").attr("style","visibility : visible");

			}).on("changeDate", function(ev) {

						$('#calendar').fullCalendar('gotoDate', moment(ev.date.valueOf()).format("YYYY-MM-DD"));
					});

			$(".fc-toolbar .fc-left h2").click(
					function() {

						var topVar = $(".fc-toolbar .fc-left").position().top;
						var leftVar = $(".fc-toolbar .fc-left").position().left;

						$("#hidden_datepicker").css('top', topVar);
						$("#hidden_datepicker").css('left', leftVar);

						$("#hidden_datepicker").datetimepicker('show');

					})
					
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
	
	// used to check events one by one
	function checkFinish(dsKey, eventDate){
		
		$.ajax({
			method : "POST",
			async: false,
			url : "CheckFinish.do",
			data: {key : dsKey, eventDate:eventDate},
			success : function(data) {
				result = data;			
			}
		});
		return result;
	}
	
	//used to check a batch of events
	function checkFinish_v1(event){
		
		$.ajax({
			method : "POST",
			async: false,
			url : "CheckFinish_v1.do",
			data: {event : event},
			success : function(data) {
				
				console.log("CheckFinish_v1.do - data : " + data);
				result = data;
			}
		});
		return result;
	}

	
	function becomeRed(event){
		console.log("becomeRed event : " + event);
		var resultStr = checkFinish_v1(event);
		
		if(resultStr.length>0){

			for(i=0;i<resultStr.length;i++){

				 $("."+resultStr[i]).each(function(){
					 
					$(this).css("background-color","red").css("border","1px red solid");
					
				 });
			}
		}	
	}
	
	
	
</script>


