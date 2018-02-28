
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script language="Javascript">
	window.onload = function() {
		document.getElementsByName('j_username')[0].focus();
	}
</script>



<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper" style="margin-left: 0px;">
	<!-- Content Header (Page header) -->
	<%-- <section class="content-header">
		<ol class="breadcrumb">
			<li class="active"><a href="Login.do"><i
					class="fa fa-sign-in"></i> <spring:message code="login.login" /></a></li>
		</ol>
	</section> --%>
	<!-- Main content -->
	<section class="content">
		<div class="container">
			<div class="row">
				<div class="col-lg-6 col-lg-offset-3">
					<div class="panel panel-default">
						<div class="panel-body">
							<form accept-charset="UTF-8" role="form"
								action="j_spring_security_check" method="post">
								<fieldset>
									<div class="row panel-heading">
										<h3 class="panel-title text-center">
											<br /> <img src="import/img2/logo_mfms_m.png" alt=""><br />
											<br />
											<spring:message code="login.please.sign.in" />
										</h3>
									</div>
									<div class="row">
										<label for="" class="col-sm-3 col-xs-5 control-label">
											<spring:message code="login.username" />
										</label> <input class="col-sm-9 col-xs-7" maxlength="25"
											placeholder="<spring:message code="login.username"/>"
											name="j_username" type="text" value="">
									</div>
									<div class="row">
										<label for="" class="col-sm-3 col-xs-5 control-label">
											<spring:message code="login.password" />
										</label> <input class="col-sm-9 col-xs-7" maxlength="25"
											placeholder="<spring:message code="login.password"/>"
											name="j_password" type="password" value="">
									</div>
									<div class="form-group">
										<!--
										Current Locale : ${pageContext.response.locale}
										 -->
										<c:if test="${param.error == '1'}">
											<font color="red"> <spring:message
													code="login.badCredentials" />
											</font>
										</c:if>
										<c:if test="${param.error == '2'}">
											<font color="red"> <spring:message
													code="login.accountLocked" />
											</font>
										</c:if>
										<c:if test="${param.error == '3'}">
											<font color="red"> <spring:message
													code="login.accountDisabled" />
											</font>
										</c:if>

										<c:if test="${param.error == '4'}">
											<font color="red"> <spring:message
													code="login.attemptsExceeded" />
											</font>
										</c:if>

									</div>
									<div class="row">
										<div class="col-xs-3"></div>
										<div class="col-xs-9" style="padding: 0px">
											<div class="form-group">
												<div class="col-xs-6"
													style="padding-left: 0px; margin-left: -5px">
													<input onclick="showLoading()"
														class="btn btn-lg btn-success btn-block" type="submit"
														value="<spring:message code="login.login"/>">
												</div>
												<div class="col-xs-6" style="padding-right: 0px">
													<a onclick="showLoading()" href="Login.do"
														class="btn btn-lg btn-primary btn-block"><spring:message
															code="button.clear" /></a>
												</div>
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-lg-12">
											<a href="ForgetPassword.do"><spring:message
													code="login.forgot.password" /></a>
										</div>
									</div>
								</fieldset>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->



