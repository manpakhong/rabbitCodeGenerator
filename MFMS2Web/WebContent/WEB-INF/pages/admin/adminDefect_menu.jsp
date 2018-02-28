<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="../common/left.jsp" />

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

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AdministrationManagement.do"><i
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.administration" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.defectMgt" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchFailureClass)
		                       || currRole.grantedPrivilegeCodeList.contains(createFailureClass)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="FailureClass.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/failure_class.png" alt="">
							<h2 class="iconName">
								<spring:message
									code="menu.administration.defectMgt.failureClass" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchProblemCode)
		                       || currRole.grantedPrivilegeCodeList.contains(createProblemCode)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="ProblemCode.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/problem_code.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.defectMgt.problemCode" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchCauseCode)
		                       || currRole.grantedPrivilegeCodeList.contains(createCauseCode)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="CauseCode.do">
						<div class=" well">
							<img class="img-responsive"
								src="import/img2/admin/cause_code.png" alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.defectMgt.causeCode" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchEquipment)
		                       || currRole.grantedPrivilegeCodeList.contains(createEquipment)}">
				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="Equipment.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/admin/equipment.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.defectMgt.equipment" />
							</h2>
						</div>
					</a>
				</div>
			</c:if>

			<c:if
				test="${currRole.grantedPrivilegeCodeList.contains(searchTools)
		                       || currRole.grantedPrivilegeCodeList.contains(createTools)}">

				<div class="col-lg-4  col-sm-6 iconStyle">
					<a href="Tool.do">
						<div class=" well">
							<img class="img-responsive" src="import/img2/admin/tools.png"
								alt="">
							<h2 class="iconName">
								<spring:message code="menu.administration.defectMgt.tools" />
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
	menu_toggle("sub_admin_defect", 1);
</script>
