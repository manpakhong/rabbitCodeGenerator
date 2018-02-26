<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AdministrationManagement.do"><i
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.administration" /></a></li>
			<%-- <li><a href="AdministrationCommon.do"> <spring:message code="menu.administration.common"/></a></li> --%>
			<li class="active"><spring:message
					code="menu.administration.common.location" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.createLocation')"
				var="createLocation" />
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.searchLocation')"
				var="searchLocation" />

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchLocation)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchLocation.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/location_search.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.common.location.search" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createLocation)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CreateLocation.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/location_create.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.common.location.create" />
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
	menu_toggle("sub_admin_location", 1);
</script>
