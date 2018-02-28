<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<script src="import/underscorejs/underscore-min.1.8.3.js"></script>

<script type="text/javascript">
var keys=[];
function addSelectedStaff(staffKey){
	
	var edit = $("#eventModal").hasClass('in');

	var temp_select = selectedStaffJson;
	var temp_edit = eventEditStaffJson;

	if(edit){
		
		if('undefined' != typeof currentMode){
			if(currentMode=="current"){
		
				temp_select = currentEventEditStaffJson;
				temp_edit = edit_selectedStaffJson;
		
			}else if(currentMode == "series"){
	
				temp_select = parentEventEditStaffJson;
				temp_edit = edit_parentSelectedStaffJson;
		
			}
		}
	}
	
	
	$.ajax({
		method : "POST",
		data : {"accountKey":staffKey, "edit":edit , "selectedStaffJson":temp_select , "eventEditStaffJson":temp_edit},
		url : "AddSelectedStaff.do",
		success : function(data) {
			
			$('#view_selectedStaff').html(data);
			
		}
	});
	$("#searched_staff_" + staffKey).parent().hide();
}
</script>

<div class="panel-heading">
	<label class="patrol-goback"><spring:message
			code="patrol.schedule.account" /></label>
</div>
<div class="patrol-childLocation" id="searchedList">
	<div class="list-group patrol-list-group">
		<c:forEach var="searchedStaff" items="${searchedStaff}"
			varStatus="status">

			<div class="list-group-item patrol-childlist-li">
				<div class="childList-Name"
					style="cursor: default; word-wrap: break-word;"
					id="searched_staff_${searchedStaff.key}"
					value="${searchedStaff.key}">${searchedStaff.loginId}-
					${searchedStaff.name}</div>
				<script>
				document.getElementById("searched_staff_${searchedStaff.key}").innerHTML = _.escape(document.getElementById("searched_staff_${searchedStaff.key}").innerHTML);
				keys.push(${searchedStaff.key});
				</script>

				<div class="patrol-btn-addLocation"
					onclick="addSelectedStaff(${searchedStaff.key})">
					<span class="glyphicon glyphicon-plus-sign"></span>
				</div>
			</div>
		</c:forEach>
	</div>
</div>

<script>

function addAllSelectedStaff(){
	var edit = $("#eventModal").hasClass('in');

	console.log("addAllSelectedStaff");
	console.log(keys);
	
	$.ajax({
		method : "POST",
		data : {"accountKeys":keys, "edit":edit , "selectedStaffJson":selectedStaffJson , "eventEditStaffJson":eventEditStaffJson},
		traditional: true,
		url : "AddAllSelectedStaff.do",
		success : function(data) {
			$('#view_selectedStaff').html(data);

		}		
	});
		for(i=0; i<keys.length; i++){
			$("#searched_staff_" + keys[i]).parent().hide();
		}
}
</script>
