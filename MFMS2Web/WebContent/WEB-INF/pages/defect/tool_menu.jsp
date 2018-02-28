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
					code="menu.administration.defectMgt.tools" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.createTools')"
				var="createTools" />
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.searchTools')"
				var="searchTools" />

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchTools)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchTool.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/tools_search.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.defectMgt.tools.search" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createTools)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CreateTool.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/tools_create.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.defectMgt.tools.create" />
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
	menu_toggle("sub_admin_tool", 1);
</script>
