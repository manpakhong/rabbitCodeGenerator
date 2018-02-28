<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script>

function openSelectLocationModal(){
	$("#selectLocationModal").modal("show");
}

</script>


<label class="col-lg-2 col-md-2 col-sm-2 col-xs-2 control-label" id="sl" style="border: none;">
<spring:message code="patrol.route.start.location"/>
&nbsp;
<a href="#" onclick="openSelectLocationModal()"><span
		class="glyphicon glyphicon-map-marker"></span></a>
</label>

<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6"
	style="display: inline-flex;" id="block">
	<form:select path="startingLocation" cssClass="form-control" multiple="true" id="select_startingLocation">
		<form:options items="${startingLocation}" />
	</form:select>
</div>
		