/**
 * 
 */
$(document).ready(function(){
	$("#testDivButton").append('<input type="button" value="test" onclick="openSelectRouteModal();" />');
	$('#clockIn').load("ShowClockIn.do");
})
function openSelectRouteModal() {
	$("#modalClockIn").dialog("open");
}

function loadShowClockIn() {
	$('#clockIn').load("ShowClockIn.do");
}
function createPunchCardVo(){
	var vo = {};
	vo.currentDateTimeString = '';
	vo.remarkLocation = '';
	vo.remarks = '';
	vo.userName = '';
	return vo;
}
function postData(punchCardForm) {

	var dataString = JSON.stringify(punchCardForm);
	console.log(dataString);
	$.ajax({
		type : "POST",
		url : "submitClockIn.do",
		data : {
			data : dataString
		}
	}).done(function(data, status, jqXHR) {
		selectBlogPageVoCallBack(data);
		// alert("Promise success callback.");
	}).fail(function(jqXHR, status, err) {
		// alert("Promise error callback.");
	}).always(function() {
		// alert("Promise completion callback.");
	})
}