/**
 * 
 */
$(document).ready(function(){
//	$("#testDivButton").append('<input type="button" value="test" onclick="openSelectRouteModal();" />');
	$('#clockIn').load("ShowClockIn.do");
	bindHandlers();
	retrievePropertiesValues();
})
var ACTION_CLOCK_IN = 'CI';
var ACTION_CLOCK_OUT = 'CO';
var modalCaller = '';

var confirmClass = '';
var areyousureClass = '';
var usernameClass = '';
var timeClass = '';
var remarklocationClass = '';
var remarksClass = '';
var yesClass = '';
var noClass = '';
var savesuccessfullyClass = '';
var savefailedClass = '';
var selectanoptionClass = '';


function retrievePropertiesValues(){
	confirmClass = $('.confirmClass').val();
	areyousureClass = $('.areyousureClass').val();
	usernameClass = $('.usernameClass').val();
	timeClass = $('.timeClass').val();
	remarklocationClass = $('.remarklocationClass').val();
	remarksClass = $('.remarksClass').val();
	yesClass = $('.yesClass').val();
	noClass = $('.noClass').val();
	savesuccessfullyClass = $('.savesuccessfullyClass').val();
	savefailedClass = $('.savefailedClass').val();
	selectanoptionClass = $('.selectanoptionClass').val();
}

function openSelectRouteModal(caller) {
	modalCaller = caller;
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
	vo.action = '';
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
		alert("Promise error callback.");
	}).always(function() {
//		alert("Promise completion callback.");
	})
}
function bindHandlers(){
	$(".clockIn").click(function(){
		openSelectRouteModal(ACTION_CLOCK_IN);
	});
	$(".clockOut").click(function(){
		openSelectRouteModal(ACTION_CLOCK_OUT);
	});
	$(".skip").click(function(){
		window.location.href= "Home.do?roleKey=2";
	});
}
function selectBlogPageVoCallBack(data){
	var vo = JSON.parse(data);
	alert(savesuccessfullyClass);
	$('#modalClockIn').dialog('close');
}