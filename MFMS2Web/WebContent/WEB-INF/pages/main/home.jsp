<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="../common/left.jsp" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createAccount')"
	var="createAccount" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createAccountGroup')"
	var="createAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createAccountRole')"
	var="createAccountRole" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createCauseCode')"
	var="createCauseCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createDefect')"
	var="createDefect" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createEquipment')"
	var="createEquipment" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createFailureClass')"
	var="createFailureClass" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createLocation')"
	var="createLocation" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createProblemCode')"
	var="createProblemCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createRoute')"
	var="createRoute" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createSite')"
	var="createSite" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createTools')"
	var="createTools" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyAccount')"
	var="modifyAccount" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyAccountGroup')"
	var="modifyAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyAccountRole')"
	var="modifyAccountRole" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyCauseCode')"
	var="modifyCauseCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyDefect')"
	var="modifyDefect" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyEquipment')"
	var="modifyEquipment" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyFailureClass')"
	var="modifyFailureClass" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyLocation')"
	var="modifyLocation" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyProblemCode')"
	var="modifyProblemCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyRoute')"
	var="modifyRoute" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifySite')"
	var="modifySite" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyTools')"
	var="modifyTools" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeAccount')"
	var="removeAccount" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeAccountGroup')"
	var="removeAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeAccountRole')"
	var="removeAccountRole" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeCauseCode')"
	var="removeCauseCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeDefect')"
	var="removeDefect" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeEquipment')"
	var="removeEquipment" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeFailureClass')"
	var="removeFailureClass" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeLocation')"
	var="removeLocation" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeProblemCode')"
	var="removeProblemCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeRoute')"
	var="removeRoute" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeSite')"
	var="removeSite" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeTools')"
	var="removeTools" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolResultReport')"
	var="patrolResultReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccount')"
	var="searchAccount" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccountGroup')"
	var="searchAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccountRole')"
	var="searchAccountRole" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchCauseCode')"
	var="searchCauseCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchDefect')"
	var="searchDefect" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchEquipment')"
	var="searchEquipment" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchFailureClass')"
	var="searchFailureClass" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchLocation')"
	var="searchLocation" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchProblemCode')"
	var="searchProblemCode" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchRoute')"
	var="searchRoute" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchSite')"
	var="searchSite" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchTools')"
	var="searchTools" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.setLocationPrivilage')"
	var="setLocationPrivilage" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.configuration')"
	var="configuration" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyOwnAccount')"
	var="modifyOwnAccount" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.defectListReport')"
	var="defectListReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.defectStatusSummaryReport')"
	var="defectStatusSummaryReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolStaffReport')"
	var="patrolStaffReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolRouteReport')"
	var="patrolRouteReport" />
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

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchPatrolSchedule')"
	var="searchPatrolSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createPatrolSchedule')"
	var="createPatrolSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyPatrolSchedule')"
	var="modifyPatrolSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removePatrolSchedule')"
	var="removePatrolSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchMaintenanceSchedule')"
	var="searchMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createMaintenanceSchedule')"
	var="createMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyMaintenanceSchedule')"
	var="modifyMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeMaintenanceSchedule')"
	var="removeMaintenanceSchedule" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.patrolMonitoring')"
	var="patrolMonitor" />

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_home_b"></i> <spring:message
					code="menu.home" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">

	<c:if test="${currRole.grantedPrivilegeCodeList.contains(searchDefect)
			           || currRole.grantedPrivilegeCodeList.contains(createDefect)
			           || currRole.grantedPrivilegeCodeList.contains(defectListReport)
			           || currRole.grantedPrivilegeCodeList.contains(defectStatusSummaryReport)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="DefectManagement.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/defect_management.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.defectMgt" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchRoute)
			           || currRole.grantedPrivilegeCodeList.contains(createRoute)
			           || currRole.grantedPrivilegeCodeList.contains(patrolMonitor)
			           || currRole.grantedPrivilegeCodeList.contains(patrolStaffReport)
			           || currRole.grantedPrivilegeCodeList.contains(patrolResultReport)
			           || currRole.grantedPrivilegeCodeList.contains(modifyPatrolSchedule)
					   || currRole.grantedPrivilegeCodeList.contains(createPatrolSchedule)
					   || currRole.grantedPrivilegeCodeList.contains(searchPatrolSchedule)
			           || currRole.grantedPrivilegeCodeList.contains(removePatrolSchedule)}">
				<div class="col-lg-4  col-sm-6 iconStyle">

					<a href="PatrolManagement.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/patrol_management.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.patrolMgt" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>


			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)
			           || currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="DefectSchedule.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/maintenance_schedule.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.defectMgt.schedule" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>


			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchAccountRole)
			           || currRole.grantedPrivilegeCodeList.contains(createAccountRole)
			           || currRole.grantedPrivilegeCodeList.contains(searchAccount)
			           || currRole.grantedPrivilegeCodeList.contains(createAccount)
			           || currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)
			           || currRole.grantedPrivilegeCodeList.contains(createAccountGroup)
			           || currRole.grantedPrivilegeCodeList.contains(modifyOwnAccount)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="AccountManagement.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/account_management.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.accountMgt" />
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
                           || currRole.grantedPrivilegeCodeList.contains(searchLocation)
                           || currRole.grantedPrivilegeCodeList.contains(createLocation)
                           || currRole.grantedPrivilegeCodeList.contains(createEquipment)
                           || currRole.grantedPrivilegeCodeList.contains(accountActionLog)
                           || currRole.grantedPrivilegeCodeList.contains(defectActionLog)
			               || currRole.grantedPrivilegeCodeList.contains(maintenanceScheduleActionLog)
			               || currRole.grantedPrivilegeCodeList.contains(patrolRouteActionLog)
			               || currRole.grantedPrivilegeCodeList.contains(patrolScheduleActionLog)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="AdministrationManagement.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/administration.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			<div class="col-lg-4  col-sm-6 iconStyle">
				<a href="SiteSelect.do"><div class=" well">
						<img class="img-responsive" src="import/img2/switch_site.png"
							alt="">
						<h2 class="iconName">
							<spring:message code="menu.switchSite" />
						</h2>
					</div></a>
			</div>
		</div>

	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script>
	menu_select("sub_home");
</script>
