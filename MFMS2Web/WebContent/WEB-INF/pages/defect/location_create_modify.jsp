<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<jsp:include page="../common/left.jsp" />

<c:choose>
	<c:when test="${locationForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${locationForm.delete == true}">
			<c:set var="submitUrl" value="DoDeleteLocation.do" />
		</c:if>
		<c:if test="${locationForm.delete == false}">
			<c:set var="submitUrl" value="ModifyLocation.do" />
		</c:if>
		<c:set var="disabled" value="disabled" />
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="disabled" value="" />
		<c:choose>
			<c:when test="${empty locationForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyLocation.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyLocation.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyLocation')"
	var="modifyLocation" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeLocation')"
	var="removeLocation" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchLocation')"
	var="searchLocation" />


<script>
	$(document).ready(function() {
		$('.locationtree ul').hide();
		$('.locationtree li').hide();
		$('.locationtree li:first').show();
		if(!${readOnly}){
			/*
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
		*/
			$('.locationtree li').on('click', function(e) {
				//console.log(e);
				var container = $(this).find('> ul');
				//console.log(container);
				if (container.is(":visible")) {
					container.hide('fast');
					// change arrow
					$(this).find('> div i').removeClass('fa-angle-down').addClass('fa-angle-left');
					//console.log($(this).find('> i'));
				} else {
					container.show('fast');
					// change arrow
					$(this).find('> div i').removeClass('fa-angle-left').addClass('fa-angle-down');
					//console.log($(this).find('> i'));
				}
				var children = $(this).find('> ul > li');
				for (var i = 0; i < children.length; i++) {
					var el = $(children[i]);
					if (el.is(":visible")) {
						el.hide('fast');
					} else {
						el.show('fast');
					}
				}
				e.stopPropagation();
			});
		}

		
	});
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AdministrationManagement.do"><i
					class="icon-icon_administration_b"></i> <spring:message code="menu.administration" /></a></li>
			<%-- <li><a href="AdministrationCommon.do"> <spring:message
						code="menu.administration.common" /></a></li> --%>
			<li><a href="Location.do"> <spring:message
						code="menu.administration.common.location" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<li class="active"><spring:message
							code="menu.administration.common.location.view" /></li>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.administration.common.location.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.administration.common.location.modify" /></li>
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
						<form:form cssClass="form-horizontal" commandName="locationForm"
							method="post" action="${submitUrl}">

							<!-- validation error messages -->
							<spring:bind path="*">
								<c:if test="${status.error}">
									<div class="form-group alert alert-danger">
										<form:errors path="*" />
									</div>
								</c:if>
							</spring:bind>
							<c:if test="${locationForm.referrer == 'c'}">
								<div class="form-group alert alert-success">
									<spring:message code="defect.create.success" />
								</div>
							</c:if>
							<c:if test="${locationForm.referrer == 'm'}">
								<div class="form-group alert alert-success">
									<spring:message code="defect.modify.success" />
								</div>
							</c:if>
							<form:hidden path="key" />
							<form:hidden path="siteKey" />
							<div class="box-body">
								<div
									class="<spring:bind path="parentKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="location.parent" />*</label>
									<div class="col-sm-10">
										<div class="panel panel-default">
											<div class="panel-heading">
												<label class="control-label"> <spring:message
														code="location.parent" />
												</label> <label id="locationName" class="control-label" style = "word-wrap: break-word;"><c:out
														value="${selectedLocation}" /></label>
											</div>
<script>
	function onLocationChange(inputCode, inputName)
	{
		event.stopPropagation();
		var locationName = ' : ' + inputCode + ' - ' + inputName;

		$('#locationName').text(locationName);
	}
</script>
											<ul class="list-group locationtree" cssClass="form-control"
												id="radioButtonLocationTreeForCreateLocation">
												<template:radioButtonLocationTreeForCreateLocation
													node="${locationForm.availableLocationTree}" />
											</ul>
										</div>
									</div>
								</div>
								<div
									class="form-group <spring:bind path="code"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="location.code" />*</label>
									<div class="col-sm-10">
										<spring:message code="location.code" var="code" />
										<form:input path="code" cssClass="form-control"
											placeholder="${code}" readonly="${readOnly}" />
									</div>
								</div>
								<div
									class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="location.name" /></label>
									<div class="col-sm-10">
										<spring:message code="location.name" var="name" />
										<form:input path="name" cssClass="form-control"
											placeholder="${name}" readonly="${readOnly}" />
									</div>
								</div>
								<div
									class="form-group  <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="location.description" /></label>
									<div class="col-sm-10">
										<spring:message code="defect.description"
											var="defectDescription" />
										<form:textarea path="desc" cssClass="form-control"
											placeholder="${defectDescription}" readonly="${readOnly}" />
									</div>
								</div>

								<div
									class="form-group  <spring:bind path="tagId"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="location.tagid" /></label>
									<div class="col-sm-10">
										<spring:message code="patrol.report.staff.tagID" var="tagId" />
										<form:input path="tagId" cssClass="form-control"
											placeholder="${tagId}" readonly="${readOnly}" />
									</div>
								</div>
								<div class="box-footer">
								<div class="col-lg-1 col-xs-2 ">

									<c:if
										test="${currRole.grantedPrivilegeCodeList.contains(searchLocation)}">


										<c:if test="${readOnly == false && isCreate == false}">
											<c:choose>
												<c:when test="${locationForm.referrer == 's'}">
													<a onclick="showLoading()" class="btn btn-primary " href="DoSearchLocation.do"><spring:message
															code="button.back.to.search" /></a>
												</c:when>
												<c:otherwise>
													<!-- this view page will go to search -->
													<a onclick="showLoading()" class="btn btn-primary "
														href="ViewLocation.do?key=${locationForm.key}&r=s"><spring:message
															code="button.back" /></a>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${readOnly == true}">
											<a onclick="showLoading()" class="btn btn-primary " href="DoSearchLocation.do"><spring:message
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
								<div class="col-lg-1 col-xs-2 ">
									<c:if test="${readOnly == true}">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(modifyLocation)}">
											<button onclick="showLoading()" type="submit" class="btn btn-primary ">
												<spring:message code="button.modify" />
											</button>
										</c:if>
									</c:if>
									<c:if test="${readOnly == false}">
										<c:if test="${isCreate == true}">
											<a onclick="showLoading()" href="CreateLocation.do" class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
										<c:if test="${isCreate == false}">
											<a onclick="showLoading()"
												href="<c:url value="ModifyLocation.do"><c:param name="key" value="${locationForm.key}"/></c:url>"
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
  menu_toggle("sub_admin_location");

  if(${isCreate}){
		menu_select("createLocation");
	} else 
		menu_select("searchLocation");
  
</script>
