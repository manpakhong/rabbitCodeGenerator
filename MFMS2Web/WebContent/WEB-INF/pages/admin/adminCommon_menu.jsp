<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="../common/left.jsp" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchLocation')"
	var="searchLocation" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createLocation')"
	var="createLocation" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.configuration')"
	var="configuration" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AdministrationManagement.do"><i
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.administration" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.common" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchLocation)
		                       || currRole.grantedPrivilegeCodeList.contains(createLocation)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="Location.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/admin/location.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.common.location" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>
		</div>

	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script>
	menu_toggle("sub_admin");
	menu_toggle("sub_admin_common", 1);
</script>
