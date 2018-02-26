<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>

<jsp:include page="../common/left.jsp" />

<c:set var="submitUrl" value="DoSetLocationPrivilege.do" />

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

				// select/deselect all child 
				$(".checkAll").change(
						function() {
							var childCheck = $(this.parentNode.parentNode)
									.find('> ul').find('input:checkbox');
							
							//var childCheck = $(this.parentNode.parentNode).find("label").find("input:checkbox");
							for (var i = 0; i < childCheck.length; i++) {
								var $el = $(childCheck[i]);
								$el.prop('checked', $(this).prop('checked'));
							
							}
							
							var parentCheckBox = $(this.parentNode.parentNode).find("label").find("input:checkbox");
							$(parentCheckBox[0]).prop('checked', $(this).prop('checked'));
							
							//console.log(parentCheckBox);
							
							//$('.checkBox').prop('checked', $(this).prop('checked'));
						}
						
				);
			});
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AccountManagement.do"><i
					class="icon-icon_account_management_b"></i> Account Management</a></li>
			<li><a href="Account.do"> Account</a></li>
			<li class="active">Set Location Privilege</li>
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
						commandName="locationPrivilegeForm" method="post"
						action="${submitUrl}">

						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>

						</spring:bind>

						<c:if test="${locationPrivilegeForm.success}">
							<div class="form-group alert alert-success"><spring:message
										code="set.privilege.success" /></div>
						</c:if>

						<div class="box-body">
							<div
								class="form-group <spring:bind path="loginId"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="account.username" />*</label>
								<div class="col-sm-5">
									<form:input path="loginId" cssClass="form-control"
										readonly="true" />
								</div>
							</div>
							<div
								class="form-group  <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="account.name" /></label>
								<div class="col-sm-10">
									<form:textarea path="name" cssClass="form-control"
										readonly="true" />
								</div>
							</div>
							<div class="form-group col-sm-12">
								<br />

								<div class="col-sm-10 col-xs-offset-2">
									<div class="panel panel-default">
										<div class="panel-heading">
											<label> <spring:message
													code="menu.administration.common.location" />
											</label>
										</div>
										<ul class="list-group locationtree" id="checkableLocationTree">
											<template:checkableLocationTree
												node="${locationPrivilegeForm.availableLocationTree}" />
										</ul>
									</div>
								</div>


							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-lg-1 ">
									<c:if
										test="${currRole.grantedPrivilegeCodeList.contains(searchAccount)}">
										<a onclick="showLoading()" class="btn btn-primary " href="DoSearchAccount.do"><spring:message
												code="button.back.to.search" /></a>
									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<button onclick="showLoading()" class="btn btn-primary " type="submit">
										<spring:message code="button.submit" />
									</button>
								</div>
								<div class="col-xs-2 col-lg-1 ">

									<button  class="btn btn-primary " type="reset">
										<spring:message code="button.reset" />
									</button>
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
</script>
