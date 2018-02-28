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
	expression="@privilegeMap.getProperty('privilege.code.searchCauseCode')"
	var="searchCauseCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchEquipment')"
	var="searchEquipment" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchFailureClass')"
	var="searchFailureClass" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchProblemCode')"
	var="searchProblemCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchTools')"
	var="searchTools" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createCauseCode')"
	var="createCauseCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createEquipment')"
	var="createEquipment" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createFailureClass')"
	var="createFailureClass" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createProblemCode')"
	var="createProblemCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createTools')"
	var="createTools" />


<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.accountActionLog')"
	var="accountActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.defectActionLog')"
	var="defectActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.maintenanceScheduleActionLog')"
	var="maintenanceScheduleActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolRouteActionLog')"
	var="patrolRouteActionLog" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolScheduleActionLog')"
	var="patrolScheduleActionLog" />


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_administration_b"></i> <spring:message
					code="menu.administration" /></li>
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
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchFailureClass)
		                       || currRole.grantedPrivilegeCodeList.contains(createFailureClass)
		                       || currRole.grantedPrivilegeCodeList.contains(searchProblemCode)
		                       || currRole.grantedPrivilegeCodeList.contains(createProblemCode)
		                       || currRole.grantedPrivilegeCodeList.contains(searchCauseCode)
		                       || currRole.grantedPrivilegeCodeList.contains(createCauseCode)
		                       || currRole.grantedPrivilegeCodeList.contains(searchTools)
		                       || currRole.grantedPrivilegeCodeList.contains(createTools)
		                       || currRole.grantedPrivilegeCodeList.contains(searchEquipment)
		                       || currRole.grantedPrivilegeCodeList.contains(createEquipment)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="AdministrationDefect.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/defect_administration.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.defectMgt" />
							</h2>
						</div>
					</a>
				</div>

			</c:if>


			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(accountActionLog)
		                       || currRole.grantedPrivilegeCodeList.contains(defectActionLog)
		                       || currRole.grantedPrivilegeCodeList.contains(maintenanceScheduleActionLog)
		                       || currRole.grantedPrivilegeCodeList.contains(patrolRouteActionLog)
		                       || currRole.grantedPrivilegeCodeList.contains(patrolScheduleActionLog)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="ActivityLog.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/action_log.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.log" />
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
	menu_toggle("sub_admin", 1);
</script>
