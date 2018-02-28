<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html class="no-js">
<head>
<c:set var="company">
	<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
</c:set>
<c:choose>
     <c:when test="${company=='ebsl'}">          
		<link rel="shortcut icon" type="image/png" href="import/img2/favicon_ebsl.png"/>
     </c:when>
     <c:when test="${company=='jec'}">          
		<link rel="shortcut icon" type="image/png" href="import/img2/favicon_jec.png"/>
     </c:when>
     <c:when test="${company=='mss'}">          
		<link rel="shortcut icon" type="image/png" href="import/img2/favicon_mss.png"/>
     </c:when>
     <c:otherwise>
     	<link rel="shortcut icon" type="image/png" href="import/img2/favicon_ebsl.png"/>
     </c:otherwise>
</c:choose>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>MFMS | Home</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<style>
a.disabled {
   pointer-events: none;
   cursor: default;
}


.table td {
	word-wrap: break-word;
}

.img-responsive {
	width: 75%;
	height: 75%;
	display: block;
	margin-left: auto;
	margin-right: auto;
}

.iconStyle {
	padding: 20px 20px 0 18px;
}

.no-js #loader {
	display: none;
}

.js #loader {
	display: block;
	position: absolute;
	left: 100px;
	top: 0;
}

div#load_screen {
	background: #e6e6e6;
	opacity: 0.8;
	position: fixed;
	z-index: 9998;
	top: 0px;
	left:0px;
	width: 100%;
	height: 100%;
}


div#load_screen>div#loading {
	position: fixed;
	left: 0px;
	top: 0px;
	width: 100%;
	height: 100%;
	z-index: 9999;
	background: url("import/img2/loader-32x/ring-alt.gif") center no-repeat
		#fff;
}


.pageLoadingAnimation{
	position: fixed;
	left: 44%;
	top: 43%;
	z-index: 9999;
 	border: 16px solid #f3f3f3; /* Light grey */
    border-top: 16px solid #3498db; /* Blue */
    border-radius: 50%;
    width: 120px;
    height: 120px;
    -webkit-animation: spin 2s linear infinite;
    animation: spin 2s linear infinite;
}

@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>



<c:set var="next">
	<spring:message code="common.next" />
</c:set>
<c:set var="previous">
	<spring:message code="common.previous" />
</c:set>

<c:set var="processing">
	<spring:message code="common.processing" />
</c:set>

		<c:set var="company">
			<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
		</c:set>

<input id="next" value="${next}" type="hidden" />
<input id="previous" value="${previous}" type="hidden" />
<input id="processing" value="${processing}" type="hidden" />

<script
	src="http://cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.2/modernizr.js"></script>

<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet" href="import/bootstrap/css/bootstrap.css">

<link rel="stylesheet" href="import/datatables/dataTables.bootstrap.css">

<!-- Font Awesome -->
<link rel="stylesheet" href="import/custom_icon/style.css">
<!-- 
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
     -->
<link rel="stylesheet" href="import/font-awesome/css/font-awesome.css">
<!-- Ionicons -->
<!-- 
    <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
     -->


<!-- Theme style -->
<link rel="stylesheet" href="import/dist/css/AdminLTE.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="import/dist/css/skins.css">


<!-- jQuery 2.1.4 -->
<script src="import/jQuery/jQuery-2.1.4.min.js"></script>


<!-- Bootstrap 3.3.5 -->
<script src="import/bootstrap/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="import/fastclick/fastclick.min.js"></script>
<!-- AdminLTE App -->
<script src="import/dist/js/app.min.js"></script>
<!-- SlimScroll 1.3.0 -->
<script src="import/slimScroll/jquery.slimscroll.min.js"></script>

<!-- Dual List box -->
<link rel="stylesheet" type="text/css"
	href="import/duallistbox/bootstrap-duallistbox.css">
<script src="import/duallistbox/jquery.bootstrap-duallistbox.js"></script>

<script type="text/javascript"
	src="import/bootstrap/js/bootstrap-filestyle.js">
	
</script>

</head>

<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">

		<div id="load_screen">
			<!-- <div id="loading"></div>-->
			<div class="pageLoadingAnimation"></div>
		</div>

		<jsp:include page="navigation.jsp" />

		<!-- content -->
		<jsp:include page="${content}" />
		<jsp:include page="footer.jsp" />
	</div>

</body>

<script>
	$(".well").mouseenter(function() {
		$(this).css('background-color', '#f5f5f5');
	});

	$(".well").mouseleave(function() {
		$(this).css('background-color', '#ffffff');
	});

	$(document).ready(function() {

		hideLoading();

	});

	function showLoading() {
		$("#load_screen").show();
	}

	function hideLoading() {
		$("#load_screen").hide();
	}

	$(".well").click(function() {
		showLoading();
	});
</script>

</html>