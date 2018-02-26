<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<jsp:include page="../common/left.jsp" />


<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createAccount')"
	var="createAccount" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccount')"
	var="searchAccount" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createAccountRole')"
	var="createAccountRole" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccountRole')"
	var="searchAccountRole" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createAccountGroup')"
	var="createAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccountGroup')"
	var="searchAccountGroup" />

<c:choose>
	<c:when test="${accountForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="submitUrl" value="ModifyOwnAccount.do" />
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="submitUrl" value="DoModifyOwnAccount.do" />
	</c:otherwise>
</c:choose>
<script>
	$(document).ready(function() {

		$('.locationtree ul').hide();
		$('.locationtree li').hide();
		$('.locationtree li:first').show();
		$('.locationtree li i').on('click', function(e) {
			var $container = $(this.parentNode.parentNode).find('> ul');
			if ($container.is(":visible")) {
				$container.hide('fast');
				// change arrow
				$(this).removeClass('fa-angle-down').addClass('fa-angle-left');
			} else {
				$container.show('fast');
				// change arrow
				$(this).removeClass('fa-angle-left').addClass('fa-angle-down');
			}
			var children = $(this.parentNode.parentNode).find('> ul > li');
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

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<c:choose>
				<c:when
					test="${currRole.grantedPrivilegeCodeList.contains(searchAccountRole)
			           || currRole.grantedPrivilegeCodeList.contains(createAccountRole)
			           || currRole.grantedPrivilegeCodeList.contains(searchAccount)
			           || currRole.grantedPrivilegeCodeList.contains(createAccount)
			           || currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)
			           || currRole.grantedPrivilegeCodeList.contains(createAccountGroup)
			           || currRole.grantedPrivilegeCodeList.contains(modifyOwnAccount)}">
					<li><a href="AccountManagement.do"><i
							class="icon-icon_account_management_b"></i> <spring:message
								code="menu.accountMgt" /></a></li>
				</c:when>
				<c:otherwise>
					<li><a class="disabled" href="#"><i
							class="icon-icon_account_management_b"></i> <spring:message
								code="menu.accountMgt" /></a></li>
				</c:otherwise>
			</c:choose>





			<c:choose>
				<c:when test="${readOnly}">
					<li class="active"><spring:message
							code="menu.accountMgt.viewOwnAccount" /></li>
				</c:when>
				<c:otherwise>
					<li class="active"><spring:message
							code="menu.accountMgt.modifyOwnAccount" /></li>
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
						<c:if test="${accountForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.modify.success" />
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
											placeholder="${username}" readonly="true" />
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

											<form:input path="contactCountryCode" cssClass="form-control"
												maxlength="3" placeholder="${countryCode}"
												onkeypress='return event.charCode >= 48 && event.charCode <= 57  '
												readonly="${readOnly}" />
											<span class="input-group-addon" style="border: none">-</span>
											<form:input path="contactAreaCode" cssClass="form-control"
												maxlength="3" placeholder="${areaCode}"
												onkeypress='return event.charCode >= 48 && event.charCode <= 57  '
												readonly="${readOnly}" />
											<span class="input-group-addon" style="border: none">-</span>
										</div>
									</div>
									<div class="col-sm-2 ">
										<form:input path="contactNumber" cssClass="form-control"
											maxlength="11" placeholder="${contactNumber}"
											onkeypress='return event.charCode >= 48 && event.charCode <= 57  '
											readonly="${readOnly}" />
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
								<c:if test="${not readOnly}">

									<div
										class="<spring:bind path="currentPassword"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="account.current.password" /></label>
										<div class="col-sm-4">

											<spring:message code="account.current.password"
												var="currentPassword" />

											<form:input path="currentPassword" cssClass="form-control"
												placeholder="${currentPassword}" readonly="${readOnly}"
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
											placeholder="${tagId}" readonly="true" />
									</div>
								</div>
							</div>
							<div class="form-group">
								<c:if test="${not readOnly}">

									<div
										class="<spring:bind path="password"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="account.new.password" /></label>
										<div class="col-sm-4">
											<spring:message code="account.new.password" var="password" />
											<form:input path="password" cssClass="form-control"
												placeholder="${password}" readonly="${readOnly}"
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
											disabled="true">
											<c:forEach items="${accountForm.availableStatusList}"
												var="availableStatus">
												<form:option value="${availableStatus.key}">
													<spring:message code="${availableStatus.value}" />
												</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								<c:if test="${not readOnly}">
									<div
										class="<spring:bind path="confirmPassword"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="account.confirm.password" /></label>
										<div class="col-sm-4">

											<spring:message code="account.confirm.password" var="confirm" />

											<form:input path="confirmPassword" cssClass="form-control"
												placeholder="${confirm}" readonly="${readOnly}"
												type="password" />
										</div>
									</div>
									<div class="col-sm-6"></div>
								</c:if>
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
									<c:if test="${readOnly == false}">
										<!-- back button for modify -->
										<c:choose>
											<c:when test="${accountForm.referrer == 's'}">
												<a class="btn btn-primary " href="DoSearchAccount.do"
													onclick="showLoading()"><spring:message
														code="button.back.to.search" /></a>
											</c:when>
											<c:otherwise>
												<!-- this view page will go to search -->
												<a class="btn btn-primary " href="ViewOwnAccount.do"
													onclick="showLoading()"><spring:message
														code="button.back" /></a>
											</c:otherwise>
										</c:choose>
									</c:if>
									<c:if test="${readOnly == true}">
										<a class="btn btn-primary " href="DoSearchAccount.do"
											onclick="showLoading()"><spring:message
												code="button.back.to.search" /></a>

									</c:if>
								</div>

								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<c:if test="${readOnly == false}">
										<button type="submit" class="btn btn-primary "
											onclick="showLoading()">
											<spring:message code="button.submit" />
										</button>
									</c:if>
								</div>
								<div class="col-xs-2 col-lg-1 ">
								<spring:eval
									expression="@privilegeMap.getProperty('privilege.code.modifyOwnAccount')"
									var="modifyOwnAccount" />
								<c:if
									test="${currRole.grantedPrivilegeCodeList.contains(modifyOwnAccount)}">
									<c:if test="${readOnly == true}">
										<button type="submit" class="btn btn-primary "
											onclick="showLoading()">
											<spring:message code="button.modify" />
										</button>
									</c:if>
								</c:if>
									<c:if test="${readOnly == false}">
										<a href="<c:url value="ModifyOwnAccount.do"><c:param name="loginId" value="${accountForm.loginId}"/></c:url>"
											class="btn btn-primary " onclick="showLoading()"><spring:message
												code="button.reset" /></a>
									</c:if>
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
	menu_select("modifyOwnAccount");
</script>
