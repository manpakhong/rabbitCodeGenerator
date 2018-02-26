<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_patrol_management_b"></i> <spring:message
					code="menu.punchcardMgt" /></li>
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

	</section>
</div>