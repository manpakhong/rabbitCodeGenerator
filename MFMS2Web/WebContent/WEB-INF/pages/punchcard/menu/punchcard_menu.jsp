<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />


<link rel="stylesheet" href="import/css/patrol/patrol_style.css">

<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<link rel="stylesheet"
	href="import/fullcalendar-3.4.0/lib/cupertino/jquery-ui.min.css">

<script src="import/jQueryUI/jquery-ui.js"></script>
<script src="import/datetimepicker/moment.js"></script>
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>
<script src="import/customLoading.js"></script>
<script src="import/punchcard/punchcard.js"></script>
<!-- <script src="import/punchcard/viewclockin.js"></script> -->



<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_patrol_management_b"></i>
				<spring:message code="menu.punchcardMgt" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<%-- 			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolMonitor)}"> --%>
			<div class="col-lg-4  col-sm-6 iconStyle">
				<a class="btn" onclick="openSelectRouteModal();">
					<div class=" well">
						<img class="img-responsive" src="import/img2/pcm/clock_in.png"
							alt="">
 						<h2 class="iconName">
							<spring:message code="menu.punchcardMgt.clockin" />
						</h2> 
					</div>
				</a>
			</div>
			<%-- 			</c:if> --%>
			<%-- 			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(modifyPatrolSchedule)
					   || currRole.grantedPrivilegeCodeList.contains(createPatrolSchedule)
					   || currRole.grantedPrivilegeCodeList.contains(searchPatrolSchedule)
			           || currRole.grantedPrivilegeCodeList.contains(removePatrolSchedule)}"> --%>

			<div class="col-lg-4  col-sm-6 iconStyle">
				<a href="PatrolAssign.do">
					<div class=" well">
						<img class="img-responsive" src="import/img2/pcm/clock_out.png"
							alt="">
 						<h2 class="iconName">
							<spring:message code="menu.punchcardMgt.clockout" />
						</h2> 
					</div>
				</a>
			</div>
			<%-- 			</c:if> --%>

			<div class="col-lg-4  col-sm-6 iconStyle">
				<a href="Home.do?roleKey=2">
					<div class=" well">
						<img class="img-responsive" src="import/img2/pcm/skip.png" alt="">
 						<h2 class="iconName">
							<spring:message code="menu.punchcardMgt.skip" />
						</h2> 
					</div>
				</a>
			</div>
<!-- 			<input type="button" value="test" onclick="openSelectRouteModal();" /> -->
 			<div id="testDivButton"></div> 
		</div>
	</section>
</div>


<div id="clockIn">
</div>

<script>
	/* 	menu_toggle("sub_pm", 1); */
</script>
