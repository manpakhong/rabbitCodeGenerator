<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script type="text/javascript">

function openSelectRouteModal(){
	$('#selectRouteModal').modal('show'); 
	slideUpThePrompt();
}

</script>

<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 ">
	<label class="control-label"
		style="border: none; white-space: nowrap;"><spring:message code="patrol.schedule.route"/>
	<a class="btn" onclick="openSelectRouteModal();"><span
		class="glyphicon glyphicon-road"></span></a>
	</label>
</div>

<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8"
	style="display: inline-flex;">
	<form:select path="route" cssClass="form-control" multiple="true" id="route">
		<form:options items="${route}" />
	</form:select>
</div>