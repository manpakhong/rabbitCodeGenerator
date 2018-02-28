<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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
			<li><a href="Tool.do"> <spring:message
						code="menu.administration.defectMgt.tools" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.defectMgt.tools.search" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form cssClass="form-horizontal" commandName="searchToolForm"
						method="post" action="DoSearchTool.do">
						<form:hidden path="siteKey" />
						<c:if test="${searchToolForm.deleteSuccess}">
							<div class="form-group alert alert-success">
								<spring:message code="tool.delete.success"
									arguments="${searchToolForm.deletedName}" />
							</div>
						</c:if>
						<c:if test="${searchToolForm.hasDefect}">
							<div class="form-group alert alert-danger">
								<spring:message code="tool.hasDefect"
									arguments="${searchToolForm.deletedName}" />
							</div>
						</c:if>
						<input type="hidden" name="deleteSuccess" value="false" />
						<input type="hidden" name="hasDefect" value="false" />
						<div class="box-body">
							<div class="form-group">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="tools.code" /></label>
								<div class="col-sm-4">
									<spring:message code="tools.code" var="toolsCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${toolsCode}" />
								</div>
								<label for="" class="col-sm-2 control-label"><spring:message
										code="tools.name" /></label>
								<div class="col-sm-4">
									<spring:message code="tools.name" var="toolsName" />
									<form:input path="name" cssClass="form-control"
										placeholder="${toolsName}" />
								</div>
							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">
									<button onclick="showLoading()" type="submit"
										class="btn btn-primary ">
										<spring:message code="button.search" />
									</button>
								</div>
								<div class="col-xs-2 col-lg-1">
									<a onclick="showLoading()" href="SearchTool.do"
										class="btn btn-primary "><spring:message
											code="button.reset" /></a>
								</div>
							</div>
						</div>
						<!-- /.box-body -->
						<form:hidden path="fullListSize"/>
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>
		<div class="row  pull-right">
			<div class="col-xs-12 ">
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.searchTools')"
					var="viewTools" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.modifyTools')"
					var="modifyTools" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.removeTools')"
					var="removeTools" />
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(viewTools)}">
							&nbsp; <i class="fa fa-search"></i>
					<spring:message code="search.view" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(modifyTools)}">
							&nbsp; <i class="fa fa-pencil"></i>
					<spring:message code="search.modify" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(removeTools)}">
							&nbsp; <i class="fa fa-trash-o"></i>
					<spring:message code="search.remove" />
				</c:if>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
					<c:if test="${searchToolForm.fullListSize>0}">
					<form:form commandName="hidden_form" cssClass="form-horizontal" style="display:none">
								<button class="btn btn-primary" id="btn_export"
									onclick="exportToExcel()">
									<spring:message code="button.exportExcel" />
								</button>
								<input id="hidden_submit" type="submit"
									style="visibility: hidden;">
							</form:form>
					</c:if>
					<c:if test="${searchToolForm.fullListSize>0}">
						<div><spring:message code="defect.totalNumber" /><span id="totalNumber"></span></div>
						</c:if>
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							<thead>
								<tr>
									<th width="13%"></th>
									<th width="20%" style="word-wrap: break-word;"><spring:message
											code="tools.code" /></th>
									<th width="30%" style="word-wrap: break-word;"><spring:message
											code="tools.name" /></th>
									<th style="word-wrap: break-word;"><spring:message
											code="tools.description" /></th>
								</tr>
							</thead>
						</table>
					</div>
					<!-- /.box-body -->
				</div>
				<!-- /.box -->
			</div>
		</div>

	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<!-- Confirm Modal -->
<div id="confirmDeleteModal" class="modal fade" role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<spring:message code="tool.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="tool.confirm.delete.message" />
				</p>
				<br />
				<p id="confirmName"></p>
			</div>
			<div class="modal-footer">
				<a class="btn btn-danger btn-ok" id="confirmButton"
					onclick="showLoading()" style="margin-bottom: 0px"><spring:message
						code="button.delete" /></a>
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
		$('#confirmButton').attr('href', 'DoDeleteTool.do?key=' + key);
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
		"sAjaxSource" : "DoToolDataTable.do",
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

	menu_toggle("sub_admin");
	menu_toggle("sub_admin_defect");
	menu_toggle("sub_admin_tool");
	menu_select("searchTool");
</script>
<script>
console.log(${searchToolForm.fullListSize})
var fullListSize = ${searchToolForm.fullListSize};
if(fullListSize>0){
	$('#totalNumber').text(fullListSize);
}

function exportToExcel() {
	var form = document.getElementById("hidden_form");
	form.setAttribute("method", "post");
	form.setAttribute("target", "_blank");
	form.setAttribute("action", "ExportTool.do");

	//form.submit();
	$("#hidden_submit").click();
}
</script>