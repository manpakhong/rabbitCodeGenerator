<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<link rel='stylesheet' href='import/fullcalendar/fullcalendar.css' />
<script src='import/fullcalendar/fullcalendar.js'></script>
<script src='import/fullcalendar/lang-all.js'></script>
<script src="import/customLoading.js"></script>
<div id="calendar"></div>

<!-- Modal -->
<div class="modal fade" id="eventModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel"></div>

<script type="text/javascript">
	//showLoading();

	$(document)
			.ready(
					function() {
						//console.log("defaultViewStr : " + defaultViewStr);

						$('#eventModal').on('hidden.bs.modal', function(e) {
							$('#view_calendar').load("ShowCalendar.do");
						});

						// page is now ready, initialize the calendar...

						$('#calendar')
								.fullCalendar(
										{
											// put your options and callbacks here
											events : function(start, end,
													timezone, callback) {

												$
														.ajax({
															method : "POST",
															url : "GetPatrolSchedule.do",
															dataType : "json",
															data : "",
															success : function(
																	data) {

																callback(data);
																//hideLoading();
																hideCustomLoading();
															}
														})
											},
											eventRender : function(event,
													element, view) {

												var canGen = false;

												if (event.ranges
														.filter(function(range) {

															if (event.end == null)
																return false;

															return (event.start
																	.isBefore(range.end) && event.end
																	.isAfter(range.start));
														}).length > 0) {
													canGen = true;
												}

												if (event.excludedDates != null) {
													event.excludedDates
															.filter(function(
																	excludedDate) {

																if (event.start
																		.isSameOrBefore(excludedDate.end)
																		&& event.end
																				.isSameOrAfter(excludedDate.start)) {
																	canGen = false;
																}

															});
												}

												return canGen;
											},
											eventClick : function(calEvent,
													jsEvent, view) {

												event.preventDefault();
												$
														.ajax({
															method : "POST",
															url : "GetPatrolScheduleEvent.do",
															dataType : "html",
															data : "scheduleKey="
																	+ calEvent.id
																	+ "&currentStart="
																	+ calEvent.start
																			.format()
																	+ "&currentEnd="
																	+ calEvent.end
																			.format(),
															success : function(
																	data) {
																$('#eventModal')
																		.html(
																				data)
																		.promise()
																		.done(
																				function() {
																					$(
																							'#eventModal')
																							.modal(
																									'show')
																				});
															}
														})

											},
											dayClick : function(date, jsEvent,
													view) {

												//alert("Date :" + date.format());
											},
											lang : "${calendarLocale}",
											defaultView : 'basicDay',
											columnFormat : 'ddd D/M',
											allDayDefault : false,
											displayEventTime : false,
											timezone : 'Asia/Hong_Kong',
											header : {
												left : 'title',
												right : 'month,basicWeek,basicDay,prev,next today'
											},
											eventLimit : true,
											views : {
												month : {
													eventLimit : 2
												}
											},
											eventLimitClick : "popover",
											
											eventAfterAllRender : function(view){
												if($("#notification").length==0){
													setNotificationHeader();
												}
											}
										});

						$(".fc-button-group .fc-month-button").click(
								function() {
									defaultViewStr = "month";
									//console.log("defaultViewStr : "+ defaultViewStr);
								});
						$(".fc-button-group .fc-basicWeek-button").click(
								function() {
									defaultViewStr = "basicWeek";
									//console.log("defaultViewStr : "+ defaultViewStr);
								});
						$(".fc-button-group .fc-basicDay-button").click(
								function() {
									defaultViewStr = "basicDay";
									//console.log("defaultViewStr : "+ defaultViewStr);
								});

						$(".fc-toolbar .fc-left").attr("style",
								"cursor:pointer");

						$(".fc-toolbar .fc-left")
								.append(
										"<input id='hidden_datepicker' type='button' style='visibility: hidden; position:absolute;'/>")

						$("#hidden_datepicker").datetimepicker({
							bootcssVer : 3,
							format : 'yyyy-mm',
							minView : 3,
							startView : 3,
							autoclose : true,
						}).on("show", function(ev) {
							//$(".table-condensed th").attr("style","visibility : visible");

						}).on(
								"changeDate",
								function(ev) {

									$('#calendar').fullCalendar(
											'gotoDate',
											moment(ev.date.valueOf()).format(
													"YYYY-MM-DD"));
								});

						$(".fc-toolbar .fc-left h2").click(
								function() {

									var topVar = $(".fc-toolbar .fc-left")
											.position().top;
									var leftVar = $(".fc-toolbar .fc-left")
											.position().left;

									$("#hidden_datepicker").css('top', topVar);
									$("#hidden_datepicker")
											.css('left', leftVar);

									$("#hidden_datepicker").datetimepicker(
											'show');

								})						
					});
	
	$(document).ready(function () {
		//make prompt msg disappear
	    $(document).on('click', 'button, span, div.fc-content', function () {
	    	console.log("Clicked Button");
			slideUpThePrompt();
	    });
	});
	
	function setNotificationHeader(){
		$(".fc-month-button").html("<spring:message code='defectSchedule.month'/>");
		$(".fc-basicWeek-button").html("<spring:message code='defectSchedule.week'/>");
		$(".fc-basicDay-button").html("<spring:message code='defectSchedule.day'/>");
		$(".fc-today-button").html("<spring:message code='defectSchedule.today'/>");
	}
</script>