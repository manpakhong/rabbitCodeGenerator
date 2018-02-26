<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="template"%>
<jsp:include page="../common/left.jsp" />



<style>
.table td:first-child {
	text-align: center;
	color: #4491bf;
	cursor: pointer;
}

.table td:not(:first-child){
	text-align: left;
}
</style>


<script>
	$(document).ready(function() {
		$('.locationtree ul').hide();
		$('.locationtree li').hide();
		$('.locationtree li:first').show();
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
			<%-- <li><a href="AdministrationCommon.do"> <spring:message
						code="menu.administration.common" /></a></li> --%>
			<li><a href="Location.do"> <spring:message
						code="menu.administration.common.location" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.common.location.search" /></li>
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
						commandName="searchLocationForm" method="post"
						action="DoSearchLocation.do">
						<form:hidden path="siteKey" />
						<c:if test="${searchLocationForm.deleteSuccess}">
							<div class="form-group alert alert-success">
								<spring:message code="location.delete.success"
									arguments="${searchLocationForm.deletedName}" />
							</div>
						</c:if>
						<c:if test="${searchLocationForm.hasChild}">
							<div class="form-group alert alert-danger">
								<spring:message code="location.hasChild"
									arguments="${searchLocationForm.deletedName}" />
							</div>
						</c:if>
						<c:if test="${searchLocationForm.hasDefect}">
							<div class="form-group alert alert-danger">
								<spring:message code="location.hasDefect"
									arguments="${searchLocationForm.deletedName}" />
							</div>
						</c:if>
						<input type="hidden" name="deleteSuccess" value="false" />
						<input type="hidden" name="hasDefect" value="false" />
						<div class="box-body">
							<div class="form-group">
								<div
									class="<spring:bind path="parentKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="location.parent" /></label>
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
	function onLocationChange(inputCode, inputName)
	{
		event.stopPropagation();
		var locationName = ' : ' + inputCode + ' - ' + inputName;

		$('#locationName').text(locationName);
	}
</script>
											<ul class="list-group locationtree" cssClass="form-control"
												id="radioButtonLocationtree">
												<template:radioButtonLocationTreeForCreateLocation
													node="${searchLocationForm.availableLocationTree}" />
											</ul>
										</div>
									</div>
								</div>
								<label for="" class="col-sm-2 control-label"><spring:message
										code="location.code" /></label>
								<div class="col-sm-4">
									<spring:message code="location.code" var="locationCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${locationCode}" />
								</div>
								<label for="" class="col-sm-2 control-label"><spring:message
										code="location.name" /></label>
								<div class="col-sm-4">
									<spring:message code="location.name" var="locationName" />
									<form:input path="name" cssClass="form-control"
										placeholder="${locationName}" />
								</div>

							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">
									<button onclick="showLoading()" type="submit" class="btn btn-primary ">
										<spring:message code="button.search" />
									</button>
								</div>

								<div class="col-xs-2 col-lg-1">
									<a onclick="showLoading()" href="SearchLocation.do" class="btn btn-primary "><spring:message
											code="button.reset" /></a>
								</div>

							</div>
						</div>
						<!-- /.box-body -->
						<form:hidden path="fullListSize" />
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>
		<div class="row  pull-right">
			<div class="col-xs-12 ">
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.searchLocation')"
					var="viewLocation" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.modifyLocation')"
					var="modifyLocation" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.removeLocation')"
					var="removeLocation" />
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(viewLocation)}">
							&nbsp; <i class="fa fa-search"></i>
					<spring:message code="search.view" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(modifyLocation)}">
							&nbsp; <i class="fa fa-pencil"></i>
					<spring:message code="search.modify" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(removeLocation)}">
							&nbsp; <i class="fa fa-trash-o"></i>
					<spring:message code="search.remove" />
				</c:if>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
					<c:if test="${searchLocationForm.fullListSize>0}">
					<form:form commandName="hidden_form" cssClass="form-horizontal" style="display:none">
								<button class="btn btn-primary" id="btn_export"
									onclick="exportToExcel()">
									<spring:message code="button.exportExcel" />
								</button>
								<input id="hidden_submit" type="submit"
									style="visibility: hidden;">
							</form:form>
					</c:if>
					<c:if test="${searchLocationForm.fullListSize>0}">
						<div><spring:message code="defect.totalNumber" /><span id="totalNumber"></span></div>
						</c:if>
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							<thead>
								<tr>
									<th width="13%"></th>
									<th width="30%" style="word-wrap: break-word;"><spring:message
											code="location.parent" /></th>
									<th width="18%" style="word-wrap: break-word;"><spring:message
											code="location.code" /></th>
									<th><spring:message code="location.name" /></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>

	</section>
</div>

<div id="confirmDeleteModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<spring:message code="location.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="location.confirm.delete.message" />
				</p>
				<br />
				<p id="confirmName"></p>
			</div>
			<div class="modal-footer">
				<a class="btn btn-danger btn-ok" id="confirmButton" onclick="showLoading()"
					style="margin-bottom: 0px"><spring:message code="button.delete" /></a>
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0px">
					<spring:message code="button.close" />
				</button>
			</div>
		</div>
	</div>
</div>

<script src="import/datatables/jquery.dataTables.js"></script>
<script src="import/datatables/dataTables.bootstrap.js"></script>
<script>
function showDelete(key, code) {
	$('#confirmDeleteModal').modal('show');
	// put link on modal
	$('#confirmButton').attr('href', 'DoDeleteLocation.do?key=' + key);
	// put name on modal
	$('#confirmName').text(code);
}

var table = $('#example1').dataTable({
	"bPaginate" : true,
	"order" : [ 0, 'asc' ],
	"bInfo" : true,
	"lengthChange" : false,
	"searching" : false,
	"order" : [],
	"iDisplayStart" : 0,
	"bProcessing" : true,
	"bServerSide" : true,
	"sAjaxSource" : "DoLocationDataTable.do",
	"language" : {
		"processing" : $('#processing').val(),
		"paginate" : {
			"previous" : $('#previous').val(),
			"next" : $('#next').val()
		},
	},
	"aoColumnDefs" : [ {
		'bSortable' : false,
		'aTargets' : [ 0 ]
	} ]
});
</script>

<script>
	menu_toggle("sub_admin");
	menu_toggle("sub_admin_location");
	menu_select("searchLocation");
</script>

<script>
console.log(${searchLocationForm.fullListSize});
var fullListSize = ${searchLocationForm.fullListSize};
if(fullListSize>0){
	$('#totalNumber').text(fullListSize);
}


function exportToExcel() {
	var form = document.getElementById("hidden_form");
	form.setAttribute("method", "post");
	form.setAttribute("target", "_blank");
	form.setAttribute("action", "ExportLocation.do");

	//form.submit();
	$("#hidden_submit").click();
}
</script>
