<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<script src="import/underscorejs/underscore-min.1.8.3.js"></script>

<script type="text/javascript">
var temp_edit = $("#eventModal").hasClass('in');
if(temp_edit){
	
	if('undefined' != typeof currentMode){
		if(currentMode=="current"){
		
			currentEventEditStaffJson = '${selectedStaffJson}';
			edit_selectedStaffJson = '${eventEditStaffJson}';
		
		}else if(currentMode =="series"){
		
			parentEventEditStaffJson = '${selectedStaffJson}';
			edit_parentSelectedStaffJson = '${eventEditStaffJson}';		
		}
	}
	
}else{

	selectedStaffJson = '${selectedStaffJson}';
	eventEditStaffJson = '${eventEditStaffJson}';
}

var selectedKeys=[];
function dropSelectedStaff(staffKey){
	
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
		data : "edit="+edit+"&accountKey="+staffKey + "&selectedStaffJson="+temp_select+"&eventEditStaffJson="+temp_edit,
		url : "DropSelectedStaff.do",
		success : function(data) {
			$('#view_selectedStaff').html(data);
		}
	});
	$("#searched_staff_" + staffKey).parent().show();
}


</script>

<div class="panel-heading">
	<label class="patrol-goback"><spring:message
			code="common.selected" /></label>
</div>
<div class="patrol-childLocation" id="container_selectedRoute">
	<div class="list-group patrol-list-group">
		<c:forEach var="selectedStaff" items="${selectedStaff}"
			varStatus="status">

			<div class="list-group-item patrol-childlist-li">
				<div class="childList-Name" id="selected_staff_${selectedStaff.key}"
					value="${selectedStaff.key}" style="word-wrap: break-word;">${selectedStaff.loginId}
					- ${selectedStaff.name}</div>
				<script>
				document.getElementById("selected_staff_${selectedStaff.key}").innerHTML = _.escape(document.getElementById("selected_staff_${selectedStaff.key}").innerHTML);
				selectedKeys.push(${selectedStaff.key});
				</script>

				<div class="patrol-btn-dropLocation"
					onclick="dropSelectedStaff(${selectedStaff.key})">
					<span class="glyphicon glyphicon-remove-sign"></span>
				</div>
			</div>
		</c:forEach>
	</div>
</div>


<script>
for(i=0; i<selectedKeys.length; i++){
	$("#searched_staff_" + selectedKeys[i]).parent().hide();
}

function dropAllSelectedStaff(){
	console.log("dropAllSelectedStaff");
	var edit = $("#eventModal").hasClass('in');
	
	$.ajax({
		method : "POST",
		data : {"accountKeys":keys, "edit":edit , "selectedStaffJson":selectedStaffJson , "eventEditStaffJson":eventEditStaffJson},
		traditional: true,
		url : "DropAllSelectedStaff.do",
		success : function(data) {
			$('#view_selectedStaff').html(data);
			
			
		}		
	});
	for(i=0; i<selectedKeys.length; i++){
		$("#searched_staff_" + selectedKeys[i]).parent().show();
	}
}

/*
var assignedKey = [];
for(i=0;i<$("#staff option").size();i++){
	assignedKey.push($("#staff option")[i].value);
}

var editAssignedKey = [];
for(i=0;i<$("#staff_modal option").size();i++){
	editAssignedKey.push($("#staff_modal option")[i].value);
}

console.log("assignedKey : " + assignedKey);
console.log("editAssignedKey : " + editAssignedKey);

var comfirmedKeys = [];
if(assignedKey.length > editAssignedKey.length){
	comfirmedKeys = assignedKey;
	console.log("(A) comfirmedKeys : " + assignedKey);
}else{
	comfirmedKeys = editAssignedKey;
	console.log("(E) comfirmedKeys : " + editAssignedKey);
}
*/

function addspecifySelectedStaff(){
		var edit = $("#eventModal").hasClass('in');
	
		var comfirmedKeys = [];
		for(i=0;i<$("#staff option").size();i++){
			comfirmedKeys.push($("#staff option")[i].value);
		}
	
		console.log("comfirmedKeys default");
		
		if(edit){
			
			if('undefined' != typeof currentMode){
				if(currentMode == "current"){
					comfirmedKeys = [];
					for(i=0;i<$("#staff_modal option").size();i++){
						comfirmedKeys.push($("#staff_modal option")[i].value);
					}
				
					console.log("comfirmedKeys current");
				
				}else if(currentMode == "series"){
					comfirmedKeys = [];
					for(i=0;i<$("#staff_modal_series option").size();i++){
						comfirmedKeys.push($("#staff_modal_series option")[i].value);
					}
				
					console.log("comfirmedKeys series");
				}
			}
			
		}
		
		console.log("comfirmedKeys : "+comfirmedKeys);
	
		$.ajax({
			method : "POST",
			data : {"accountKeys":comfirmedKeys, "edit":edit , "selectedStaffJson":selectedStaffJson , "eventEditStaffJson":eventEditStaffJson},
			traditional: true,
			url : "AddAllSelectedStaff.do",
			success : function(data) {
				$('#view_selectedStaff').html(data);

			}		
		});
			for(i=0; i<comfirmedKeys.length; i++){
				$("#searched_staff_" + comfirmedKeys[i]).parent().hide();
			}
}

function ACBack2Previous(){
	submitClearedStaffSearch();
	
	dropAllSelectedStaff();
	
	//if(comfirmedKeys.length>0){
		addspecifySelectedStaff();
	//}
}

</script>