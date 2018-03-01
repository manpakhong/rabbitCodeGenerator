/**
 * 
 */

$(document).ready(
		function() {
        	$('#clockIn').load("ShowClockIn.do");
            $("#modalClockIn").dialog({
            	autoOpen: false,
                modal: true,
                title: "Confirmation",
                width: 600,
                height: 300,
                resizable: true,
	               position: {
                my: "center",
                at: "center"
             },
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
        	$('#clockIn').load("ShowClockIn.do");
//            if ($.isFunction(loadShowClockIn)){
//            	loadShowClockIn();
//            }
			


		})
		
