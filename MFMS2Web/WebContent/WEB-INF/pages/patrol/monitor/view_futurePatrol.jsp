
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<script>
futureTimeJson = ${futureTimeJson};


$(document).ready(function(){

	//console.log(futureTimeJson);
	
	
	for(i=0; i <futureTimeJson.length; i++){
		setFutureTimeout(futureTimeJson[i].scheduleKey, futureTimeJson[i].expTime);
	}
	
});

</script>

<div class="">
<span class="glyphicon glyphicon-menu-down" aria-hidden="true"
								style="cursor: pointer; transition-duration: 0.5s; display:inline-block"
								data-toggle="collapse" data-target="#collapseDetail"
								aria-expanded="false" aria-controls="collapseDetail"
								
								id="collapseDetailBtn"></span>
							<script>
								$("#collapseDetailBtn").click(function() {
									if ($("#collapseDetailBtn").hasClass("collapsed")) {
										$("#collapseDetailBtn").css({'transform' : 'rotate(0deg)'});
									} else {
										$("#collapseDetailBtn").css({'transform' : 'rotate(-90deg)'});
									}

								});
							</script>
<h3 class="modal-title" style="display:inline-block">
  <spring:message code="patrol.monitor.section.route"/>
</h3>
</div>
<div class="box-footer collapse in" id="collapseDetail">
<table class="table table-striped table-hover table-responsive">
  <thead>
	<tr>
		<th><spring:message code="patrol.monitor.route"/></th>
		<th><spring:message code="patrol.monitor.startTime"/></th>
		<th style="display:none"><spring:message code="patrol.monitor.account"/></th>
		<th><spring:message code="patrol.monitor.status"/></th>
		<th></th>
	</tr>
  </thead>
  <tbody>
	<c:forEach var="futurePatrol" items="${futurePatrol}"
		varStatus="status">
		<tr id="schedule_${futurePatrol.scheduleKey}" class="<c:if test="${futurePatrol.scheduleStatus eq 'InProgress'}">monitor-progress</c:if><c:if test="${futurePatrol.scheduleStatus eq 'Finished'}">monitor-finish</c:if>">
			<td>${futurePatrol.routeCode} - ${futurePatrol.routeName}</td>
			<td>${futurePatrol.attendTime}</td>
			<td style="display: none;" id="schedule_ac_${futurePatrol.scheduleKey}" >${futurePatrol.accountName}</td>
			<td id="schedule_status_${futurePatrol.scheduleKey}">${futurePatrol.scheduleStatus}</td>
		</tr>
		
		<script>
		$('#schedule_ac_${futurePatrol.scheduleKey}')
				.text(_.unescape(document
										.getElementById('schedule_ac_${futurePatrol.scheduleKey}').innerHTML));
		
		var status = $('#schedule_status_${futurePatrol.scheduleKey}').text();
		console.log(status);
		switch(status){
		case "InProgress":              
			$('#schedule_status_${futurePatrol.scheduleKey}').text('<spring:message code="patrol.monitor.inProgress"/>');
			
			break;
		case "Finished":
			$('#schedule_status_${futurePatrol.scheduleKey}').text('<spring:message code="patrol.monitor.finished"/>');	
			
			break;
		case "Skipped":
			$('#schedule_status_${futurePatrol.scheduleKey}').text('<spring:message code="patrol.monitor.result.skipped"/>');	
			
			break;
		case "Overtime":
			$('#schedule_status_${futurePatrol.scheduleKey}').text('<spring:message code="patrol.monitor.overTime"/>');	
			break;
			default : 
				console.log("default");
			break;
		}

		</script>
	</c:forEach>
  </tbody>
</table>
</div>