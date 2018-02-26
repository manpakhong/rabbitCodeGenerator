<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<script src="import/datetimepicker/moment.js"></script>
<script src="import/datetimepicker/moment-timezone-with-data.js"></script>
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>

<script>
	$(".allStartDate").datetimepicker({
		format : 'yyyy-mm-dd hh:ii',
		minView : 0,
		endDate : '+0d',
		autoclose : true,
		todayBtn : true,
		clearBtn:true
	}).on("show", function() {
		$(".table-condensed th").attr("style", "visibility : visible");

	}).on(
			"changeDate",
			function(ev) {

				var fullDate = $("#startDatePicker" + ev.currentTarget.name)
						.val()

				$("#endDatePicker" + ev.currentTarget.name).datetimepicker(
						"setStartDate", fullDate);

				if ($("#endDatePicker" + ev.currentTarget.name).val() === "") {
					$("#endDatePicker" + ev.currentTarget.name).val(fullDate);
				}
			});

	$(".allEndDate").datetimepicker({
		format : 'yyyy-mm-dd hh:ii',
		minView : 0,
		endDate : '+0d',
		autoclose : true,
		todayBtn : true,
		clearBtn:true
	}).on("show", function(ev) {
		$(".table-condensed th").attr("style", "visibility : visible");
		//$(".table-condensed tfoot th").attr("style", "visibility : hidden");
	}).on(
			"changeDate",
			function(ev) {

				var fullDate = $("#endDatePicker" + ev.currentTarget.name)
						.val()

				$("#startDatePicker" + ev.currentTarget.name).datetimepicker(
						"setEndDate", fullDate);
			});
	var now = moment().format("YYYY-MM-DD HH:mm");
	resetDate();
	
	$(".allStartDate").datetimepicker(
			"setEndDate", now);
	$(".allEndDate").datetimepicker(
			"setEndDate", now);
	
	function resetDate(){
		var nowDate = moment().format("YYYY-MM-DD");

		$("#startDatePicker").val(nowDate+" 00:00");
		$("#endDatePicker").val(nowDate+" 23:59");

		$(".allStartDate").datetimepicker(
				"setEndDate", now);
		$(".allEndDate").datetimepicker(
				"setEndDate", now);
		$(".allEndDate").datetimepicker(
				"setStartDate", "1970-01-01 00:00");
	}
	
</script>


<div class="form-group">

	<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
		<label class="control-label form-control"
			style="border: none; white-space: nowrap;"><spring:message
				code="patrol.report.date.start" /></label>
	</div>

	<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
		<spring:message code="patrol.report.date.start" var="startDate" />
		<input readonly type="text"
			class="form-control allStartDate dateTimePicker" value=""
			id="startDatePicker" placeholder="${startDate}" name="">
	</div>
</div>
<div class="form-group">

	<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
		<label class="control-label form-control"
			style="border: none; white-space: nowrap;"><spring:message
				code="patrol.report.date.end" /></label>
	</div>

	<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
		<spring:message code="patrol.report.date.end" var="endDate" />
		<input readonly type="text"
			class="form-control allEndDate dateTimePicker" value=""
			id="endDatePicker" placeholder="${endDate}" name="">
	</div>
</div>