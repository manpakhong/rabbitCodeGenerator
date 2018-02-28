<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<style>
.table td:first-child {
	text-align: center;
	color: #4491bf;
	/*cursor: pointer;*/
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
			<li><a href="AccountManagement.do"><i
					class="icon-icon_account_management_b"></i> <spring:message
						code="menu.accountMgt" /></a></li>
			<li><a href="Role.do"> <spring:message
						code="menu.accountMgt.role" /></a></li>
			<li class="active"><spring:message
					code="menu.accountMgt.role.search" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form cssClass="form-horizontal" commandName="searchRoleForm"
						method="post" action="DoSearchRole.do">
						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<c:if test="${searchRoleForm.deleteSuccess}">
							<div class="form-group alert alert-success">
								<spring:message code="role.delete.success"
									arguments="${searchRoleForm.deletedName}" />
							</div>
						</c:if>
						<form:hidden path="siteKey" />
						<input type="hidden" name="deleteSuccess" value="false" />
						<div class="box-body">
							<div class="form-group">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="role.name" /></label>
								<div class="col-sm-10">
								
								<spring:message code="role.name" var="name" />
									<form:input path="name" placeholder="${name}" cssClass="form-control" />
								</div>
							</div>
							<div class="form-group">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="role.description" /></label>
								<div class="col-sm-10">
								
								<spring:message code="role.description" var="description" />
								
									<form:textarea path="description" placeholder="${description}" cssClass="form-control" />
								</div>
							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">
									<button type="submit" class="btn btn-primary ">
										<spring:message code="button.search" />
									</button>
								</div>
								<div class="col-xs-2 col-lg-1">
									<a href="SearchRole.do" class="btn btn-primary "><spring:message
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
					expression="@privilegeMap.getProperty('privilege.code.searchAccountRole')"
					var="viewAccountRole" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.modifyAccountRole')"
					var="modifyAccountRole" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.assignAccount')"
					var="assignAccount" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.removeAccountRole')"
					var="removeAccountRole" />
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(viewAccountRole)}">
							&nbsp; <i class="fa fa-search"></i>
					<spring:message code="search.view" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(modifyAccountRole)}">
							&nbsp; <i class="fa fa-pencil"></i>
					<spring:message code="search.modify" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(assignAccount)}">
							&nbsp; <i class="fa fa-user"></i>
					<spring:message code="button.account.assign" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(removeAccountRole)}">
							&nbsp; <i class="fa fa-trash-o"></i>
					<spring:message code="search.remove" />
				</c:if>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
					<c:if test="${searchRoleForm.fullListSize>0}">
					<form:form commandName="hidden_form" cssClass="form-horizontal" style="display:none">
								<button class="btn btn-primary" id="btn_export"
									onclick="exportToExcel()">
									<spring:message code="button.exportExcel" />
								</button>
								<input id="hidden_submit" type="submit"
									style="visibility: hidden;">
							</form:form>
					</c:if>
					<c:if test="${searchRoleForm.fullListSize>0}">
						<div><spring:message code="defect.totalNumber" /><span id="totalNumber"></span></div>
						</c:if>

						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							<thead>
								<tr>
									<th width="13%"></th>
									<th width="30%" style="word-wrap: break-word; "><spring:message
											code="role.name" /></th>
									<th><spring:message code="role.description" /></th>
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
<!-- /.content-wrapper -->

<!-- Confirm Modal -->
<div id="confirmDeleteModal" class="modal fade" role="dialog">
	<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<spring:message code="role.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="role.confirm.delete.message" />
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

<script type="text/javascript">
	function showDelete(key, code) {
		$('#confirmDeleteModal').modal('show');
		// put link on modal
		$('#confirmButton').attr('href', 'DoDeleteRole.do?key=' + key);
		// put name on modal
		$('#confirmName').text(code);
	}
	//var d = $(document).width();
	
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
			"sAjaxSource" : "DoRoleDataTable.do",
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
	menu_toggle("sub_am_role");
	menu_select("searchRole");
</script>



<script>
console.log(${searchRoleForm.fullListSize});

var fullListSize = ${searchRoleForm.fullListSize};

	if(fullListSize > 0){
				
		$('#totalNumber').text(fullListSize);
		}	

	function exportToExcel() {
		var form = document.getElementById("hidden_form");
		form.setAttribute("method", "post");
		form.setAttribute("target", "_blank");
		form.setAttribute("action", "ExportRole.do");

		//form.submit();
		$("#hidden_submit").click();
	}
	

</script>
