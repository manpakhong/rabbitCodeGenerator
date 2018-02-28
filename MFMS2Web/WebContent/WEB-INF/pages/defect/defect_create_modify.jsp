<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<script src="import/customLoading.js"></script>
<jsp:include page="../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">
<script src="import/datetimepicker/moment.js"></script>
<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>
<script src="import/datetimepicker/date.js"></script>

<input id="p1" value="${p1}" type="hidden" />
<input id="p2" value="${p2}" type="hidden" />
<input id="p3" value="${p3}" type="hidden" />
<input id="p4" value="${p4}" type="hidden" />
<input id="photoPath" value="${photoPath}" type="hidden" />
<input id="videoPath" value="${videoPath}" type="hidden" />

<style>
.table td:first-child {
	text-align: center;
	color: #4491bf;
	cursor: pointer;
}
</style>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyDefect')"
	var="modifyDefect" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeDefect')"
	var="removeDefect" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchDefect')"
	var="searchDefect" />

<style>
video {
	max-width: 100%;
	height: auto;
}

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
	<c:when test="${defectForm.readOnly}">
		<c:set var="dateBackgroundColour" value="" />
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${defectForm.delete == true}">
			<c:set var="submitUrl" value="DoDeleteDefect.do" />
		</c:if>
		<c:if test="${defectForm.delete == false}">
			<c:set var="submitUrl" value="ModifyDefect.do" />
		</c:if>
		<c:set var="disabled" value="disabled" />
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="disabled" value="" />
		<c:set var="dateBackgroundColour" value="background-color: #fff;" />
		<c:choose>
			<c:when test="${empty defectForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyDefect.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyDefect.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<script>
		
		var inprogressTime = "";
		var completeTime = "";
		
		$(document).ready(function() {
		$('#fileTable').load("ShowFileTable.do");
		$('.locationtree ul').hide();
		$('.locationtree li').hide();
		$('.locationtree li:first').show();
		if(!${readOnly}){
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
			});*/
			
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
			
			//function determindOnclick(e, )
		}
	});
</script>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="DefectManagement.do"><i
					class="icon-icon_defect_management_b"></i> <spring:message
						code="menu.defectMgt" /></a></li>
			<li><a href="Defect.do"> <spring:message
						code="menu.defectMgt.defect" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<li class="active"><spring:message
							code="menu.defectMgt.defect.view" /></li>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.defectMgt.defect.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.defectMgt.defect.modify" /></li>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form commandName="defectForm" method="post"
						enctype="multipart/form-data" action="${submitUrl}">
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>

						<div style="display: none" id="errorMsg"
							class="form-group alert alert-success">
							<div class="inline">
								<spring:message code="defect.fileName" />
								<label id="fileName2"></label>
								<spring:message code="has.been.deleted" />
							</div>
						</div>

						<c:if test="${defectForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.create.success" />
							</div>
						</c:if>
						<c:if test="${defectForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.modify.success" />
							</div>
						</c:if>
						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<div class="box-body">
							<div
								class="<spring:bind path="locationKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.location" />*</label>
								<div class="col-lg-9">
									<div class="panel panel-default">
										<div class="panel-heading">
											<label class="control-label"> <spring:message
													code="defect.location.select" />
											</label> <label id="locationName" class="control-label"
												style="word-wrap: break-word;"><c:out
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
												node="${defectForm.availableLocationTree}" />
										</ul>
									</div>
								</div>
							</div>
							<div
								class="<spring:bind path="code"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.code" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.code" var="defectCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${defectCode}" readonly="true" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-3 control-label"><spring:message
										code="defect.status" /></label>
								<div class="col-lg-3">
									<form:input path="orgStatusID" type="hidden" />
									<form:select path="statusID" cssClass="form-control"
										id="statusID" items="${statusList}" disabled="${readOnly}">
									</form:select>
								</div>
							</div>
							<div
								class="form-group <spring:bind path="failureClassKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.failureClass" />*</label>
								<div class="col-lg-9">
									<form:select path="failureClassKey" cssClass="form-control"
										id="failureClass" items="${failureClassList}"
										disabled="${readOnly}">
									</form:select>
								</div>
							</div>
							<div
								class="form-group <spring:bind path="problemCodeKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.problemCode" />*</label>
								<div class="col-lg-9">
									<form:select path="problemCodeKey" cssClass="form-control"
										id="problemCode" items="${problemCodeList}"
										disabled="${readOnly}">
										<form:option value="">
											<spring:message code="common.pleaseSelect" />
										</form:option>
									</form:select>
								</div>
							</div>
							<div
								class="form-group <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.description" />*</label>
								<div class="col-lg-9">
									<spring:message code="defect.description"
										var="defectDescription" />
									<form:textarea path="desc" cssClass="form-control"
										id="description" placeholder="${defectDescription}"
										readonly="${readOnly}" />
								</div>
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.causeCode" /></label>
							<div class="col-lg-9">
								<form:select path="causeCodeKey" cssClass="form-control"
									items="${causeCodeList}" disabled="${readOnly}">
								</form:select>
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.equipment" /></label>
							<div class="col-lg-9">
								<form:select path="equipmentKey" cssClass="form-control"
									id="equipment" items="${equipmentList}" disabled="${readOnly}">
								</form:select>
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.tools" /></label>
							<div class="col-lg-9">
								<form:select path="toolKey" cssClass="form-control"
									items="${toolList}" disabled="${readOnly}">
								</form:select>
							</div>
							<div>
								<label class="col-lg-3 control-label"><spring:message
										code="defect.priority.1" /></label>
								<div class="col-lg-3">
									<form:select path="priority" cssClass="form-control"
										id="priority" items="${priorityList}" disabled="${readOnly}">
									</form:select>
								</div>
								<c:choose>				
							  	<c:when test="${readOnly == false}">
									<div class="col-lg-6"> <label class="control-label" style="margin-top: 5px; margin-bottom: 10px; font-style: italic; color:black; font-size:15px">
										<strong><spring:message code="defect.response.time.priority" />${priorityResponseTime}</strong></label>
									</div>
							  	</c:when>
							  	<c:otherwise>
							 		<div class="col-lg-6" style="margin-bottom:6px;"></div>
							  	</c:otherwise>
								</c:choose>
							</div>

							<div class="<spring:bind path="assignedGroupKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.assignedGroup" />*</label>
								<div class="col-lg-3">
									<form:select path="assignedGroupKey" cssClass="form-control"
										id="accountGroup" items="${accountGroupList}"
										disabled="${readOnly}">
									</form:select>
								</div>
							</div>
							<div class="<spring:bind path="assignedAccountKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.assignedAccount" /></label>
								<div class="col-lg-3">
									<form:select path="assignedAccountKey" cssClass="form-control"
										id="account" items="${accountList}" disabled="${readOnly}">
										<form:option value="">
											<spring:message code="common.pleaseSelect" />
										</form:option>
									</form:select>
								</div>
							</div>
							
							<div
								class="form-group <spring:bind path="contactName"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.contactName" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.contactName"
										var="defectContactName" />
									<form:input path="contactName" cssClass="form-control"
										placeholder="${defectContactName}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group <spring:bind path="contactTel"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.contactTel" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.contactTel" var="defectContactTel" />
									<form:input path="contactTel" cssClass="form-control"
										placeholder="${defectContactTel}" readonly="${readOnly}"
										onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 43 || event.charCode == 44 || event.charCode == 45' />
								</div>
							</div>
							<div
								class="<spring:bind path="contactEmail"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.contactEmail" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.contactEmail"
										var="defectContactEmail" />
									<form:input path="contactEmail" cssClass="form-control"
										placeholder="${defectContactEmail}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="<spring:bind path="emergencyTel"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-lg-3 control-label"><spring:message
										code="defect.emergencyTel" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.emergencyTel"
										var="defectEmergencyTel" />
									<form:input path="emergencyTel" cssClass="form-control"
										placeholder="${defectEmergencyTel}" readonly="${readOnly}"
										onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 43 || event.charCode == 44 || event.charCode == 45' />
								</div>
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.callFrom" /></label>
							<div class="col-lg-3">
								<form:select path="callFrom" cssClass="form-control"
									disabled="${readOnly}">
									<c:forEach items="${callFromList}" var="callFromList">
										<form:option value="${callFromList.key}">
											<spring:message code="${callFromList.value}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							<label for="" class="col-lg-3 control-label"><spring:message
									code="defect.reportDate" /></label>
							<div class="col-lg-3">
								<spring:message code="defect.reportDate" var="defectReportDate" />
								<form:input id="reportDate" path="reportDateTime"
									cssClass="form-control" placeholder="${defectReportDate}"
									disabled="${readOnly}" readonly="true"
									style="${dateBackgroundColour}" />
							</div>
							<div
								class="form-group <spring:bind path="targetStartDateTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.targetStartDate" />*</label>
								<div class="col-lg-3">
									<spring:message code="defect.targetStartDate"
										var="defectTargetStartDate" />
									<form:input path="targetStartDateTime" cssClass="form-control"
										placeholder="${defectTargetStartDate}" id="targetStartDate"
										disabled="${readOnly}" readonly="true"
										style="${dateBackgroundColour}" />
								</div>
							</div>
							<label for="" class="col-lg-3 control-label"><spring:message
									code="defect.targetFinishDate" /></label>
							<div class="col-lg-3">
								<spring:message code="defect.targetFinishDate"
									var="defectTargetFinishDate" />
								<form:input id="targetFinishDate" path="targetFinishDateTime"
									cssClass="form-control" placeholder="${defectTargetFinishDate}"
									disabled="${readOnly}" readonly="true"
									style="${dateBackgroundColour}" />
							</div>
							<div
								class="form-group <spring:bind path="actualStartDateTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.actualStartDate" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.actualStartDate"
										var="defectActualStartDate" />
									<form:input id="actualStartDate" path="actualStartDateTime"
										cssClass="form-control" placeholder="${defectActualStartDate}"
										disabled="${readOnly}" readonly="true"
										style="${dateBackgroundColour}" />
								</div>
							</div>
							<script>
							inprogressTime = "${defectForm.actualStartDateTime}";
							completeTime = "${defectForm.actualFinishDateTime}";
							</script>

							<label for="" class="col-lg-3 control-label"><spring:message
									code="defect.actualFinishDate" /></label>
							<div class="col-lg-3">
								<spring:message code="defect.actualFinishDate"
									var="defectActualFinishDate" />
								<form:input id="actualFinishDate" path="actualFinishDateTime"
									cssClass="form-control" placeholder="${defectActualFinishDate}"
									disabled="${readOnly}" readonly="true"
									style="${dateBackgroundColour}" />
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.checkBy" /></label>
							<div class="col-lg-3">
								<form:select path="checkBy" cssClass="form-control" id="checkBy"
									items="${checkByList}" disabled="${readOnly}">
									<form:option value="">
										<spring:message code="common.pleaseSelect" />
									</form:option>
								</form:select>
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.checkedDate" /></label>
							<div class="col-lg-3">
								<spring:message code="defect.checkedDate"
									var="defectCheckedDate" />
								<form:input id="checkedDate" path="checkDateTime"
									cssClass="form-control" placeholder="${defectCheckedDate}"
									disabled="${readOnly}" readonly="true"
									style="${dateBackgroundColour}" />
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.issueBy" /></label>
							<div class="col-lg-3">
								<form:select path="issueBy" cssClass="form-control" id="issueBy"
									items="${issueByList}" disabled="${readOnly}">
									<form:option value="">
										<spring:message code="common.pleaseSelect" />
									</form:option>
								</form:select>
							</div>
							<label class="col-lg-3 control-label"><spring:message
									code="defect.issueDate" /></label>
							<div class="col-lg-3">
								<spring:message code="defect.issueDate" var="defectIssueDate" />
								<form:input id="issuedDate" path="issueDateTime"
									cssClass="form-control" placeholder="${defectIssueDate}"
									disabled="${readOnly}" readonly="true"
									style="${dateBackgroundColour}" />
							</div>
							<div
								class="<spring:bind path="remarks"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-lg-3 control-label"><spring:message
										code="defect.remarks" /></label>
								<div class="col-lg-9">
									<spring:message code="defect.remarks" var="defectRemarks" />
									<form:input path="remarks" cssClass="form-control"
										placeholder="${defectRemarks}" readonly="${readOnly}" />
								</div>
							</div>

							<div
								class="<spring:bind path="key"><c:if test="${status.error}">has-error</c:if></spring:bind>">


								<c:if test="${!readOnly}">
									<c:if test="${defectForm.photoRemain>0}">
										<div class="form-group">
											<label class="col-lg-3 control-label" var="${defectForm.photoRemain}"><spring:message
													code="defect.photo" /> 1 </label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<form:input type="file" path="picture1.file"
													id="picture1.file" data-buttonName="btn-primary"
													data-placeholder="${photo} 1" accept="image/*"
													class="form-control filestyle" />
											</div>
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 1 <spring:message
													code="defect.description" /></label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<spring:message code="defect.description" var="desc" />
												<form:input path="picture1.desc" id="picture1.desc"
													placeholder="${photo} 1 ${desc}" cssClass="form-control" />
											</div>
										</div>
										<div class="row"></div>
									</c:if>
									<c:if test="${defectForm.photoRemain>1}">
										<div class="form-group">
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 2 </label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<form:input type="file" path="picture2.file"
													id="picture2.file" data-buttonName="btn-primary"
													data-placeholder="${photo} 2" accept="image/*"
													class="form-control filestyle" />
											</div>
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 2 <spring:message
													code="defect.description" /></label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<spring:message code="defect.description" var="desc" />
												<form:input path="picture2.desc" id="picture2.desc"
													placeholder="${photo} 2 ${desc}" cssClass="form-control" />
											</div>
										</div>
										<div class="row"></div>
									</c:if>
									<c:if test="${defectForm.photoRemain>2}">
										<div class="form-group">
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 3 </label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<form:input type="file" path="picture3.file"
													id="picture3.file" data-buttonName="btn-primary"
													data-placeholder="${photo} 3" accept="image/*"
													class="form-control filestyle" />
											</div>
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 3 <spring:message
													code="defect.description" /></label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<spring:message code="defect.description" var="desc" />
												<form:input path="picture3.desc" id="picture3.desc"
													placeholder="${photo} 3 ${desc}" cssClass="form-control" />
											</div>
										</div>
										<div class="row"></div>
									</c:if>
									<c:if test="${defectForm.photoRemain>3}">
										<div class="form-group">
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 4 </label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<form:input type="file" path="picture4.file"
													id="picture4.file" data-buttonName="btn-primary"
													data-placeholder="${photo} 4" accept="image/*"
													class="form-control filestyle" />
											</div>
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 4 <spring:message
													code="defect.description" /></label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<spring:message code="defect.description" var="desc" />
												<form:input path="picture4.desc" id="picture4.desc"
													placeholder="${photo} 4 ${desc}" cssClass="form-control" />
											</div>
										</div>
										<div class="row"></div>
									</c:if>
									<c:if test="${defectForm.photoRemain>4}">
										<div class="form-group">
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 5 </label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<form:input type="file" path="picture5.file"
													id="picture5.file" data-buttonName="btn-primary"
													data-placeholder="${photo} 5" accept="image/*"
													class="form-control filestyle" />
											</div>
											<label class="col-lg-3 control-label"><spring:message
													code="defect.photo" /> 5 <spring:message
													code="defect.description" /></label>
											<div class="col-lg-3">
												<spring:message code="defect.photo" var="photo" />
												<spring:message code="defect.description" var="desc" />
												<form:input path="picture5.desc" id="picture5.desc"
													placeholder="${photo} 5 ${desc}" cssClass="form-control" />
											</div>
										</div>
										<div class="row"></div>
									</c:if>
									<c:if test="${defectForm.videoRemain>0}">
										<div class="form-group">
											<label class="col-lg-3 control-label"><spring:message
													code="defect.video" /> </label>
											<div class="col-lg-3">
												<spring:message code="defect.video" var="video" />
												<form:input type="file" path="video.file" id="video.file"
													data-buttonName="btn-primary" data-placeholder="${video}"
													accept="video/*" class="form-control filestyle" />
											</div>
											<label class="col-lg-3 control-label"><spring:message
													code="defect.video" /> <spring:message
													code="defect.description" /></label>
											<div class="col-lg-3">
												<spring:message code="defect.video" var="video" />
												<spring:message code="defect.description" var="desc" />
												<form:input path="video.desc" id="video.desc"
													placeholder="${video} ${desc}" cssClass="form-control" />
											</div>
										</div>
										<div class="row"></div>
									</c:if>
								</c:if>
							</div>
							<div class="form-group">
								<label class="col-lg-3 control-label"><spring:message
										code="defect.lastModifyBy" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.lastModifyBy" var="lastModifyBy" />
									<form:input path="lastModifyBy" cssClass="form-control"
										placeholder="${lastModifyBy}" readonly="true" />
								</div>
								<label class="col-lg-3 control-label"><spring:message
										code="defect.lastModifyDateTime" /></label>
								<div class="col-lg-3">
									<spring:message code="defect.lastModifyDateTime"
										var="lastModifyDateTime" />
									<form:input path="lastModifyDateTime" cssClass="form-control"
										placeholder="${lastModifyDateTime}" readonly="true" />
								</div>
								<c:if test="${isCreate == false }">

									<div id="fileTable"></div>

								</c:if>
							</div>
							<div class="row"></div>
							<div class="box-footer">
								<div class="col-lg-1 col-xs-2 ">
									<c:if
										test="${currRole.grantedPrivilegeCodeList.contains(searchDefect)}">

										<c:if test="${readOnly == false && isCreate == false}">
											<c:choose>
												<c:when test="${defectForm.referrer == 's'}">
													<a class="btn btn-primary " href="DoSearchDefect.do"
														onclick="showLoading()"><spring:message
															code="button.back.to.search" /></a>
												</c:when>
												<c:otherwise>
													<!-- this view page will go to search -->
													<a class="btn btn-primary "
														href="ViewDefect.do?key=${defectForm.key}&r=s"
														onclick="showLoading()"><spring:message
															code="button.back" /></a>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${readOnly == true}">
											<c:choose>
												<c:when test="${defectForm.referrer == 'c'}">
													<a class="btn btn-primary " href="SearchDefect.do"
														onclick="showLoading()"><spring:message
															code="button.back.to.search" /></a>
												</c:when>
												<c:otherwise>
													<a class="btn btn-primary " href="DoSearchDefect.do"
														onclick="showLoading()"><spring:message
															code="button.back.to.search" /></a>
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<c:if test="${readOnly == false}">
										<button type="submit" class="btn btn-primary "
											onclick="showLoading()">
											<spring:message code="button.submit" />
										</button>
									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 ">
									<c:if test="${readOnly == true}">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(modifyDefect)}">
											<button type="submit" class="btn btn-primary "
												onclick="showLoading()">
												<spring:message code="button.modify" />
											</button>
										</c:if>
									</c:if>
									<c:if test="${readOnly == false}">
										<c:if test="${isCreate == true}">
											<a href="CreateDefect.do" class="btn btn-primary "
												onclick="showLoading()"><spring:message
													code="button.reset" /></a>
										</c:if>
										<c:if test="${isCreate == false}">
											<a onclick="showLoading()"
												href="<c:url value="ModifyDefect.do" ><c:param name="key" value="${defectForm.key}"/></c:url>"
												class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
									</c:if>
								</div>
							</div>
							<!-- /.box-footer -->
						</div>
						<!-- /.box-body -->
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<div id="displayPhotoModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<p id="fileName"></p>
				</h4>
			</div>
			<div class="modal-body">
				<img class="img-responsive" id="filePath">
			</div>
		</div>

	</div>
</div>

<div id="displayVideoModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<p id="videoName"></p>
				</h4>
			</div>
			<div class="modal-body">
				<video id='videoPlayer' width="560" controls="controls">
					<source id='mp4Source' src="movie.mp4" type="video/mp4" />
					<!--  <source id='oggSource' src="movie.ogg" type="video/ogg" />-->
				</video>
				
			
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
					<spring:message code="file.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="file.confirm.delete.message" />
				</p>
				<br />
				<p id="confirmName"></p>
				<input id="confirmPath" type="hidden" /> <input id="fileType"
					type="hidden" />
			</div>
			<div class="modal-footer">
				<button class="btn btn-danger btn-ok"
					onclick="confirmDeleteFile($('#confirmPath').val(), $('#fileType').val())"
					style="margin-bottom: 0px">
					<spring:message code="button.delete" />
					</a>
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						style="margin-bottom: 0px">
						<spring:message code="button.close" />
					</button>
			</div>
		</div>

	</div>
</div>

<!-- Confirm Modal -->
<div id="confirmModifyFileDescriptionModal" class="modal fade"
	role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<spring:message code="file.confirm.modify.file.description" />
				</h4>
			</div>
			<div class="modal-body">
				<div style="display: none" id="fileDescTooLong"
					class="form-group alert alert-danger">

					<spring:eval
						expression="@propertyConfigurer.getProperty('defect.desc.length.max')"
						var="maxDescLen" />

					<spring:message code="defect.fileDesc.exceeded"
						arguments="${maxDescLen}" />

				</div>
				<div class="row">
					<label class="control-label col-lg-6"> <spring:message
							code="defect.fileDesc" />
					</label>
					<spring:message code="defect.fileDesc" var="fileDesc" />
					<input class="col-lg-6" id="modifyFileDesc"
						placeholder=" ${fileDesc}" />
				</div>
				<input id="confirmPath" type="hidden" /> <input id="fileType"
					type="hidden" />
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary btn-ok"
					onclick="modifyFileDescription($('#confirmPath').val(), $('#fileType').val(), $('#modifyFileDesc').val(), ${maxDescLen} )"
					style="margin-bottom: 0px">
					<spring:message code="button.modify" />
					</a>
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						style="margin-bottom: 0px">
						<spring:message code="button.close" />
					</button>
			</div>
		</div>

	</div>
</div>

<!-- Warn Modal -->
<div class="modal fade" id="warnModal" tabindex="-1" role="dialog"
	aria-labelledby="warnModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="warnModalLabel">
					<spring:message code="defect.search.warn.modalTitle" />
				</h4>
			</div>
			<div class="modal-body" id="warnModalBody"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary"
					style="margin-bottom: 0px;" data-dismiss="modal">
					<spring:message code="button.ok" />
				</button>
			</div>
		</div>
	</div>
</div>

<script>

	function showModifyFileDescription(name, path, type, currentDesc) {
		$('#confirmName').text(name);
		$('#confirmPath').val(path);
		$('#fileType').val(type);
		
		$("#modifyFileDesc").val(currentDesc);
		
		$('#confirmModifyFileDescriptionModal').modal('show');
	
	}
	
	function showDeleteFile(name, path, type) {
		$('#confirmName').text(name);
		$('#confirmPath').val(path);
		$('#fileType').val(type);
		$('#confirmDeleteModal').modal('show');
	
	}
	
	
	function confirmDeleteFile(path, type) {
		
		showLoading();
		
		$.ajax({
			url:'DoDeleteFile.do',
			data : "path=" + path + "&type=" + type,
			type : "GET",
			success:function(data){
				$('#confirmDeleteModal').modal('hide');
				$('#fileTable').load("ShowFileTable.do");
				hideLoading();
				
				$('#fileName2').text(path);
				$("#errorMsg").show();
			}
		});
	}
	
	function modifyFileDescription(path, type, desc, maxDescLen) {
		if(desc.length <= maxDescLen ){
		showLoading();
			$.ajax({
				url:'DoModifyFileDescription.do',
				data : "path=" + path + "&type=" + type + "&desc=" + desc,
				type : "GET",
				success:function(data){
					$('#confirmModifyFileDescriptionModal').modal('hide');
					$('#fileTable').load("ShowFileTable.do");
					hideLoading();
					$("#fileDescTooLong").hide();
					$("#modifyFileDesc").val('');
				}
			});
			
		} else {
			$("#fileDescTooLong").show();
		}
	}
	
	$('#displayPhotoModal').on(
			'show.bs.modal',
			function(e) {
				var filePath = $(e.relatedTarget).data('path');
				
			    // config at /etc/tomcat/server.xml or /Tomcat v7.0 Server at localhost-config\server.xml
				$('#filePath').attr('src', $('#photoPath').val() + filePath);
			    //$('#filePath').attr('src', '/MFMS2Web/photo/' + filePath);
				
				var fileDesc = $(e.relatedTarget).data('name');
				$('#fileName').text(fileDesc);
			});
	
	$('#displayVideoModal').on('show.bs.modal',
			function(e) {
				  var filePath = $(e.relatedTarget).data('path');
				
				  var player = document.getElementById('videoPlayer');
			      var mp4Vid = document.getElementById('mp4Source');
					
			      player.pause();
    
			      // config at /etc/tomcat/server.xml or /Tomcat v7.0 Server at localhost-config\server.xml
			      mp4Vid.src = $('#videoPath').val() + filePath;

			      player.load();

				var fileDesc = $(e.relatedTarget).data('name');
				$('#videoName').text(fileDesc);
				
				
			});
	
	
</script>

<script src="import/datatables/jquery.dataTables.js"></script>
<script src="import/datatables/dataTables.bootstrap.js"></script>
<script>
	$(function() {
		$('#example1').DataTable({
			"paging" : false,
			"lengthChange" : false,
			"searching" : false,
			 "order": [],
			"info" : false,
			"autoWidth" : false,
			"language" : {
				"paginate" : {
					"previous" : $('#previous').val(),
					"next" : $('#next').val()
				}
			},
			"aoColumnDefs": [
			                 { 'bSortable': false, 'aTargets': [ 0 ] }
			              ]
		});
	});
</script>

<script type="text/javascript">
var desc = [];
	$(document).ready(function () {
		
		$('#reportDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:00',
			clearBtn:  1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			forceParse : 0,
			showMeridian : 0
		});
		
		$('#targetStartDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:00',
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev){
			var fullDate = $("#targetStartDate").val()
			$("#targetFinishDate").datetimepicker("setStartDate",fullDate);
		});
		
		$('#targetFinishDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:00',
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev){
			var fullDate = $("#targetFinishDate").val()
			$("#targetStartDate").datetimepicker("setEndDate",fullDate);

			var targetStartDate = timeToLong($('#targetStartDate').val());
			var targetFinishDate = timeToLong($('#targetFinishDate').val());
			
			if(targetStartDate > targetFinishDate){
				$('#targetStartDate').val(getTimeString(0, $('#targetFinishDate').val()));
				 var newDate = $("#targetStartDate").val()
					$("#targetFinishDate").datetimepicker("setStartDate",newDate);
			} 
				
		});
		
		$('#actualStartDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:00',
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev){
			var fullDate = $("#actualStartDate").val()
			$("#actualFinishDate").datetimepicker("setStartDate",fullDate);
		});
		
		
		$('#actualFinishDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:00',
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		}).on("changeDate", function(ev){
			var fullDate = $("#actualFinishDate").val()
			$("#actualStartDate").datetimepicker("setEndDate",fullDate);
			
			var actualStartDate = timeToLong($('#actualStartDate').val());
			var actualFinishDate = timeToLong($('#actualFinishDate').val());
			
			if(actualStartDate > actualFinishDate){
				$('#actualStartDate').val(getTimeString(0, $('#actualFinishDate').val()));
				 var newDate = $("#actualStartDate").val()
					$("#actualFinishDate").datetimepicker("setStartDate",newDate);
			} 

		});

			
		$('#checkedDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:00',
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		});
		$('#issuedDate').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:00',
			clearBtn:  1,
			todayBtn : 1,
			autoclose : true,
			startView : 2
		});
	});

	$(document).ready(
					function() {
						
						    if($("#targetStartDate").val().length>0)
							$("#targetFinishDate").datetimepicker("setStartDate",$("#targetStartDate").val());
							
							if($("#targetFinishDate").val().length>0)
							$("#targetStartDate").datetimepicker("setEndDate",$("#targetFinishDate").val());
							
							 if($("#actualStartDate").val().length>0)
							$("#actualFinishDate").datetimepicker("setStartDate",$("#actualStartDate").val());
							
							if($("#actualFinishDate").val().length>0)
							$("#actualStartDate").datetimepicker("setEndDate",$("#actualFinishDate").val());
						
						
						/*$('#issueBy').change(	
								function() {
									if($('#issueBy').val() > 0)
									$('#issuedDate').val(getNowTimeString());
								else
									$('#issuedDate').val("");
									
								}
						);*/
						
						
						
						$('#statusID').change(
	
							function() {	
								if($("#statusID").val() == 'N'){
									$("#actualStartDate").val(inprogressTime);
									$("#actualFinishDate").val(completeTime);
								} else if($("#statusID").val() == 'I'){
									if(inprogressTime=="")
										$("#actualStartDate").val(getNowTimeString());
									else
										$("#actualStartDate").val(inprogressTime);
									$("#actualFinishDate").val(completeTime);

									/*
									if($("#actualStartDate").val() =="" && inprogressTime=="")
										$("#actualStartDate").val(getNowTimeString());
									else{
										if(inprogressTime==""){
											$("#actualStartDate").val(getNowTimeString());
											
										}else{
											$("#actualStartDate").val(inprogressTime);
										}
									}
									*/
								} else if($("#statusID").val() == 'C'){
									$("#actualStartDate").val(inprogressTime);
									if(completeTime=="")
										$("#actualFinishDate").val(getNowTimeString());

									//if($("#actualStartDate").val() == ""){
									//	if(inprogressTime==""){
									//		$("#actualStartDate").val(getNowTimeString());
									//	}else{
									//		$("#actualStartDate").val(inprogressTime);
									//	}
									//}
									//$("#actualFinishDate").val(getNowTimeString());
								} else {
									$("#actualStartDate").val(inprogressTime);
									$("#actualFinishDate").val(completeTime);
								}
								
								/*
								if($("#statusID").val() != 'I' && $("#statusID").val() != 'C'){
									$("#actualStartDate").val('');
									$("#actualFinishDate").val('');
								}
								*/
								
								var startDate = $("#actualFinishDate").val()
								$("#actualStartDate").datetimepicker("setEndDate",startDate);
								
								var endDate = $("#actualStartDate").val()
								$("#actualFinishDate").datetimepicker("setStartDate",endDate);
							});
						
						
						$('#failureClass').change(
								
							function() {$.getJSON('getProblemCodeByFailureClassKey.do',
								{
									failureClassKey : $(this).val(),
									ajax : 'true'
								},
								function(data) {
									
									var html = '<option value=""><spring:message code="common.pleaseSelect" /></option>';
									var len = data.length;
									for (var i = 0; i < len; i++) {
										html += '<option value="' + data[i].key + '">'
												+ data[i].code + ' - ' + data[i].name
												+'</option>';
										desc[data[i].key] = data[i].desc;										
									}
								
									//html += '</option>';
									
									$('#problemCode').html(html);
									
								});
							});

						$('#accountGroup').change(
							function() {
								$.getJSON('getUserAccountByAccountGroup.do',
										{
											accountGroupKey : $(this).val(),
											ajax : 'true'
										},
										function(data) {
											$('#account').html("");
											var pleaseselect = '<option value=""><spring:message code="common.pleaseSelect" /></option>';
											 $('#account').append(pleaseselect);
											
											var len = data.length;
											for (var i = 0; i < len; i++) {
												 $('#account').append($("<option></option>").text(data[i].name).val(data[i].key));
											}											
										});
							});
						
						$('#problemCode').change(
					        function() {

								//console.log(desc);
								//$("#description").val($("#problemCode option:selected").text());					        	
					        	getDesc();
					        
					            $.getJSON(
					                    'getPriorityByProblemCode.do',
					                    {
					                        problemCodeKey : $(this).val(),
					                        ajax : 'true'
					                    },
					                    function(data) {
				                    	 	if(data >0){
						                        $("#priority").val(data);		
						                        if($('#targetStartDate').val() != ""){
							                        onChangePriority();
							                    }        
						                    }
					                     
					                        setDefaultAccountGroup();
					                    });
					            });
						
						$('#priority').change(
								function() {
									var empty = "";
									switch ($(this).val()) {
									case '1':
										if($('#targetStartDate').val() == empty){
											/*$('#targetStartDate').val(getTimeString(0, ""));
											$('#targetFinishDate').val(getTimeString(1, ""));*/
										} else {
											$('#targetFinishDate').val(getTimeString(1, $('#targetStartDate').val()));
										}
										break;
									case '2':
										if($('#targetStartDate').val() == empty){
											/*$('#targetStartDate').val(getTimeString(0, ""));
											$('#targetFinishDate').val(getTimeString(2, ""));*/
										} else {
											$('#targetFinishDate').val(getTimeString(2, $('#targetStartDate').val()));
										}
										break;
									case '3':
										if($('#targetStartDate').val() == empty){
											/*$('#targetStartDate').val(getTimeString(0, ""));
											$('#targetFinishDate').val(getTimeString(3, ""));*/
										} else {
											$('#targetFinishDate').val(getTimeString(3, $('#targetStartDate').val()));
										}
										break;
									case '4':
										if($('#targetStartDate').val() == empty){
											/*$('#targetStartDate').val(getTimeString(0, ""));
											$('#targetFinishDate').val(getTimeString(4, ""));*/
										} else {
											$('#targetFinishDate').val(getTimeString(4, $('#targetStartDate').val()));
										}
										break;
									}
								});
						
						$('#targetStartDate').change(
								function() {
									
									var priority = $('#priority').val()
									if(priority>0){
										switch (priority) {
										case "1":
											$('#targetFinishDate').val(getTimeString(1, $('#targetStartDate').val()));
											break;
										case "2":

											$('#targetFinishDate').val(getTimeString(2, $('#targetStartDate').val()));
											break;
										case "3":
											$('#targetFinishDate').val(getTimeString(3, $('#targetStartDate').val()));
											break;
										case "4":
											$('#targetFinishDate').val(getTimeString(4, $('#targetStartDate').val()));
											break;
										}
									}
								});
						});
	if(document.getElementById("video.file")!=null){
	document.getElementById("video.file").addEventListener("change", warnModal);
	}
	
	function warnModal() {
		var file = document.getElementById('video.file').files[0];
		var fileSize = file.size;
		$.ajax({
			method : "POST",
			url : "WarnSize.do",
			data: {size : fileSize},
			success : function(data) {			
				if(data == "success"){
					console.log("do nothing");					
				}else{
					$('#warnModal').modal('show');
					$('#warnModalBody').text(data);
					document.getElementById('video.file').form.reset();
					console.log(data);

				}
				
				//hideLoading();
				
			}
		});
	}
	
	function getDesc(){
		var problemKey = $("#problemCode").val();
    	console.log("Problem Key : " + problemKey);
    	var desc = null;
    	$.ajax({
			method : "POST",
			url : "ProblemCodeDescription.do",
			data: {key : problemKey},
			success : function(data) {	
					console.log("description : " + data);
					$("#description").val(data);
				}							
			
		});
	}
						function getNowTimeString() {
							var ndt = Date.parse('now');

							var y = ndt.getFullYear();
							var M = ndt.getMonth() + 1;
							var d = ndt.getDate();
							var h = ndt.getHours();
							var m = ndt.getMinutes();
							
							M = M > 9 ? M : "0" + M;
							d = d > 9 ? d : "0" + d;
							h = h > 9 ? h : "0" + h;
							m = m > 9 ? m : "0" + m;
							var time = y + "-" + M + "-" + d + " " + h + ":" + m + ":00";
					
							
							return time;
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
							case 1:
								ndt.setTime(dt.getTime() + ($('#p1').val() * 60 * 60 * 1000));
								break;
							case 2:
								ndt.setTime(dt.getTime() + ($('#p2').val() * 60 * 60 * 1000));
								break;
							case 3:
								ndt.setTime(dt.getTime() +  ($('#p3').val() * 60 * 60 * 1000));
								break;
							case 4:
								ndt.setTime(dt.getTime() +  ($('#p4').val() * 60 * 60 * 1000));
								break;
							}
							var y = ndt.getFullYear();
							var M = ndt.getMonth() + 1;
							var d = ndt.getDate();
							var h = ndt.getHours();
							var m = ndt.getMinutes();
							
							M = M > 9 ? M : "0" + M;
							d = d > 9 ? d : "0" + d;
							h = h > 9 ? h : "0" + h;
							m = m > 9 ? m : "0" + m;
							var time = y + "-" + M + "-" + d + " " + h + ":" + m + ":00";

							
							return time;
						}
						
						function timeToLong(dateString) {
							
							var dt = Date.parse('today');
							if( dateString != ""){
								dt = Date.parse(dateString);
								return dt.getTime();
							} else return 0;

						}
						
						
						function onChangePriority() {
							var priority = $('#priority').val()
							var empty = "";
							switch (priority) {
							case '1':
								if($('#targetStartDate').val() == empty){
									/*$('#targetStartDate').val(getTimeString(0, ""));
									$('#targetFinishDate').val(getTimeString(1, ""));*/
								} else {
									$('#targetFinishDate').val(getTimeString(1, $('#targetStartDate').val()));
								}
								break;
							case '2':
								if($('#targetStartDate').val() == empty){
									/*$('#targetStartDate').val(getTimeString(0, ""));
									$('#targetFinishDate').val(getTimeString(2, ""));*/
								} else {
									$('#targetFinishDate').val(getTimeString(2, $('#targetStartDate').val()));
								}
								break;
							case '3':
								if($('#targetStartDate').val() == empty){
									/*$('#targetStartDate').val(getTimeString(0, ""));
									$('#targetFinishDate').val(getTimeString(3, ""));*/
								} else {
									$('#targetFinishDate').val(getTimeString(3, $('#targetStartDate').val()));
								}
								break;
							case '4':
								if($('#targetStartDate').val() == empty){
									/*$('#targetStartDate').val(getTimeString(0, ""));
									$('#targetFinishDate').val(getTimeString(4, ""));*/
								} else {
									$('#targetFinishDate').val(getTimeString(4, $('#targetStartDate').val()));
								}
								break;
							}
						}
						
						function setDefaultAccountGroup() {
							$.getJSON('setDefaultAccountGroup.do',
								{	
									problemCodeKey : $('#problemCode').val(), 
									ajax : 'true'
								},
								function(data) {
									 var accountGroup = document.getElementById('accountGroup');
									 if(data>0){
									 accountGroup.value = data;
									 onChangeAccountGroup();
									 } else{
										 accountGroup.value = "";
										 onChangeAccountGroup();
									 }
								});
						}

						function setDefaultAccount() {
							$.getJSON('setDefaultAccount.do',
									{	
								problemCodeKey : $('#problemCode').val(), 
								ajax : 'true'
							},
							function(data) {
								 var account = document.getElementById('account');
								 if(data>0){
									 account.value = data;
								 } else{
									 account.value = null;
								 }
							});
						}
						
						function onChangeAccountGroup() {
							$.getJSON('getUserAccountByAccountGroup.do',
									{
										accountGroupKey : $('#accountGroup').val(), 
										ajax : 'true'
									},
									function(data) {
										$('#account').html("");
										var pleaseselect = '<option value=""><spring:message code="common.pleaseSelect" /></option>';
										 $('#account').append(pleaseselect);
										
										var len = data.length;
										for (var i = 0; i < len; i++) {
											 $('#account').append($("<option></option>").text(data[i].name).val(data[i].key));
										}
										setDefaultAccount();
									});
						}
						
						
						

	menu_toggle("sub_dm");
	menu_toggle("sub_dm_defect");
	
	if(${isCreate}){
		menu_select("createDefect");
	} else 
		menu_select("searchDefect");
	
</script>