<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<jsp:include page="../common/left.jsp" />


<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccount')"
	var="searchAccount" />

<script>
	$(document).ready(
			function() {

				$('.locationtree ul').hide();
				$('.locationtree li').hide();
				$('.locationtree li:first').show();
				$('.locationtree li i').on(
						'click',
						function(e) {
							var $container = $(this.parentNode.parentNode)
									.find('> ul');
							if ($container.is(":visible")) {
								$container.hide('fast');
								// change arrow
								$(this).removeClass('fa-angle-down').addClass(
										'fa-angle-left');
							} else {
								$container.show('fast');
								// change arrow
								$(this).removeClass('fa-angle-left').addClass(
										'fa-angle-down');
							}
							var children = $(this.parentNode.parentNode).find(
									'> ul > li');
							for (var i = 0; i < children.length; i++) {
								var $el = $(children[i]);
								if ($el.is(":visible")) {
									$el.hide('fast');
								} else {
									$el.show('fast');
								}
							}
							e.stopPropagation();
						});

				
				$(".checkBox").prop('disabled', true);
				$(".checkAll").hide();
				
			});
</script>

<c:choose>
	<c:when test="${accountForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${accountForm.delete == true}">
			<c:set var="submitUrl" value="DoDeleteAccount.do" />
		</c:if>
		<c:if test="${accountForm.delete == false}">
			<c:set var="submitUrl" value="ModifyAccount.do" />
		</c:if>
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:choose>
			<c:when test="${empty accountForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyAccount.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyAccount.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AccountManagement.do"><i
					class="icon-icon_account_management_b"></i> <spring:message
						code="menu.accountMgt" /></a></li>
			<li><a href="Account.do"> <spring:message
						code="menu.accountMgt.account" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<c:choose>
						<c:when test="${accountForm.delete}">
							<li class="active"><spring:message
									code="menu.accountMgt.account.remove" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.accountMgt.account.view" /></li>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.accountMgt.account.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.accountMgt.account.modify" /></li>
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
					<form:form cssClass="form-horizontal" commandName="accountForm"
						method="post" action="${submitUrl }">
						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>

						</spring:bind>
						<c:if test="${accountForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="account.create.success" />
							</div>
						</c:if>
						<c:if test="${accountForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="account.modify.success" />
							</div>
						</c:if>
						<form:hidden path="key" />
						<div class="box-body">
							<div class="form-group">
								<div
									class="<spring:bind path="loginId"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="account.username" />*</label>
									<div class="col-sm-4">
										<spring:message code="account.username" var="username" />
										<form:input path="loginId" cssClass="form-control"
											placeholder="${username}"
											readonly="${readOnly or not isCreate}" />
									</div>
								</div>
								<div
									class="<spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="account.name" />*</label>
									<div class="col-sm-4">
										<spring:message code="account.name" var="name" />
										<form:input path="name" cssClass="form-control"
											placeholder="${name}" readonly="${readOnly}" />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<div
									class="<spring:bind path="contactNumber"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="account.contact.number" />*</label>
									<div class="col-sm-2 ">
										<div class="input-group">

											<spring:message code="site.countryCode" var="countryCode" />
											<spring:message code="site.areaCode" var="areaCode" />
											<spring:message code="site.contactNumber" var="contactNumber" />


											<form:input path="contactCountryCode" maxlength="3"
												placeholder="${countryCode}" cssClass="form-control"
												readonly="${readOnly}"
												onkeypress='return event.charCode >= 48 && event.charCode <= 57 ' />
											<span class="input-group-addon" style="border: none">-</span>
											<form:input path="contactAreaCode" cssClass="form-control"
												maxlength="3" readonly="${readOnly}"
												placeholder="${areaCode}"
												onkeypress='return event.charCode >= 48 && event.charCode <= 57 ' />
											<span class="input-group-addon" style="border: none">-</span>
										</div>
									</div>
									<div class="col-sm-2 ">
										<form:input path="contactNumber" cssClass="form-control"
											maxlength="11" readonly="${readOnly}"
											placeholder="${contactNumber}"
											id="contactNumber"
											onkeypress='return event.charCode >= 48 && event.charCode <= 57 ' />
									</div>
								</div>								
								<div
									class="<spring:bind path="email"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="account.email" />*</label>
									<div class="col-sm-4">

										<spring:message code="account.email" var="email" />

										<form:input path="email" cssClass="form-control"
											placeholder="${email}" readonly="${readOnly}" />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<c:if test="${isCreate}">
									<div
										class="<spring:bind path="password"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="account.password" />*</label>
										<div class="col-sm-4">
											<spring:message code="account.password" var="password" />

											<form:input path="password" cssClass="form-control"
												placeholder="${password}" readonly="${readOnly}"
												type="password" />
										</div>
									</div>
								</c:if>
								<div
									class="<spring:bind path="tagId"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="account.tagId" /></label>
									<div class="col-sm-4">
										<spring:message code="account.tagId" var="tagId" />

										<form:input path="tagId" cssClass="form-control"
											placeholder="${tagId}" readonly="${readOnly}" />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<c:if test="${isCreate}">
									<div
										class="<spring:bind path="confirmPassword"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="account.confirm.password" />*</label>
										<div class="col-sm-4">

											<spring:message code="account.confirm.password" var="confirm" />

											<form:input path="confirmPassword" cssClass="form-control"
												placeholder="${confirm}" readonly="${readOnly}"
												type="password" />
										</div>
									</div>
								</c:if>

								<div
									class="<spring:bind path="status"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="account.status" /></label>
									<div class="col-sm-4">
										<form:select path="status" cssClass="form-control"
											disabled="${readOnly}">
											<c:forEach items="${accountForm.availableStatusList}"
												var="availableStatus">
												<form:option value="${availableStatus.key}">
													<spring:message code="${availableStatus.value}" />
												</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<div
									class="<spring:bind path="selectedRoleKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="account.role" /></label>
									<div class="col-sm-4">
										<form:select path="selectedRoleKey" cssClass="form-control"
											disabled="${readOnly}">
											<c:forEach items="${accountForm.availableRoleList}"
												var="availableRole">
												<form:option value="${availableRole.key}">
													<c:out value="${availableRole.name}" />
												</form:option>
											</c:forEach>
										</form:select>
									</div>
									<div class="col-sm-6"></div>
								</div>
							</div>

						<c:if test="${readOnly}">
							<div class="form-group">
								<br />
								<div class="col-sm-10 col-xs-offset-2">
									<div class="panel panel-default">
										<div class="panel-heading">
											<label> <spring:message
													code="menu.administration.common.location" />
											</label>
										</div>
										<ul class="list-group locationtree"
											id="checkableLocationTree">
											<template:checkableLocationTree
												node="${accountForm.availableLocationTree}" />
										</ul>
									</div>
								</div>
							</div>
						</c:if>

							<div class="col-xs-2 col-lg-1 ">
								<c:if test="${readOnly == false && isCreate == false}">
									<!-- back button for modify -->
									<c:choose>
										<c:when test="${accountForm.referrer == 's'}">
											<a class="btn btn-primary " href="DoSearchAccount.do"><spring:message
													code="button.back.to.search" /></a>
										</c:when>
										<c:otherwise>
											<!-- this view page will go to search -->
											<a class="btn btn-primary "
												href="<c:url value="ViewAccount.do"><c:param name="loginId" value="${accountForm.loginId }"/><c:param name="r" value="s"/></c:url>"><spring:message
													code="button.back" /></a>
										</c:otherwise>
									</c:choose>

								</c:if>
								<c:if test="${readOnly == true}">

									<a class="btn btn-primary " href="DoSearchAccount.do"><spring:message
											code="button.back.to.search" /></a>

								</c:if>
							</div>
							<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
								<c:if test="${readOnly == false}">

									<button onclick="showLoading()" type="submit"
										class="btn btn-primary ">
										<spring:message code="button.submit" />
									</button>

								</c:if>
							</div>
							<div class="col-xs-2 col-lg-1 ">
								<spring:eval
									expression="@privilegeMap.getProperty('privilege.code.modifyAccount')"
									var="modifyAccount" />
								<c:if
									test="${currRole.grantedPrivilegeCodeList.contains(modifyAccount)}">
								<c:if test="${readOnly == true}">
									<c:if test="${accountForm.delete == false}">
										<a onclick="showLoading()"
											href="<c:url value="ModifyAccount.do"><c:param name="loginId" value="${accountForm.loginId }"/><c:param name="r" value="v"/></c:url>"
											class="btn btn-primary "><spring:message
												code="button.modify" /></a>

									</c:if>
								</c:if>
								</c:if>
								<!--  reset button -->
								<c:if test="${readOnly == false}">
									<c:if test="${isCreate == true}">
										<a onclick="showLoading()" href="CreateAccount.do"
											class="btn btn-primary "><spring:message
												code="button.reset" /></a>
									</c:if>
									<c:if test="${isCreate == false}">
										<a onclick="showLoading()"
											href="<c:url value="ModifyAccount.do"><c:param name="loginId" value="${accountForm.loginId }"/><c:param name="r" value="${accountForm.referrer }"/></c:url>"
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
	menu_toggle("sub_am");
	menu_toggle("sub_am_account");

	if(${isCreate}){
		menu_select("createAccount");
	} else 
		menu_select("searchAccount");
	

	$("#contactNumber")

</script>
