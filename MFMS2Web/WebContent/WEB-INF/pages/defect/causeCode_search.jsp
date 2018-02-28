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
			<li><a href="CauseCode.do"> <spring:message
						code="menu.administration.defectMgt.causeCode" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.defectMgt.causeCode.search" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<form:form cssClass="form-horizontal"
						commandName="searchCauseCodeForm" method="post"
						action="DoSearchCauseCode.do">
						<form:hidden path="siteKey" />
						<c:if test="${searchCauseCodeForm.deleteSuccess}">
							<div class="form-group alert alert-success">
								<spring:message code="causeCode.delete.success"
									arguments="${searchCauseCodeForm.deletedName}" />
							</div>
						</c:if>
						<c:if test="${searchCauseCodeForm.hasDefect}">
							<div class="form-group alert alert-danger">
								<spring:message code="causeCode.hasDefect"
									arguments="${searchCauseCodeForm.deletedName}" />
							</div>
						</c:if>
						<input id="next" value="${next}" type="hidden" />
						<input id="previous" value="${previous}" type="hidden" />
						<input type="hidden" name="deleteSuccess" value="false" />
						<input type="hidden" name="hasDefect" value="false" />
						<div class="box-body">
							<div class="form-group">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="causeCode.code" /></label>
								<div class="col-sm-4">
									<spring:message code="causeCode.code" var="causeCodeCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${causeCodeCode}" />
								</div>
								<label class="col-sm-2 control-label"><spring:message
										code="causeCode.name" /></label>
								<div class="col-sm-4">
									<spring:message code="causeCode.name" var="causeCodeName" />
									<form:input path="name" cssClass="form-control"
										placeholder="${causeCodeName}" />
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
									<a onclick="showLoading()" href="SearchCauseCode.do"
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
					expression="@privilegeMap.getProperty('privilege.code.searchCauseCode')"
					var="viewCauseCode" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.modifyCauseCode')"
					var="modifyCauseCode" />
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.removeCauseCode')"
					var="removeCauseCode" />
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(viewCauseCode)}">
					&nbsp; <i class="fa fa-search"></i>
					<spring:message code="search.view" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(modifyCauseCode)}">
						&nbsp; <i class="fa fa-pencil"></i>
					<spring:message code="search.modify" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(removeCauseCode)}">
						&nbsp; <i class="fa fa-trash-o"></i>
					<spring:message code="search.remove" />
				</c:if>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
					<c:if test="${searchCauseCodeForm.fullListSize>0}">
					<form:form commandName="hidden_form" cssClass="form-horizontal"  style="display:none">
								<button class="btn btn-primary" id="btn_export"
									onclick="exportToExcel()">
									<spring:message code="button.exportExcel" />
								</button>
								<input id="hidden_submit" type="submit"
									style="visibility: hidden;">
							</form:form>
					</c:if>
					<c:if test="${searchCauseCodeForm.fullListSize>0}">
						<div><spring:message code="defect.totalNumber" /><span id="totalNumber"></span></div>
						</c:if>
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							<thead>
								<tr>
									<th width="13%"></th>
									<th width="20%" style="word-wrap: break-word;"><spring:message
											code="causeCode.code" /></th>
									<th width="30%" style="word-wrap: break-word;"><spring:message
											code="causeCode.name" /></th>
									<th style="word-wrap: break-word;"><spring:message
											code="causeCode.description" /></th>
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
</div>


<div id="confirmDeleteModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">
					<spring:message code="defect.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="defect.confirm.delete.message" />
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

<script type="text/javascript">
	function showDelete(key, code) {
		$('#confirmDeleteModal').modal('show');
		// put link on modal
		$('#confirmButton').attr('href', 'DoDeleteCauseCode.do?key=' + key);
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
		"sAjaxSource" : "DoCauseCodeDataTable.do",
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
	menu_toggle("sub_admin_causecode");
	menu_select("searchCauseCode");
</script>

<script>
console.log(${searchCauseCodeForm.fullListSize})
var fullListSize = ${searchCauseCodeForm.fullListSize};
if(fullListSize>0){
	$('#totalNumber').text(fullListSize);
}

function exportToExcel() {
	var form = document.getElementById("hidden_form");
	form.setAttribute("method", "post");
	form.setAttribute("target", "_blank");
	form.setAttribute("action", "ExportCauseCode.do");

	//form.submit();
	$("#hidden_submit").click();
}
</script>
