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
			<li class="active"><spring:message
					code="menu.accountMgt.accountGroup" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.createAccountGroup')"
				var="createAccountGroup" />
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.searchAccountGroup')"
				var="searchAccountGroup" />

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchAccountGroup.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/am/account_group_search.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.accountGroup.search" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createAccountGroup)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CreateAccountGroup.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/am/account_group_create.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.accountGroup.create" />
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
	menu_toggle("sub_am_group", 1);
</script>
