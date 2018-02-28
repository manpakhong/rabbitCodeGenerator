<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.accountActionLog')"
	var="accountActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.defectActionLog')"
	var="defectActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.maintenanceScheduleActionLog')"
	var="maintenanceScheduleActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolRouteActionLog')"
	var="patrolRouteActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolScheduleActionLog')"
	var="patrolScheduleActionLog" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AdministrationManagement.do"><i
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.administration" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.log" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(accountActionLog)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchAccountAction.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/actionLog/account.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.log.account" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(defectActionLog)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchDefectAction.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/actionLog/defect.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.log.defect" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(maintenanceScheduleActionLog)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchDefectScheduleAction.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/actionLog/mschedule.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.log.defect.schedule" />
							</h2>
						</div>
					</a>
				</div>

			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolRouteActionLog)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchRouteDefAction.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/actionLog/patrol.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.log.route" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(patrolScheduleActionLog)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchPatrolScheduleAction.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/actionLog/pschedule.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.log.route.schedule" />
							</h2>
						</div>
					</a>
				</div>

			</c:if>

		</div>

	</section>
</div>
<!-- /.content-wrapper -->

<script>
	menu_toggle("sub_admin");
	menu_toggle("sub_admin_activityLog", 1);
</script>
