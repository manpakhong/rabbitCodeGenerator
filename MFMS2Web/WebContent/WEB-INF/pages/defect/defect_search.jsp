<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			$('#accountGroup').change(function() {
				$.getJSON('getUserAccountByAccountGroup.do',
						{accountGroupKey : $(this).val(),ajax : 'true'},
								function(data) {
										$('#account').html("");
											var pleaseselect = '<option value=""><spring:message code="common.pleaseSelect" /></option>';
										$('#account').append(pleaseselect);
											var len = data.length;
											for (var i = 0; i < len; i++) {
												$('#account').append($("<option></option>").text(data[i].name).val(data[i].key));
												}
											});
										});

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
						$('.locationtree li').on(
								'click',
								function(e) {
									console.log(e);
									var container = $(this).find('> ul');
									//console.log(container);
									if (container.is(":visible")) {
										//container.hide('fast');
										// change arrow
										$(this).find('> div i').removeClass(
												'fa-angle-down').addClass(
												'fa-angle-left');
										//console.log($(this).find('> i'));
									} else {
										container.show('fast');
										// change arrow
										$(this).find('> div i').removeClass(
												'fa-angle-left').addClass(
												'fa-angle-down');
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


<script type="text/javascript">
	$(document).ready(function() {
		$('#failureClass').change(function() {
			$.getJSON('getProblemCodeByFailureClassKey.do',{
				failureClassKey : $(this).val(), ajax : 'true'
				},
			
				function(data) {
					var html = '<option value=""><spring:message code="common.pleaseSelect" /></option>';
					var len = data.length;
						for (var i = 0; i < len; i++) {
							html += '<option value="' + data[i].key + '">'
								+ data[i].code + ' - ' + data[i].name + '</option>';
								}
						html += '</option>';
								$('#problemCode').html(html);
						});
					});
				});
</script>

<script type="text/javascript">
	function exportToExcel() {
		$("#errorMsg").hide();
		$(".has-error").removeClass("has-error");

		var hasError = false;
		var errorStr = "";

		if (hasError) {

			$("#errorMsgStr").html(errorStr);
			$("#errorMsg").show();
			return;
		}

		var form = document.getElementById("hidden_form");
		form.setAttribute("method", "post");
		form.setAttribute("target", "_blank");
		form.setAttribute("action", "DoDefectExportExcel.do");

		//form.submit();
		$("#hidden_submit").click();
	}
</script>

<div class="content-wrapper">
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="DefectManagement.do"><i
					class="icon-icon_defect_management_b"></i> <spring:message
						code="menu.defectMgt" /></a></li>
			<li><a href="Defect.do"> <spring:message
						code="menu.defectMgt.defect" /></a></li>
			<li class="active"><spring:message
					code="menu.defectMgt.defect.search" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<form:form cssClass="form-horizontal"
						commandName="searchDefectForm" method="post"
						action="DoSearchDefect.do">
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>
						<form:hidden path="siteKey" />
						<form:hidden path="currAccountKey" />
						<c:if test="${searchDefectForm.deleteSuccess}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.delete.success"
									arguments="${searchDefectForm.deletedName}" />
							</div>
						</c:if>
						<input type="hidden" name="deleteSuccess" value="false" />
						<div class="box-body">
							<div
								class="form-group <spring:bind path="key"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<div
									class="<spring:bind path="locationKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-lg-2 control-label"><spring:message
											code="defect.location" /></label>
									<div class="col-lg-10">
										<div class="panel panel-default">
											<div class="panel-heading">
												<label class="control-label"> <spring:message
														code="defect.location.select" />
												</label> <label id="locationName" class="control-label"
													style="word-wrap: break-word;"><c:out
														value="${selectedLocation}" /></label>
											</div>
<script>
	function onLocationChange(inputKey, inputCode, inputName)
	{
		event.stopPropagation();
		var locationName = ' : ' + inputCode + ' - ' + inputName;

		$('#locationName').text(locationName);

		$
				.getJSON(
						'getEquipmentByLocation.do',
						{
							locationKey : inputKey,
							ajax : 'true'
						},
						function(data) {
							var html = '<option value=""><spring:message
								code="common.pleaseSelect" /></option>';
							var len = data.length;
							for (var i = 0; i < len; i++) {
								html += '<option value="' + data[i].key + '">'
										+ data[i].code + ' - ' + data[i].name + '</option>';
							}
							html += '</option>';
							$('#equipment').html(html);

						});

	}
</script>
											<ul class="list-group locationtree" cssClass="form-control"
												id="radioButtonLocationtree">
												<template:radioButtonLocationTree
													node="${searchDefectForm.availableLocationTree}" />
											</ul>
											<script>
											$("#locationKey1")[0].click();
											</script>
										</div>
									</div>
								</div>
								<label style="white-space: nowrap;"
									class="col-lg-2 control-label"><spring:message
										code="defect.code" /></label>
								<div class="col-lg-10">
									<spring:message code="defect.code" var="defectCode" />
									<form:input path="code" cssClass="form-control"
										placeholder="${defectCode}" />
								</div>
								<label class="col-lg-2 control-label"><spring:message
										code="defect.description" /></label>
								<div class="col-lg-10">
									<spring:message code="defect.description" var="desc" />
									<form:input path="description" cssClass="form-control"
										placeholder="${desc}" />
								</div>
								<label class="col-lg-2 control-label"><spring:message
										code="defect.failureClass" /></label>
								<div class="col-lg-10">
									<form:select path="failureClassKey" cssClass="form-control"
										id="failureClass" items="${failureClassList}">
									</form:select>
								</div>
								<label class="col-lg-2 control-label"><spring:message
										code="defect.problemCode" /></label>
								<div class="col-lg-10">
									<form:select path="problemCodeKey" cssClass="form-control"
										id="problemCode" items="${problemCodeList}">
									</form:select>
								</div>
								<label class="col-lg-2 control-label"><spring:message
										code="defect.causeCode" /></label>
								<div class="col-lg-10">
									<form:select path="causeCodeKey" cssClass="form-control"
										items="${causeCodeList}">
									</form:select>
								</div>
								<label class="col-lg-2 control-label"><spring:message
										code="defect.priority.1" /></label>
								<div class="col-lg-4">
									<form:select path="priority" cssClass="form-control"
										items="${priorityList}">
									</form:select>
								</div>
								<label class="col-lg-2 control-label"><spring:message
										code="defect.status" /></label>
								<div class="col-lg-4">
									<form:select path="status" cssClass="form-control"
										items="${statusList}">
									</form:select>
								</div>


								<label style="white-space: nowrap;"
									class="col-lg-2 control-label"><spring:message
										code="defect.assignedGroup" /></label>
								<div class="col-lg-4">
									<form:select id="accountGroup" path="groupKey"
										cssClass="form-control" items="${accountGroupList}">
									</form:select>
								</div>
								<label style="white-space: nowrap;"
									class="col-lg-2 control-label"><spring:message
										code="defect.assignedAccount" /></label>
								<div class="col-lg-4">
									<form:select id="account" path="accountKey"
										cssClass="form-control" items="${accountList}">
									</form:select>
								</div>



							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-xs-offset-8 col-lg-offset-10 col-lg-1">
									<button type="button" class="btn btn-primary " id="searchBtn" onclick="warnModal();">
										<spring:message code="button.search" />
									</button>
								</div>
								<div class="col-xs-2 col-lg-1">
									<a href="SearchDefect.do" class="btn btn-primary "
										onclick="showLoading()"><spring:message
											code="button.reset" /></a>
								</div>
							</div>
							<!-- Modal -->
							<div class="modal fade" id="warnModal" tabindex="-1" role="dialog"
								aria-labelledby="warnModalLabel">
								<div class="modal-dialog" role="document">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
											<h4 class="modal-title" id="warnModalLabel"><spring:message code="defect.search.warn.modalTitle"/></h4>
										</div>
										<div class="modal-body" id="warnModalBody"></div>
										<div class="modal-footer"> 
											<button type="button" class="btn btn-primary" style="margin-bottom: 0px;"
												data-dismiss="modal"><spring:message code="button.No"/></button>
											<button type="submit" class="btn btn-primary" id="continue" onclick="showLoading()"><spring:message code="button.Yes"/></button>
										</div>
									</div>
								</div>
							</div>


						</div>
						<!-- /.box-body -->
						<form:hidden path="fullListSize" />
						<form:hidden path="sortColIndex" />
						<form:hidden path="sortDirection" />
					</form:form>
				</div>
				<!-- /.box -->
			</div>
			<!-- /.row -->
		</div>

		<div class="row  pull-right">
			<div class="col-xs-12 ">
				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.searchDefect')"
					var="viewDefect" />

				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.modifyDefect')"
					var="modifyDefect" />

				<spring:eval
					expression="@privilegeMap.getProperty('privilege.code.removeDefect')"
					var="removeDefect" />

				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(viewDefect)}">
							&nbsp; <i class="fa fa-search"></i>
					<spring:message code="search.view" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(modifyDefect)}">
							&nbsp; <i class="fa fa-pencil"></i>
					<spring:message code="search.modify" />
				</c:if>
				<c:if
					test="${currRole.grantedPrivilegeCodeList.contains(removeDefect)}">
							&nbsp; <i class="fa fa-trash-o"></i>
					<spring:message code="search.remove" />
				</c:if>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-body">
						<c:if test="${searchDefectForm.fullListSize>0}">
							<form:form commandName="hidden_form" cssClass="form-horizontal">
								<button class="btn btn-primary" id="btn_export"
									onclick="exportToExcel()">
									<spring:message code="button.exportExcel" />
								</button>
								<input id="hidden_submit" type="submit"
									style="visibility: hidden;">
							</form:form>
						</c:if>
						<c:if test="${searchDefectForm.fullListSize>0}">
						<div><spring:message code="defect.totalNumber" /><span id="totalNumber"></span></div>
						</c:if>
						<table id="example1" class="table table-bordered table-striped"
							style="table-layout: fixed; width: 100%; max-width: 100%;">
							
							<thead>						
								<tr>
									<th width="10%"></th>
									<th width="10%" style="word-wrap: break-word;"><spring:message code="defect.code" /></th>
									<th width="15%" style="word-wrap: break-word;"><spring:message code="defect.location" /></th>
									<th width="30%" style="word-wrap: break-word;"><spring:message code="defect.description" /></th>
									<th width="15%" style="word-wrap: break-word;"><spring:message code="defect.assignedAccount" /></th>
									<th width="10%" style="word-wrap: break-word;"><spring:message code="defect.priority" /></th>
									<th width="10%" style="word-wrap: break-word;"><spring:message code="defect.status" /></th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>

	</section>
	<!-- /.content -->
</div>
<!-- Confirm Modal -->
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
				<a class="btn btn-danger btn-ok" id="confirmButton"
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
		$('#confirmButton').attr('href', 'DoDeleteDefect.do?key=' + key);
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

		"sAjaxSource" : "DoDefectDataTable.do",
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
	
	//override the original function para
	$( document ).ready(function() {
		//set the minimun width
		var MW = 980;
		
		$("#example1").css("min-width", MW+"px");
		 var sw = $("#example1_wrapper").width();
		scrollDiv(sw-30, MW);
		
		$( window ).resize(function() {			
			$("#example1").css("min-width", MW+"px");
			 sw2 = $("#scrollDiv").width();
			 scrollDiv(sw2, MW);			
			});
	});
	

	menu_toggle("sub_dm");
	menu_toggle("sub_dm_defect");
	menu_select("searchDefect");
</script>

<script>
	function warnModal() {
		var json = $("#searchDefectForm").serialize();
		
		$.ajax({
			method : "POST",
			url : "WarnNumber.do",
			data: json,
			//dataType:'json',
			success : function(data) {
				
				if(data == "success"){
					$('#continue').click();
					
				}else{
					$('#warnModal').modal('show');
					$('#warnModalBody').text(data);
					console.log(data);

				}
				
				//hideLoading();
				
			}
		});
	}

	
	//function totalNumber(){
//	 var json = $("#searchDefectForm").serialize();
//	 console.log(json);
//		$.ajax({
//			method : "POST",
//			url : "totalNumber.do",
//			data: json,
			//dataType:'json',
//			success : function(data) {
//				$("#totalNumber").show();
//				if(data == "success"){
//					$('#totalNumber').text("");
//				}else{
//					$('#totalNumber').text(data);
//				}	
//			}
//		});
//	}
	
	

console.log(${searchDefectForm.fullListSize})
var fullListSize = ${searchDefectForm.fullListSize};
if(fullListSize>0){
	$('#totalNumber').text(fullListSize);
}
	
</script>
