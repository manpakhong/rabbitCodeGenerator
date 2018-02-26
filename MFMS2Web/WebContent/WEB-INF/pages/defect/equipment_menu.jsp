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
					code="menu.administration.defectMgt.equipment" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.createEquipment')"
				var="createEquipment" />
			<spring:eval
				expression="@privilegeMap.getProperty('privilege.code.searchEquipment')"
				var="searchEquipment" />

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchEquipment)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="SearchEquipment.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/equipment_search.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.defectMgt.equipment.search" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(createEquipment)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CreateEquipment.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/equipment_create.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.defectMgt.equipment.create" />
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
	menu_toggle("sub_admin_equipment", 1);
</script>
