<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="../common/left.jsp" />


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="Sysadmin.do"><i
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.systemMgt" /></a></li>
			<li class="active"><spring:message code="menu.systemMgt.site" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-lg-4  col-sm-6 iconStyle">
				<a href="SearchSite.do">
					<div class=" well">
						<img class="img-responsive"
							src="import/img2/system/site_search.png" alt="">
						<h2 class="iconName">
							<spring:message code="menu.systemMgt.site.search" />
						</h2>
					</div>
				</a>
			</div>
			<div class="col-lg-4  col-sm-6 iconStyle">
				<a href="CreateSite.do">
					<div class=" well">
						<img class="img-responsive"
							src="import/img2/system/site_create.png" alt="">
						<h2 class="iconName">
							<spring:message code="menu.systemMgt.site.create" />
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
	menu_toggle("sub_system");
	menu_toggle("sub_system_site", 1);
</script>
