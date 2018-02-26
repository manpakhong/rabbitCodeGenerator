<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script src="import/underscorejs/underscore-min.1.8.3.js"></script>

<script src="import/datatables/jquery.dataTables.js"></script>
<script src="import/datatables/dataTables.bootstrap.js"></script>
<script>
	$(function() {		
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
			"sAjaxSource" : "DoPatrolDataTable.do",
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
		
	});
	
	$( document ).ready(function() {
		$("#example1").wrap("<div id='scrollDiv'></div>" );
		$("#example1").css("min-width",scrollDivWidth+"px");
	    var sw = $("#example1_wrapper").width();
	    scrollDiv(sw-30, scrollDivWidth);
		
		$( window ).resize(function() {			
			$("#example1").css("min-width",scrollDivWidth+"px");
			 sw2 = $("#scrollDiv").width();
			 scrollDiv(sw2, scrollDivWidth);			
			});
	});
</script>




<form style="display: hidden" action="/the/url" method="POST" id="form">
	<input type="hidden" id="var1" name="var1" value="" />
</form>
<div id="RouteNumberDiv" style="display:none"><spring:message code="defect.totalNumber" /><span id="totalNumber"></span></div>
<table id="example1" class="table table-bordered table-striped"
	style="table-layout: fixed; width: 100%; max-width: 100%;">
	<thead>
		<th style="width: 13%;"></th>
		<th style="width: 30%; word-wrap: break-word;"><spring:message
				code="patrol.route.code" /></th>
		<th style="width: 28%; word-wrap: break-word;"><spring:message
				code="patrol.route.name" /></th>
		<th><spring:message code="patrol.route.start.location" /></th>
	</thead>
</table>
<!-- 
<jsp:include page="../common/pagination.jsp" />
 -->

<!-- Modal -->
<div class="modal fade" id="routeModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel"><spring:message code="patrol.route.start.location.select" /></h4>
			</div>
			
			<div class="modal-body" id="routeInfoBody"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="refreshStartingLocation();">
					<spring:message code="button.ok" />
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"
					style="margin-bottom: 0;">
					<spring:message code="button.cancel" />
				</button>
			</div>
		</div>
	</div>
</div>

<!-- Delete Confirm Modal -->
<div class="modal fade" id="deleteConfirmModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="">
					<spring:message code="patrol.route.delete.title" />
				</h4>
			</div>
			<div class="modal-body" id="deleteContent">
				<div class="row">
					<div class="col-xs-12">
						<spring:message code="patrol.route.delete.msg" />
					</div>
					<div class="col-xs-12">
						<spring:message code="patrol.route.code" />
						: <label id="deleteCode"></label>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal"
					style="margin-bottom: 0;" onclick="" id="deleteConfirmBtn">
					<spring:message code="button.delete" />
				</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal"
					style="margin-bottom: 0;">
					<spring:message code="button.close" />
				</button>
			</div>
		</div>
	</div>
</div>

<script>

var searchResult = '${searchResultJson}';

var resultJson = document.getElementById('hidden_searchResultJson');
if (resultJson != null) {
	resultJson.value = searchResult;
}

</script>
