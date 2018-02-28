/**
 * 
 */
$(document).ready(
		function() {
			$('#clockIn').load("ShowClockIn.do");
			$("#modalClockIn").dialog({
	               autoOpen: false, 
	               buttons: {
	                  OK: function() {$(this).dialog("close");}
	               },
	               title: "Confirm Punch Card Clock In?",
	               position: {
	                  my: "center",
	                  at: "center"
	               }
	            });
	            $( "#opener-2" ).click(function() {
	               $( "#dialog-2" ).dialog( "open" );
	            });
			
			
		})
		
