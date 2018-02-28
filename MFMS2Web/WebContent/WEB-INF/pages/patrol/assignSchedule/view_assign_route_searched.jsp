<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<script src="import/underscorejs/underscore-min.1.8.3.js"></script>

<script type="text/javascript">
var RouteKeys=[];
function addSelectedRoute(routeDefKey){
	$.ajax({
		method : "POST",
		data : {routeDefKey :routeDefKey , selectedRouteJson:selectedRouteJson},
		url : "AddSelectedRoute.do",
		//dataType:'html',
		success : function(data) {
			
			$('#view_selectedRoute').html(data);
		},
		error: function(data){
			console.log(data);
		}
	});
	$("#route_searched_name_" + routeDefKey).parent().hide();
}


</script>

<div class="panel-heading">
	<label class="patrol-goback"><spring:message code="patrol.schedule.route"/></label>
</div>
<div class="patrol-childLocation">
	<div class="list-group patrol-list-group">
		<c:forEach var="routeSearchTbl" items="${searchedRoute}" varStatus="status">

			<div class="list-group-item patrol-childlist-li">
				<div class="childList-Name" style="cursor:default;" id="route_searched_name_${routeSearchTbl.routeDefKey}">${routeSearchTbl.code} - ${routeSearchTbl.name}</div>
				<script>
				
				//document.getElementById("route_searched_name_${routeSearchTbl.routeDefKey}").innerHTML = _.escape(document.getElementById("route_searched_name_${routeSearchTbl.routeDefKey}").innerHTML);
				
				
				$("#route_searched_name_${routeSearchTbl.routeDefKey}").html($("#route_searched_name_${routeSearchTbl.routeDefKey}").text());
				RouteKeys.push(${routeSearchTbl.routeDefKey});
				</script>
				
				<div class="patrol-btn-addLocation"
					onclick="addSelectedRoute(${routeSearchTbl.routeDefKey})">
					<span class="glyphicon glyphicon-plus-sign"></span>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
<script>
function addAllSelectedRoute(){
	$.ajax({
		method : "POST",
		data : {"RouteKeys" : RouteKeys , "selectedRouteJson" : selectedRouteJson},
		traditional: true,
		url : "AddAllSelectedRoute.do",
		success : function(data) {
			
			$('#view_selectedRoute').html(data);
		},
		error: function(data){
			console.log(data);
		}
	});
	for(i=0;i<RouteKeys.length;i++){
	$("#route_searched_name_" + RouteKeys[i]).parent().hide();
	}
}



</script>