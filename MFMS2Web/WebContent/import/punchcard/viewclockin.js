/**
 * 
 */

$(document).ready(function() {
    $("#modalClockIn").dialog({
    	autoOpen: false,
        modal: true,
        title: confirmClass,
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
                text: yesClass,
                click: function (event) {
                	confirmSave(event);
                }
            },
            {
                id: "no",
                text: noClass,
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

function collectPunchCardVoData(){
	var vo = createPunchCardVo();
	var userName = $(".userAccountName").text();
	var currentDateTimeString = $(".currentDateTimeString").text();
	var locationListSelectObj = $(".locationListSelect");
	var remarks = $(".remarks").val();
	vo.userName = userName;
	vo.currentDateTimeString = currentDateTimeString;
	vo.remarkLocation = $(locationListSelectObj).val();
	vo.remarks = remarks;
	vo.action = modalCaller;
	
	return vo;
}
function confirmSave(e){
	var vo = collectPunchCardVoData();
	 if ($.isFunction(postData)){
		 postData(vo);
	}
}