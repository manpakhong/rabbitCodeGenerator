<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script src="import/underscorejs/underscore-min.1.8.3.js"></script>

<script>

$(document).ready(function(){
	
	 if(mode==="view"){
		$('.patrol-btn-addLocation').hide();
	 }
})

function getChildren(parentId) {

	$.ajax({
		method : "POST",
		url : "ShowChildreUnderParent.do",
		data : "parentId=" + parentId,
		success : function(data) {

			$('#childLocation').html(data);

		}

	});

};

function getParent(parentId) {

	$.ajax({
		method : "POST",
		url : "ShowParent.do",
		data : "parentId=" + parentId,
		success : function(data) {

			$('#childLocation').html(data);

		}

	});

};

function addSelectedLocation(locationId) {
	var minTime = document.getElementById("routeMinTime").value;
	var maxTime = document.getElementById("routeMaxTime").value;
	
	if(minTime <0){
		//document.getElementById("email").setCustomValidity("This email is already used");
		$('#patrolCreateForm button[type=submit]').trigger('click');
		return;
	}
	
	if(maxTime <0){
		$('#patrolCreateForm button[type=submit]').trigger('click');
		return;
	}
	

	var json = locationJson;
	
	$.ajax({
		method : "POST",
		url : "AddSelectedLocation.do",
		data : "locationId=" + locationId + "&maxTime=" + maxTime
				+ "&minTime=" + minTime + "&locationJson="+encodeURIComponent(json),
		success : function(data) {
			
			$('#selectedLocation').html(data);
			
		}
	});
}


function reminderModal(locationKey){
	$("#reminderModal").modal('show');
	var content = $('span[value=' + locationKey + ']').html()
	$("#reminderModalBody").html("<b>" + content + "</b> "  + '<spring:message code="patrol.route.duplicates"/>');
}


function CheckDuplicate(locationKey){
	//unique was declared in view_showSelectedLocation.jsp
	
	duplicates = false;

	if(unique.length>0){
	for ( i=0; i<unique.length; i++){
		if(locationKey == unique[i]){
			duplicates = true;
			reminderModal(locationKey);
			break;
		} else{
			duplicates = false;
			}
		}
	} 
	
	if(!duplicates){
		//console.log("There is no duplicate.");
		addSelectedLocation(locationKey);
	}
}

</script>

<div class="panel-heading">
	<label class="patrol-goback" onclick="getParent(${parentKey})"><span
		class="glyphicon glyphicon-chevron-left patrol-logo-font"></span> <spring:message
			code="patrol.route.location" /></label>
</div>
<div class="patrol-childLocation">
	<div class="list-group patrol-list-group">
		<c:forEach var="rootList" items="${childrenList}" varStatus="status">

			<div class="list-group-item patrol-childlist-li"
				style="display: flex;">
				<div class="childList-Name" onclick="getChildren(${rootList.key})"
					id="childName_${rootList.key}" style="word-wrap:break-word;">${rootList.code} - ${rootList.name}</div>
				<script>
				//document.getElementById('childName_'+${rootList.key}).innerHTML = _.escape(document.getElementById('childName_'+${rootList.key}).innerHTML);
				$('#childName_'+${rootList.key}).text(_.unescape(document.getElementById('childName_'+${rootList.key}).innerHTML));
				</script>
				<div onclick="getChildren(${rootList.key})" style="cursor: pointer;">
					<c:choose>
						<c:when test="${rootList.hasChildren}">
							<span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</div>
				<c:choose>
					<c:when test="${rootList.hasPivilege}">
						<!--  <div class="patrol-btn-addLocation"
							onclick="addSelectedLocation(${rootList.key})"> -->
							
							<div class="patrol-btn-addLocation"
							onclick="CheckDuplicate(${rootList.key})">
							
							<span class="glyphicon glyphicon-plus-sign"></span>
						</div>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>

			</div>
		</c:forEach>
	</div>
</div>