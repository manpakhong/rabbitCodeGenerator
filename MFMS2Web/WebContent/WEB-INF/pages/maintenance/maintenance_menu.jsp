<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<spring:eval expression="@privilegeMap.getProperty('privilege.code.maintenanceSchedule')" var="maintenanceSchedule"/>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_maintenance_schedule_b"></i> <spring:message code="menu.defectMgt.schedule"/></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
			<div class="row">
 				<c:if test="${currRole.grantedPrivilegeCodeList.contains(maintenanceSchedule)}">
				<div class="col-lg-4 col-sm-6">
					<div class="row">
						<div class="col-lg-12">
							<a href="DefectSchedule.do"><img class="img-responsive"
								src="import/img2/maintenance_schedule.png" alt=""></a>
						</div>
						<div class="col-lg-12">
							<h2 class="iconName"><spring:message code="menu.defectMgt.schedule"/></h2>
						</div>
					</div>
				</div>
				</c:if>
			</div>
	</section>
</div>
<!-- /.content-wrapper -->

<script>  
  menu_toggle("sub_ms");
  menu_toggle("sub_ms_schedule",1);
</script>
