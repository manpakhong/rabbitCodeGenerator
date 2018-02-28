<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="company">
	<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
</c:set>

<jsp:include page="../common/left.jsp" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchDefect')"
	var="searchDefect" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createDefect')"
	var="createDefect" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.alarmConsole')"
	var="alarmConsole" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.defectListReport')"
	var="defectListReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.defectStatusSummaryReport')"
	var="defectStatusSummaryReport" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.maintenanceSchedule')"
	var="defectSchedule" />
<spring:eval
		expression="@privilegeMap.getProperty('privilege.code.viewOwnerInformation')"
		var="viewOwnerInformation"/>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_defect_management_b"></i>
				<spring:message code="menu.defectMgt" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchDefect)
			           || currRole.grantedPrivilegeCodeList.contains(createDefect)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="Defect.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/dm/defect.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.defectMgt.defect" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>
			
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(alarmConsole)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="AlarmConsole.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/dm/alarmconsole.png"
								alt="">
							<h2 class="iconName">
								<c:choose>
									<c:when test="${company=='jec'}">
     									<spring:message code="menu.alarmconsole.jec" />
     								</c:when>
     								<c:otherwise>
     									<spring:message code="menu.alarmconsole" />
     								</c:otherwise>
     							</c:choose>
							</h2>
						</div>
					</a>
				</div>
			</c:if>			
			
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(defectListReport)
			           || currRole.grantedPrivilegeCodeList.contains(defectStatusSummaryReport)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="DefectReport.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/dm/defect_reports.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.defectMgt.report" />
							</h2>
						</div>
					</a>
				</div>


			</c:if>

			<c:if test="${currRole.grantedPrivilegeCodeList.contains(\"ViewOwnerInfomation\")}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="STDemo.do">
						<div class="well">
							<img class="img-responsive"
								 src="import/img2/admin/location.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.ownerinfo"/>
							</h2>
						</div>
					</a>
				</div>
			</c:if>
		</div>

	</section>
</div>

<script>
	menu_toggle("sub_dm", 1);
</script>
