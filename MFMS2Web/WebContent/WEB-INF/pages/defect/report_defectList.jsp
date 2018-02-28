<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<jsp:include page="../common/left.jsp" />
<script src="import/datetimepicker/date.js"></script>


<script src="import/jQuery/jquery.cookie.js"></script>

<script>
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
		});*/
		$('.locationtree li').on('click', function(e) {
			//console.log(e);
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

	function checkDownloadComplete() {
		
		//showLoading();
		var startDate = $("#from").val();
		var endDate = $("#to").val();
		var hasError = false;
		var errorStr = "";
		if (startDate == "" ||endDate == "") {	
			hasError = true;		
		}

		var token = "fileDownloadToken"; //use the current timestamp as the token value
		$('#download_token_value_id').val(token);
		fileDownloadCheckTimer = window.setInterval(function() {
 
			var cookieValue = $.cookie('fileDownloadToken');

			//alert('cookieValue ' + cookieValue + ', token ' + token);

			if (cookieValue == token) {

				window.clearInterval(fileDownloadCheckTimer);
				$.removeCookie('fileDownloadToken'); //clears this cookie value				
				hideLoading();
			}
		}, 500);
		
		if (hasError) {
			$("#errorMsgStr").html(errorStr);
			$("#errorMsg").show();
			return;
		}

	}

	function validation() {

		var check = false;

		if (typeof $('.validation:checked').val() !== "undefined")
			check = true;

		if ($('#code').val().length > 0)
			check = true;

		if ($('#priority').val().length > 0)
			check = true;

		if ($('#from').val().length > 0 && $('#to').val().length > 0)
			check = true;

		if ($('#failureClass').val().length > 0)
			check = true;

		if ($('#problemCode').val().length > 0)
			check = true;

		if ($('#causeCode').val().length > 0)
			check = true;

		if ($('#equipment').val().length > 0)
			check = true;

		if ($('#accountGroup').val().length > 0)
			check = true;

		if ($('#account').val().length > 0)
			check = true;

		if ($('#accountGroup').val().length > 0)
			check = true;

		if ($('#callFrom').val().length > 0)
			check = true;

		if ($('#status').val().length > 0)
			check = true;

		if (check) {
			$("#errorMsg").hide();
			$(".has-error").removeClass("has-error");
			$("#form").attr('target','_blank');
		}
		else{
			$("#form").attr('target','_self');
		}
		

	}
	//document.getElementById("form").reset();
</script>

<script src="import/datetimepicker/moment.js"></script>
<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="DefectManagement.do"><i
					class="icon-icon_defect_management_b"></i> <spring:message
						code="menu.defectMgt" /></a></li>
			<li><a href="DefectReport.do"> <spring:message
						code="menu.defectMgt.report" /></a></li>
			<li class="active"><spring:message
					code="menu.defectMgt.report.defectList" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form cssClass="form-horizontal" id="form" commandName="defectListForm"
						onsubmit="validation();" method="post" 
						action="DoCreateDefectList.do">
						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div id="errorMsg" class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<div class="box-body">
							<div
								class="form-group <spring:bind path="key"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<div
									class="<spring:bind path="locationKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-lg-3  control-label"><spring:message
											code="defect.location" /></label>
									<div class="col-lg-9">
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
													node="${defectListForm.availableLocationTree}" />
											</ul>
											<script>
											$("#locationKey1")[0].click();
											</script>
										</div>
									</div>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.code" /></label>
								<div class="col-lg-3"
									<spring:message code="defect.code" var="code"/>>
									<form:input path="code" cssClass="form-control" id="code"
										placeholder="${code}" />
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.priority.1" /></label>
								<div class="col-lg-3">
									<form:select path="priority" cssClass="form-control"
										id="priority" items="${priorityList}">
									</form:select>
								</div>

								<div
									class="form-group <spring:bind path="from"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label class="col-lg-3  control-label"><spring:message
											code="common.issue.from" /></label>
									<div class="col-lg-3"
										<spring:message code="common.issue.from" var="from"/>>
										<form:input id="from" path="from" cssClass="form-control"
											placeholder="${from}" readonly="true"
											style="background-color: #fff;" />
									</div>
									<label class="col-lg-3  control-label"><spring:message
											code="common.issue.to" /></label>
									<div class="col-lg-3"
										<spring:message code="common.issue.to" var="to"/>>
										<form:input id="to" path="to" cssClass="form-control"
											placeholder="${to}" readonly="true"
											style="background-color: #fff;" />
									</div>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.failureClass" /></label>
								<div class="col-lg-3">
									<form:select path="failureClassKey" cssClass="form-control"
										id="failureClass" items="${failureClassList}">
									</form:select>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.problemCode" /></label>
								<div class="col-lg-3">
									<form:select path="problemCodeKey" cssClass="form-control"
										id="problemCode" items="${problemCodeList}">
									</form:select>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.causeCode" /></label>
								<div class="col-lg-3">
									<form:select path="causeCodeKey" cssClass="form-control"
										id="causeCode" items="${causeCodeList}">
									</form:select>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.equipment" /></label>
								<div class="col-lg-3">
									<form:select path="equipmentKey" cssClass="form-control"
										id="equipment" items="${equipmentList}">
									</form:select>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.assignedGroup" /></label>
								<div class="col-lg-3">
									<form:select path="accountGroupKey" cssClass="form-control"
										id="accountGroup" items="${accountGroupList}">
									</form:select>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.assignedAccount" /></label>
								<div class="col-lg-3">
									<form:select path="assignedToKey" cssClass="form-control"
										id="account" items="${assignedToKeyList}">
									</form:select>
								</div>

								<label class="col-lg-3  control-label"><spring:message
										code="defect.callFrom" /></label>
								<div class="col-lg-3">
									<form:select path="callFrom" cssClass="form-control"
										id="callFrom" disabled="${readOnly}">
										<c:forEach items="${callFromList}" var="callFromList">
											<form:option value="${callFromList.key}">
												<spring:message code="${callFromList.value}" />
											</form:option>
										</c:forEach>
									</form:select>
								</div>
								<label class="col-lg-3  control-label"><spring:message
										code="defect.status" /></label>
								<div class="col-lg-3">
									<form:select path="status" cssClass="form-control" id="status"
										items="${statusList}">
									</form:select>
								</div>
							</div>

							<label class="col-lg-3  control-label"><spring:message
									code="common.sortBy" /></label>
							<div class="col-lg-3">
								<form:select path="sortBy" cssClass="form-control"
									disabled="${readOnly}">
									<c:forEach items="${sortByList}" var="sortByList">
										<form:option value="${sortByList.key}">
											<spring:message code="${sortByList.value}" />
										</form:option>
									</c:forEach>
								</form:select>
							</div>
							<label class="col-lg-3  control-label"><spring:message
									code="common.order" /></label>
							<div class="col-lg-3" style="padding: 8px 21px;">
								<form:radiobutton path="order" value="ASC" />
								<spring:message code="common.ascending" />
								<form:radiobutton path="order" value="DESC" />
								<spring:message code="common.descending" />
							</div>
							<label class="col-lg-3  control-label"><spring:message
									code="common.fileFormat" /></label>
							<div class="col-lg-3" style="padding: 8px 21px;">
								<form:radiobutton path="fileFormat" value="pdf" />
								PDF
								<!-- 
								<form:radiobutton path="fileFormat" value="xls" />
								Excel
								 -->
							</div>

							<div class="box-footer">
								<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">

									<input type="hidden" id="download_token_value_id" />

									<button type="submit" onclick="checkDownloadComplete()"
										class="btn btn-primary ">
										<spring:message code="button.submit" />
									</button>
								</div>
								<div class="col-xs-2 col-lg-1">
								<!--  onclick="showLoading()" -->
									<a href="DefectList.do"
										class="btn btn-primary "><spring:message
											code="button.reset" /></a>
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

<script type="text/javascript">
	$(function() {

		$('#from').datetimepicker({
			format : 'yyyy-mm-dd',
			weekStart : 0,
			todayBtn : 1,
			autoclose : 1,
			clearBtn : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			forceParse : 0,
			showMeridian : 0
		}).on("changeDate", function(ev) {
			var fullDate = $("#from").val()
			$("#to").datetimepicker("setStartDate", fullDate);

			if ($('#to').val() == '') {
				$('#to').val($('#from').val());
			}

		});

		$('#to').datetimepicker({
			format : 'yyyy-mm-dd',
			weekStart : 0,
			todayBtn : 1,
			autoclose : 1,
			clearBtn : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			forceParse : 0,
			showMeridian : 0
		}).on("changeDate", function(ev) {
			var fullDate = $("#to").val()
			$("#from").datetimepicker("setEndDate", fullDate);

			if ($('#from').val() == '') {

				$('#from').val($('#to').val());

			}

		});

	});
</script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						if ($("#from").val().length > 0)
							$("#to").datetimepicker("setStartDate",
									$("#from").val());

						if ($("#to").val().length > 0)
							$("#from").datetimepicker("setEndDate",
									$("#to").val());

						$('#failureClass')
								.change(
										function() {
											$
													.getJSON(
															'getProblemCodeByFailureClassKey.do',
															{
																failureClassKey : $(
																		this)
																		.val(),
																ajax : 'true'
															},
															function(data) {
																var html = '<option value=""><spring:message
					code="common.pleaseSelect" /></option>';
																var len = data.length;
																for (var i = 0; i < len; i++) {
																	html += '<option value="' + data[i].key + '">'
																			+ data[i].code
																			+ ' - '
																			+ data[i].name
																			+ '</option>';
																}
																html += '</option>';
																$(
																		'#problemCode')
																		.html(
																				html);
															});
										});

						$('#accountGroup')
								.change(
										function() {
											$
													.getJSON(
															'getUserAccountByAccountGroup.do',
															{
																accountGroupKey : $(
																		this)
																		.val(),
																ajax : 'true'
															},
															function(data) {
																$('#account')
																		.html(
																				"");
																var pleaseselect = '<option value=""><spring:message code="common.pleaseSelect" /></option>';
																$('#account')
																		.append(
																				pleaseselect);

																var len = data.length;
																for (var i = 0; i < len; i++) {
																	$(
																			'#account')
																			.append(
																					$(
																							"<option></option>")
																							.text(
																									data[i].name)
																							.val(
																									data[i].key));
																}

															});
										});
					});

	menu_toggle("sub_dm");
	menu_toggle("sub_dm_report");
	menu_select("defectList");
</script>