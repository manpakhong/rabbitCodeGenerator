<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:set var="company">
	<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
</c:set>

<script type="text/javascript">
	$(window).resize(function() {
		//$('.main-sidebar').height($(".content-wrapper").height());
	});

	function checkSession(){
		$.ajax({
			method : "POST",
			url:"checkSession.do",
			success : function(data){
				//redirect to login page when check session fail
				console.log("check session : " + data);
				if(data=="fail"){
					showLoading();
					window.location.href = "Login.do";
				}
			}
		});
	}
	
	
	$(document).ready(function () {
		//listen to the follow ui, check the session on every action
	    $(document).on('click', 'button, span, a', function () {
	    	checkSession();
	    });
	});
</script>

<c:choose>
	<c:when test="${currRole.siteKey == 1}">
		<c:set var="siteSelected" value="false" />
	</c:when>
	<c:when test="${empty currRole}">
		<c:set var="siteSelected" value="false" />
	</c:when>
	<c:otherwise>
		<c:set var="siteSelected" value="true" />
	</c:otherwise>
</c:choose>



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
	expression="@privilegeMap.getProperty('privilege.code.alarmConsole')"
	var="alarmConsole" />
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
	expression="@privilegeMap.getProperty('privilege.code.patrolMonitoring')"
	var="patrolMonitor" />

<spring:eval
		expression="@privilegeMap.getProperty('privilege.code.viewOwnerInformation')"
		var="viewOwnerInformation"/>

<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">
		<ul class="sidebar-menu">

			<c:if test="${siteSelected == 'true'}">
				<li id="sub_home"><a href="Home.do"><i
						class="icon-icon_home_b"></i> <span><spring:message
								code="menu.home" /></span></a>
			</c:if>
			<c:if test="${isSysAdmin == 'true'}">
				<li class="treeview"><a href="#"
					onClick="location.href='Sysadmin.do'"> <i
						class="icon-icon_administration_b"></i> <span><spring:message
								code="menu.systemMgt" /></span> <i class="fa fa-angle-left pull-right"></i>
				</a>
					<ul id="sub_system" class="treeview-menu">
						<li class="treeview"><a href="#"
							onClick="location.href='Site.do'"> <spring:message
									code="menu.systemMgt.site" /> <i
								class="fa fa-angle-left pull-right"></i></a>
							<ul id="sub_system_site" class="treeview-menu">
								<li id="searchSite"><a href="SearchSite.do"><spring:message
											code="menu.systemMgt.site.search" /></a></li>
								<li id="createSite"><a href="CreateSite.do"><spring:message
											code="menu.systemMgt.site.create" /></a></li>
							</ul></li>
						<!-- 
				<li>
				  <a href="#">
				    <i class="fa fa-group"></i>Account
				  </a>
				</li>
				 -->
					</ul></li>
			</c:if>

			<c:if test="${siteSelected == 'true'}">
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(searchDefect)
			           || currRole.grantedPrivilegeCodeList.contains(createDefect)
			           || currRole.grantedPrivilegeCodeList.contains(alarmConsole)
			           || currRole.grantedPrivilegeCodeList.contains(defectListReport)
			           || currRole.grantedPrivilegeCodeList.contains(defectStatusSummaryReport)}">
					<li class="treeview"><a href="#"
						onClick="location.href='DefectManagement.do'"> <i
							class="icon-icon_defect_management_b"></i> <span><spring:message
									code="menu.defectMgt" /></span> <i
							class="fa fa-angle-left pull-right"></i>
					</a>
						<ul id="sub_dm" class="treeview-menu">
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchDefect)
			               || currRole.grantedPrivilegeCodeList.contains(createDefect)}">
								<li><a href="#" onClick="location.href='Defect.do'"> <spring:message
											code="menu.defectMgt.defect" /> <i
										class="fa fa-angle-left pull-right"></i></a>
									<ul id="sub_dm_defect" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchDefect)}">
											<li id="searchDefect"><a href="SearchDefect.do"> <spring:message
														code="menu.defectMgt.defect.search" /></a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(createDefect)}">
											<li id="createDefect"><a href="CreateDefect.do"> <spring:message
														code="menu.defectMgt.defect.create" /></a></li>
										</c:if>
									</ul></li>
							</c:if>


							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(alarmConsole)}">
								<li id="sub_dm_alarmConsole"><a href="AlarmConsole.do">
								<c:choose>
									<c:when test="${company=='jec'}">
     									<spring:message code="menu.alarmconsole.jec" />
     								</c:when>
     								<c:otherwise>
     									<spring:message code="menu.alarmconsole" />
     								</c:otherwise>
     							</c:choose>
								</a></li>
							</c:if>

							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(defectListReport)
			               || currRole.grantedPrivilegeCodeList.contains(defectStatusSummaryReport)}">
								<li><a href="#" onClick="location.href='DefectReport.do'">
										<spring:message code="menu.defectMgt.report" /> <i
										class="fa fa-angle-left pull-right"></i>
								</a>
									<ul id="sub_dm_report" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(defectListReport)}">
											<li id="defectList"><a href="DefectList.do"> <spring:message
														code="menu.defectMgt.report.defectList" /></a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(defectStatusSummaryReport)}">
											<li id="defectStatusSummary"><a
												href="DefectStatusSummary.do"> <spring:message
														code="menu.defectMgt.report.summary" /></a></li>
										</c:if>
									</ul></li>
							</c:if>

							<c:if test="${currRole.grantedPrivilegeCodeList.contains(\"ViewOwnerInfomation\")}">
								<li id="st_demo">
									<a href="STDemo.do"><spring:message code="menu.ownerinfo"/></a>
								</li>
							</c:if>
						</ul></li>
				</c:if>
			</c:if>
			<c:if test="${siteSelected == 'true'}">
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(searchRoute)
			           || currRole.grantedPrivilegeCodeList.contains(createRoute)
			           || currRole.grantedPrivilegeCodeList.contains(patrolStaffReport)
			           || currRole.grantedPrivilegeCodeList.contains(patrolResultReport)
			           || currRole.grantedPrivilegeCodeList.contains(patrolRouteReport)
			           || currRole.grantedPrivilegeCodeList.contains(patrolMonitor)
					   || currRole.grantedPrivilegeCodeList.contains(removePatrolSchedule)
			           || currRole.grantedPrivilegeCodeList.contains(searchPatrolSchedule)}">
					<li class="treeview"><a href="#"
						onClick="location.href='PatrolManagement.do'"> <!-- <i class="fa fa-shield"> -->
							<i class="icon-icon_patrol_management_b"></i> <span><spring:message
									code="menu.patrolMgt" /></span> <i
							class="fa fa-angle-left pull-right"></i>
					</a>
						<ul id="sub_pm" class="treeview-menu">
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(patrolMonitor)}">
								<li id="patrolMonitor"><a href="PatrolMonitor.do"> <spring:message
											code="menu.patrolMgt.monitor" /></a></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchPatrolSchedule)
		                           || currRole.grantedPrivilegeCodeList.contains(createPatrolSchedule)
					               || currRole.grantedPrivilegeCodeList.contains(modifyPatrolSchedule)
					               || currRole.grantedPrivilegeCodeList.contains(removePatrolSchedule)}">
								<li id="patrolSchedule"><a href="PatrolAssign.do"> <spring:message
											code="menu.patrolMgt.schedule" /></a></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchRoute)
			             		|| currRole.grantedPrivilegeCodeList.contains(createRoute)}">
								<li><a href="#" onClick="location.href='PatrolRoute.do'">
										<spring:message code="menu.patrolMgt.route" /> <i
										class="fa fa-angle-left pull-right"></i></a>
									<ul id="sub_pm_route" class="treeview-menu">
								<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchRoute)}">
										<li id="searchRoute"><a href="SearchPatrolRoute.do"><spring:message
													code="menu.patrolMgt.route.search" /></a></li>
								</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(createRoute)}">
										<li id="createRoute"><a href="PatrolCreate.do"><spring:message
													code="menu.patrolMgt.route.create" /></a></li>
							</c:if>
									</ul></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(patrolStaffReport)
			               || currRole.grantedPrivilegeCodeList.contains(patrolResultReport)
			               || currRole.grantedPrivilegeCodeList.contains(patrolRouteReport)}">
								<li><a href="#" onClick="location.href='PatrolReport.do'">
										<spring:message code="menu.patrolMgt.report" /> <i
										class="fa fa-angle-left pull-right"></i>
								</a>
									<ul id="sub_pm_report" class="treeview-menu">
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(patrolResultReport)}">
										<li id="patrolResult"><a href="PatrolResultReport.do">
												<spring:message code="menu.patrolMgt.report.result" />
										</a></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(patrolStaffReport)}">
										<li id="patrolStaff"><a href="PatrolStaffReport.do">
												<spring:message code="menu.patrolMgt.report.staff" />
										</a></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(patrolRouteReport)}">
										<li id="patrolRoute"><a href="PatrolRouteReport.do">
												<spring:message code="menu.patrolMgt.report.route" />
										</a></li>
							</c:if>
									</ul></li>
							</c:if>
							<!-- 26-06-2017 -->
							<c:if test='true'>
								<li id="patrolPhoto" style="display: none"><a href="#" onClick="location.href='PatrolPhoto.do'">
										Patrol Photo 
								</a>
							</c:if>
							
						</ul></li>
				</c:if>
			</c:if>

			<c:if test="${siteSelected == 'true'}">
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(searchMaintenanceSchedule)
			           || currRole.grantedPrivilegeCodeList.contains(createMaintenanceSchedule)}">
					<li id="maintenanceSchedule"><a href="#"
						onClick="location.href='DefectSchedule.do'"> <i
							class="icon-icon_maintenance_schedule_b"></i> <span> <spring:message
									code="menu.defectMgt.schedule" />
						</span>
					</a></li>
				</c:if>
			</c:if>

			<c:if test="${siteSelected == 'true'}">
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(searchAccountRole)
			           || currRole.grantedPrivilegeCodeList.contains(createAccountRole)
			           || currRole.grantedPrivilegeCodeList.contains(searchAccount)
			           || currRole.grantedPrivilegeCodeList.contains(createAccount)
			           || currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)
			           || currRole.grantedPrivilegeCodeList.contains(createAccountGroup)
			           || currRole.grantedPrivilegeCodeList.contains(modifyOwnAccount)}">
					<li class="treeview"><a href="#"
						onClick="location.href='AccountManagement.do'"> <i
							class="icon-icon_account_management_b"></i> <span><spring:message
									code="menu.accountMgt" /></span> <i
							class="fa fa-angle-left pull-right"></i>
					</a>
						<ul id="sub_am" class="treeview-menu">
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchAccountRole) 
		                 || currRole.grantedPrivilegeCodeList.contains(createAccountRole)}">
								<li class="treeview"><a href="#"
									onClick="location.href='Role.do'"> <spring:message
											code="menu.accountMgt.role" /> <i
										class="fa fa-angle-left pull-right"></i></a>
									<ul id="sub_am_role" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchAccountRole)}">
											<li id="searchRole"><a href="SearchRole.do"><spring:message
														code="menu.accountMgt.role.search" /></a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(createAccountRole)}">
											<li id="createRole"><a href="CreateRole.do"><spring:message
														code="menu.accountMgt.role.create" /></a></li>
										</c:if>
									</ul></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchAccount) 
 		                 || currRole.grantedPrivilegeCodeList.contains(createAccount)}">
								<li><a href="#" onClick="location.href='Account.do'"><spring:message
											code="menu.accountMgt.account" /> <i
										class="fa fa-angle-left pull-right"></i></a>
									<ul id="sub_am_account" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchAccount)}">
											<li id="searchAccount"><a href="SearchAccount.do"><spring:message
														code="menu.accountMgt.account.search" /></a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(createAccount)}">
											<li id="createAccount"><a href="CreateAccount.do"><spring:message
														code="menu.accountMgt.account.create" /></a></li>
										</c:if>
									</ul></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)
		                 || currRole.grantedPrivilegeCodeList.contains(createAccountGroup)}">
								<li><a href="#" onClick="location.href='AccountGroup.do'"><spring:message
											code="menu.accountMgt.accountGroup" /> <i
										class="fa fa-angle-left pull-right"></i></a>
									<ul id="sub_am_group" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)}">
											<li id="listAccountGroup"><a
												href="SearchAccountGroup.do"><spring:message
														code="menu.accountMgt.accountGroup.search" /></a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(createAccountGroup)}">
											<li id="createAccountGroup"><a
												href="CreateAccountGroup.do"><spring:message
														code="menu.accountMgt.accountGroup.create" /></a></li>
										</c:if>
									</ul></li>
							</c:if>
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(modifyOwnAccount) }">
								<!-- <li><a href="#"> Account Location Privilege</a></li>  -->
								<li id="modifyOwnAccount"><a href="#"
									onClick="location.href='ModifyOwnAccount.do?loginId=${user.loginId}'"><spring:message
											code="menu.accountMgt.modifyOwnAccount" /></a></li>
							</c:if>
						</ul></li>
				</c:if>
			</c:if>
			<c:if test="${siteSelected == 'true'}">
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
					<li class="treeview"><a href="#"
						onClick="location.href='AdministrationManagement.do'"> <i
							class="icon-icon_administration_b"></i> <span><spring:message
									code="menu.administration" /></span> <i
							class="fa fa-angle-left pull-right"></i>
					</a>
						<ul id="sub_admin" class="treeview-menu">
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(searchLocation)
		                   || currRole.grantedPrivilegeCodeList.contains(createLocation)}">
								<li><a href="#" onClick="location.href='Location.do'">
										<spring:message code="menu.administration.common.location" />
										<i class="fa fa-angle-left pull-right"></i>
								</a>
									<ul id="sub_admin_location" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchLocation)}">
											<li id="searchLocation"><a href="SearchLocation.do">
													<spring:message
														code="menu.administration.common.location.search" />
											</a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(createLocation)}">
											<li id="createLocation"><a href="CreateLocation.do">
													<spring:message
														code="menu.administration.common.location.create" />
											</a></li>
										</c:if>
									</ul></li>
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
								<li><a href="#"
									onClick="location.href='AdministrationDefect.do'"> <spring:message
											code="menu.administration.defectMgt" /> <i
										class="fa fa-angle-left pull-right"></i></a>
									<ul id="sub_admin_defect" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchFailureClass)
											|| currRole.grantedPrivilegeCodeList.contains(createFailureClass)}">
											<li><a href="#"
												onClick="location.href='FailureClass.do'"> <spring:message
														code="menu.administration.defectMgt.failureClass" /><i
													class="fa fa-angle-left pull-right"></i>
											</a>
												<ul id="sub_admin_failureclass" class="treeview-menu">
													<c:if
														test="${currRole.grantedPrivilegeCodeList.contains(searchFailureClass)}">
													<li id="searchFailureClass"><a
														href="SearchFailureClass.do"> <spring:message
																code="menu.administration.defectMgt.failureClass.search" /></a>
													</li>
													</c:if>
													<c:if
														test="${currRole.grantedPrivilegeCodeList.contains(createFailureClass)}">
													<li id="createFailureClass"><a
														href="CreateFailureClass.do"> <spring:message
																code="menu.administration.defectMgt.failureClass.create" /></a>
													</li>
													</c:if>
												</ul></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchProblemCode)
		                       				|| currRole.grantedPrivilegeCodeList.contains(createProblemCode)}">
											<li><a href="#" onClick="location.href='ProblemCode.do'">
													<spring:message
														code="menu.administration.defectMgt.problemCode" /><i
													class="fa fa-angle-left pull-right"></i>
											</a>
												<ul id="sub_admin_problemcode" class="treeview-menu">
													<c:if
														test="${currRole.grantedPrivilegeCodeList.contains(searchProblemCode)}">
													<li id="searchProblemCode"><a
														href="SearchProblemCode.do"> <spring:message
																code="menu.administration.defectMgt.problemCode.search" /></a>
													</li>
													</c:if>
													<c:if
														test="${currRole.grantedPrivilegeCodeList.contains(createProblemCode)}">
													<li id="createProblemCode"><a
														href="CreateProblemCode.do"> <spring:message
																code="menu.administration.defectMgt.problemCode.create" /></a>
													</li>
													</c:if>
												</ul></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchCauseCode)
		                       				|| currRole.grantedPrivilegeCodeList.contains(createCauseCode)}">
											<li><a href="#" onClick="location.href='CauseCode.do'">
													<spring:message
														code="menu.administration.defectMgt.causeCode" /><i
													class="fa fa-angle-left pull-right"></i>
											</a>
												<ul id="sub_admin_causecode" class="treeview-menu">
												<c:if
													test="${currRole.grantedPrivilegeCodeList.contains(searchCauseCode)}">
													<li id="searchCauseCode"><a href="SearchCauseCode.do">
															<spring:message
																code="menu.administration.defectMgt.causeCode.search" />
													</a></li>
												</c:if>
												<c:if
													test="${currRole.grantedPrivilegeCodeList.contains(createCauseCode)}">
													<li id="createCauseCode"><a href="CreateCauseCode.do"><spring:message
																code="menu.administration.defectMgt.causeCode.create" /></a>
													</li>
												</c:if>
												</ul></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchEquipment)
		                       				|| currRole.grantedPrivilegeCodeList.contains(createEquipment)}">
											<li><a href="#" onClick="location.href='Equipment.do'">
													<spring:message
														code="menu.administration.defectMgt.equipment" /><i
													class="fa fa-angle-left pull-right"></i>
											</a>
												<ul id="sub_admin_equipment" class="treeview-menu">
												<c:if
													test="${currRole.grantedPrivilegeCodeList.contains(searchEquipment)}">
													<li id="searchEquipment"><a href="SearchEquipment.do">
															<spring:message
																code="menu.administration.defectMgt.equipment.search" />
													</a></li>
												</c:if>
												<c:if
													test="${currRole.grantedPrivilegeCodeList.contains(createEquipment)}">
													<li id="createEquipment"><a href="CreateEquipment.do">
															<spring:message
																code="menu.administration.defectMgt.equipment.create" />
													</a></li>
												</c:if>
												</ul></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchTools)
		                       				|| currRole.grantedPrivilegeCodeList.contains(createTools)}">
											<li><a href="#" onClick="location.href='Tool.do'"> <spring:message
														code="menu.administration.defectMgt.tools" /><i
													class="fa fa-angle-left pull-right"></i></a>
												<ul id="sub_admin_tool" class="treeview-menu">
												<c:if
													test="${currRole.grantedPrivilegeCodeList.contains(searchTools)}">
													<li id="searchTool"><a href="SearchTool.do"> <spring:message
																code="menu.administration.defectMgt.tools.search" /></a></li>
												</c:if>
												<c:if
													test="${currRole.grantedPrivilegeCodeList.contains(createTools)}">
													<li id="createTool"><a href="CreateTool.do"> <spring:message
																code="menu.administration.defectMgt.tools.create" /></a></li>
												</c:if>
												</ul></li>
										</c:if>
									</ul></li>
							</c:if>

							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(accountActionLog)
                           || currRole.grantedPrivilegeCodeList.contains(defectActionLog)
			               || currRole.grantedPrivilegeCodeList.contains(maintenanceScheduleActionLog)
			               || currRole.grantedPrivilegeCodeList.contains(patrolRouteActionLog)
			               || currRole.grantedPrivilegeCodeList.contains(patrolScheduleActionLog)}">
								<li><a href="#" onClick="location.href='ActivityLog.do'">
										<spring:message code="menu.administration.log" /> <i
										class="fa fa-angle-left pull-right"></i>
								</a>
									<ul id="sub_admin_activityLog" class="treeview-menu">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(accountActionLog)}">
											<li id="accountActionLog"><a
												href="SearchAccountAction.do"> <spring:message
														code="menu.administration.log.account" /></a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(defectActionLog)}">
											<li id="defectActionLog"><a href="SearchDefectAction.do">
													<spring:message code="menu.administration.log.defect" />
											</a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(maintenanceScheduleActionLog)}">
											<li id="maintenanceScheduleActionLog"><a
												href="SearchDefectScheduleAction.do"> <spring:message
														code="menu.administration.log.defect.schedule" /></a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(patrolRouteActionLog)}">
											<li id="patrolActionLog"><a
												href="SearchRouteDefAction.do"> <spring:message
														code="menu.administration.log.route" />
											</a></li>
										</c:if>
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(patrolScheduleActionLog)}">
											<li id="patrolScheduleActionLog"><a
												href="SearchPatrolScheduleAction.do"> <spring:message
														code="menu.administration.log.route.schedule" /></a></li>
										</c:if>
									</ul></li>
							</c:if>
						</ul></li>
				</c:if>
			</c:if>
			
			<c:choose>
				<c:when test="${currRole.siteKey == 1}">
				</c:when>
				<c:otherwise>
					<li id="sub_siteSelect"><a href="SiteSelect.do"><i
							class="icon-icon_switch_site_b"></i> <span><spring:message
									code="menu.switchSite" /></span></a></li>
									
				</c:otherwise>
			</c:choose>		
<!-- 
			<li id="sub_about"><a href="About.do"> <i
					class="icon-icon_about_b"></i> <span><spring:message
							code="menu.about" /></span></a></li>
			<%-- <li><a href="j_spring_security_logout"><i class="icon-icon_logout_b"></i> <span><spring:message
							code="menu.logout" /></span></a></li> --%>
 -->							
		</ul>
	</section>
	<!-- /.sidebar -->
</aside>

<script>
	$("li a").click(function() {
		showLoading();
	});

	function sub_toggle(mnu_id, className) {
		var sub_menu = document.getElementById(mnu_id);
		var menu_state = sub_menu.style.display;
		sub_menu.parentElement.className = className;
		if (menu_state == "" || menu_state == "none") {
			sub_menu.style.display = "block";
		} else {
			sub_menu.style.display = "none";
		}
	}
	function menu_toggle(mnu_id, hightlight) {
		sub_toggle(mnu_id, hightlight ? "active-highlight" : "active");
	}
	function menu_select(mnu_id) {
		var menu = document.getElementById(mnu_id);
		menu.className = "active-highlight";
	}
</script>
