<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>

<jsp:include page="../../common/left.jsp" />

<link rel="stylesheet" href="import/css/patrol/patrol_style.css">

<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/moment.js"></script>
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>
<script src="import/customLoading.js"></script>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.routePatrol')"
	var="routePatrol" />
<script type="text/javascript">
	var mode = "${mode}";
	var locationJson = '${locationJson}';
	var selectedRouteJson = '${selectedRouteJson}';
	var selectedStaffJson = '${selectedStaffJson}';
	var eventEditStaffJson = '${eventEditStaffJson}';
	var schedulerCounter = 0;
	var selectedLocaionEmpty = "";
	var defaultViewStr = "month";

	showCustomLoading();

	$(document).ready(
			function() {

				$("#errorSection").hide();
				$("#successMsg").hide();
				$("#errorMsg").hide();

				$.ajaxSetup({
					cache : false,
				});

				$.ajax({
					method : "POST",
					data : "mode=" + mode,
					url : "ClearPatrolCreateData.do",
					success : function(data) {
						//$('#selectedLocation').load("ShowSelectedLocation.do");
					}
				});

				$.ajax({
					method : "POST",
					data : "selectedRouteJson=" + "",
					url : "PatrolAssignSelectRoute.do",
					success : function(data) {
						$('#selectedRoute').html(data);
					}
				});
				//$('#selectedRoute').load("PatrolAssignSelectRoute.do");

				$.ajax({
					method : "POST",
					url : "PatrolAssignSelectStaff.do",
					data : "edit=false&selectedStaffJson=" + selectedStaffJson
							+ "&eventEditStaffJson=" + eventEditStaffJson
							+ "&extraId=",
					success : function(data) {

						$('#selectedStaff').html(data);
						$("#accountLabel").css("margin-left",0).css("padding-left",0);
					}
				});

				//$('#selectedStaff').load("PatrolAssignSelectStaff.do?edit=false&selectedStaffJson="+selectedStaffJson);

				//for modal selecteStaffModal
				$('#view_searchedStaff').load(
						"ShowStaffSearched.do?privilege=${routePatrol}");

				$.ajax({
					method : "POST",
					url : "ShowStaffSelected.do",
					data : "edit=false&selectedStaffJson=" + selectedStaffJson
							+ "&eventEditStaffJson=" + eventEditStaffJson,
					success : function(data) {

						$('#view_selectedStaff').html(data);

					}
				});

				//$('#view_selectedStaff').load("ShowStaffSelected.do?edit=false&selectedStaffJson="+selectedStaffJson);
				$('#view_accountName').on('input', function(e) {
					submitStaffSearch();
				});

				//for modal selectLocationModal
				$('#startingLocation').load("ShowStartingLocation.do");
				$('#childLocation').load("ShowChildren.do");
				$('#selectedLocation').load("ShowSelectedLocation.do");

				//for modal selectRouteModal
				$('#view_searchedRoute').load("ShowRouteNameSearch.do");
				$('#view_selectedRoute').load("ShowRouteNameSelected.do");
				$('#view_routeName').on('input', function(e) {
					submitRouteNameSearch();
				});
				$('#view_routeCode').on('input', function(e) {
					submitRouteNameSearch();
				});

				//for calendar

				$('#view_calendar').load("ShowCalendar.do");

				setParam(window.location.search.substring(1));
				

			});

	function setParam(urlParam) {

		console.log("urlParam:" + urlParam);

		if (urlParam != "") {

			var temp = urlParam.split('&');
			if (temp.length > 1) {
				if (temp[1].split('=')[0] == "lang") {
					return;
				}
			}

			$("#successMsg").load(
					'ShowSuccessMsg.do?msg=' + temp[0].split('=')[1]
							+ '&extraMsg=');
			$("#successMsg").show();
			//time out
			//slideUpThePrompt();
			//$("#errorSection").show();

		}
	}
	
	function slideUpThePrompt(){	
		 $( "#successMsg" ).slideUp( 800 );
	}
	
	function refreshRouteName() {
		$.ajax({
			method : "POST",
			data : {selectedRouteJson : selectedRouteJson},
			url : "PatrolAssignSelectRoute.do",
			success : function(data) {
				$('#selectedRoute').html(data);
			}
		});
	}

	function refreshStartingLocation() {
		var json = locationJson;

		$.ajax({
			method : "POST",
			url : "RefreshStartingLocation.do",
			data : "locationJson=" + encodeURIComponent(json),
			success : function(data) {

				$('#startingLocation').html(data);
				submitRouteNameSearch();

			}
		});
	}

	function refreshAccount() {
		if ($("#eventModal").hasClass('in')) {
			console.log("#eventModal");
			
			var temp_select = eventEditStaffJson;
			var temp_edit = eventEditStaffJson;
			var extraId = "";
			
			
			if('undefined' != typeof currentMode){
				if(currentMode=="current"){
				
					temp_select = currentEventEditStaffJson;
					temp_edit = edit_selectedStaffJson;
			
				}else if(currentMode == "series"){
		
					temp_select = parentEventEditStaffJson;
					temp_edit = edit_parentSelectedStaffJson;
					extraId = "_series";
			
				}
			}
			
			console.log("select : "+temp_select + "||edit :"+temp_edit);
			
			$.ajax({
				method : "POST",
				url : "PatrolAssignSelectStaff.do",
				data : "edit=true&selectedStaffJson=" + temp_edit
						+ "&eventEditStaffJson=" + temp_edit
						+ "&extraId="+extraId,
				success : function(data) {
					if('undefined' != typeof currentMode){
						if(currentMode =="current"){
							$('#modal_account').html(data);
						}else if(currentMode == "series"){
							$('#modal_series_account').html(data);
						}
					}
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
					$('#selectedStaff').html(data);
				}
			});

		}
	}

	function submitRouteNameSearch() {

		var json = "";

		var routeName = document.getElementById('view_routeName').value;
		var routeCode = document.getElementById('view_routeCode').value;

		var startingLocation = document
				.getElementById('select_startingLocation');
		var locationId = new Array();
		for (i = 0; i < startingLocation.length; i++) {
			locationId[i] = startingLocation.options[i].value;
		}

		json = JSON.stringify({
			routeName : routeName,
			routeCode : routeCode,
			locationKey : locationId
		});

		//alert(json);

		$.ajax({
			method : "POST",
			data : json,
			dataType : "html",
			contentType : 'application/json',
			url : "SubmitRouteNameSearch.do",
			success : function(data) {
				$('#view_searchedRoute').html(data);
			}
		});

	}

	function submitStaffSearch() {

		var roleKey = $('#view_accountRole').val();
		var accountName = document.getElementById('view_accountName').value;

		var privilege = '${routePatrol}';

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
	
	function submitClearedStaffSearch() {

		//var roleKey = $('#view_accountRole').val("-1");
		//var accountName = document.getElementById('view_accountName').value="";
		
		
		$("#view_accountName").val("");
		$("#view_accountRole").val(-1);
		var privilege = '${routePatrol}';

		$.ajax({
			method : 'POST',
			data : "accountName=" + "" + "&accountRoleKey=" + "-1"
					+ "&privilege=" + privilege,
			url : "SubmitStaffSearch.do",
			success : function(data) {
				$('#view_searchedStaff').html(data);
			}
		})

	}

	function appendScheduleForm() {

		$.ajax({
			url : "AddScheduleForm.do",
			method : "POST",
			data : "schedulerCounter=" + schedulerCounter,
			success : function(data) {
				$('#scheduleForm').append(data);
			}
		})
	}

	function removeScheduleForm(counter) {
		$("#scheduler" + counter).remove();
	}

	function submitNewSchedule() {

		$(".has-error").removeClass("has-error");
		$("#errorSection").hide();
		$("#successMsg").hide();
		$("#errorMsg").hide();

		var routeName = document.getElementById("route");
		var staff = document.getElementById("staff");
		var startDate = document.getElementsByClassName('allStartDate');
		var endDate = document.getElementsByClassName('allEndDate');
		var scheduleTime = document.getElementsByClassName('allTimePicker');
		var frequency = document.getElementsByClassName('frequency');
		var weekDayBox = document.getElementsByClassName('weekDayBox');

		var errorStr = "";
		var hasError = false;

		//check if routeName empty
		if (0 == routeName.length) {
			$("#selectedRoute").addClass("has-error");
			hasError = true;

			if (errorStr == "") {
				errorStr += '<spring:message code="patrol.schedule.route.empty"/>';
			} else {
				errorStr += '<br/><spring:message code="patrol.schedule.route.empty	" />';
			}
		}

		//check if account empty
		if (0 == staff.length) {
			$("#selectedStaff").addClass("has-error");
			hasError = true;
			if (errorStr == "") {
				errorStr += '<spring:message code="patrol.schedule.account.empty"/>';
			} else {
				errorStr += '<br/><spring:message code="patrol.schedule.account.empty"/>';
			}
		}

		//check if all start date filled
		if (0 == startDate.length) {
			$("#scheduleForm_title").addClass("has-error");
			hasError = true;
			if (errorStr == "") {
				errorStr += '<spring:message code="patrol.schedule.schedule.empty"/>';
			} else {
				errorStr += '<br/><spring:message code="patrol.schedule.schedule.empty"/>';
			}
		} else {
			for (i = 0; i < startDate.length; i++) {
				if ("" == startDate[i].value) {

					$(startDate[i]).parent().parent().addClass("has-error");

					hasError = true;

					if (errorStr == "") {
						errorStr += '<spring:message code="patrol.schedule.date.start.empty"/>';
					} else {
						errorStr += '<br/><spring:message code="patrol.schedule.date.start.empty"/>';
					}

				}
			}
		}

		//check if all end date filled
		/*
		if (0 == endDate.length) {
			hasError = true;
		} else {
			for (i = 0; i < endDate.length; i++) {
				if ("" == endDate[i].value) {

					$(endDate[i]).parent().parent().addClass("has-error");

					hasError = true;

					if (errorStr == "") {
						errorStr += '<spring:message code="patrol.schedule.date.end.empty"/>';
					} else {
						errorStr += '<br/><spring:message code="patrol.schedule.date.end.empty"/>';
					}
				}
			}
		}
		 */
		//check if al time filled
		if (0 == scheduleTime.length) {
			hasError = true;
		} else {
			for (i = 0; i < scheduleTime.length; i++) {
				if ("" == scheduleTime[i].value) {

					$(scheduleTime[i]).parent().parent().addClass("has-error");

					hasError = true;

					if (errorStr == "") {
						errorStr += '<spring:message code="patrol.schedule.date.time.empty"/>';
					} else {
						errorStr += '<br/><spring:message code="patrol.schedule.date.time.empty"/>';
					}
				}
			}
		}

		//check if weekdays selected if "weekly" is selected
		for (i = 0; i < weekDayBox.length; i++) {
			if (103 == frequency[i].value) {
				var weekDay = document.getElementsByClassName("weekDay"
						+ $(weekDayBox[i]).attr("name"));

				var tempCounter = 0;
				for (j = 0; j < weekDay.length; j++) {
					if ($(weekDay[j]).prop('checked')) {
						tempCounter++;
					}
				}

				if (tempCounter == 0) {
					hasError = true;
					$(weekDayBox[i]).addClass("has-error");

					if (errorStr == "") {
						errorStr += '<spring:message code="patrol.schedule.date.weekDay.empty"/>';
					} else {
						errorStr += '<br/><spring:message code="patrol.schedule.date.weekDay.empty"/>';
					}
				}

			}

		}

		//stop submit if there is error
		if (hasError) {
			$("#errorMsg").show();
			$("#errorText").html(errorStr);
			$("#errorSection").show();

			return;
		}

		//for generate json 
		var json_route = new Array();
		var json_staff = new Array();
		var json_startDate = new Array();
		var json_endDate = new Array();
		var json_time = new Array();
		var json_frequency = new Array();
		var json_weekDay = new Array();

		for (i = 0; i < routeName.length; i++) {
			json_route[i] = routeName.options[i].value;
		}

		for (i = 0; i < staff.length; i++) {
			json_staff[i] = staff.options[i].value;
		}

		for (i = 0; i < startDate.length; i++) {
			json_startDate[i] = startDate[i].value;
		}

		for (i = 0; i < endDate.length; i++) {
			json_endDate[i] = endDate[i].value;
		}

		for (i = 0; i < scheduleTime.length; i++) {
			json_time[i] = scheduleTime[i].value;
		}

		for (i = 0; i < frequency.length; i++) {
			json_frequency[i] = frequency[i].value;
		}
		for (i = 0; i < weekDayBox.length; i++) {
			var weekDay = document.getElementsByClassName("weekDay"
					+ $(weekDayBox[i]).attr("name"));
			var temp = new Array();
			var tempCounter = 0;
			for (j = 0; j < weekDay.length; j++) {
				if ($(weekDay[j]).prop('checked')) {
					temp[tempCounter] = weekDay[j].value;
					tempCounter++;
				}
			}

			json_weekDay[i] = temp;
		}

		var json = "";
		json = JSON.stringify({
			routeDefKey : json_route,
			accountKey : json_staff,
			startDate : json_startDate,
			endDate : json_endDate,
			time : json_time,
			frequency : json_frequency,
			weekDay : json_weekDay
		});
		//alert(json);

		$
				.ajax({
					method : "POST",
					data : json,
					dataType : "html",
					contentType : 'application/json',
					url : "SubmitRouteSchedule.do",
					success : function(data) {
						//$('#view_searchedRoute').html(data);
						if (data == "success") {
							//$('#view_calendar').load("ShowCalendar.do");
							//$("#errorMsg").load('ShowSuccessMsg.do?msg=createScheduleSuccess&extraMsg=');
							//$("#errorSection").show();
							window.location.href = "PatrolAssign.do?createMode=createScheduleSuccess";
						} else {
							alert(data);
						}
						//window.location.reload();
					}
				});

	}

	function clearData() {

		$(".has-error").removeClass("has-error");
		$("#errorSection").hide();
		$("#successMsg").hide();
		$("#errorMsg").hide();

		locationJson = '';
		selectedRouteJson = '';
		selectedStaffJson = '';

		$.ajax({
			method : "POST",
			data : "selectedRouteJson=" + selectedRouteJson,
			url : "PatrolAssignSelectRoute.do",
			success : function(data) {
				$('#selectedRoute').html(data);
			}
		});

		$.ajax({
			method : "POST",
			url : "PatrolAssignSelectStaff.do",
			data : "edit=false&selectedStaffJson=" + selectedStaffJson
					+ "&eventEditStaffJson=" + eventEditStaffJson
					+ "&extraId=",
			success : function(data) {

				$('#selectedStaff').html(data);

			}
		});

		$("#scheduleForm").empty();
		$("#view_routeCode").val("");
		$("#view_routeName").val("");
		$("#view_accountName").val("");
		$("#view_accountRole").val(-1);

		$('#startingLocation').load("ShowStartingLocation.do");
		$('#childLocation').load("ShowChildren.do");
		$('#selectedLocation').load("ShowSelectedLocation.do");

		$('#view_searchedRoute').load("ShowRouteNameSearch.do");
		$('#view_selectedRoute').load("ShowRouteNameSelected.do");

		submitStaffSearch();
	}

	function test() {
		$.ajax({
			method : "POST",
			url : "testEvenCalendar.do",
			success : function(data) {

			}
		});
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
			<li class="active"><spring:message
					code="menu.patrolMgt.schedule" /></a></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<!--  <input type="button" value="test" onclick="test()"/>-->

		<div class="row">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="box">
					<div id="successMsg">
						<!-- load view_errorMsg.jsp -->

					</div>


					<form:form id="patrolAssignForm" cssClass="form-horizontal"
						commandName="patrolAssignForm" method="post" action="">

						<div class="box-body">

							<div class="row">
								<div class="ol-lg-6 col-md-6 col-sm-12 col-xs-12"
									id="view_calendar"></div>


								<div class="ol-lg-6 col-md-6 col-sm-12 col-xs-12">


									<!-- New Schedule -->
									<div class="modal-header">
										<h3 class="modal-title">
											<spring:message code="patrol.schedule.new" />
										</h3>
									</div>
									<div class="modal-body">
										<div class="row" id="errorSection">
											<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
												<div class="box">
													<div id="errorMsg">
														<!-- load view_errorMsg.jsp -->

														<div class="form-group alert alert-danger" id="errorText">
														</div>

													</div>
												</div>
											</div>
										</div>
										<div class="row">
											<!-- Left Column -->
											<div class="col-xs-12">
												<div class="row form-group" id="selectedRoute">
													<!-- load assignSchedule/view_assign_selectRoute.jsp -->
												</div>

											</div>
										</div>
										<div class="row">
											<!-- Right Column -->
											<div class="col-xs-12">
											<!--  
											<style>
											#accountDiv{padding-left: 8px;}
											</style>
											--> 
												<div class="row form-group" id="selectedStaff">
													<!-- load assignSchedule/view_assign_selectStaff.jsp -->
												</div>
											</div>
										</div>

										<div class="row">
											<div class="col-lg-12">
												<div class="row" id="scheduleForm_title">
													<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
														<label class="control-label" style="border: none"><spring:message
																code="patrol.schedule.date" /></label>
													</div>
													<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
														<label class="control-label" style="border: none"><spring:message
																code="patrol.schedule.time" /></label>
													</div>
													<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
														<label class="control-label" style="border: none"><spring:message
																code="patrol.schedule.frequency" /></label>
													</div>
													<div class="col-lg-1 col-md-1 col-sm-1 col-xs-1">
														<div class="patrol-btn-addLocation" style="float: left"
															onclick="appendScheduleForm()">
															<span class="glyphicon glyphicon-plus-sign"
																style="margin-top: 15px;"></span>
														</div>
													</div>
												</div>
												<div class="row" id="scheduleForm">

													<!-- load assignSchedule/view_assign_schedule_form.jsp -->

												</div>
											</div>
										</div>
									</div>
									<div class="modal-footer">
										<div class="col-xs-12">
											<button type="button" class="btn btn-success"
												id="btn_submit_route" onclick="submitNewSchedule()"
												style="margin-bottom: 0">
												<!--<spring:message code="button.search" />-->
												<spring:message code="button.addSchedule" />
											</button>
											<button type="reset" class="btn btn-primary "
												onclick="clearData();">
												<spring:message code="button.reset" />
											</button>
										</div>
									</div>


								</div>

							</div>


						</div>
					</form:form>
				</div>
			</div>
		</div>

	</section>
</div>


<!-- Modal selectRouteModal -->
<div class="modal fade" id="selectRouteModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close" onclick="RouteBack2Previous();">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" id="myModalLabel">
					<spring:message code="patrol.schedule.select.route" />
				</h3>
			</div>
			<div class="modal-body">
				<!-- Body/Content for Modal -->
				<div class="row">
					<div class="col-xs-12 col-lg-12 col-md-12 col-sm-12">
						<div class="row form-group">
							<label class="col-lg-2 col-md-2 col-sm-2 col-xs-2 control-label"><spring:message
									code="patrol.schedule.route.code" /></label>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<input class="form-control patrol_input" id="view_routeCode"
									type="text" value="" />
							</div>

							<label class="col-lg-2 col-md-2 col-sm-2 col-xs-2 control-label"><spring:message
									code="patrol.schedule.route.name" /></label>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
								<input class="form-control patrol_input" id="view_routeName"
									type="text" value="" />
							</div>
						</div>
					</div>
				</div>
				<style>
				#sl{padding-right: 0px;}
				#select_startingLocation{margin-left: 27px; margin-right: 40px;}
				#resetbtn{margin-top: 60px; margin-left: 40px;}
				</style>
				<div class="row">
					<div class="col-xs-10 col-lg-10 col-md-10 col-sm-10">
						<div class="row form-group" id="startingLocation">
							<!-- load view_startingLocation.jsp -->
						</div>
					</div>
					
					<div class="col-xs-2 col-lg-2 col-md-2 col-sm-2">
					<button type="reset" class="btn btn-primary " id="resetbtn" onclick="dropAllSelectedLocation()">
					<spring:message code="button.reset" />
					</button>
					</div>
					
				</div>				
				<br>			
				<div class="row">
					<div class="col-xs-6 col-lg-6 col-md-6 col-sm-6">
						<div id="view_searchedRoute" class="panel panel-default">
							<!-- load assignSchedule/view_assign_route_searched.jsp -->
						</div>
					</div>
					<div class="col-xs-6 col-lg-6 col-md-6 col-sm-6">
						<div id="view_selectedRoute" class="panel panel-default">
							<!-- load assignSchedule/view_assign_route_selected.jsp -->
						</div>
					</div>
				</div>
			</div>
			<!-- End of Body/Content for Modal -->
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="refreshRouteName();">
					<spring:message code="button.ok" />
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="RouteBack2Previous();">
					<spring:message code="button.cancel" />
				</button>
			</div>
		</div>
	</div>
</div>

<!-- Modal selectStaffModal -->
<div class="modal fade" id="selectStaffModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close" onclick="ACBack2Previous();">
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
					style="margin-bottom: 0;" onclick="ACBack2Previous();">
					<spring:message code="button.cancel" />
				</button>
			</div>
		</div>
	</div>
</div>

<!-- Modal selectLocationModal -->
<div class="modal fade" id="selectLocationModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" id="myModalLabel">
					<spring:message code="patrol.route.start.location.select" />
				</h3>
			</div>
			<div class="modal-body">
				<!-- Body/Content for Modal -->
				<input type="hidden" value="0" id="routeMinTime" /> <input
					type="hidden" value="0" id="routeMaxTime" />
				<div class="row">
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<div id="childLocation" class="panel panel-default">
							<!-- load "patrol/view_showChildren.jsp" -->
						</div>
					</div>
					<style> #hide4SearchRoutePage { display: none; }</style>
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">

						<div id="selectedLocation" class="panel panel-default">
							<!-- load "patrol/view_showSelectedLocation" -->
						</div>
					</div>
				</div>
				<!-- End of Body/Content for Modal -->
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="refreshStartingLocation();">
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

<script>
	menu_toggle("sub_pm");
	menu_select("patrolSchedule");
</script>

