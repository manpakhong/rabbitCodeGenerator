<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">
<script src="import/customLoading.js"></script>


<script>
	var mode = "${mode}";

	$(document).ready(
				function() {
						
						$.ajaxSetup({
							cache : false,
						});

						if (mode === "view") {							
							$("#routeSelect").attr('multiple', false);
							var routeKey = document.getElementById('hidden_routeDefKey').value;
							var url = window.location.href;
							var n = url.search("date=");
							if(n!=-1){
								var date = url.substring(url.indexOf("date")+5);
							} else{
								var date="";
							}
							$("#viewPatrolPhoto").load("ShowPatrolPhoto.do?routeDefKey=" + routeKey + "&date=" + date);
						}
						setAsPatrolPhoto();
						
						
						$('select').on('change', function() {
							  //alert( this.value );
							  $("#hidden_routeDefKey").val(this.value);					
							  
							  var routeKey = document.getElementById('hidden_routeDefKey').value;
							  var date = document.getElementById('view_photo_date').value;
							  //window.location.href = "PatrolPhoto.do?routeKey=" + routeKey + "&date=" + date;
							  
							  		window.location.href = "PatrolPhoto.do?routeKey=" + routeKey;
									
							  $("#viewPatrolPhoto").load("ShowPatrolPhoto.do?routeDefKey=" + routeKey + "&date=" + date);
							});
						$("#routeSelect").val($("#hidden_routeDefKey").val());
						
					});

</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="PatrolManagement.do"><i class="icon-icon_patrol_management_b"></i> 
				<spring:message code="menu.patrolMgt" />
			</a></li>
			<li><a href="PatrolRoute.do"> <spring:message
						code="menu.patrolMgt.patrol.photo" /></a>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-lg-12">
				<div class="box">
					<form:form id="patrolPhotoSearchForm" cssClass="form-horizontal"
						commandName="patrolPhotoSearchForm" method="post" action="">

						<form:hidden path="routeDefKey" id="hidden_routeDefKey" />

						<div class="box-body">	
							<div class="row">
								<label style="white-space: nowrap;"
									class="col-lg-2 control-label">Route</label>
								<div class="col-lg-4">
									<form:select id="routeSelect" path="routeMap"
										cssClass="form-control" items="${routeList}">
									</form:select>
								</div>
							
							</div>					


							<div class="box-footer">
								<div class="col-lg-1 col-xs-2">
									
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



<script>
	menu_toggle("sub_pm");
	menu_toggle("sub_pm_route");
	function setAsPatrolPhoto() {

		menu_select("patrolPhoto");
	}

</script>
