<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />
<link rel="stylesheet" href="import/css/patrol/patrol_style.css">
<script src="import/underscorejs/underscore-min.1.8.3.js"></script>
<script src="import/fullcalendar/moment.min.js"></script>
<script src="import/customLoading.js"></script>

<script>
	var socket;
	var lastLoadPatrolResult = "";
	var lastCreatePatrolResult = "";
	var expireTimeJson = "";
	var expTimer = [];
	var futureTimeJson = "";
	var futureTimer = [];
	var currentSiteKey = ${currentSiteKey};

	showCustomLoading();
	
	$(document).ready(
			function() {

				$("#currentPatrol").load("CurrentPatrol.do");
				$("#futurePatrol").load("FuturePatrol.do", function() {
					hideCustomLoading();
				});

				connectServer();

				//console.log("Not NULL TR : "
				//		+ $('#table_currentResult_body tr:first').length);
			});

	function autoRefreshFuturePatrol() {

		setTimeout(function() {
			$("#futurePatrol").load("FuturePatrol.do");
		}, 1000 * 60 * 15);

	}

	function connectServer() {

		var ip = $("#ip").val();
		var port = $("#port").val();

		var webapp = window.location.pathname.split("/");
		var host = "ws://" + window.location.host + "/" + webapp[1]
				+ "/websocket/patrolMonitor";
		//console.log(host);
		socket = new WebSocket(host);

		socket.onopen = function(event) {
			console.log("Emulator : socket open");
			ajaxManager.run(); 

		}

		socket.onclose = function(event) {
			console.log("Emulator : socket close");
			socket = null;
			
			$("#alertSection").append('<div class="alert alert-info alert-dismissible" role="alert">'
					+'<button type="button" class="close" data-dismiss="alert"aria-label="Close">'
					+'<span aria-hidden="true">&times;</span>'
					+'</button>'
					+'<strong><spring:message code="patrol.monitor.socket.close"/></strong>'
					+'</div>');
			
		}

		socket.onmessage = function(event) {
			console.log("Emulator : MSG : " + event.data);
			//DigestMessage(event);

			updatePatrolResult(event.data);
		}

	}

	function updatePatrolResult(result) {
		var json = JSON.parse(result);
		//console.log(json.routeDefKey);
		//console.log(json.patrolResultKey);
		console.log(json.action);

		var routeDefKey = json.routeDefKey;
		var patrolResultKey = json.patrolResultKey;
		var action = json.action;
		var scheduleKey = json.scheduleKey;
		var siteKey = json.siteKey;
		
		if(siteKey != currentSiteKey){
			return;
		}

		if (action == "Create") {
			if (lastCreatePatrolResult == routeDefKey)
				return;
			else
				lastCreatePatrolResult = routeDefKey;

			FnQueue.add(function(){setTimeout(function() {
				updateResultUI(routeDefKey, patrolResultKey, action,
						scheduleKey);
			}, 1000);

			
			});
		} else {
			FnQueue.add(function(){
				updateResultUI(routeDefKey, patrolResultKey, action, scheduleKey);
			});
		}

	}
	
	(function(ns){

		  var queue = [];
		  var isRunning = false;
		  var runner = null;

		  ns.add = function(fn){
		    queue.push(fn);
		    if(!isRunning) ns.resume();
		    return ns;
		  };

		  ns.clear = function(){
		    queue.length = 0;
		    return ns;
		  };

		  ns.stop = function(){
		    clearInterval(runner);
		    runner = null;
		    isRunning = false;
		    return ns;
		  };

		  ns.resume = function(){
		    if(!isRunning && !runner){
		      isRunning = true;
		      runner = setInterval(function(){
		        // Here, I intentionally set the context to null. This makes the
		        // callbacks have a `this` value of null, instead of `window`.
		        queue.shift().call(null);
		        if(!queue.length) ns.stop();
		      }, 0);
		    }
		    return ns;
		  };

		  ns.getQueue = function(){
		    return queue.slice(0);
		  };

		  ns.isRunning = function(){
		    return isRunning;
		  };

		}(this.FnQueue = this.FnQueue || {}));
	
	var ajaxManager = (function() {
	     var requests = [];

	     return {
	        addReq:  function(opt) {
	            requests.push(opt);
	        },
	        removeReq:  function(opt) {
	            if( $.inArray(opt, requests) > -1 )
	                requests.splice($.inArray(opt, requests), 1);
	        },
	        run: function() {
	            var self = this,
	                oriSuc;

	            if( requests.length ) {
	                oriSuc = requests[0].complete;

	                requests[0].complete = function() {
	                     if( typeof(oriSuc) === 'function' ) oriSuc();
	                     requests.shift();
	                     self.run.apply(self, []);
	                };   

	                $.ajax(requests[0]);
	            } else {
	              self.tid = setTimeout(function() {
	                 self.run.apply(self, []);
	              }, 1000);
	            }
	        },
	        stop:  function() {
	            requests = [];
	            clearTimeout(this.tid);
	        }
	     };
	}());
	

	function updateResultUI(routeDefKey, patrolResultKey, action, scheduleKey) {

		$("#futurePatrol").load("FuturePatrol.do");
		if (futureTimer[scheduleKey] != null) {
			//console.log("clear future timeout : " + scheduleKey);
			clearTimeout(futureTimer[scheduleKey]);

		}

		if (action == "Create") {
			if ($("#schedule_alert_" + scheduleKey).length > 0) {
				$("#schedule_alert_" + scheduleKey).remove();
				//console.log("Remove Div : scheduleKey : "+scheduleKey );
			}
		}

		

		if ($("#schedule_alert_" + scheduleKey).length > 0) {
			$("#schedule_routeDef_" + routeDefKey + "_" + patrolResultKey)
					.remove();
			//console.log("Remove Div : scheduleKey : "+scheduleKey );
		}

		if (action == "Complete") {

			$("#routeDef_" + routeDefKey).remove();
			
			if (lastCreatePatrolResult == routeDefKey)
				lastCreatePatrolResult = -1;

			if (lastLoadPatrolResult == routeDefKey)
				lastLoadPatrolResult = -1;

			if (expTimer[routeDefKey] != null) {
				//console.log("clear timeout");
				clearTimeout(expTimer[routeDefKey]);
			}

			if ($("#cur_result_" + patrolResultKey).length > 0) {
				if ($("#cur_result_" + patrolResultKey + " .at").html() == "" && $("#cur_result_" + patrolResultKey + " .ac").html() == "") {
					$("#cur_result_" + patrolResultKey + " .at").html("---");
					$("#cur_result_" + patrolResultKey + " .ac").html("---");
					$("#cur_result_" + patrolResultKey + " .rmk").html('<spring:message code="patrol.monitor.result.skipped"/>');
				}
				
				$("#resultDetails").collapse('hide');
			}

			return;
		}

		//ajaxManager.addReq
		  $.ajax({
			url : "UpdatePatrolResult.do",
			data : "routeDefKey=" + routeDefKey,
			method : "POST",
			success : function(data) {
				//console.log(data);
				$("#routeDef_" + routeDefKey).remove();
				
				if ($('#table_currentResult_body tr:first').length == 0) {
					$('#table_currentResult_body').append(data);
				} else {
					$('#table_currentResult_body tr:first').before(data);
				}
			}
		});

		if ($("#cur_result_" + patrolResultKey).length > 0) {

			console.log("#cur_result_" + patrolResultKey);
			ajaxManager.addReq({
				url : "UpdateCurrentPatrolResult.do",
				method : "POST",
				data : "patrolResultKey=" + patrolResultKey + "&index="
						+ $("#cur_result_index_" + patrolResultKey).val(),
				success : function(data) {
					
					$(".result-current-location").removeClass(
							"result-current-location");
					$(".result-overtime-location").removeClass(
							"result-overtime-location");
					$("#cur_result_" + patrolResultKey).html(data);
					$("#cur_result_" + patrolResultKey).addClass(
							"result-current-location");
				}
			});
		}

	}

	function setTimeoutTimer(routeDefKey, resultKey, expireTime, accountKey) {
		var now = moment();
		var exp = moment(expireTime);
		exp = exp.add(<spring:eval expression="@propertyConfigurer.getProperty('patrol.web.overtime.delay.minute')" />, 'minutes');
			
		//console.log(now);
		//console.log(exp);
		//console.log(expireTime + "||" + routeDefKey);

		if (exp.isBefore(now)) {

			//console.log("Expired");
			setDefTimeoutUI(routeDefKey, resultKey, accountKey);

		} else {
			//console.log("set Timer 1");

			var timeout = exp.diff(now);

			//console.log(timeout);

			if (expTimer[routeDefKey] != null) {
				//console.log("clear timeout");
				clearTimeout(expTimer[routeDefKey]);
			}

			expTimer[routeDefKey] = setTimeout(function() {
				setDefTimeoutUI(routeDefKey, resultKey, accountKey);
			}, timeout);

		}

	}

	function setDefTimeoutUI(routeDefKey, resultKey, accountKey) {

		//console.log("run ");
		$("#routeDef_" + routeDefKey).addClass("monitor-danger");
		$("#cur_result_" + resultKey).addClass("monitor-danger");
		addAlert(-1, routeDefKey, resultKey, accountKey);

	}

	function setFutureTimeout(scheduleKey, expireTime) {
		var now = moment();
		var exp = moment(expireTime);

		if (exp.isBefore(now)) {

			//console.log("Expired");
			setScheduleTimeoutUI(scheduleKey);

		} else {
			//console.log("set Timer");

			var timeout = exp.diff(now);

			//console.log(timeout);

			if (futureTimer[scheduleKey] != null) {
				//console.log("clear timeout");
				clearTimeout(futureTimer[scheduleKey]);
			}

			futureTimer[scheduleKey] = setTimeout(function() {
				setScheduleTimeoutUI(scheduleKey);
				//console.log("Time out exp");
			}, timeout);

		}
	}

	function setScheduleTimeoutUI(scheduleKey) {
		$("#schedule" + scheduleKey).addClass("monitor-danger");
		addAlert(scheduleKey, -1, -1, -1);
	}

	function addAlert(scheduleKey, routeDefKey, routeResultKey, accountKey) {

		if (scheduleKey != -1) {

			if ($("#schedule_alert_" + scheduleKey).length > 0) {
				return;
			}

		} else {

			if (routeDefKey != -1) {
				if ($("#schedule_routeDef_" + routeDefKey + "_" + accountKey).length > 0) {
					return;
				}
			}
		}

		if(scheduleKey <=0 && routeDefKey<=0){
			return;
		}
		
		$.ajax({
			url : "AddAlert.do",
			data : "scheduleKey=" + scheduleKey + "&routeDefKey=" + routeDefKey
					+ "&routeResultKey=" + routeResultKey + "&accountKey=-1",
			success : function(data) {
				$("#alertSection").append(data);
				if(data != ""){
					document.getElementById('alertAudio').play();
				}
			}
		})
	}
	
	function removeOverTime(id){
		$("#"+id).removeClass("result-overtime-location");
		$("#"+id).removeClass("monitor-danger");
	}
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="PatrolManagement.do"><i
					class="icon-icon_patrol_management_b"></i> <spring:message
						code="menu.patrolMgt" /></a></li>
			<li class="active"><spring:message
						code="menu.patrolMgt.monitor" /></li>

		</ol>
	</section>

	<section class="content">
			<div class="row">
				<div class="col-lg-12">
					<div class="box">
						<div class="box-body">

							<div id="futurePatrol" style="width: 100%; float: left;"></div>

							<div style="width: 100%; float: left; max-height: 50%;" id="currentPatrol"></div>

						</div>
					</div>
				</div>
			</div>
			
			<audio id="alertAudio" controls style="display:none;">
  				<source src="import/sound/alert.mp3" type="audio/mpeg">
			</audio>
	</section>

	<section id="alertSection" class="alert"
		style="z-index:101; position: absolute; top: 60px; right: 0;"></section>


</div>


<script>
	menu_toggle("sub_pm");
	menu_select("patrolMonitor");
</script>