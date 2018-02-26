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
			<li><a href="AdministrationDefect.do"> <spring:message
						code="menu.administration.defectMgt" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.defectMgt.problemCode" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.createProblemCode')"
				var="createProblemCode" />
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.searchProblemCode')"
				var="searchProblemCode" />

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchProblemCode)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchProblemCode.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/problem_code_search.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.defectMgt.problemCode.search" />
							</h2>
						</div>
					</a>
				</div>

			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createProblemCode)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CreateProblemCode.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/problem_code_create.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.defectMgt.problemCode.create" />
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
	menu_toggle("sub_admin_defect");
	menu_toggle("sub_admin_problemcode", 1);
</script>
