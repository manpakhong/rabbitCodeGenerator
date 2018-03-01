/**
 * 
 */
function openSelectRouteModal(){
	$("#modalClockIn").dialog( "open" );
}	
	
$(document).ready(
		function() {

            $("#modalClockIn").dialog({
            	autoOpen: false,
                modal: true,
                title: "Confirmation",
                width: 600,
                height: 300,
                resizable: true,
                buttons: [
		            {
		                id: "yes",
		                text: "Yes",
		                click: function () {
		                    alert("Delete clicked.");
		                }
		            },
		            {
		                id: "no",
		                text: "No",
		                click: function () {
		                    $(this).dialog('close');
		                }
		            }
	            ]
            });			
            
            if ($.isFunction(loadShowClockIn)){
            	loadShowClockIn();
            }
			


		})
		
