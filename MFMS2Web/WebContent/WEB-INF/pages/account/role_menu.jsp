<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AccountManagement.do"><i
					class="icon-icon_account_management_b"></i> <spring:message
						code="menu.accountMgt" /></a></li>
			<li class="active"><spring:message code="menu.accountMgt.role" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.createAccountRole')"
				var="createAccountRole" />
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.searchAccountRole')"
				var="searchAccountRole" />
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchAccountRole)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchRole.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/am/role_search.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.role.search" />
							</h2>
						</div>
					</a>
				</div>

			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createAccountRole)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CreateRole.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/am/role_create.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.role.create" />
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
	menu_toggle("sub_am");
	menu_toggle("sub_am_role", 1);
</script>
