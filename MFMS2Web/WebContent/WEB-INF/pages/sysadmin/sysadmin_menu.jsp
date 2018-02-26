<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="../common/left.jsp" />


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><i class="icon-icon_administration_b"></i> <spring:message
					code="menu.systemMgt" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-lg-4  col-sm-6 iconStyle">
				<a href="Site.do">
					<div class=" well">
						<img class="img-responsive" src="import/img2/system/site.png"
							alt="">
						<h2 class="iconName">
							<spring:message code="menu.systemMgt.site" />
						</h2>
					</div>
				</a>
			</div>
		</div>
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script>
	menu_toggle("sub_system", 1);
</script>
