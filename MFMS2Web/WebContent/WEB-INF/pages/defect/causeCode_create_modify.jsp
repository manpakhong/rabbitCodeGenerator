<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<c:choose>
	<c:when test="${causeCodeForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${causeCodeForm.deleted == true}">
			<c:set var="submitUrl" value="DoDeleteCauseCode.do" />
		</c:if>
		<c:if test="${causeCodeForm.deleted == false}">
			<c:set var="submitUrl" value="ModifyCauseCode.do" />
		</c:if>
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:choose>
			<c:when test="${empty causeCodeForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyCauseCode.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyCauseCode.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyCauseCode')"
	var="modifyCauseCode" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeCauseCode')"
	var="removeCauseCode" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchCauseCode')"
	var="searchCauseCode" />


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
			<li><a href="CauseCode.do"> <spring:message
						code="menu.administration.defectMgt.causeCode" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<li class="active"><spring:message
							code="menu.administration.defectMgt.causeCode.view" /></li>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.administration.defectMgt.causeCode.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.administration.defectMgt.causeCode.modify" /></li>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form cssClass="form-horizontal" commandName="causeCodeForm"
						method="post" action="${submitUrl}">
						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<c:if test="${causeCodeForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.create.success" />
							</div>
						</c:if>
						<c:if test="${causeCodeForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.modify.success" />
							</div>
						</c:if>
						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<div class="box-body">
							<div
								class="form-group <spring:bind path="code"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="causeCode.code" />*</label>
								<div class="col-sm-10">
									<spring:message code="causeCode.code" var="defectCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${defectCode}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="causeCode.name" /></label>
								<div class="col-sm-10">
									<spring:message code="causeCode.name" var="name" />
									<form:input path="name" cssClass="form-control"
										placeholder="${name}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group  <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="problemCode.description" /></label>
								<div class="col-sm-10">
									<spring:message code="defect.description"
										var="defectDescription" />
									<form:textarea path="desc" cssClass="form-control"
										placeholder="${defectDescription}" readonly="${readOnly}" />
								</div>
							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-lg-1 ">

									<c:if
										test="${currRole.grantedPrivilegeCodeList.contains(searchCauseCode)}">
										<c:if test="${readOnly == false && isCreate == false}">
											<c:choose>
												<c:when test="${causeCodeForm.referrer == 's'}">
													<a onclick="showLoading()" class="btn btn-primary " href="DoSearchCauseCode.do"><spring:message
															code="button.back.to.search" /></a>
												</c:when>
												<c:otherwise>
													<!-- this view page will go to search -->
													<a onclick="showLoading()" class="btn btn-primary "
														href="ViewCauseCode.do?key=${causeCodeForm.key}&r=s"><spring:message
															code="button.back" /></a>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${readOnly == true}">
											<a onclick="showLoading()" class="btn btn-primary " href="DoSearchCauseCode.do"><spring:message
													code="button.back.to.search" /></a>
										</c:if>

									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<c:if test="${readOnly == false}">
										<button onclick="showLoading()" type="submit" class="btn btn-primary ">
											<spring:message code="button.submit" />
										</button>
									</c:if>
								</div>
								<div class="col-xs-2 col-lg-1 ">
									<c:if test="${readOnly == true}">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(modifyCauseCode)}">
											<button onclick="showLoading()" type="submit" class="btn btn-primary ">
												<spring:message code="button.modify" />
											</button>
										</c:if>
									</c:if>
									<c:if test="${readOnly == false}">
										<c:if test="${isCreate == true}">
											<a onclick="showLoading()" href="CreateCauseCode.do" class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
										<c:if test="${isCreate == false}">
											<a onclick="showLoading()"
												href="<c:url value="ModifyCauseCode.do"><c:param name="key" value="${causeCodeForm.key}"/></c:url>"
												class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
									</c:if>
								</div>
							</div>
							<!-- /.box-footer -->
						</div>
						<!-- /.box-body -->
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script>
	menu_toggle("sub_admin");
	menu_toggle("sub_admin_defect");
	menu_toggle("sub_admin_causecode");

	if(${isCreate}){
		menu_select("createCauseCode");
	} else 
		menu_select("searchCauseCode");

</script>
