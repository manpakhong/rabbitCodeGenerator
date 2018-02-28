<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script src="import/jQueryUI/jquery-ui.js"></script>
<script src="import/underscorejs/underscore-min.1.8.3.js"></script>

<script>
var selectedLocationKey=[];
locationJson = '${locationJson}';
oldLocationJson = '${oldLocationJson}';

$(document).ready(function(){
	
	changeUnitTag();
	

	if(selectedLocaionEmpty == "has-error-cm"){
		$("#selected_title").addClass("has-error-cm");
	}
	
	
	var element = document.getElementById("selectedLocation_scroll");
	   element.scrollTop = element.scrollHeight - element.clientHeight;
	   
	   $( "#sortable_selected_location" ).sortable();
	   $( "#sortable_selected_location" ).disableSelection();
	   
	   var startIndex = -1;
	   $( "#sortable_selected_location" ).sortable({
		   start: function( event, ui ) {
			   startIndex = ui.item.index();
		   },
		   
		   update: function( event, ui ) {
			   $.ajax({
					method : "POST",
					url : "RearrangeLocation.do",
					data : "orginalIndex=" + startIndex +"&newIndex="+ui.item.index() + "&locationJson="+encodeURIComponent(locationJson),
					success : function(data) {

						$('#selectedLocation').html(data);

					}

				});
			   
		   }
		 });
	   if(mode ==="search"){
		   $('.dur_tag').hide();
			$('.dur_unitTag').hide();
			$("#sortable_selected_location").sortable("disable");
			$("#sortable_selected_location").enableSelection();
			$('.minute_tag').hide();
	   }
	   if(mode==="view"){
		   $('.maxValue').attr('readonly', true);
			$('.minValue').attr('readonly', true);
			$('.patrol-btn-dropLocation').hide();
			$("#sortable_selected_location").sortable("disable");
			$("#sortable_selected_location").enableSelection();
	   }
	   
	   if(mode ==="edit"){
		   //old version
		   //$('.patrol-btn-dropLocation').hide();
		   
		   //19-6-2017 
		   $('.patrol-btn-dropLocation').show();
	   }
	   
	   
	   var formJson = document.getElementById("hidden_selectedLocationJson");
	   if(formJson != null){
		   formJson.value = locationJson;
	   }
});

//$(".minValue").keyup(function(event){
$(".minValue").bind('keyup change click',function(event){
	console.log("min change keypress");

	var v = this.value;
	if(v ==""){
		v=0;
	}
	
	$.ajax({
		method:"POST",
		url:"MinValueKeyPressChange.do",
		data:"index="+this.name + "&time="+v+ "&locationJson="+encodeURIComponent(locationJson),
		success : function(data) {

			console.log(data);
			
			locationJson = data;

			 var formJson = document.getElementById("hidden_selectedLocationJson");
			   if(formJson != null){
				   formJson.value = locationJson;
			   }
			
		}
	});
	
});

//$(".maxValue").keyup(function(){
$(".maxValue").bind('keyup change click',function(){
	console.log("max change keypress");

	var v = this.value;
	if(v ==""){
		v=0;
	}
	
	$.ajax({
		method:"POST",
		url:"MaxValueKeyPressChange.do",
		data:"index="+this.name + "&time="+v+ "&locationJson="+encodeURIComponent(locationJson),
		success : function(data) {

			console.log(data);
			
			locationJson = data;

			 var formJson = document.getElementById("hidden_selectedLocationJson");
			   if(formJson != null){
				   formJson.value = locationJson;
			   }
			
		}
	});
});




/*
$('.minValue').change(function(){
	console.log("min change trigger");
	
	$.ajax({
		method:"POST",
		url:"MinValueChange.do",
		data:"index="+this.name + "&time="+this.value+ "&locationJson="+encodeURIComponent(locationJson),
		success : function(data) {

			$('#selectedLocation').html(data);

		}
	});
	
});
*/
/*
$('.maxValue').change(function(){
	
	console.log("max change trigger");
	$.ajax({
		method:"POST",
		url:"MaxValueChange.do",
		data:"index="+this.name + "&time="+this.value+ "&locationJson="+encodeURIComponent(locationJson),
		success : function(data) {

			$('#selectedLocation').html(data);

		}
	});
	
});
*/

function dropSelectedLocation(index) {
	$.ajax({
		method : "POST",
		url : "DropSelectedLocation.do",
		data : "indexId=" + index + "&locationJson="+encodeURIComponent(locationJson),
		success : function(data) {

			$('#selectedLocation').html(data);

		}
	});
}

function dropAllSelectedLocation(){
	$.ajax({
		method : "POST",
		url : "DropAllSelectedLocation.do",
		data: {"locationJson" : locationJson},
		success : function(data) {

			$('#selectedLocation').html(data);
			$("#view_routeCode").val("");
			$("#view_routeName").val("");
			refreshStartingLocation();
		}
	}).done(function(){
		$( document ).ajaxComplete(function() {
		for(i=0;i<selected_RouteKeys.length;i++){
			$("#route_searched_name_" + selected_RouteKeys[i]).parent().hide();
				}		
			})
		});	
}

function changeUnitTag(){
	
	if($("#routeMinTimeUnit").val() == 0){
		$(".start_unit_tag").html("<spring:message code='patrol.mins' />");
	}
	if($("#routeMinTimeUnit").val() == 1){
		$(".start_unit_tag").html("<spring:message code='patrol.hrs' />");
	}
	
	if($("#routeMaxTimeUnit").val() == 0){
		$(".end_unit_tag").html("<spring:message code='patrol.mins' />");
	}
	if($("#routeMaxTimeUnit").val() == 1){
		$(".end_unit_tag").html("<spring:message code='patrol.hrs' />");
	}
	
}

</script>

<div class="panel-heading">
	<div class="row">
		<div class="col-xs-5">
			<label id="selected_title" class=""><spring:message
					code="common.selected" /></label>
		</div>
	  	<div class="col-xs-6" id="hide4SearchRoutePage">
			<div class="col-xs-5">
				 <spring:message code="common.Min" /> 
			</div>
			<div class="col-xs-1"></div>
			<div class="col-xs-5">
				 <spring:message code="common.Max" /> 
			</div>
		</div> 
	</div>
</div>

<div id="selectedLocation_scroll" class="patrol-childLocation">
	<spring:message code="common.min" var="min" />
	<spring:message code="common.max" var="max" />
	<ul id="sortable_selected_location"
		class="list-group patrol-list-group">
		<c:forEach var="selectedList" items="${selectedLocationList}"
			varStatus="status">
			<li class="list-group-item patrol-childlist-li">
				<div class="row" style="display: flex;">
					<div class="col-xs-5">
						<span>${selectedList.seq}.</span> 
						<span id="sel_name_${selectedList.seq}" class="" value = ${selectedList.locationKey} style="word-wrap: break-word;">
							  ${selectedList.code} - ${selectedList.name}
						</span>
				<script>
				//document.getElementById('sel_name_'+${selectedList.seq}).innerHTML = _.escape(document.getElementById('sel_name_'+${selectedList.seq}).innerHTML);
				$('#sel_name_'+${selectedList.seq}).text(_.unescape(document.getElementById('sel_name_'+${selectedList.seq}).innerHTML));
				selectedLocationKey.push(${selectedList.locationKey});
				</script>
					</div>
					<div class="col-xs-6">
						<div class="row dur_tag" style="display: flex;">
							<!--  <div class="col-xs-1 help-block">Min.</div> -->
							<div class="col-xs-5">
								<input style="width: 80%;" class="minValue help-block"
									type="number" min="0" max="999" placeholder="${min}"
									value="${selectedList.minTime}" name="${selectedList.seq}" />
							</div>
							<div class="col-xs-1">
								<span class="minute_tag start_unit_tag help-block"
									style="float: right;"><spring:message code="patrol.mins" /></span>
							</div>
							<!--  <div class="col-xs-1 help-block">Max.</div>-->
							<div class="col-xs-5">
								<input style="width: 80%;" class="maxValue help-block"
									type="number" min="0" max="999" placeholder="${max}"
									value="${selectedList.maxTime}" name="${selectedList.seq}" />
							</div>

							<div class="col-xs-1">
								<span class="minute_tag end_unit_tag help-block"
									style="float: right;"><spring:message code="patrol.mins" /></span>
							</div>

						</div>
					</div>
					<div class="col-xs-1">
						<span
							class="glyphicon glyphicon-remove-sign patrol-btn-dropLocation"
							onclick="dropSelectedLocation(${selectedList.seq})"></span>
					</div>


				</div>
			</li>
		</c:forEach>
	</ul>
</div>

<script>
//for not to add duplicate location to patrol route
var unique = selectedLocationKey.filter(function(elem, index, arr) {
    return index == arr.indexOf(elem);
});
</script>

<!-- 

<div class="panel-heading">
										<label>Selected</label>
									</div>

<div id="selectedLocation" class="patrol-childLocation">
	<ul class="list-group patrol-list-group">
		<li class="list-group-item">Cras justo odio</li>
		<li class="list-group-item">Dapibus ac facilisis in</li>
		<li class="list-group-item">Morbi leo risus</li>
		<li class="list-group-item">Porta ac consectetur ac</li>
		<li class="list-group-item">Vestibulum at eros</li>
		<li class="list-group-item">Vestibulum at eros</li>
	</ul>
</div>
-->
