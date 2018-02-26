<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<style>
.iconStyle {
	min-height: 150px;
	display: flex;
	align-items: center;
	text-align: center;
}
</style>


<jsp:include page="../common/left.jsp" />
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<%--  <section class="content-header">
          <ol class="breadcrumb">
            <li class="active"><a href="#"><i class="icon-icon_about_b"></i> <spring:message code="menu.about"/></a></li>
          </ol>
        </section> --%>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<div class="col-lg-12">
				<h1 style="color: #3a9cff">
					<spring:message code="about.company.name" />
				</h1>
			</div>
			<div align="justify" class="col-lg-12">
				<spring:message code="about.description.1" />
				<br/>
				<h3>
					<spring:message code="about.description.2" />
				</h3>
				<spring:message code="about.description.3" />
				<br/>
			</div>
			<div style="text-align: center; margin-top:40px;" class="col-lg-4">
				<div class="row ">
					<img width="180px" src="import/img2/logo_ebsl.png" alt=""><br />
					<label class="aboutText" style="margin-top:16px;"><spring:message
							code="about.company.name" /></label>
				</div>
				<div class="row ">
					<label class="aboutText"><spring:message
							code="about.address.line.1" /></label>
				</div>
				<div class="row ">
					<label class="aboutText"><spring:message
							code="about.address.line.2" /></label>
				</div>
			</div>
			<div class=" inline col-lg-4 col-md-6 col-sm-12 iconStyle">
				<img class="" src="import/img2/about/icon_about_tel.png" alt="">
				<label class="aboutText"><spring:message code="about.tel" /></label>
			</div>

			<div class="inline col-lg-4 col-md-6 col-sm-12 iconStyle">

				<img class="" src="import/img2/about/icon_about_email.png" alt="">
				<label class="aboutText"><spring:message code="about.email" /></label>

			</div>

			<div class="inline col-lg-4 col-md-6 col-sm-12 iconStyle">

				<img class="" src="import/img2/about/icon_about_fax.png" alt="">
				<label class="aboutText"><spring:message code="about.fax" /></label>

			</div>

			<div class="inline col-lg-4 col-md-6 col-sm-12 iconStyle">

				<img class="" src="import/img2/about/icon_about_web.png" alt="">
				<label class="aboutText"><spring:message
						code="about.website" /></label>

			</div>

		</div>

	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script>
	menu_select("sub_about");
</script>
