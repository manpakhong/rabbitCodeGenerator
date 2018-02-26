<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:forEach var="currentPatrolResult" items="${currentPatrolResult}"
	varStatus="status">
	<tr id="routeDef_${currentPatrolResult.routeDefKey}"
		onclick="showPatrolResultDetail(${currentPatrolResult.routeDefKey})"
		class="<c:if test="${currentPatrolResult.status eq 'Normal'}">monitor-success</c:if>">
		<td><span id="pr_expand_${currentPatrolResult.routeDefKey}"
						class="glyphicon glyphicon-menu-right pr_expand"
						aria-hidden="true"
						style="cursor: pointer; transition-duration: 0.5s; display: inline-block"></span></td>
		<td id="pr_route_${currentPatrolResult.routeDefKey}">${currentPatrolResult.routeCode}-
			${currentPatrolResult.routeName}</td>
		<td id="pr_current_order_${currentPatrolResult.routeDefKey}">${currentPatrolResult.currentLocationSeq}</td>
		<td id="pr_loc_${currentPatrolResult.routeDefKey}">${currentPatrolResult.locationCode}-
			${currentPatrolResult.locationName}</td>
		<td>${currentPatrolResult.progress}</td>
		<td>${currentPatrolResult.attendTime}</td>
		<td id="pr_name_${currentPatrolResult.routeDefKey}"
			data-toggle="tooltip" data-trigger="hover" data-container="body"
			data-placement="bottom" data-html="true"
			title="Email : ${currentPatrolResult.accountEmail}<br/>
							Contact : ${currentPatrolResult.accountTel}">${currentPatrolResult.accountName}

		</td>
		<!-- 
		<td><span class="glyphicon glyphicon-menu-left"
			aria-hidden="true"></span></td>
		-->
		<script>
				
				$('#pr_route_${currentPatrolResult.routeDefKey}').text(_.unescape(document.getElementById('pr_route_${currentPatrolResult.routeDefKey}').innerHTML));
				$('#pr_loc_${currentPatrolResult.routeDefKey}').text(_.unescape(document.getElementById('pr_loc_${currentPatrolResult.routeDefKey}').innerHTML));
				$('#pr_name_${currentPatrolResult.routeDefKey}').text(_.unescape(document.getElementById('pr_name_${currentPatrolResult.routeDefKey}').innerHTML));
		
				if(lastLoadPatrolResult == ${currentPatrolResult.routeDefKey} && $("#resultDetails").hasClass("in")){
					
					$("#pr_expand_"+${currentPatrolResult.routeDefKey}).css({'transform' : 'rotate(90deg)'});
				}
				</script>

	</tr>
</c:forEach>

<script>
expireTimeJson = ${expireTimeJson};

$(document).ready(function(){
	//console.log(expireTimeJson);
	
	for(i=0; i <expireTimeJson.length; i++){
		setTimeoutTimer(expireTimeJson[i].routeDefKey,expireTimeJson[i].patrolResultKey , expireTimeJson[i].expTime,expireTimeJson[i].accountKey);
	}
	
	
});

</script>