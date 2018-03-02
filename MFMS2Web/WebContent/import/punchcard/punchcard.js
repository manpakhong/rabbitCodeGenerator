/**
 * 
 */
$(document).ready(function() {

			
//	$("#testDivButton").append('<input type="button" value="test" onclick="openSelectRouteModal();" />');   
			$('#clockIn').load("ShowClockIn.do");
		})
function openSelectRouteModal(){
	$("#modalClockIn").dialog("open");
}	
		
function loadShowClockIn() {
	$('#clockIn').load("ShowClockIn.do");
}
		
		
function saveData(){
	
}