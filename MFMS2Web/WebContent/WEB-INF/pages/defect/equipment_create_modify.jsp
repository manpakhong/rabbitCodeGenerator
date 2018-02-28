<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>

<jsp:include page="../common/left.jsp" />

<c:choose>
	<c:when test="${equipmentForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${equipmentForm.delete == true}">
			<c:set var="submitUrl" value="DoDeleteEquipment.do" />
		</c:if>
		<c:if test="${equipmentForm.delete == false}">
			<c:set var="submitUrl" value="ModifyEquipment.do" />
		</c:if>
		<c:set var="disabled" value="disabled" />
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="disabled" value="" />
		<c:choose>
			<c:when test="${empty equipmentForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyEquipment.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyEquipment.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyEquipment')"
	var="modifyEquipment" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeEquipment')"
	var="removeEquipment" />

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchEquipment')"
	var="searchEquipment" />


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
		}); */
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
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.administration" /></a></li>
			<li><a href="AdministrationDefect.do"> <spring:message
						code="menu.administration.defectMgt" /></a></li>
			<li><a href="Equipment.do"> <spring:message
						code="menu.administration.defectMgt.equipment" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<li class="active"><spring:message
							code="menu.administration.defectMgt.equipment.view" /></li>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.administration.defectMgt.equipment.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.administration.defectMgt.equipment.modify" /></li>
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
					<form:form cssClass="form-horizontal" commandName="equipmentForm"
						method="post" action="${submitUrl}">

						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<c:if test="${equipmentForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.create.success" />
							</div>
						</c:if>
						<c:if test="${equipmentForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.modify.success" />
							</div>
						</c:if>
						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<div class="box-body">
							<div
								class="<spring:bind path="locationKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="defect.location" />*</label>
								<div class="col-sm-10">
									<div class="panel panel-default">
										<div class="panel-heading">
											<label class="control-label"> <spring:message
													code="defect.location.select" />
											</label> <label id="locationName" class="control-label"
												style="word-wrap: break-word;"><c:out
													value="${selectedLocation}" /></label>
										</div>
<script>
	function onLocationChange(inputKey, inputCode, inputName)
	{
		event.stopPropagation();
		var locationName = ' : ' + inputCode + ' - ' + inputName;

		$('#locationName').text(locationName);

		$
				.getJSON(
						'getEquipmentByLocation.do',
						{
							locationKey : inputKey,
							ajax : 'true'
						},
						function(data) {
							var html = '<option value=""><spring:message
								code="common.pleaseSelect" /></option>';
							var len = data.length;
							for (var i = 0; i < len; i++) {
								html += '<option value="' + data[i].key + '">'
										+ data[i].code + ' - ' + data[i].name + '</option>';
							}
							html += '</option>';
							$('#equipment').html(html);

						});

	}
</script>
										<ul class="list-group locationtree" cssClass="form-control"
											id="radioButtonLocationtree">
											<template:radioButtonLocationTree
												node="${equipmentForm.availableLocationTree}" />
										</ul>
									</div>
								</div>
							</div>
							<div
								class="form-group <spring:bind path="code"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label"><spring:message
										code="equipment.code" />*</label>
								<div class="col-sm-10">
									<spring:message code="equipment.code" var="defectCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${defectCode}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="equipment.name" /></label>
								<div class="col-sm-10">
									<spring:message code="equipment.name" var="name" />
									<form:input path="name" cssClass="form-control"
										placeholder="${name}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group  <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="equipment.description" /></label>
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
										test="${currRole.grantedPrivilegeCodeList.contains(searchEquipment)}">
										<c:if test="${readOnly == false && isCreate == false}">
											<c:choose>
												<c:when test="${equipmentForm.referrer == 's'}">
													<a  onclick="showLoading()" class="btn btn-primary " href="DoSearchEquipment.do"><spring:message
															code="button.back.to.search" /></a>
												</c:when>
												<c:otherwise>
													<!-- this view page will go to search -->
													<a  onclick="showLoading()" class="btn btn-primary "
														href="ViewEquipment.do?key=${equipmentForm.key}&r=s"><spring:message
															code="button.back" /></a>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${readOnly == true}">
											<a  onclick="showLoading()" class="btn btn-primary " href="DoSearchEquipment.do"><spring:message
													code="button.back.to.search" /></a>
										</c:if>
									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<c:if test="${readOnly == false}">
										<button  onclick="showLoading()" type="submit" class="btn btn-primary ">
											<spring:message code="button.submit" />
										</button>
									</c:if>
								</div>
								<div class="col-xs-2 col-lg-1 ">
									<c:if test="${readOnly == true}">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(modifyEquipment)}">
											<button onclick="showLoading()" type="submit" class="btn btn-primary ">
												<spring:message code="button.modify" />
											</button>
										</c:if>
									</c:if>
									<c:if test="${readOnly == false}">
										<c:if test="${isCreate == true}">
											<a onclick="showLoading()" href="CreateEquipment.do" class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
										<c:if test="${isCreate == false}">
											<a onclick="showLoading()"
												href="<c:url value="ModifyEquipment.do"><c:param name="key" value="${equipmentForm.key}"/></c:url>"
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
  menu_toggle("sub_admin_equipment");

  if(${isCreate}){
		menu_select("createEquipment");
	} else 
		menu_select("searchEquipment");
  
</script>
