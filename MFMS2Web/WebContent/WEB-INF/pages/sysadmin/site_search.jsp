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
</style>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="Sysadmin.do"><i
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.systemMgt" /></a></li>
			<li><a href="Site.do"> <spring:message
						code="menu.systemMgt.site" /></a></li>
			<li class="active"><spring:message
					code="menu.systemMgt.site.search" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form cssClass="form-horizontal" commandName="searchSiteForm"
						method="post" action="DoSearchSite.do">
						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<c:if test="${searchSiteForm.deleteSuccess}">
							<div class="form-group alert alert-success">
								<spring:message code="site.delete.success"
									arguments="${searchSiteForm.deletedName}" />
							</div>
						</c:if>
						<input type="hidden" name="deleteSuccess" value="false" />
						<div class="box-body">
							<div class="form-group">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="site.name" /></label>
								<div class="col-sm-10">

									<spring:message code="site.name" var="name" />

									<form:input path="name" cssClass="form-control"
										placeholder="${name}" />
								</div>
							</div>
							<div class="form-group">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="site.address" /></label>
								<div class="col-sm-10">

									<spring:message code="site.address" var="address" />

									<form:textarea path="address" cssClass="form-control"
										placeholder="${address}" />
								</div>
							</div>
							<div class="box-footer">
								<div class="form-group">
									<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">
										<button onclick="showLoading()" type="submit"
											class="btn btn-primary ">
											<spring:message code="button.search" />
										</button>
									</div>
									<div class="col-xs-2 col-lg-1">
										<a onclick="showLoading()" href="SearchSite.do"
											class="btn btn-primary "><spring:message
												code="button.reset" /></a>
									</div>
								</div>
							</div>
						</div>
						<!-- /.box-body -->
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>
		<div class="row  pull-right">
			<div class="col-xs-12 ">
				&nbsp; <i class="fa fa-search"></i>
				<spring:message code="search.view" />
				&nbsp; <i class="fa fa-pencil"></i>
				<spring:message code="search.modify" />
				&nbsp; <i class="fa fa-trash-o"></i>
				<spring:message code="search.remove" />
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							<thead>
								<tr>
									<th width="80"></th>
									<th width="250" style="word-wrap: break-word;"><spring:message
											code="site.name" /></th>
									<th style="word-wrap: break-word;"><spring:message
											code="site.address" /></th>
									<th width="200" style="word-wrap: break-word;"><spring:message
											code="site.contact.number" /></th>
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
					<spring:message code="site.confirm.delete" />
				</h4>
			</div>
			<div class="modal-body">
				<p>
					<spring:message code="site.confirm.delete.message" />
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
		$('#confirmButton').attr('href', 'DoDeleteSite.do?key=' + key);
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
		"sAjaxSource" : "DoSiteDataTable.do",
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
	menu_toggle("sub_system");
	menu_toggle("sub_system_site");
	menu_select("searchSite");
</script>
