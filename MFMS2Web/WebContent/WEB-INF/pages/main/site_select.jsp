<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="../common/left.jsp" />


<c:set var="next">
	<spring:message code="common.next" />
</c:set>
<c:set var="previous">
	<spring:message code="common.previous" />
</c:set>
<input id="next" value="${next}" type="hidden" />
<input id="previous" value="${previous}" type="hidden" />

<style>
#scrollDiv{
			overflow-x: hidden !important;
}

</style>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_switch_site_b"></i> <spring:message
					code="menu.systemMgt.site" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 102%; max-width: 102%;">
							<thead>
								<tr>
									<th width="10%"></th>
									<th width="20%"><spring:message code="site.site" /></th>
									<th width="70%"><spring:message code="site.role" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${siteSelectionForm.accountRoleList}"
									var="accountRole">
									<tr>
										<td class="text-center"><a onclick="showLoading()"
											href="Home.do?roleKey=${accountRole.role.key}"><i
												class="icon-icon_home_b fa-lg"></i></a></td>
										<td style="word-wrap: break-word;"><c:out
												value="${accountRole.role.site.name }" /></td>
										<td style="word-wrap: break-word;"><c:out
												value="${accountRole.role.name }" /></td>
									</tr>
								</c:forEach>
							</tbody>
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

<script src="import/datatables/jquery.dataTables.js"></script>
<script src="import/datatables/dataTables.bootstrap.js"></script>
<script>
	$(function() {
		$('#example1').DataTable({
			"paging" : true,
			"lengthChange" : false,
			"searching" : false,
			"order" : [],
			"info" : false,
			"autoWidth" : false,
			"language" : {
				"paginate" : {
					"previous" : $('#previous').val(),
					"next" : $('#next').val()
				}
			},
			"aoColumnDefs" : [ {
				'bSortable' : false,
				'aTargets' : [ 0 ]
			} ]
		});
	});
</script>



<script>
	menu_select("sub_siteSelect");
</script>
