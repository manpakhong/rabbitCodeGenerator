<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<style>
.hiddenRow {
	padding: 0 !important;
}
</style>
<script>

expireTimeJson = ${expireTimeJson};


$(document).ready(function(){
//	$('.collapse').on('show.bs.collapse', function() {
//		$('.collapse.in').collapse('hide');
//	});
	
	//console.log(expireTimeJson);

	
	for(i=0; i <expireTimeJson.length; i++){
		setTimeoutTimer(expireTimeJson[i].routeDefKey,expireTimeJson[i].patrolResultKey , expireTimeJson[i].expTime, expireTimeJson[i].accountKey);
	}
	
});

	
	
	function showPatrolResultDetail(routeDefKey){
		console.log(lastLoadPatrolResult +"||"+routeDefKey);
		
		$(".pr_expand").css({'transform' : 'rotate(0deg)'});
		
		if(lastLoadPatrolResult == ""){
			
			$("#patrolResultDetail").html();
			showCustomLoading();
			$("#patrolResultDetail").load("GetPatrolResultDetail.do?routeDefKey="+routeDefKey, function(){
				$("#resultDetails").collapse("show");
				$("#pr_expand_"+routeDefKey).css({'transform' : 'rotate(90deg)'});
				hideCustomLoading();
			});
			
		}else{
		
			if(lastLoadPatrolResult != routeDefKey){
				showCustomLoading();
				
				
				if($("#resultDetails").hasClass("in")){
					$("#resultDetails").collapse("hide");
					$("#resultDetails").on("hidden.bs.collapse",function(){
				
						$("#patrolResultDetail").load("GetPatrolResultDetail.do?routeDefKey="+routeDefKey, function(){
							$("#resultDetails").collapse("show");
							$("#pr_expand_"+routeDefKey).css({'transform' : 'rotate(90deg)'});
							$("#resultDetails").off("hidden.bs.collapse");
							hideCustomLoading();
						});
					});
				}else{
					$("#patrolResultDetail").load("GetPatrolResultDetail.do?routeDefKey="+routeDefKey, function(){
						$("#resultDetails").collapse("show");
						$("#pr_expand_"+routeDefKey).css({'transform' : 'rotate(90deg)'});
						hideCustomLoading();
					});
				}
			}else{
				if($("#resultDetails").hasClass("in")){
					$("#resultDetails").collapse("hide");
				}else{
					$("#resultDetails").collapse("show");
					$("#pr_expand_"+routeDefKey).css({'transform' : 'rotate(90deg)'});
				}
			}
		
		
		}
		lastLoadPatrolResult = routeDefKey;
	}
			
			
</script>

<h3 class="modal-title">
	<spring:message code="patrol.monitor.section.progress" />
</h3>
<div class="box-footer">
	<table id="table_currentResult"
		class="table table-striped table-hover table-responsive">
		<thead>
			<tr>
				<th></th>
				<th><spring:message code="patrol.schedule.route" /></th>
				<th><spring:message code="patrol.monitor.order" /></th>
				<th><spring:message code="patrol.monitor.currentLocation" /></th>
				<th><spring:message code="patrol.monitor.progress" /></th>
				<th><spring:message code="patrol.monitor.attendTime" /></th>
				<th><spring:message code="patrol.monitor.account" /></th>
				<!--  <th></th> -->
			</tr>
		</thead>
		<tbody id="table_currentResult_body">

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
				</script>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<div class="collapse" id="resultDetails">
	<div class="well" id="patrolResultDetail"></div>
</div>
