<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script src="import/underscorejs/underscore-min.1.8.3.js"></script>

<script type="text/javascript">
	var selected_RouteKeys=[];
	selectedRouteJson = '${selectedRouteJson}';
	$(document).ready(function() {
		var element = document.getElementById("container_selectedRoute");
		element.scrollTop = element.scrollHeight - element.clientHeight;
	})
	
	
	function dropSelectedRoute(routeDefKey){
		
		$.ajax({
			method : "POST",
			data : {routeDefKey : routeDefKey , selectedRouteJson : selectedRouteJson},
			url : "DropSelectedRoute.do",
			success : function(data) {
				$('#view_selectedRoute').html(data);
				
			}
		});
		$("#route_searched_name_" + routeDefKey).parent().show();
	}
	
</script>
<div class="panel-heading">
	<label class="patrol-goback"><spring:message code="common.selected"/></label>
</div>
<div class="patrol-childLocation" id="container_selectedRoute">
	<div class="list-group patrol-list-group">
		<c:forEach var="selectedRoute" items="${selectedRoute}"
			varStatus="status">

			<div class="list-group-item patrol-childlist-li">
				<div class="childList-Name" id="tbl_selectedRoute_${selectedRoute.routeDefKey}">${selectedRoute.code} - ${selectedRoute.name}</div>
				
				<script>
			
				//document.getElementById("tbl_selectedRoute_${selectedRoute.routeDefKey}").innerHTML = _.escape(document.getElementById("tbl_selectedRoute_${selectedRoute.routeDefKey}").innerHTML);
				
				$("#tbl_selectedRoute_${selectedRoute.routeDefKey}").html($("#tbl_selectedRoute_${selectedRoute.routeDefKey}").text());
				selected_RouteKeys.push(${selectedRoute.routeDefKey});
				</script>
				
				<div class="patrol-btn-dropLocation"
					onclick="dropSelectedRoute(${selectedRoute.routeDefKey})">
					<span class="glyphicon glyphicon-remove-sign"></span>
				</div>
			</div>
		</c:forEach>
	</div>
</div>

<script>

function dropAllSelectedRoute(){
	$.ajax({
		method : "POST",
		data : {"RouteKeys" : RouteKeys , "selectedRouteJson" : selectedRouteJson},
		traditional: true,
		url : "DropAllSelectedRoute.do",
		success : function(data) {
			
			$('#view_selectedRoute').html(data);
		},
		error: function(data){
			console.log(data);
		}
	});
	for(i=0;i<selected_RouteKeys.length;i++){
		$("#route_searched_name_" + selected_RouteKeys[i]).parent().show();
	}
}

var assignedRouteKey = [];
for(i=0;i<$("#route option").size();i++){
	assignedRouteKey.push($("#route option")[i].value);
}

	
function addspecifiedSelectedRoute(){
	$.ajax({
		method : "POST",
		data : {"RouteKeys" : assignedRouteKey , "selectedRouteJson" : selectedRouteJson},
		traditional: true,
		url : "AddAllSelectedRoute.do",
		success : function(data) {
			
			$('#view_selectedRoute').html(data);
		},
		error: function(data){
			console.log(data);
		}
	});
	for(i=0;i<assignedRouteKey.length;i++){
	$("#route_searched_name_" + assignedRouteKey[i]).parent().hide();
	}
}
function RouteBack2Previous(){
	dropAllSelectedRoute();
	dropAllSelectedLocation();
	
	if(assignedRouteKey.length>0)
	addspecifiedSelectedRoute();
}

</script>