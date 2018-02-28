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

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="PatrolManagement.do"><i
					class="icon-icon_patrol_management_b"></i> <spring:message
						code="menu.patrolMgt" /></a></li>
			<li class="active"><spring:message code="menu.patrolMgt.route" /></a></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchRoute)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchPatrolRoute.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_route_search.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.route.search" />
							</h2>
						</div>
					</a>
				</div>

			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createRoute)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="PatrolCreate.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/pm/patrol_route_create.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt.route.create" />
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
	menu_toggle("sub_pm_route", 1);
</script>
