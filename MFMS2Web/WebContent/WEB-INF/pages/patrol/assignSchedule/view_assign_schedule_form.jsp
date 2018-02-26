<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<section id="scheduler${schedulerCounter}">
	<script>
	
	schedulerCounter = '${schedulerCounter}';
	
	$(".allStartDate").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 2,
		startDate : '+0d',
		autoclose:true,
	}).on("show", function(){
		$(".table-condensed th").attr("style","visibility : visible");
		
	}).on("changeDate", function(ev){

		var fullDate = $("#startDatePicker"+ev.currentTarget.name).val()
		
		$("#endDatePicker"+ev.currentTarget.name).datetimepicker("setStartDate",fullDate);

		//if($("#endDatePicker"+ev.currentTarget.name).val() === ""){
		//	$("#endDatePicker"+ev.currentTarget.name).val(fullDate);
		//}
	});
	
	$(".allEndDate").datetimepicker({
		format : 'yyyy-mm-dd',
		minView : 2,
		startDate : '+0d',
		autoclose:true,
		todayBtn:false,
	}).on("show", function(ev){
		$(".table-condensed th").attr("style","visibility : visible");
	}).on("changeDate", function(ev){

		var fullDate = $("#endDatePicker"+ev.currentTarget.name).val()
		
		$("#startDatePicker"+ev.currentTarget.name).datetimepicker("setEndDate",fullDate);
	});;

	$(".allTimePicker").datetimepicker({
		format : 'hh:ii',
		startView : 1,
		maxView : 1,
		autoclose:true,
		keyboardNavigation : false
	}).on("show", function(){
		$(".table-condensed th").attr("style","visibility : hidden");
	});
	
	function displayWeekDay(id){
		var frequency = $("#frequency"+id).val();
		if(frequency === "${weeklyValue}"){
			 
			$("#weekDay"+id).show();

		}else{
			$("#weekDay"+id).hide();

		}
		if(frequency === "${onceValue}"){
			$("#endDatePicker"+id).val("");
			$("#endDatePicker"+id).prop('disabled', true);
		}else{
			$("#endDatePicker"+id).prop('disabled', false);
		}
		
	}
	function clearFormEndDate(counter){
		$("#endDatePicker"+counter).val("");
	}
</script>
<!--  
	<div class="col-xs-12">
		<hr>
	</div>
	<div class="col-xs-12">
		<div class="row form-group">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<spring:message code="patrol.schedule.date.start" var="startDate" />
				<input readonly type="text"
					class="form-control allStartDate dateTimePicker" value=""
					id="startDatePicker${schedulerCounter}" placeholder="${startDate}"
					name="${schedulerCounter}">
			</div>
		</div>
		<div class="row form-group">
			<div class="col-xs-12">
				<spring:message code="patrol.schedule.date.end" var="endDate" />
				<div style="display: flex;">
					<input readonly type="text"
						class="form-control allEndDate dateTimePicker" value=""
						id="endDatePicker${schedulerCounter}" placeholder="${endDate}"
						name="${schedulerCounter}"> <span
						class="glyphicon glyphicon-remove-sign patrol-btn-dropLocation"
						onclick="clearFormEndDate(${schedulerCounter});"
						style="float: left; margin: auto;"></span>
				</div>
			</div>
		</div>

	</div>

	<div class="col-xs-12">
		<div class="row form-group">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<spring:message code="patrol.schedule.time" var="time" />
				<input readonly
					class="form-control patrol_input allTimePicker dateTimePicker"
					placeholder="${time}" id="time${schedulerCounter}" />
			</div>
		</div>
	</div>

	<div class="col-xs-12">
		<div class="row form-group">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<form:select path="frequency" multiple="false"
					cssClass="form-control frequency" id="frequency${schedulerCounter}"
					onchange="displayWeekDay(${schedulerCounter})">
					<form:options items="${frequency}" />
				</form:select>

				<div class="row weekDayBox" id="weekDay${schedulerCounter}"
					style="display: none;" name="${schedulerCounter}">
					<div class="col-lg-12">
						<c:forEach items="${weekDay}" var="weekDay">
							<div class="form-group col-xs-12 ">
								<div class="checkbox">
									<label> <form:checkbox path="weekDay"
											value="${weekDay.key}" cssClass="weekDay${schedulerCounter}" />${weekDay.value}
									</label>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>

			</div>
		</div>
	</div>

	<div class="col-xs-offset-11 col-xs-1">

		<div class="patrol-btn-dropLocation" style="float: right"
			onclick="removeScheduleForm(${schedulerCounter})">
			<span class="glyphicon glyphicon-remove-sign"></span>
		</div>

	</div>

-->
	<div class="row">
		<div class="col-lg-4 col-md-4 col-sm-6 col-xs-6">
			<div class="row form-group">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<spring:message code="patrol.schedule.date.start" var="startDate" />
					<input readonly type="text"
						class="form-control allStartDate dateTimePicker" value=""
						id="startDatePicker${schedulerCounter}" placeholder="${startDate}"
						name="${schedulerCounter}">
				</div>
			</div>
			<div class="row form-group">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<spring:message code="patrol.schedule.date.end" var="endDate" />
					<div style="display: flex;">
						<input readonly type="text"
							class="form-control allEndDate dateTimePicker" value=""
							id="endDatePicker${schedulerCounter}" placeholder="${endDate}"
							name="${schedulerCounter}" disabled="true"> 
					</div>
				</div>
			</div>

		</div>

		<div class="col-lg-4 col-md-4 col-sm-6 col-xs-6">
			<div class="row form-group">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<spring:message code="patrol.schedule.time" var="time" />
					<input readonly
						class="form-control patrol_input allTimePicker dateTimePicker"
						placeholder="${time}" id="time${schedulerCounter}" />
				</div>
			</div>
		</div>

		<div class="col-lg-3 col-md-3 col-sm-6 col-xs-6">
			<div class="row form-group">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<form:select path="frequency" multiple="false"
						cssClass="form-control frequency"
						id="frequency${schedulerCounter}"
						onchange="displayWeekDay(${schedulerCounter})">
						<form:options items="${frequency}" />
					</form:select>

					<div class="row weekDayBox" id="weekDay${schedulerCounter}"
						style="display: none;" name="${schedulerCounter}">
						<div class="col-lg-12">
							<c:forEach items="${weekDay}" var="weekDay">
								<div class="form-group col-xs-12 ">
									<div class="checkbox">
										<label> <form:checkbox path="weekDay"
												value="${weekDay.key}" cssClass="weekDay${schedulerCounter}" />${weekDay.value}
										</label>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>

				</div>
			</div>
		</div>

		<div class="col-lg-1 col-md-1 col-sm-6 col-xs-6">

			<div class="patrol-btn-dropLocation" style="float: left"
				onclick="removeScheduleForm(${schedulerCounter})">
				<span class="glyphicon glyphicon-remove-sign"></span>
			</div>

		</div>
	</div>

</section>