/**
 * 
 */

$(document).ready(function() {
    $("#modalClockIn").dialog({
    	autoOpen: false,
        modal: true,
        title: "Confirmation",
        width: 600,
        height: 500,
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

function connectionPunchCardVoData(){
	var vo = createPunchCardVo();
	var userName = $(".userAccountName").text();
	var currentDateTimeString = $(".currentDateTimeString").text();
	var locationListSelectObj = $(".locationListSelect");
	var remarks = $(".remarks").val();
	vo.userName = userName;
	vo.currentDateTimeString = currentDateTimeString;
	vo.remarkLocation = $(locationListSelectObj).val();
	vo.remarks = remarks;
	return vo;
}
function confirmSave(e){
	var vo = connectionPunchCardVoData();
	 if ($.isFunction(postData)){
		 postData(vo);
	}
}