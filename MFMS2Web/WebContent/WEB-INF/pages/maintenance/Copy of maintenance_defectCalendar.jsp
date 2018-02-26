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
<!--
var selectedStaffJson = '${selectedStaffJson}';
var eventEditStaffJson = '${eventEditStaffJson}';
//-->

var selectedStaffJson = '';
var eventEditStaffJson = '';

$(document).ready(function(){
	
	$.ajax({
		method : "POST",
		url : "PatrolAssignSelectStaff.do",
		data : "edit=false&selectedStaffJson=" + selectedStaffJson
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

						<spring:eval
							expression="@privilegeMap.getProperty('privilege.code.searchMaintenanceSchedule')"
							var="searchMaintenanceSchedule" />
						<spring:eval
							expression="@privilegeMap.getProperty('privilege.code.createMaintenanceSchedule')"
							var="createMaintenanceSchedule" />

						<div class="box-body">

							<c:if test="${currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)}">
								<c:if test="${currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
									<div class="col-lg-6">
								</c:if>
								<c:if test="${!currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
									<div class="col-lg-12">
								</c:if>
									<div id="calendar"></div>
									<div class="modal fade" id="eventModal" tabindex="-1"
										role="dialog" aria-labelledby="myModalLabel"></div>
								</div>
							</c:if>

							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
								<c:if test="${currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)}">
									<div class="col-lg-6">
								</c:if>
								<c:if test="${!currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)}">
									<div class="col-lg-12">
								</c:if>
									<div class="modal-header">
										<h3 class="modal-title">
											<spring:message code="patrol.schedule.new" />
										</h3>
									</div>
									<div
										class="<spring:bind path="locationKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<br>
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
										class="form-group <spring:bind path="selectedAccountKeyList">
											<c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-xs-6 control-label"><spring:message
												code="role.availableUsers" />*</label> <label
											class="col-xs-6 control-label"><spring:message
												code="role.assignedUsers" />*</label>
										<div class="col-xs-12 ">
											<form:select id="userAccountBox1"
												path="selectedAccountKeyList" size="3">
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
												<div class="row form-group <spring:bind path="selectedAccountKeyList">
											<c:if test="${status.error}">has-error</c:if></spring:bind>" id="selectedStaff">
													<!-- load assignSchedule/view_assign_selectStaff.jsp -->
												</div>
											</div>
									
									
									<div
										class="form-group <spring:bind path="description"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-4 control-label"><spring:message
												code="defect.description" />*</label>
										<div class="col-sm-8">
											<spring:message code="defect.description"
												var="defectDescription" />
											<form:textarea path="description" cssClass="form-control"
												placeholder="${defectDescription}" />
										</div>
									</div>
									<label class="col-sm-4 control-label"><spring:message
											code="defect.equipment" /></label>
									<div class="col-sm-8">
										<form:select path="equipmentKey" cssClass="form-control"
											id="equipment" items="${equipmentList}">
										</form:select>
									</div>
									<label class="col-sm-4 control-label"><spring:message
											code="defect.tools" /></label>
									<div class="col-sm-8">
										<form:select path="toolKey" cssClass="form-control"
											items="${toolList}">
										</form:select>
									</div>
									<div
										class="form-group <spring:bind path="startDate"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-4 control-label"><spring:message
												code="patrol.schedule.date.start" />*</label>
										<div class="col-sm-8"
											<spring:message code="patrol.schedule.date.start" var="from"/>>
											<form:input id="startDate" path="startDate"
												cssClass="form-control" placeholder="${from}" readonly="true"
												style="background-color: #fff;" />
										</div>
									</div>
									<div id='endDateDiv'
										class="form-group <spring:bind path="endDate"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-4 control-label"><spring:message
												code="patrol.schedule.date.end" /></label>
										<div class="col-sm-8"
											<spring:message code="patrol.schedule.date.end" var="to"/>>
											<form:input id="endDate" path="endDate"
												cssClass="form-control" placeholder="${to}" readonly="true"
												style="background-color: #fff;" />
										</div>
									</div>
									<div
										class="form-group <spring:bind path="frequency"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label class="col-sm-4 control-label"><spring:message
												code="patrol.schedule.frequency" />*</label>
										<div class="col-sm-8" style="padding: 8px 21px;">
											<form:radiobutton path="frequency" value="101" id="once" />
											<spring:message code="schedule.frequency.Once" />
											<form:radiobutton path="frequency" value="102" />
											<spring:message code="schedule.frequency.Daily" />
											<form:radiobutton path="frequency" value="103" id="weekly" />
											<spring:message code="schedule.frequency.Weekly" />
											<form:radiobutton path="frequency" value="104" />
											<spring:message code="schedule.frequency.Monthly" />
											<form:radiobutton path="frequency" value="105" />
											<spring:message code="schedule.frequency.Annually" />
										</div>
										<div id="weekDay"
											class="form-group <spring:bind path="weekDays"><c:if test="${status.error}">has-error</c:if></spring:bind>">
											<label class="col-sm-4 control-label"><spring:message
													code="schedule.frequency.Weekly" />*</label>
											<div class="col-sm-8" style="padding: 8px 21px;">
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
										<label class="col-sm-4 control-label"><spring:message
												code="patrol.schedule.time" /></label>
										<div class="col-sm-8"
											style="padding-left: 21px; padding-top: 6px;">
											<form:radiobutton id="fullDay" path="isFullDay" value="true" />
											<spring:message code="schedule.fullDay" />
										</div>
										<div class=" col-sm-offset-4 col-sm-8">
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
											<label class="col-sm-4 control-label"><spring:message
													code="defect.remarks" /></label>
											<div class="col-sm-8">
												<spring:message code="defect.remarks" var="defectRemarks" />
												<form:textarea path="remarks" cssClass="form-control"
													placeholder="${defectRemarks}" />
												<br>
											</div>
										</div>
									</div>
									<div class="modal-footer">
	
										<button onclick="showLoading()" type="submit" style=" float: left;"
											class="btn btn-success">
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
								style="margin-bottom: 0;margin-left: 0px;" onclick="addAllSelectedStaff();">
								<i class="fa fa-chevron-right"></i>
								<i class="fa fa-chevron-right"></i>
						</button>
				</div>
				<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<button type="button" class="btn btn-default btn-block" 
								style="margin-bottom: 0;margin-left: 0px;" onclick="dropAllSelectedStaff();">
								<i class="fa fa-chevron-left"></i>
								<i class="fa fa-chevron-left"></i>
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
					style="margin-bottom: 0;">
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

<script type="text/javascript">
	menu_select("maintenanceSchedule");
	$("#weekDay").hide();	
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
			var fullDate = $("#startDate").val();
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
		}).on("changeDate", function(ev) {
			var fullDate = $("#endDate").val();
				$("#startDate").datetimepicker("setEndDate", fullDate)
		}).on("show", function(ev){
			$(".table-condensed th").attr("style","visibility : visible");		
		});
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
	
	
	var lang = "${pageContext.response.locale}";
	
	if(lang == 'zh_HK')
		lang = 'zh-tw'
		else 
			lang = 'en'	

				$(window).load(function() {
					$('#calendar').fullCalendar(
							{
								events : function(start, end,
										timezone, callback) {
									
									showLoading();
											
									$.ajax({
												method : "POST",
												url : "GetDefectSchedule.do",
												dataType : "json",
												success : function(
														data) {
													
													
													
													callback(data);
													
												
													
													hideLoading();
												}
											})
								},
								eventRender : function(event,
										element, view) {

									
									var canGen = false;
									if (event.ranges
											.filter(function(range) {
												if (event.end == null)
													return false;													
												return (event.start
														.isBefore(range.end) && event.end
														.isAfter(range.start));
											}).length > 0) {
										canGen = true;
									}
									if (event.excludedDates != null) {
										event.excludedDates
												.filter(function(
														excludedDate) {
													if (event.start
															.isSameOrBefore(excludedDate.end)
															&& event.end
																	.isSameOrAfter(excludedDate.start)) {
														canGen = false;
													}
												});
									}
									return canGen;
								},
								eventClick : function(calEvent,
										jsEvent, view) {
					
									var editCurrentUrl = "ModifyCurrentSchedule.do?key=" + calEvent.id + "&currentStart=" + calEvent.start
                                    .format() + "&currentEnd=" + calEvent.end
                                    .format();
									
									var editSeriesUrl = "ModifySeriesSchedule.do?key=" + calEvent.id + "&currentStart=" + calEvent.start
                                    .format() + "&currentEnd=" + calEvent.end
                                    .format();
									
									$("#editCurrentUrl").attr("href",editCurrentUrl);
									$("#editSeriesUrl").attr("href",editSeriesUrl);
									$('#modifyModal').modal('show')
									
									/*$.ajax({
												url : "EditSchedule.do?key=" + calEvent.id,
												dataType : "html",
												success : function(data) {
													window.location.href = "EditSchedule.do?key=" + calEvent.id;
												}
									})*/
								},
								dayClick : function(date, jsEvent,
										view) {
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
									month : {
													eventLimit : 2
									}
								},
											eventLimitClick :  "popover"

										});
					
					$(".fc-toolbar .fc-left").attr("style",
					"cursor:pointer");

			$(".fc-toolbar .fc-left")
					.append(
							"<input id='hidden_datepicker' type='button' style='visibility: hidden; position:absolute;'/>")

			$("#hidden_datepicker").datetimepicker({
				bootcssVer : 3,
				format : 'yyyy-mm-dd',
				minView : 3,
				startView : 3,
				autoclose : true,
			}).on("show", function(ev) {
				//$(".table-condensed th").attr("style","visibility : visible");

			}).on(
					"changeDate",
					function(ev) {

						$('#calendar').fullCalendar(
								'gotoDate',
								moment(ev.date.valueOf()).format(
										"YYYY-MM-DD"));
					});

			$(".fc-toolbar .fc-left h2").click(
					function() {

						var topVar = $(".fc-toolbar .fc-left")
								.position().top;
						var leftVar = $(".fc-toolbar .fc-left")
								.position().left;

						$("#hidden_datepicker").css('top', topVar);
						$("#hidden_datepicker")
								.css('left', leftVar);

						$("#hidden_datepicker").datetimepicker(
								'show');

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
	
	
</script>


