<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="content-wrapper" style="margin-left: 0px;">
	<!-- Content Header (Page header) -->

	<section class="content">
		<!-- Main content -->
		<section class="content">

			<div class="container">

				<div class="row">

					<section class="content-header"
						style="width: 101%; margin-bottom: 17px;">
						<ol class="breadcrumb">
							<li class="active"><spring:message
									code="login.forgot.password.header" /></li>
						</ol>
					</section>

					<div class="col-xs-12">
						<!-- Horizontal Form -->
						<div class="box">
							<!-- form start -->
							<form:form cssClass="form-horizontal"
								commandName="forgetPasswordForm" method="post"
								action="DoForgetPassword.do">
								<!-- validation error messages -->
								<spring:bind path="*">
									<c:if test="${status.error}">
										<div class="form-group alert alert-danger">
											<form:errors path="*" />
										</div>
									</c:if>
								</spring:bind>
								<c:if test="${forgetPasswordForm.referrer == 'c'}">
									<div class="form-group alert alert-success">
										<spring:message code="email.sent" />
									</div>
								</c:if>
								<div class="box-body">
									<div
										class="form-group <spring:bind path="loginId"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="login.username" />*</label>
										<div class="col-sm-10">
											<spring:message code="login.username" var="username" />
											<form:input path="loginId" cssClass="form-control"
												placeholder="${username}" />
										</div>
									</div>
									<div
										class="form-group <spring:bind path="email"><c:if test="${status.error}">has-error</c:if></spring:bind>">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="account.email" />*</label>
										<div class="col-sm-10">
											<spring:message code="account.email" var="email" />
											<form:input path="email" cssClass="form-control"
												placeholder="${email}" />
										</div>
									</div>
									<div class="box-footer">
										<div class="col-lg-1 col-xs-2">
											<a onclick="showLoading()" class="btn btn-primary " href="Login.do"><spring:message
													code="button.back" /></a>
										</div>
										<div
											class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
											<button onclick="showLoading()" type="submit" class="btn btn-primary ">
												<spring:message code="button.submit" />
											</button>
										</div>
										<div class="col-xs-2 col-lg-1 ">
											<a onclick="showLoading()" href="ForgetPassword.do" class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</div>
									</div>
									<!-- /.box-footer -->
								</div>
								<!-- /.box-body -->
							</form:form>
						</div>
						<!-- /.box -->
					</div>
					<!-- /.row -->
				</div>
			</div>
		</section>
		<!-- /.content -->
</div>