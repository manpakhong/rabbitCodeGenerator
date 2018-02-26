<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolResultReport')"
	var="patrolResultReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolStaffReport')"
	var="patrolStaffReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolRouteReport')"
	var="patrolRouteReport" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="PatrolManagement.do"><i
					class="icon-icon_patrol_management_b"></i> <spring:message
						code="menu.patrolMgt" /></a></li>
			<li class="active"><spring:message code="menu.patrolMgt.report" /></a></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolResultReport)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolResultReport.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_route_report.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.report.result" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolStaffReport)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolStaffReport.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_staff_reports.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.report.staff" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolRouteReport)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolRouteReport.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_result_report.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.report.route" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

		</div>
	</section>
</div>

<script>
	menu_toggle("sub_pm");
	menu_toggle("sub_pm_report", 1);
</script>
