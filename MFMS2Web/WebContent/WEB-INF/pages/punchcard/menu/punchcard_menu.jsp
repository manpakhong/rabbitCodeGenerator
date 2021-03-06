<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../../common/left.jsp" />


<link rel="stylesheet" href="import/css/patrol/patrol_style.css">

<link rel="stylesheet"
	href="import/datetimepicker/bootstrap-datetimepicker2.css">
<link rel="stylesheet"
	href="import/fullcalendar-3.4.0/lib/cupertino/jquery-ui.min.css">
<link rel="stylesheet"
	href="import/punchcard/css/punchcard.css">
<script src="import/jQueryUI/jquery-ui.js"></script>
<script src="import/datetimepicker/moment.js"></script>
<script src="import/datetimepicker/bootstrap-datetimepicker2.js"></script>
<script src="import/customLoading.js"></script>
<script src="import/punchcard/punchcard.js"></script>
<!-- <script src="import/punchcard/viewclockin.js"></script> -->

<input type="hidden" class="confirmClass" value="<spring:message code="punchcard.message.confirmation" />" />
<input type="hidden" class="areyousureClass" value="<spring:message code="punchcard.message.areyousure" />" />
<input type="hidden" class="usernameClass" value="<spring:message code="punchcard.message.username" />" />
<input type="hidden" class="timeClass" value="<spring:message code="punchcard.message.time" />" />
<input type="hidden" class="remarklocationClass" value="<spring:message code="punchcard.message.remarklocation" />" />
<input type="hidden" class="remarksClass" value="<spring:message code="punchcard.message.remarks" />" />
<input type="hidden" class="yesClass" value="<spring:message code="punchcard.message.yes" />" />
<input type="hidden" class="noClass" value="<spring:message code="punchcard.message.no" />" />
<input type="hidden" class="savesuccessfullyClass" value="<spring:message code="punchcard.message.savesuccessfully" />" />
<input type="hidden" class="savefailedClass" value="<spring:message code="punchcard.message.savefailed" />" />
<input type="hidden" class="selectanoptionClass" value="<spring:message code="punchcard.message.selectanoption" />" />


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_patrol_management_b"></i>
				<spring:message code="menu.punchcardMgt" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">

			<div class="col-lg-4  col-sm-6 punchCardIcon iconhover" >

						<img class="img-responsive clockIn" src="import/img2/pcm/clock_in.png"
							alt="" >


				</a>
			</div>


			<div class="col-lg-4  col-sm-6 punchCardIcon iconhover">
						<img class="img-responsive clockOut" src="import/img2/pcm/clock_out.png"
							alt="">
				</a>
			</div>


			<div class="col-lg-4  col-sm-6 punchCardIcon iconhover">

						<img class="img-responsive skip" src="import/img2/pcm/skip.png" alt="">

				</a>
			</div>
<!-- 			<input type="button" value="test" onclick="openSelectRouteModal();" /> -->
 			<div id="testDivButton"></div> 
		</div>
	</section>
</div>


<div id="clockIn">
</div>

<script>
	/* 	menu_toggle("sub_pm", 1); */
</script>
