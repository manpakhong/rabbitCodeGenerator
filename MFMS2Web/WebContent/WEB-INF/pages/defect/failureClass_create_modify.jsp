<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<c:choose>
	<c:when test="${failureClassForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${failureClassForm.deleted == true}">
			<c:set var="submitUrl" value="DoDeleteFailureClass.do" />
		</c:if>
		<c:if test="${failureClassForm.deleted == false}">
			<c:set var="submitUrl" value="ModifyFailureClass.do" />
		</c:if>
		<c:set var="disabled" value="disabled" />
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="disabled" value="" />
		<c:choose>
			<c:when test="${empty failureClassForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyFailureClass.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyFailureClass.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>


<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyFailureClass')"
	var="modifyFailureClass" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeFailureClass')"
	var="removeFailureClass" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchFailureClass')"
	var="searchFailureClass" />

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
			<li><a href="FailureClass.do"> <spring:message
						code="menu.administration.defectMgt.failureClass" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<li class="active"><spring:message
							code="menu.administration.defectMgt.failureClass.view" /></li>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.administration.defectMgt.failureClass.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.administration.defectMgt.failureClass.modify" /></li>
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
					<form:form cssClass="form-horizontal"
						commandName="failureClassForm" method="post" action="${submitUrl}">
						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<c:if test="${failureClassForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.create.success" />
							</div>
						</c:if>
						<c:if test="${failureClassForm.referrer == 'm'}">
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
										code="failureClass.code" />*</label>
								<div class="col-sm-10">
									<spring:message code="failureClass.code" var="defectCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${defectCode}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="failureClass.name" /></label>
								<div class="col-sm-10">
									<spring:message code="failureClass.name" var="name" />
									<form:input path="name" cssClass="form-control"
										placeholder="${name}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group  <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="defect.description" /></label>
								<div class="col-sm-10">
									<spring:message code="defect.description"
										var="defectDescription" />
									<form:textarea path="desc" cssClass="form-control"
										placeholder="${defectDescription}" readonly="${readOnly}" />
								</div>
							</div>
							<!-- /.box-body -->
							<div class="box-footer">
								<div class="col-lg-1 col-xs-2 ">

									<c:if
										test="${currRole.grantedPrivilegeCodeList.contains(searchFailureClass)}">
										<c:if test="${readOnly == false && isCreate == false}">
											<c:choose>
												<c:when test="${failureClassForm.referrer == 's'}">
													<a onclick="showLoading()" class="btn btn-primary " href="DoSearchFailureClass.do"><spring:message
															code="button.back.to.search" /></a>
												</c:when>
												<c:otherwise>
													<!-- this view page will go to search -->
													<a onclick="showLoading()" class="btn btn-primary "
														href="ViewFailureClass.do?key=${failureClassForm.key}&r=s"><spring:message
															code="button.back" /></a>
												</c:otherwise>
											</c:choose>


										</c:if>
										<c:if test="${readOnly == true}">
											<a onclick="showLoading()" class="btn btn-primary " href="DoSearchFailureClass.do"><spring:message
													code="button.back.to.search" /></a>
										</c:if>
									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9  ">
									<c:if test="${readOnly == false}">
										<button onclick="showLoading()" type="submit" class="btn btn-primary ">
											<spring:message code="button.submit" />
										</button>
									</c:if>
								</div>
								<div class="col-xs-2 col-lg-1 ">
									<c:if test="${readOnly == true}">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(modifyFailureClass)}">
											<button onclick="showLoading()" type="submit" class="btn btn-primary ">
												<spring:message code="button.modify" />
											</button>
										</c:if>
									</c:if>
									<c:if test="${readOnly == false}">
										<c:if test="${isCreate == true}">
											<a onclick="showLoading()" href="CreateFailureClass.do" class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
										<c:if test="${isCreate == false}">
											<a onclick="showLoading()" href="<c:url value="ModifyFailureClass.do"><c:param name="key" value="${failureClassForm.key}"/></c:url>"
												class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
									</c:if>
								</div>
							</div>
							<!-- /.box-footer -->
						</div>
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
	menu_toggle("sub_admin_failureclass");

	  if(${isCreate}){
			menu_select("createFailureClass");
		} else 
			menu_select("searchFailureClass");
</script>