<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_account_management_b"></i>
				<spring:message code="menu.accountMgt" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<spring:eval
			expression="@privilegeMap.getProperty('privilege.code.createAccount')"
			var="createAccount" />
		<spring:eval
			expression="@privilegeMap.getProperty('privilege.code.searchAccount')"
			var="searchAccount" />
		<spring:eval
			expression="@privilegeMap.getProperty('privilege.code.createAccountGroup')"
			var="createAccountGroup" />
		<spring:eval
			expression="@privilegeMap.getProperty('privilege.code.searchAccountGroup')"
			var="searchAccountGroup" />
		<spring:eval
			expression="@privilegeMap.getProperty('privilege.code.createAccountRole')"
			var="createAccountRole" />
		<spring:eval
			expression="@privilegeMap.getProperty('privilege.code.searchAccountRole')"
			var="searchAccountRole" />
		<spring:eval
			expression="@privilegeMap.getProperty('privilege.code.modifyOwnAccount')"
			var="modifyOwnAccount" />

		<div class="row">
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createAccountRole) or currRole.grantedPrivilegeCodeList.contains(searchAccountRole)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="Role.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/am/role.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.role" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createAccount) or currRole.grantedPrivilegeCodeList.contains(searchAccount)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="Account.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/am/account.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.account" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createAccountGroup) or currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="AccountGroup.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/am/account_group.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.accountGroup" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(modifyOwnAccount)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="ModifyOwnAccount.do?loginId=${user.loginId}">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/am/modify_own_account.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt.modifyOwnAccount" />
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
	menu_toggle("sub_am", 1);
</script>