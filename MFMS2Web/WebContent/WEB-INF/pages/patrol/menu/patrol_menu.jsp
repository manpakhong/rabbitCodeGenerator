<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchRoute')"
	var="searchRoute" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createRoute')"
	var="createRoute" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolResultReport')"
	var="patrolResultReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolStaffReport')"
	var="patrolStaffReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolRouteReport')"
	var="patrolRouteReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolMonitoring')"
	var="patrolMonitor" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchPatrolSchedule')"
	var="searchPatrolSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createPatrolSchedule')"
	var="createPatrolSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyPatrolSchedule')"
	var="modifyPatrolSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removePatrolSchedule')"
	var="removePatrolSchedule" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_patrol_management_b"></i> <spring:message
					code="menu.patrolMgt" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolMonitor)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolMonitor.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_monitor.png" " alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.monitor" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(modifyPatrolSchedule)
					   || currRole.grantedPrivilegeCodeList.contains(createPatrolSchedule)
					   || currRole.grantedPrivilegeCodeList.contains(searchPatrolSchedule)
			           || currRole.grantedPrivilegeCodeList.contains(removePatrolSchedule)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolAssign.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_schedule.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.schedule" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchRoute)
			           || currRole.grantedPrivilegeCodeList.contains(createRoute)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolRoute.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/pm/patrol_route.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.route" />
							</h2>
						</div>
					</a>
				</div>

			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolStaffReport)
			           || currRole.grantedPrivilegeCodeList.contains(patrolResultReport)
			           || currRole.grantedPrivilegeCodeList.contains(patrolRouteReport)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolReport.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_reports.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.report" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			
				<!-- 26-6-2017  -->
				<!--  
				<div class="col-lg-4 col-sm-6 iconStyle" style="display:none;">
					<a href="PatrolPhoto.do">
						<div class="well">
						<img class="img-responsive"
								src="import/img2/redPanda.jpg" alt="">
						<h2 class="iconName">Patrol Photo</h2>
						</div>
					</a>		
				</div>
				--> 
			  
		</div>

	</section>
</div>

<script>
	menu_toggle("sub_pm", 1);
</script>
