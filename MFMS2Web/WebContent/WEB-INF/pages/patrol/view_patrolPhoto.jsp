<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="import/datetimepicker/moment.js"></script>
<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">

<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/lodash.js/0.10.0/lodash.min.js"></script>
<script>
	$(document)
			.ready(
					function() {
						$("#view_photo_date")
								.datetimepicker({
									bootcssVer : 3,
									format : 'yyyy-mm-dd',
									minView : 2,
									autoclose : true,
									todayBtn : true,
								})
								.on("show", function() {

								})
								.on("changeDate",
										function(ev) {

											showCustomLoading();

											var routeKey = document.getElementById('hidden_routeDefKey').value;
											var date = document.getElementById('view_photo_date').value;
											
											$("#viewPatrolPhoto").load(
													"ShowPatrolPhoto.do?routeDefKey="
															+ routeKey
															+ "&date=" + date,
															
													function() {
														hideCustomLoading();
													});
											
											//window.location.href = "PatrolPhoto.do?routeKey=" + routeKey + "&date=" + date;
										});

						$('.edit').click(function() {
							$(this).hide();
							$(this).siblings('.save, .cancel').show();
						});
						$('.cancel').click(function() {
							$(this).siblings('.edit').show();
							$(this).siblings('.save').hide();
							$(this).hide();
						});
						$('.save').click(function() {
							$(this).siblings('.edit').show();
							$(this).siblings('.cancel').hide();
							$(this).hide();
						});

					});

	function divClicked(index) {
		var remark = $("#edit_"+index).parent().parent().find("div")[1];
		var divHtml = $(remark).html();
		var idx = "input_" + index;
		var editableText = $('<input class="col-xs-4"/>').attr("id", idx);
		editableText.val(divHtml);
		$(remark).replaceWith(editableText);
		editableText.focus();
		// setup the blur event for this new textarea
		//editableText.blur(editableTextBlurred);
		
		$(".carousel-control, #CI").hide();
		$('.carousel').carousel('pause');
	}

	function editableTextBlurred(index) {
		//var html = $(this).val();
		var viewableText = $('<div class="col-xs-4">');
		viewableText.html(html);
		$("#input_"+index).replaceWith(viewableText);
		// setup the click event for this new div
		//viewableText.click(divClicked);
	}
	
	function UpdateRemark(index){
		//var remarkContent = $($("#edit_"+index).parent().parent().find("input")[1]).html();
		var remarkContent = $("#input_"+index).val();
		var photoKey = $("#photoKey_"+index).val();
		$.ajax({
			method : "POST",
			url : "UpdateRemark.do",
			data: {remark : remarkContent, key : photoKey},
			success : function(data) {
				if(data=="success"){
				//$($("#edit_"+index).parent().parent().find("div")[1]).html(remarkContent);
				var viewableText = $('<div class="col-xs-4">');
				viewableText.html(remarkContent);
				$("#input_"+index).replaceWith(viewableText);	
				$(".carousel-control, #CI").show();
				
				}
				else{
					console.log(data);
					//$($("#edit_"+index).parent().parent().find("div")[1]).html(data);
					var viewableText = $('<div class="col-xs-4">');
					viewableText.html(data);
					$("#input_"+index).replaceWith(viewableText);
					$(".carousel-control, #CI").show();
					
				}
				}	
				//hideLoading();

		});
	}
	
	function PreviousRemark(index){
		var photoKey = $("#photoKey_"+index).val();
		$.ajax({
			method : "POST",
			url : "PreviousRemark.do",
			data: {key : photoKey},
			success : function(data) {
				
				//$($("#edit_"+index).parent().parent().find("div")[1]).html(data);
				var viewableText = $('<div class="col-xs-4">');
				viewableText.html(data);
				$("#input_"+index).replaceWith(viewableText);
				$(".carousel-control, #CI").show();
				
				}	
				//hideLoading();

		});
	}

</script>

<div class="box-body" style="padding-bottom: 50px">
	<div class="row">
		<div class="col-xs-12">
			<form class="form-inline col-xs-offset-1" style="float: right;">
				<div class="form-group has-feedback">
					<label class="control-label" for="inputSuccess4"><spring:message
							code="patrol.photo.date" /> : </label> <input id="view_photo_date"
						class="form-control" type="text" value="${selectedDate}" /> <span
						class="icon-icon_maintenance_schedule_b form-control-feedback"
						aria-hidden="true" style="margin-top: 10px"></span>
				</div>
			</form>
		</div>
	</div>

	<div id="carousel-example-generic" class="carousel slide" data-interval="false">

		<ol class="carousel-indicators" id="CI" style="bottom: -42px">
			<c:forEach var="patroPhotohotoList" items="${patrolPhotoList}"
				varStatus="status">
				<li data-target="#carousel-example-generic"
					data-slide-to="${status.index}"
					class="photo-indication <c:if test="${status.index==0}">active</c:if>"></li>
			</c:forEach>
		</ol>

		<!-- Wrapper for slides -->
		<div class="carousel-inner" role="listbox">
			<c:forEach var="patroPhotohotoList" items="${patrolPhotoList}"
				varStatus="status">
				<div class="item <c:if test="${status.index==0}">active</c:if>">
					
					<img class="img-responsive center-block"
						src="${patroPhotohotoList.photoPath}"
						style="max-height: 400px; width: auto; height: auto; min-height: 400 px;">
					
					<div class="">
						<div class="row">

						<input type="hidden" id="photoKey_${status.index}" value="${patroPhotohotoList.photoKey}"/>
						
							<div class="col-xs-offset-1 col-xs-2">
								<label><spring:message code="patrol.photo.locationTaken" />
									: </label>
							</div>
							<div id="pp_loc_${status.index}" class="col-xs-4">${patroPhotohotoList.locationName}</div>
						</div>
						<div class="row">
							<div class="col-xs-offset-1 col-xs-2">
								<label><spring:message code="patrol.photo.timeTaken" />
									: </label>
							</div>
							<div id="pp_tt_${status.index}" class="col-xs-4">${patroPhotohotoList.takenTime}</div>
						</div>
						<div class="row">
							<div class="col-xs-offset-1 col-xs-2">
								<label><spring:message code="patrol.photo.takenBy" /> :
								</label>
							</div>
							<div id="pp_ac_${status.index}" class="col-xs-4">${patroPhotohotoList.accountName}</div>
						</div>
						<div class="row">
							<div class="col-xs-offset-1 col-xs-2">
								<label><spring:message code="patrol.photo.remark" /> :
								</label>
							</div>
						
							<div id="pp_rmk_${status.index}" class="col-xs-4">${patroPhotohotoList.remark}</div>
							<c:if test="${patroPhotohotoList.takenTime != null}">
							<div class="row">
								<button class="btn btn-primary edit" type="button" value="Edit"
									id="edit_${status.index}" onclick="divClicked(${status.index})">Edit</button>
								<button class="btn btn-primary save" type="button" value="Save"
									id="save_${status.index}" style="display: none" onclick="UpdateRemark(${status.index})">Save</button>
								<button class="btn btn-primary cancel" type="button"
									value="Cancel" id="cancel_${status.index}" style="display: none" onclick="PreviousRemark(${status.index});">Cancel</button>
							
							</div>
							</c:if>
						</div>
					</div>
					
				</div>
				<script>
					$('#pp_loc_${status.index}').text(_.unescape(document.getElementById('pp_loc_${status.index}').innerHTML));
					$('#pp_tt_${status.index}').text(_.unescape(document.getElementById('pp_tt_${status.index}').innerHTML));
					$('#pp_ac_${status.index}').text(_.unescape(document.getElementById('pp_ac_${status.index}').innerHTML));
					$('#pp_rmk_${status.index}').text(_.unescape(document.getElementById('pp_rmk_${status.index}').innerHTML));
				</script>
			</c:forEach>
			<div id="all" style="display:none">
			<c:out value="${fn:length(patrolPhotoList)}"/>
			</div>

			<!-- Controls -->
			<a class="left carousel-control" href="#carousel-example-generic"
				role="button" data-slide="prev"> <span
				class="glyphicon glyphicon-chevron-left" aria-hidden="true"
				style="color: #0D5185;"> </span> <span class="sr-only">Previous</span>
			</a> <a class="right carousel-control" href="#carousel-example-generic"
				role="button" data-slide="next"> <span
				class="glyphicon glyphicon-chevron-right" aria-hidden="true"
				style="color: #0D5185;"></span> <span class="sr-only">Next</span>
			</a>
		</div>
	</div>
	
	<script>
	
	</script>