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

.table td:not (:first-child ){
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
						code="menu.administration.defectMgt" /><a></a></li>
			<li><a href="ProblemCode.do"> <spring:message
						code="menu.administration.defectMgt.problemCode" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.defectMgt.problemCode.search" /></a></li>
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
						commandName="searchProblemCodeForm" method="post"
						action="DoSearchProblemCode.do">
						<form:hidden path="siteKey" />
						<c:if test="${searchProblemCodeForm.deleteSuccess}">
							<div class="form-group alert alert-success">
								<spring:message code="problemCode.delete.success"
									arguments="${searchProblemCodeForm.deletedName}" />
							</div>
						</c:if>
						<c:if test="${searchProblemCodeForm.hasDefect}">
							<div class="form-group alert alert-danger">
								<spring:message code="problemCode.hasDefect"
									arguments="${searchProblemCodeForm.deletedName}" />
							</div>
						</c:if>
						<input type="hidden" name="deleteSuccess" value="false" />
						<input type="hidden" name="hasDefect" value="false" />
						<div class="box-body">
							<div class="form-group">
								<div class="row">
									<label class="col-lg-2 control-label"><spring:message
											code="problemCode.failureClass" /></label>
									<div class="col-lg-10">
										<form:select path="parentKey" cssClass="form-control"
											items="${failureClassList}">
										</form:select>
									</div>
								</div>
								<div class="row">
									<label class="col-lg-2 control-label"><spring:message
											code="problemCode.code" /></label>
									<div class="col-lg-4">
										<spring:message code="problemCode.code" var="problemCodeCode" />
										<form:input path="code" cssClass="form-control"
											placeholder="${problemCodeCode}" />
									</div>
									<label class="col-lg-2 control-label"><spring:message
											code="problemCode.name" /></label>
									<div class="col-lg-4">
										<spring:message code="problemCode.name" var="problemCodeName" />
										<form:input path="name" cssClass="form-control"
											placeholder="${problemCodeName}" />
									</div>
								</div>
								<div class="row">
									<label class="col-lg-2 control-label"><spring:message
											code="problemCode.priority" /></label>
									<div class="col-lg-4">
										<form:select path="defaultPriority" cssClass="form-control"
											items="${priorityList}"></form:select>
									</div>
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
									<a onclick="showLoading()" href="SearchProblemCode.do"
										class="btn btn-primary "><spring:message
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
					expression="@privilegeMap.getProperty('privilege.code.searchProblemCode')"
					var="viewProblemCode" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.modifyProblemCode')"
					var="modifyProblemCode" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.removeProblemCode')"
					var="removeProblemCode" />


				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(viewProblemCode)}">
						
						&nbsp; <i class="fa fa-search"></i>
					<spring:message code="search.view" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(modifyProblemCode)}">
						&nbsp; <i class="fa fa-pencil"></i>
					<spring:message code="search.modify" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(removeProblemCode)}">
						&nbsp; <i class="fa fa-trash-o"></i>
					<spring:message code="search.remove" />
				</c:if>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
						<c:if test="${searchProblemCodeForm.fullListSize>0}">
							<form:form commandName="hidden_form" cssClass="form-horizontal" style="display:none">
								<button class="btn btn-primary" id="btn_export"
									onclick="exportToExcel()">
									<spring:message code="button.exportExcel" />
								</button>
								<input id="hidden_submit" type="submit"
									style="visibility: hidden;">
							</form:form>
						</c:if>
						<c:if test="${searchProblemCodeForm.fullListSize>0}">
							<div>
								<spring:message code="defect.totalNumber" />
								<span id="totalNumber"></span>
							</div>
						</c:if>
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							<thead>
								<tr>
									<th width="13%"></th>
									<th width="20%" style="word-wrap: break-word;"><spring:message
											code="problemCode.failureClass" /></th>
									<th width="15%" style="word-wrap: break-word;"><spring:message
											code="problemCode.code" /></th>
									<th width="25%" style="word-wrap: break-word;"><spring:message
											code="problemCode.name" /></th>
									<th width="10%" style="word-wrap: break-word;"><spring:message
											code="problemCode.priority" /></th>
									<th style="word-wrap: break-word;"><spring:message
											code="problemCode.description" /></th>
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
					<spring:message code="problemCode.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="problemCode.confirm.delete.message" />
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
	$('#confirmButton').attr('href', 'DoDeleteProblemCode.do?key=' + key);
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
	"sAjaxSource" : "DoProblemCodeDataTable.do",
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
	menu_toggle("sub_admin_problemcode");
	menu_select("searchProblemCode");
</script>

<script>
console.log(${searchProblemCodeForm.fullListSize})
var fullListSize = ${searchProblemCodeForm.fullListSize};
if(fullListSize>0){
	$('#totalNumber').text(fullListSize);
}

function exportToExcel() {
	var form = document.getElementById("hidden_form");
	form.setAttribute("method", "post");
	form.setAttribute("target", "_blank");
	form.setAttribute("action", "ExportProblemCode.do");

	//form.submit();
	$("#hidden_submit").click();
}

</script>
