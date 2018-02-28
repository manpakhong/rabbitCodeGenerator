<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createDefect')"
	var="createDefect" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchDefect')"
	var="searchDefect" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="DefectManagement.do"><i
					class="icon-icon_defect_management_b"></i> <spring:message
						code="menu.defectMgt" /></a></li>
			<li class="active"><spring:message code="menu.defectMgt.defect" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">


			


			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchDefect)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchDefect.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/dm/defect_search.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.defectMgt.defect.search" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createDefect)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CreateDefect.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/dm/defect_create.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.defectMgt.defect.create" />
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
	menu_toggle("sub_dm");
	menu_toggle("sub_dm_defect", 1);
</script>
