/**
 * 
 */
$(document).ready(function() {
			$("#modalClockIn").dialog({
					modal: true,
	               autoOpen: false, 
	               width: 350,
	               height: 350,
	               title: "Confirm Punch Card Clock In?",
	               position: {
	                  my: "center",
	                  at: "center"
	               },
	               buttons: [
	                   {
	                       id: "Delete",
	                       text: "Delete",
	                       click: function () {
	                           alert("Delete clicked.");
	                       }
	                   },
	                   {
	                       id: "Cancel",
	                       text: "Cancel",
	                       click: function () {
	                           $(this).dialog('close');
	                       }
	                   }
	                   ]
	            });
	            $( "#opener-2" ).click(function() {
	               $( "#dialog-2" ).dialog("open" );
	            });
			
//	$("#testDivButton").append('<input type="button" value="test" onclick="openSelectRouteModal();" />');   
			$('#clockIn').load("ShowClockIn.do");
		})
function openSelectRouteModal(){
	alert('open');
	$("#modalClockIn").dialog("open");
}	
		
function loadShowClockIn() {
	$('#clockIn').load("ShowClockIn.do");
}
		
		
function saveData(){
	
}