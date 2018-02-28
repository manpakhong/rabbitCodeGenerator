<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<jsp:include page="../common/left.jsp" />
<script src="import/datetimepicker/moment.js"></script>
<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>





<style>
.table td:first-child {
	text-align: left;
	color: black;
	cursor: auto;
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
			<li><a href="ActivityLog.do"><spring:message
						code="menu.administration.log" /></a></li>
			<li class="active"><spring:message
					code="menu.administration.log.route" /></li>
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
						commandName="routeDefActionForm" method="post"
						action="DoSearchRouteDefAction.do">
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<div class="box-body">
							<div
								class="form-group <spring:bind path="key"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label class="col-sm-2 control-label"><spring:message
										code="common.from" /></label>
								<div class="col-sm-4"
									<spring:message code="common.from" var="from"/>>
									<form:input id="from" path="from" cssClass="form-control"
										readonly="true" style="background-color: #fff;"
										placeholder="${from}" />
								</div>
								<label class="col-sm-2 control-label"><spring:message
										code="common.to" /></label>
								<div class="col-sm-4"
									<spring:message code="common.to" var="to"/>>
									<form:input id="to" path="to" cssClass="form-control"
										readonly="true" style="background-color: #fff;"
										placeholder="${to}" />
								</div>
								<label class="col-sm-2 control-label"><spring:message
										code="menu.accountMgt.accountGroup" /></label>
								<div class="col-sm-4">
									<form:select path="accountGroupKey" cssClass="form-control"
										id="accountGroup" items="${accountGroupList}">
									</form:select>
								</div>
								<label class="col-sm-2 control-label"><spring:message
										code="menu.accountMgt.account" /></label>
								<div class="col-sm-4">
									<form:select path="accountKey" cssClass="form-control"
										id="account" items="${accountList}">
									</form:select>
								</div>
							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">
									<button onclick="showLoading()" type="submit"
										class="btn btn-primary ">
										<spring:message code="button.submit" />
									</button>
								</div>
								<div class="col-xs-2 col-lg-1 ">
									<a onclick="showLoading()" href="SearchRouteDefAction.do"
										class="btn btn-primary "><spring:message
											code="button.reset" /></a>
								</div>
							</div>
							<!-- /.box-footer -->
						</div>
						<!-- /.box-body -->
						<form:hidden path="fullListSize"/>
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>

		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
					<c:if test="${routeDefActionForm.fullListSize>0}">
						<div><spring:message code="defect.totalNumber" /><span id="totalNumber"></span></div>
						</c:if>
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							<thead>
								<tr>
									<th width="20%" style="word-wrap: break-word;"><spring:message
											code="action.date" /></th>
									<th width="40%" style="word-wrap: break-word;"><spring:message
											code="action.account" /></th>
									<th width="40%" style="word-wrap: break-word;"><spring:message
											code="action.type" /></th>
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


<script src="import/datatables/jquery.dataTables.js"></script>
<script src="import/datatables/dataTables.bootstrap.js"></script>
<script>
	menu_toggle("sub_admin");
	menu_toggle("sub_admin_activityLog");
	menu_select("patrolActionLog");
</script>


<script type="text/javascript">
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
		"sAjaxSource" : "DoRouteDefActionDataTable.do",
		"language" : {
			"processing" : $('#processing').val(),
			"paginate" : {
				"previous" : $('#previous').val(),
				"next" : $('#next').val()
			},

		}
	});
</script>

<script type="text/javascript">
	$(function() {
		$('#from').datetimepicker({
			format : 'yyyy-mm-dd',
			weekStart : 0,
			clearBtn : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			forceParse : 0,
			showMeridian : 0
		}).on("changeDate", function(ev) {
			var fullDate = $("#from").val()
			$("#to").datetimepicker("setStartDate", fullDate);
		});
		$('#to').datetimepicker({
			format : 'yyyy-mm-dd',
			weekStart : 0,
			clearBtn : 1,
			todayBtn : 1,
			minView : 2,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			forceParse : 0,
			showMeridian : 0
			
		}).on("changeDate", function(ev) {
			var fullDate = $("#to").val()
			$("#from").datetimepicker("setEndDate", fullDate);
		});
	});
</script>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						
						var fullDate = $("#from").val()
						$("#to").datetimepicker("setStartDate", fullDate);
						
						var fullDate = $("#to").val()
						$("#from").datetimepicker("setEndDate", fullDate);
						
						$('#accountGroup')
								.change(
										function() {
											$
													.getJSON(
															'getUserAccountByAccountGroup.do',
															{
																accountGroupKey : $(
																		this)
																		.val(),
																ajax : 'true'
															},
															function(data) {
																$('#account')
																		.html(
																				"");
																var pleaseselect = '<option value=""><spring:message code="common.pleaseSelect" /></option>';
																$('#account')
																		.append(
																				pleaseselect);

																var len = data.length;
																for (var i = 0; i < len; i++) {
																	$(
																			'#account')
																			.append(
																					$(
																							"<option></option>")
																							.text(
																									data[i].name)
																							.val(
																									data[i].key));
																}

															});
										});
					});
</script>
<script>
console.log(${routeDefActionForm.fullListSize})
var fullListSize = ${routeDefActionForm.fullListSize};
if(fullListSize>0){
	$('#totalNumber').text(fullListSize);
}

</script>

