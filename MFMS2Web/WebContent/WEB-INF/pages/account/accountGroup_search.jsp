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
			<li ><a href="AccountManagement.do"><i class="icon-icon_account_management_b"></i> <spring:message code="menu.accountMgt"/></a></li>
			<li><a href="AccountGroup.do"> <spring:message code="menu.accountMgt.accountGroup"/></a></li>
			<li class="active"><spring:message code="menu.accountMgt.accountGroup.search"/></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
	
			<div class="row  pull-right">
				<div class="col-xs-12 ">
					<spring:eval
						expression="@privilegeMap.getProperty('privilege.code.searchAccountGroup')"
						var="viewAccountGroup" />
					<spring:eval
						expression="@privilegeMap.getProperty('privilege.code.modifyAccountGroup')"
						var="modifyAccountGroup" />
					<spring:eval
						expression="@privilegeMap.getProperty('privilege.code.removeAccountGroup')"
						var="removeAccountGroup" />
					<c:if
						test="${currRole.grantedPrivilegeCodeList.contains(viewAccountGroup)}">
							&nbsp; <i class="fa fa-search"></i>
						<spring:message code="search.view" />
					</c:if>
					<c:if
						test="${currRole.grantedPrivilegeCodeList.contains(modifyAccountGroup)}">
							&nbsp; <i class="fa fa-pencil"></i>
						<spring:message code="search.modify" />
					</c:if>
					<c:if
						test="${currRole.grantedPrivilegeCodeList.contains(removeAccountGroup)}">
							&nbsp; <i class="fa fa-trash-o"></i>
						<spring:message code="search.remove" />
					</c:if>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<!-- validation error messages -->
						<form:form cssClass="form-horizontal"
							commandName="searchAccountGroupForm">
							<spring:bind path="*">
								<c:if test="${status.error}">
									<div class="form-group alert alert-danger">
										<form:errors path="*" />
									</div>
								</c:if>
							</spring:bind>
							<c:if test="${searchAccountGroupForm.referrerStr == 'd'}">
								<div class="form-group alert alert-success">
									<spring:message code="menu.accountMgt.accountGroup" />
									<c:out value="${searchAccountGroupForm.deletedName}" />
									<spring:message code="has.been.deleted" />
								</div>
							</c:if>
						</form:form>
						<div class="box-body">
						
						<form:form commandName="hidden_form" cssClass="form-horizontal" style="display:none">
								<button class="btn btn-primary" id="btn_export"
									onclick="exportToExcel()">
									<spring:message code="button.exportExcel" />
								</button>
								<input id="hidden_submit" type="submit"
									style="visibility: hidden;">
							</form:form>
							
							<table id="example1" class="table table-bordered table-striped"
								style="table-layout: fixed; width: 100%; max-width: 100%;">
								<thead>
									<tr>
										<th width="13%"></th>
										<th width="30%" style=" word-wrap: break-word;"><spring:message
												code="accountGroup.name" /></th>
										<th><spring:message code="accountGroup.description" /></th>
									</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
				</div>
				<!-- /.col -->
			</div>
	</section>
	<!-- /.content -->
</div>

<div id="confirmDeleteModal" class="modal fade" role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<spring:message code="accountGroup.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="accountGroup.confirm.delete.message" />
				</p>
				<br />
				<p id="confirmName"></p>
			</div>
			<div class="modal-footer">
				<a onclick="showLoading()" class="btn btn-danger btn-ok" id="confirmButton"
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

<script type="text/javascript">
	function showDelete(key, code) {
		$('#confirmDeleteModal').modal('show');
		// put link on modal
		$('#confirmButton').attr('href', 'DoDeleteAccountGroup.do?key=' + key);
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
		"sAjaxSource" : "DoAccountGroupDataTable.do",
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
	
	menu_toggle("sub_am");
	menu_toggle("sub_am_group");
	menu_select("listAccountGroup");
</script>

<script>
function exportToExcel() {

	var form = document.getElementById("hidden_form");
	form.setAttribute("method", "post");
	form.setAttribute("target", "_blank");
	form.setAttribute("action", "ExportAccountGroup.do");

	//form.submit();
	$("#hidden_submit").click();
}


</script>
