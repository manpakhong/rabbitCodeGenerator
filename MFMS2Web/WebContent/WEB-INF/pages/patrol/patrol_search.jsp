<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">
<script src="import/customLoading.js"></script>

<style>
.table td:first-child {
	text-align: center;
	color: #4491bf;
	cursor: pointer;
}

.table td:not(:first-child){
	text-align: left;
}
</style>

<script>
	showCustomLoading();

	var mode = "${mode}";
	var locationJson = '${locationJson}';
	var oldLocationJson = '${oldLocationJson}';
	var selectedLocaionEmpty = "";

	$(document)
			.ready(
					function() {

						$.ajaxSetup({
							cache : false,
						});

						//clearData();

						$('#searchTable').load("ShowSearchTable.do");
						$('#childLocation').load("ShowChildren.do");
						$('#selectedLocation').load("ShowSelectedLocation.do");
						$('#startingLocation').load("ShowStartingLocation.do",
								function() {
									hideCustomLoading();
									$('#block').attr('class', 'col-lg-4 col-md-4 col-sm-4 col-xs-4').attr('style', 'padding-right: 15px')
								});
						$("#errorMsgByJs").hide();
						//setParam(window.location.search.substring(1));
						$("#routeName")
								.val(
										"<spring:message code="patrol.route.name" var="name" />");
						setParam(window.location.search.substring(1));
					});

	function setParam(urlParam) {
		if (urlParam != "") {

			var temp = urlParam.split('&');

			console.log(temp[1]);
			console.log(temp[1].split('=')[1]);

			$("#routeCode").val(temp[0].split('=')[1]);

			if (temp[1].split('=')[1] != "en" && temp[1].split('=')[1] != "zh_hk") {
				$("#routeName").val(temp[1].split('=')[1]);
			}
			//$("#routeCode").val(temp[2].split('=')[1]);
			if (temp.lenth > 2) {
				getJsonForSelectedLocation(temp[2].split('=')[1]);
				$("#routeCode").val(temp[2].split('=')[1]);
			}
			if (temp[1].split('=')[1] != "en"&& temp[1].split('=')[1] != "zh_hk") {
				submitSearch(false);
			}
		}
	}

	function getJsonForSelectedLocation(selected) {

		var param = "";

		var data = selected.split(',');

		if (data == null) {

		} else {

			for (var i = 0; i < data.length; i++) {
				if (param == "") {
					param += "loc=" + data[i];
				} else {
					param += "&loc=" + data[i];
				}
			}
		}

		$.ajax({
			method : "POST",
			data : param,
			url : "GetJsonForSelectedLocation.do",
			success : function(rtn) {
				locationJson = rtn;

				$("#hidden_selectedLocationJson").val(locationJson);
				refreshStartingLocation();

				oldLocationJson = locationJson;
				recoverData();

				submitSearch(false);
			}
		})

	}

	function refreshStartingLocation() {

		var json = locationJson;
		localStorage.setItem("locationJson", json);
		$.ajax({
			method : "POST",
			url : "RefreshStartingLocation.do",
			data : "locationJson=" + encodeURIComponent(json),
			success : function(data) {

				$('#startingLocation').html(data);
				$('#block').attr('class', 'col-lg-4 col-md-4 col-sm-4 col-xs-4')
			}
		});

	}
	//var routeName = null;
	
	
	function submitSearch(removeMsg) {
		
		showLoading();
		
		if(removeMsg){
			$("#errorMsgByJs").hide();
		}
		
		
		var json = $("#patrolSearchForm").serialize();
		var routeName = $("#routeName").val();
		var routeCode = $("#routeCode").val();
		localStorage.setItem("json", json);
		localStorage.setItem("routeName", routeName);
		localStorage.setItem("routeCode", routeCode);

		$.ajax({
			method : "POST",
			url : "SubmitSearch.do",
			data : json,
			success : function(data) {
				$('#searchTable').html(data);
				RouteNumber();
				hideLoading();

			},
			error : function(data) {
				alert("error");
			}
		});

	}

	var url = window.location.href;
	var search = url.search("lang=");
	var getJson = localStorage.getItem("json");
	var getRouteName = localStorage.getItem("routeName");
	var getRouteCode = localStorage.getItem("routeCode");
	var getLocationJson = localStorage.getItem("locationJson");
	if (search != -1) {
		if (url.search("lang=en") != -1) {
			if (getJson == null) {
				console.log("getJson==null");
			} else {
				submitSearch4Refresh(getJson);

				$(document).ready(function() {
					$("#routeName").val(getRouteName);
					$("#routeCode").val(getRouteCode);
					refreshStartingLocation4Refresh(getLocationJson);

				});
			}
		}
		if (url.search("lang=zh_hk") != -1) {
			if (getJson == null) {
				console.log("getJson==null");
			} else {
				submitSearch4Refresh(getJson);
				//refreshStartingLocation4Refresh(getLocationJson);
				$(document).ready(function() {
					$("#routeName").val(getRouteName);
					$("#routeCode").val(getRouteCode);
					refreshStartingLocation4Refresh(getLocationJson);

				});
			}
		}
	} else {

		localStorage.removeItem("routeName");
		localStorage.removeItem("routeCode");
		localStorage.removeItem("locationJson");
		localStorage.removeItem("json");
	}

	function submitSearch4Refresh(json) {
		$.ajax({
			method : "POST",
			url : "SubmitSearch.do",
			data : json,
			success : function(data) {
				$('#searchTable').html(data);
				RouteNumber();
				hideLoading();

			},
			error : function(data) {
				alert("error");
			}
		});

	}

	function refreshStartingLocation4Refresh(locationjson) {

		$.ajax({
			method : "POST",
			url : "RefreshStartingLocation.do",
			data : "locationJson=" + encodeURIComponent(locationjson),
			success : function(data) {

				$('#startingLocation').html(data);
				$('#block').attr('class', 'col-lg-4 col-md-4 col-sm-4 col-xs-4')
			}
		});

	}

	function clearData() {

		showLoading();

		$("#errorMsgByJs").hide();
		$('#selectedLocation').load("ShowSelectedLocation.do");
		$('#startingLocation').load("ShowStartingLocation.do");
		$('#searchTable').load("ShowSearchTable.do");

		hideLoading();

		/*
		$(".patrol_input").val("");

		$.ajax({
			method : "POST",
			url : "ClearData.do",
			data : "mode=" + mode,
			success : function(data) {
				$('#selectedLocation').load("ShowSelectedLocation.do");
				$('#startingLocation').load("ShowStartingLocation.do");
			}
		});
		 */
	}

	function recoverData() {
		$.ajax({
			method : "POST",
			url : "RecoverData.do",
			data : "oldLocationJson=" + oldLocationJson,
			success : function(data) {
				$('#selectedLocation').html(data);

			}
		});
	}
</script>


<script>
	//var searchResult = '${searchResultJson}';

	//var resultJson = document.getElementById('hidden_searchResultJson');
	//if (resultJson != null) {
	//	resultJson.value = searchResult;
	//}

	/*
	function gotoPage(page) {
		$.ajax({
			method : "POST",
			url : "GotoPage.do",
			data : "page=" + page + "&searchResultJson=" + resultJson.value,
			success : function(data) {
				$('#searchTable').html(data);

			},
			error : function(data) {
				alert("error");
			}
		});
	}
	 */
	function viewRoute(routeKey) {
		/*
		$.ajax({
			method : "POST",
			url : "PatrolView.do",
			data : "routeKey="+routeKey,
			success : function(data) {
				window.location.href = data;

			}
		});
		 */
		showCustomLoading();

		var code = $("#routeCode").val();
		var name = $("#routeName").val();
		var loc = document.getElementById("select_startingLocation");
		var mode = "&mode=search";

		var locVal = "";
		for (var i = 0; i < loc.length; i++) {
			if (locVal == "") {
				locVal += loc.options[i].value;
			} else {
				locVal += "," + loc.options[i].value;
			}

		}

		window.location.href = "PatrolView.do?routeKey=" + routeKey + "&code="
				+ code + "&name=" + name + "&loc=" + locVal + mode;
	}

	function editRoute(routeKey) {

		showCustomLoading();

		var code = $("#routeCode").val();
		var name = $("#routeName").val();
		var loc = document.getElementById("select_startingLocation");
		var mode = "&mode=search";

		var locVal = "";
		for (var i = 0; i < loc.length; i++) {
			if (locVal == "") {
				locVal += loc.options[i].value;
			} else {
				locVal += "," + loc.options[i].value;
			}

		}

		window.location.href = "PatrolEdit.do?routeKey=" + routeKey + "&code="
				+ code + "&name=" + name + "&loc=" + locVal + mode;

	}

	function deleteRoute(routeKey) {
		document.getElementById("deleteConfirmBtn").onclick = function() {
			confirmDelete(routeKey);
		};
		$.ajax({
			method : "POST",
			url : "ViewDeleteRoute.do",
			data : {routeKey : routeKey},
			success : function(data) {
				//$('#searchTable').html(data);
				$("#deleteCode").html(data).promise().done(function() {
					$("#deleteConfirmModal").modal("show");
				});
			}
		});

		/*
		var currentPage = "${currentPage}";
		$.ajax({
			method : "POST",
			url : "DeleteRoute.do",
			data : "routeKey="+routeKey+"&currentPage="+currentPage+"&searchResultJson="+resultJson.value,
			success : function(data) {
				$('#searchTable').html(data);
			}
		});
		 */
	}
	function confirmDelete(routeKey) {

		showLoading();

		//var currentPage = "${currentPage}";
		var currentPage = 1;

		$.ajax({
			method : "POST",
			url : "DeleteRoute.do",
			data : {routeKey : routeKey , currentPage : currentPage,
					searchResultJson :""},
			success : function(data) {

				$('#deleteConfirmModal').modal('hide');
				setTimeout(function() {
					//$('#searchTable').html(data);

					$("#errorMsgByJs").load(
							'ShowSuccessMsg.do?msg=' + "deleteSuccess"
									+ "&extraMsg=" + routeKey);
					$("#errorMsgByJs").show();

					submitSearch(false);
				}, 800);

			}
		});
	}

	var popstate = function() {
		content.innerHTML = history.state.content;
		page = history.state.page;
	};
	window.addEventListener('popstate', popstate, false);
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
			<li class="active"><spring:message
					code="menu.patrolMgt.route.search" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<div id="errorMsgByJs"></div>

					<!-- form start -->
					<form:form id="patrolSearchForm" cssClass="form-horizontal"
						commandName="patrolSearchForm" >
						<form:hidden path="selectedLocationJson"
							id="hidden_selectedLocationJson" />
						<form:hidden path="searchResultJson" id="hidden_searchResultJson" />
						
						<div class="box-body">
							<div class="row">
								<label class="col-lg-2 col-md-2 col-sm-2 col-xs-2 control-label"
									style="border: none;"><spring:message
										code="patrol.route.code" /></label>
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">

									<spring:message code="patrol.route.code" var="code" />

									<form:input cssClass="form-control patrol_input"
										placeholder="${code}" path="routeCode" id="routeCode" />
								</div>
								<label
									class="col-lg-2 col-md-2 col-sm-2 col-xs-2
										control-label"
									style="border: none; white-space: nowrap;"><spring:message
										code="patrol.route.name" /></label>
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">

									<spring:message code="patrol.route.name" var="name" />

									<form:input cssClass="form-control patrol_input"
										placeholder="${name}" path="routeName" id="routeName" />
								</div>
							</div>

							<div class="row">
								<div id="startingLocation">
									<!-- load view_startingLocation.jsp -->
								</div>
							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">
									<button type="button" class="btn btn-primary" id="searchBtn"
										onclick="submitSearch(true);">
										<spring:message code="button.search" />
									</button>
								</div>
								<div class="col-xs-2 col-lg-1">
									<a onclick="showLoading()" href="SearchPatrolRoute.do"
										class="btn btn-primary "><spring:message
											code="button.reset" /></a>
								</div>
							</div>
						</div>
						<!-- /.box-body -->
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>

		<div class="row  pull-right">
			<div class="col-xs-12 ">
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.searchRoute')"
					var="viewRoute" />

				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.modifyRoute')"
					var="modifyRoute" />

				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.removeRoute')"
					var="removeRoute" />

				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(viewRoute)}">
							&nbsp; <i class="fa fa-search"></i>
					<spring:message code="search.view" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(modifyRoute)}">
							&nbsp; <i class="fa fa-pencil"></i>
					<spring:message code="search.modify" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(removeRoute)}">
							&nbsp; <i class="fa fa-trash-o"></i>
					<spring:message code="search.remove" />
				</c:if>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body" id="searchTable">
						<!-- load view_searchTable.jsp -->
					</div>
					<!-- /.box-body -->
				</div>
				<!-- /.box -->
			</div>
			<!-- /.col -->
		</div>
	</section>
	<!-- /.content -->
</div>


<div class="modal fade" id="selectLocationModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					<spring:message code="patrol.route.start.location.select" />
				</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" value="0" id="routeMinTime" /> <input
					type="hidden" value="0" id="routeMaxTime" />
				<div class="row">
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<div id="childLocation" class="panel panel-default">

							<!-- load "patrol/view_showChildren.jsp" -->

						</div>

					</div>
					<style>
#hide4SearchRoutePage {
	display: none;
}
</style>
					<div class="col-xs-12 col-lg-6 col-md-6 col-sm-12">
						<div id="selectedLocation" class="panel panel-default">
							<!-- load "patrol/view_showSelectedLocation" -->

						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="refreshStartingLocation();">
					<spring:message code="button.ok" />
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="recoverData()">
					<spring:message code="button.cancel" />
				</button>
			</div>
		</div>
	</div>
</div>
<script>
function RouteNumber(){
var json = $("#patrolSearchForm").serialize();

$.ajax({
	method : "POST",
	url : "RouteNumber.do",
	data : json,
	success : function(data) {
		console.log("Route Number : " + data);
		if(data>0){
			$("#RouteNumberDiv").show();
			$("#totalNumber").text(data);
		}
	},
	error : function(data) {
		alert("error");
	}
});
}

</script>

<script>
	menu_toggle("sub_pm");
	menu_toggle("sub_pm_route");
	menu_select("searchRoute");
</script>
