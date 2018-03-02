/**
 * 
 */

$(document).ready(function() {
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
                click: function (event) {
                	confirmSave(event);
                }
            },
            {
                id: "no",
                text: "No",
                click: function (event) {
                    $(this).dialog('close');
                }
            }
        ]
    });			

//    if ($.isFunction(loadShowClockIn)){
//    	loadShowClockIn();
//    }
})
		
function confirmSave(e){
	var punchCardForm = {};
	
	 if ($.isFunction(postData)){
		 postData(punchCardForm);
	}
}