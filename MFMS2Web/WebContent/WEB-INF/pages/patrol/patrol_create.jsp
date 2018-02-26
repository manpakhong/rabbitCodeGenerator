<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">
<script src="import/customLoading.js"></script>


<script>
	var canEdit = "${canEdit}";
	var mode = "${mode}";
	var locationJson = '${locationJson}';
	var selectedLocaionEmpty = "";
	var searchCriteria = "";
	showCustomLoading();

	$(document).ready(
				function() {
						
						$.ajaxSetup({
							cache : false,
						});

						$("#errorMsgByJs").hide();
						$("#childLocation").load("ShowChildren.do");
						$("#selectedLocation").load("ShowSelectedLocation.do");
						//$('#errorMsg').load('showErrorMsg.do');
						var success = "${submitStatus}";
						if (success === "createSuccess" || success === "modifySuccess") {
							$('#errorMsg').load('ShowSuccessMsg.do?msg=' + success+ "&extraMsg=");
						}

						if (selectedLocaionEmpty == "has-error-cm") {
							setTimeout(function() {
								$("#selected_title").addClass("has-error-cm"),200});
						}

						var title_create = document.getElementById("title_create_route");
						var title_edit = document.getElementById("title_modify_route");
						var title_view = document.getElementById("title_view_route");

						var btn_edit = document.getElementById("btn_modify_route");
						var btn_submit = document.getElementById("btn_submit_route");
						var btn_reset = document.getElementById("btn_reset");
						var btn_modify = document.getElementById("btn_modify");
						var btn_back = document.getElementById("btn_back");
						var btn_back_view = document.getElementById("btn_back_view");
						var btn_reset = document.getElementById("btn_reset");
						var btn_modify_reset = document.getElementById("btn_modify_reset")
						var btn_delete = document.getElementById("btn_delete");

						if (mode === "create") {
							$("#collapseDetailBtn").hide();

							var form = document.getElementById("patrolCreateForm");

							title_create.style.display = 'display';
							title_edit.style.display = 'none';
							title_view.style.display = 'none';

							btn_edit.style.display = 'none';
							btn_submit.style.display = 'display';
							btn_reset.style.display = 'display';
							btn_modify.style.display = 'none';
							btn_back.style.display = 'none';
							btn_back_view.style.display = 'none';
							btn_delete.style.display = 'none';
							btn_modify_reset.style.display = 'none';
							btn_reset.style.display = 'display';

							form.action = "SubmitPatrolForm.do";

							if (locationJson != "" && locationJson != '[]') {
								$.ajax({
									method : "POST",
									url : "RecoverData.do",
									data : "oldLocationJson=" + encodeURIComponent(locationJson),
									success : function(data) {

										setTimeout(function() {
											$("#selectedLocation").html(data);
											hideCustomLoading();
										}, 200);

									}
								});
							} else {
								hideCustomLoading();
							}

						} else if (mode === "edit") {
							$("#collapseDetailBtn").hide();
							var form = document.getElementById("patrolCreateForm");

							title_create.style.display = 'none';
							title_edit.style.display = 'display';
							title_view.style.display = 'none';

							btn_edit.style.display = 'display';
							btn_submit.style.display = 'none';
							btn_reset.style.display = 'display';
							btn_modify.style.display = 'none';
							btn_delete.style.display = 'none';
							btn_modify_reset.style.display = 'display';
							btn_reset.style.display = 'none';

							form.action = "SubmitEditPatrolForm.do";

							$.ajax({
								method : "POST",
								url : "RecoverData.do",
								data : "oldLocationJson=" + locationJson,
								success : function(data) {

									setTimeout(function() {
										$("#selectedLocation").html(data);
										hideCustomLoading();
										
									}, 200);

								}
							});

							$(form).submit(function(e) {
								//showCustomLoading();

							});

							//$("#childLocation").parent().hide();
							//$("#selectedLocation").parent().attr('class', 'col-xs-12');

						} else if (mode === "view") {
							var btn_edit = document.getElementById("btn_modify_route");
							var form = document.getElementById("patrolCreateForm");

							title_create.style.display = 'none';
							title_edit.style.display = 'none';
							title_view.style.display = 'display';

							btn_edit.style.display = 'none';
							btn_submit.style.display = 'none';
							btn_reset.style.display = 'none';
							
							if (canEdit == "true") {
								btn_modify.style.display = 'display';
							} else {
								btn_modify.style.display = 'none';
							}
							btn_back.style.display = 'display';
							btn_modify_reset.style.display = 'none';
							btn_reset.style.display = 'none';
							btn_delete.style.display = 'none';

							$(".patrol_input").attr('readonly', true);
							$('.timeUnit_select').attr("disabled", true);
							$('#btn_submit_route').hide();
							$('#btn_modify_route').prop('type', 'button');
							$('#btn_modify_route').click(
											function() {
												showCustomLoading();
												var routeKey = document.getElementById('hidden_routeDefKey').value;
												window.location.href = "PatrolEdit.do?routeKey=" + routeKey;
											});

							$.ajax({
								method : "POST",
								url : "RecoverData.do",
								data : "oldLocationJson=" + encodeURIComponent(locationJson),
								success : function(data) {
										setTimeout(function() {
										$("#selectedLocation").html(data);
										hideCustomLoading();
									}, 200);
								}
							});

							var routeKey = document.getElementById('hidden_routeDefKey').value;
							$("#viewPatrolPhoto").load("ShowPatrolPhoto.do?routeDefKey=" + routeKey + "&date=");
							$("#childLocation").parent().hide();
							$("#selectedLocation").parent().attr('class', 'col-xs-12');
						}

						setHref();
						checkSubmit();
					});

	function setHref() {
		var hrefLink = "SearchPatrolRoute.do";
		var temp = window.location.search;
		var array = temp.split('&');

		for (var i = 1; i < array.length; i++) {

			var checkMode = array[i].split("=");
			if (checkMode[0] == "mode") {
				if (checkMode[1] == "search") {
					
					console.log("back mode search");
					$("#btn_back").show();
					$("#btn_back_view").hide();
					
				} else if (checkMode[1] == "view") {
					console.log("back mode view");
					$("#btn_back_view").show();
					$("#btn_back").hide();
				}
			} else {
				if (searchCriteria == "") {
					searchCriteria += array[i];
				} else {
					searchCriteria += "&" + array[i];
				}
			}

		}

		if (searchCriteria != "") {
			hrefLink += "?" + searchCriteria;
			setAsSearch();
			
		} else {
			setAsCreate();
		}

		$("#btn_back").attr("href", hrefLink);

		var routeKey = document.getElementById('hidden_routeDefKey').value;
		$("#btn_back_view").attr("href", "PatrolView.do?routeKey=" + routeKey + "&" + searchCriteria + "&mode=search");

		if (temp == "") {
			//console.log("Tmp is empty");
			$("#btn_back").hide();
			$("#btn_back_view").hide();

			if (mode == "view") {
				$("#btn_back").show();
			}
		}
	}

	function clearData() {

		$.ajax({
			method : "POST",
			data : "mode=" + mode,
			url : "ClearPatrolCreateData.do",
			success : function(data) {

				$(".patrol_input").val("");
				$("#errorMsg").empty();
				$("#errorMsgByJs").empty();
				$(".has-error").removeClass("has-error");
				selectedLocaionEmpty = "";
				$(".has-error-cm").removeClass("has-error-cm");
				$("#routeMinTime").val(0);
				$("#routeMaxTime").val(0);

				$('#selectedLocation').load("ShowSelectedLocation.do");
			}
		});
	}
	function modifyRest() {
		window.location.reload();
	}

	/*
	 function submitData() {
	 var jsonStr = $('#patrolCreateForm').serialize();

	 $.ajax({
	 method : "POST",
	 url : "submitPatrolForm.do",
	 data : jsonStr,
	 success : function(data) {
	 $('#errorMsg').html(data);
	 },
	 error : function(data) {
	 $('#errorMsg').html(data);
	 }
	 });
	 }
	 */
	function editRoute() {
		showCustomLoading();
		var routeKey = document.getElementById('hidden_routeDefKey').value;
		window.location.href = "PatrolEdit.do?routeKey=" + routeKey + "&" + searchCriteria + "&mode=view";
	}

	function deleteRoute() {

		document.getElementById("deleteConfirmBtn").onclick = function() {
			confirmDelete($("#hidden_routeDefKey").val());
		};
		
		$.ajax({
			method : "POST",
			url : "ViewDeleteRoute.do",
			data : "routeKey=" + $("#hidden_routeDefKey").val(),
			success : function(data) {
				//$('#searchTable').html(data);
				$("#deleteCode").html(data).promise().done(function() {
					$("#deleteConfirmModal").modal("show");
				});
			}
		});
	}

	function confirmDelete(routeKey) {

		//var currentPage = "${currentPage}";
		$.ajax({
			method : "POST",
			url : "DeleteRoute.do",
			data : "routeKey=" + routeKey + "&currentPage=0" + "&searchResultJson=",
			success : function(data) {

				$('#deleteConfirmModal').modal('hide');
				
				setTimeout(function() {
					window.location.href = "SearchPatrolRoute.do";
				}, 800);

			}
		});
	}
	function checkSubmit() {

		//$(".maxValue").trigger("change");
		//$(".minValue").trigger("change");
		setTimeout(function() {
					$("#patrolCreateForm").submit(
						function(event) {

							//event.preventDefault();

							$("#errorMsg").empty();
							$("#errorMsgByJs").hide();
							$(".has-error").removeClass("has-error");
							$(".has-error-cm").removeClass("has-error-cm");

							var hasError = false;
							var errorStr = "";

							var minValue = document.getElementsByClassName('minValue');
							var maxValue = document.getElementsByClassName('maxValue');

							if ($("#routeMinTime").val() == "") {
								//$("#routeMinTimeError").addClass("has-error");
								//hasError = true;
								$("#routeMinTime").val("0")
								}
							if ($("#routeMaxTime").val() == "") {
								//$("#routeMaxTimeError").addClass("has-error");
								//hasError = true;
								$("#routeMaxTime").val("0")
								}
										
							var strArray = new Array();

							for (i = 0; i < minValue.length; i++) {
								if (minValue[i].value == "") {
									//minValue[i].parent().parent().parent().addClass("has-error");

									$(minValue[i]).parent().parent().parent().addClass("has-error");
									hasError = true;
									if (strArray.length <= 2) {
										strArray[strArray.length] = '<spring:message code="patrol.route.location.duration.empty"/>';
													//errorStr+='<spring:message code="patrol.route.location.duration.empty"/>';
										}
									}
									if (maxValue[i].value == "") {
										$(maxValue[i]).parent().parent().parent().addClass("has-error");
										hasError = true;
										if (strArray.length <= 2) {
											strArray[strArray.length] = '<spring:message code="patrol.route.location.duration.empty"/>';
											//errorStr+='<spring:message code="patrol.route.location.duration.empty"/>';
												}
											}

									if (!hasError) {

										var minV = minValue[i+1].value;
										var maxV = maxValue[i+1].value;
										var defaultMinV = $(routeMinTime).val();
										var defaultMaxV = $(routeMaxTime).val();

										if ($("#routeMinTimeUnit").val() == "1") {
											minV = minV * 60;
											defaultMinV = defaultMinV * 60;
											}

										if ($("#routeMaxTimeUnit").val() == "1") {
											maxV = maxV * 60;
											defaultMaxV = defaultMaxV * 60;
											}

										if($("#routeCode").val()==""){
											$(routeCode).parent().parent().parent().addClass("has-error");
											hasError = true;
											if (strArray.length <= 5) {
												strArray[strArray.length] = '<spring:message code="patrol.route.code.empty"/>';
														
													}
												}
												
										if($("#routeName").val()==""){
												$(routeName).parent().parent().parent().addClass("has-error");
												hasError = true;
												if (strArray.length <= 5) {
														strArray[strArray.length] = '<spring:message code="patrol.route.name.empty"/>';
														
												}
											}
												
										
										//if (Number(defaultMinV) >= Number(defaultMaxV)) {
										//	$(routeMinTime).parent().parent().parent().addClass("has-error");
										//	hasError = true;
										//	if (strArray.length <= 5) {
										//		strArray[strArray.length] = '<spring:message code="patrol.route.location.default.min.greater"/>';
										//		//errorStr+='<spring:message code="patrol.route.location.min.greater"/>';
										//		}
										//	}
										
										if (Number(minV) >= Number(maxV)) {
											$(minValue[i+1]).parent().parent().parent().addClass("has-error");
											hasError = true;
											if (strArray.length <= 5) {
												strArray[strArray.length] = '<spring:message code="patrol.route.location.min.greater"/>';
														//errorStr+='<spring:message code="patrol.route.location.min.greater"/>';
												}													
											}
												
										if(Number(maxV)==0){								
											$(maxValue[i+1]).parent().parent().parent().addClass("has-error");
											hasError = true;
											if (strArray.length <= 5) {
												strArray[strArray.length] = '<spring:message code="patrol.route.location.max.greater.than.zero"/>';											
													}
													
											}
												
										
										//if(Number(defaultMaxV)==0){		
										//	$(routeMaxTime).parent().parent().parent().addClass("has-error");
										//	hasError = true;
										//	if (strArray.length <= 5) {
										//		strArray[strArray.length] = '<spring:message code="patrol.route.location.default.max.greater.than.zero"/>';														
										//	}
										//}
										
									}

							}

							if (hasError) {
									for (i = 0; i < strArray.length; i++) {

										if (errorStr == "") {
											errorStr += strArray[i];
										} else {
											errorStr += "<br/>" + strArray[i];
												}
										}

										$("#errorText").html(errorStr);
										$("#errorMsgByJs").show();
										hideLoading();
										event.preventDefault();	
									}
								});
				}, 500);
				
	}
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="PatrolManagement.do"><i
					class="icon-icon_patrol_management_b"></i> <spring:message
						code="menu.patrolMgt" /></a></li>
			<li><a href="PatrolRoute.do"> <spring:message
						code="menu.patrolMgt.route" /></a></li>
			<li class="active" id="title_create_route"><spring:message
					code="menu.patrolMgt.route.create" /></li>
			<li class="active" id="title_modify_route"><spring:message
					code="menu.patrolMgt.route.modify" /></li>
			<li class="active" id="title_view_route"><spring:message
					code="menu.patrolMgt.route.view" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-lg-12">
				<div class="box">
					<form:form id="patrolCreateForm" cssClass="form-horizontal"
						commandName="patrolCreateForm" method="post" action="">

						<form:hidden path="selectedLocationJson"
							id="hidden_selectedLocationJson" />
						<form:hidden path="routeDefKey" id="hidden_routeDefKey" />
						<div id="errorMsg">
							<!-- load view_errorMsg.jsp -->
							<spring:bind path="*">
								<c:if test="${status.error}">
									<div class="form-group alert alert-danger">
										<form:errors path="*" />
									</div>
								</c:if>
							</spring:bind>
						</div>
						<div id="errorMsgByJs">
							<div class="form-group alert alert-danger" id="errorText"></div>
						</div>
						<script>
							selectedLocaionEmpty = '<spring:bind path="selectedLocationJson"><c:if test="${status.error}">has-error-cm</c:if></spring:bind>';
						</script>

						<div class="box-body">
							<span class="glyphicon glyphicon-menu-down" aria-hidden="true"
								style="cursor: pointer; z-index: 100; position: absolute; top: 10px; right: 10px; transition-duration: 0.5s;"
								data-toggle="collapse" data-target="#collapseDetail"
								aria-expanded="false" aria-controls="collapseDetail"
								id="collapseDetailBtn"></span>
							<script>
								$("#collapseDetailBtn").click(
												function() {
													if ($("#collapseDetailBtn").hasClass("collapsed")){
														$("#collapseDetailBtn").css({'transform' : 'rotate(0deg)'});
													} else {
														$("#collapseDetailBtn").css({'transform' : 'rotate(90deg)'});
													}
												});
							</script>
							<div class="row">

								<div
									class="col-lg-6 col-md-6 col-sm-12 col-xs-12 <spring:bind path="routeCode"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<div class="row">
										<label for="" class="col-sm-4 control-label"><spring:message
												code="patrol.route.code" />*</label>
										<div class="col-sm-8">

											<spring:message code="patrol.route.code" var="code" />

											<form:input path="routeCode" placeholder="${code}"
												cssClass="form-control patrol_input" id="routeCode"
												/>
										</div>

									</div>
								</div>
								<div
									class="col-lg-6 col-md-6 col-sm-12 col-xs-12 <spring:bind path="routeName"><c:if test="${status.error}">has-error</c:if></spring:bind>">

									<div class="row">
										<label for="" class="col-sm-4 control-label"><spring:message
												code="patrol.route.name" />*</label>
										<div class="col-sm-8">

											<spring:message code="patrol.route.name" var="name" />

											<form:input path="routeName" placeholder="${name}"
												cssClass="form-control patrol_input" id="routeName"
												/>

										</div>
									</div>
								</div>

							</div>

							<div id="collapseDetail" class="collapse in">
								<div class="row">
									<div id="routeMinTimeError"
										class="col-lg-6 col-md-6 col-sm-12 col-xs-12 <spring:bind path="routeMinTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<div class="row">

											<label for=""
												class="col-lg-4 col-md-4 col-sm-4 col-xs-4 control-label"><spring:message
													code="patrol.route.default.min" /></label>
											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"
												style="display: inline-flex;">

												<spring:message code="patrol.route.default.min" var="min" />

												<form:input cssClass="form-control patrol_input"
													placeholder="${min}" path="routeMinTime" type="number"
													id="routeMinTime" min="0" max="999" />
											</div>
											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"
												style="display: inline-flex;">
												<form:select path="routeMinTimeUnit"
													cssClass="form-control timeUnit_select"
													onchange="changeUnitTag();">
													<form:options items="${timeUnit}" />
												</form:select>
												<script>
													$("#routeMinTimeUnit option[value=1]").hide();
												</script>
												
												</script>
											</div>
										</div>
									</div>
									<div id="routeMaxTimeError"
										class="col-lg-6 col-md-6 col-sm-12 col-xs-12 <spring:bind path="routeMaxTime"><c:if test="${status.error}">has-error</c:if></spring:bind>">

										<div class="row">
											<label for=""
												class="col-lg-4 col-md-4 col-sm-4 col-xs-4 control-label"><spring:message
													code="patrol.route.default.max" /></label>
											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"
												style="display: inline-flex;">

												<spring:message code="patrol.route.default.max" var="max" />

												<form:input cssClass="form-control patrol_input"
													placeholder="${max}" path="routeMaxTime" type="number"
													id="routeMaxTime" min="0" max="999" />
											</div>
											<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4"
												style="display: inline-flex;">
												<form:select path="routeMaxTimeUnit"
													cssClass="form-control timeUnit_select"
													onchange="changeUnitTag();">
													<form:options items="${timeUnit}" />
												</form:select>
												
												<script>
													$("#routeMaxTimeUnit option[value=1]").hide();
												</script>
											</div>
										</div>
									</div>
								</div>
								<br>
								<div class="row">
									<div class="col-xs-12 col-lg-4 col-md-4 col-sm-12">
										<div id="childLocation" class="panel panel-default">
											<!-- load "patrol/view_showChildren.jsp" -->
										</div>
									</div>
									<div class="col-xs-12 col-lg-8 col-md-8 col-sm-12">
										<div id="selectedLocation" class="panel panel-default">
											<!-- load "patrol/view_showSelectedLocation" -->
										</div>
									</div>
								</div>

							</div>

							<div class="box-footer">
								<div class="col-lg-1 col-xs-2">
									<a class="btn btn-primary " id="btn_back"
										href="SearchPatrolRoute.do"> <spring:message
											code="button.back.to.search" />
									</a> <a class="btn btn-primary " id="btn_back_view"
										href="PatrolView.do"><spring:message code="button.back" />
									</a>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<button class="btn btn-primary" type="submit"
										id="btn_submit_route" onclick="showLoading();">
										<spring:message code="button.submit" />
									</button>
									<button class="btn btn-primary" type="submit"
										id="btn_modify_route" onclick="showLoading();">
										<spring:message code="button.submit" />
									</button>
									<button type="button" class="btn btn-danger " id="btn_delete"
										onclick="deleteRoute();">
										<spring:message code="button.delete" />
									</button>
								</div>
								<div class="col-lg-1 col-xs-2">
									<button class="btn btn-primary" type="reset" id="btn_reset"
										onclick="clearData();">
										<spring:message code="button.reset" />
									</button>
									<button class="btn btn-primary" type="reset"
										id="btn_modify_reset" onclick="editRoute();">
										<spring:message code="button.reset" />
									</button>
									<button type="button" class="btn btn-primary " id="btn_modify"
										onclick="editRoute();">
										<spring:message code="button.modify" />
									</button>
								</div>
							</div>

						</div>
				</div>

				</form:form>
			</div>
		</div>
		<!-- Patrol Photo -->

		<div class="row">
			<div class="col-lg-12">

				<div id="viewPatrolPhoto" class="box" style="width: 100%"></div>
			</div>
		</div>
</div>
</section>
</div>

<!-- Delete Confirm Modal -->
<div class="modal fade" id="deleteConfirmModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="">Delete</h4>
			</div>
			<div class="modal-body" id="deleteContent">
				Are you sure you want to delete this Route? Code : <label
					id="deleteCode"></label>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="" id="deleteConfirmBtn">Delete</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"
					style="margin-bottom: 0;">Cancel</button>
			</div>
		</div>
	</div>
</div>


<!-- Reminder modal -->
<div class="modal fade" id="reminderModal" tabindex="-1" role="dialog" aria-labelledby="reminderModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="reminderModalLabel"><spring:message code="defect.search.warn.modalTitle"/></h4>
			</div>
		<div class="modal-body" id="reminderModalBody"></div>
			<div class="modal-footer"> 
				<button type="button" class="btn btn-primary" id="" onclick="" data-dismiss="modal"><spring:message code="button.ok"/></button>
			</div>
		</div>
	</div>
</div>



<script>
	menu_toggle("sub_pm");
	menu_toggle("sub_pm_route");
	function setAsCreate() {

		menu_select("createRoute");
	}
	function setAsSearch() {

		menu_select("searchRoute");
	}
	
	$("#routeCode, routeName")
</script>
