<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<c:choose>
	<c:when test="${siteForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${siteForm.delete == true}">
			<c:set var="submitUrl" value="DoDeleteSite.do" />
		</c:if>
		<c:if test="${siteForm.delete == false}">
			<c:set var="submitUrl" value="ModifySite.do" />
		</c:if>
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:choose>
			<c:when test="${empty siteForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifySite.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifySite.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${siteForm.createDefaultAdmin}">
		<c:set var="createDefaultAdminStyle" value="display:block;" />
	</c:when>
	<c:otherwise>
		<c:set var="createDefaultAdminStyle" value="display:none;" />
	</c:otherwise>
</c:choose>


<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="Sysadmin.do"><i
					class="icon-icon_administration_b"></i> <spring:message
						code="menu.systemMgt" /></a></li>
			<li><a href="Site.do"> <spring:message
						code="menu.systemMgt.site" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<c:choose>
						<c:when test="${siteForm.delete}">
							<li class="active">Delete Site</li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.systemMgt.site.view" /></li>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.systemMgt.site.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.systemMgt.site.modify" /></li>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">
	
			<div class="row">
				<div class="col-lg-12">
					<!-- Horizontal Form -->
					<div class="box">
						<!-- form start -->
						<form:form cssClass="form-horizontal" commandName="siteForm"
							method="post" action="${submitUrl}">

							<!-- validation error messages -->
							<spring:bind path="*">
								<c:if test="${status.error}">
									<div class="form-group alert alert-danger">
										<form:errors path="*" />
									</div>
								</c:if>

							</spring:bind>
							<c:if test="${siteForm.referrer == 'c'}">
								<div class="form-group alert alert-success">
									<spring:message code="site.create.success" />
								</div>
							</c:if>
							<c:if test="${siteForm.referrer == 'm'}">
								<div class="form-group alert alert-success">
									<spring:message code="site.modify.success" />
								</div>
							</c:if>
							<form:hidden path="key" />
							<div class="box-body">
								<div
									class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="site.name" />*</label>
									<div class="col-sm-6">
										<spring:message code="site.name" var="name"/>
										<form:input path="name" cssClass="form-control" placeholder="${name}"
											readonly="${readOnly}" />
									</div>
								</div>
								<div
									class="form-group <spring:bind path="contactNumber"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="site.contact.number" />*</label>
									<div class="col-sm-3 ">
										<div class="input-group">
										
											<spring:message code="site.countryCode" var="countryCode"/>
											<spring:message code="site.areaCode" var="areaCode"/>
											<spring:message code="site.contactNumber" var="contactNumber"/>
										
											<form:input path="contactCountryCode" cssClass="form-control" placeholder="${countryCode}"
												maxlength="3"
												onkeypress='return event.charCode >= 48 && event.charCode <= 57  '
												readonly="${readOnly}" />
											<span class="input-group-addon" style="border: none">-</span>
											<form:input path="contactAreaCode" cssClass="form-control" placeholder="${areaCode}"
												maxlength="3"
												onkeypress='return event.charCode >= 48 && event.charCode <= 57  '
												readonly="${readOnly}" />
											<span class="input-group-addon" style="border: none">-</span>
										</div>
									</div>
									<div class="col-sm-3 ">
										<form:input path="contactNumber" cssClass="form-control" placeholder="${contactNumber}"
											maxlength="11"
											onkeypress='return event.charCode >= 48 && event.charCode <= 57  '
											readonly="${readOnly}" />
									</div>
								</div>
								<div
									class="form-group <spring:bind path="address"><c:if test="${status.error}">has-error</c:if></spring:bind>">
									<label for="" class="col-sm-2 control-label"><spring:message
											code="site.address" />*</label>
									<div class="col-sm-10">
									
									<spring:message code="site.address" var="address"/>
									
										<form:textarea path="address" cssClass="form-control" placeholder="${address}" 
											readonly="${readOnly}" />
									</div>
								</div>
								<c:if test="${readOnly }">
									<div class="form-group ">
										<label for="" class="col-sm-2 control-label"><spring:message
												code="site.admin" /></label>
										<div class="col-sm-10">
											<ul class="list-group"
												style="max-height: 90%; overflow-y: auto;">
												<c:forEach items="${siteForm.adminAccountList}"
													var="adminAccount">
													<li class="list-group-item"><c:out
															value="${adminAccount.loginId }" /></li>
												</c:forEach>
											</ul>
										</div>
									</div>
								</c:if>
								<c:if test="${isCreate}">
									<br />
									<!-- Create default site admin -->
									<div class="box box-solid box-default">
										<div class="box-header with-border">
											<h3 class="box-title checkbox">
												<label> <spring:message
														code="site.create.default.admin" />
												</label>
											</h3>
										</div>
										<!-- /.box-header -->
										<div class="box-body" id="createAdminBody"
											style="${createDefaultAdminStyle}">
											<div
												class="form-group col-sm-6 <spring:bind path="defaultAdminId"><c:if test="${status.error}">has-error</c:if></spring:bind>">
												<label for="" class="col-sm-4 control-label"><spring:message
														code="account.username" />*</label>
												<div class="col-sm-8">
												
												<spring:message code="account.username" var="username"/>
												
													<form:input path="defaultAdminId" cssClass="form-control" placeholder="${username}"
														readonly="${readOnly}" />
												</div>
											</div>
											<div
												class="form-group col-sm-6 <spring:bind path="defaultAdminName"><c:if test="${status.error}">has-error</c:if></spring:bind>">
												<label for="" class="col-sm-4 control-label"><spring:message
														code="account.name" />*</label>
												<div class="col-sm-8">
												
													<spring:message code="account.name" var="name"/>
												
													<form:input path="defaultAdminName" cssClass="form-control" placeholder="${name}"
														readonly="${readOnly}" />
												</div>
											</div>
											<div
												class="form-group col-sm-6 <spring:bind path="defaultAdminPass"><c:if test="${status.error}">has-error</c:if></spring:bind>">
												<label for="" class="col-sm-4 control-label"><spring:message
														code="account.password" />*</label>
												<div class="col-sm-8">
												
												 	<spring:message code="account.password" var="password"/> 
												
													<form:input path="defaultAdminPass" cssClass="form-control" placeholder="${password}"
														readonly="${readOnly}" type="password" />
												</div>
											</div>
											<div
												class="form-group col-sm-6 <spring:bind path="defaultAdminConfirmPass"><c:if test="${status.error}">has-error</c:if></spring:bind>">
												<label for="" class="col-sm-4 control-label"><spring:message
														code="account.confirm.password" />*</label>
												<div class="col-sm-8">
													<spring:message code="account.confirm.password" var="confirm"/> 
												
													<form:input path="defaultAdminConfirmPass"
														cssClass="form-control" readonly="${readOnly}" placeholder="${confirm}"
														type="password" />
												</div>
											</div>
										</div>
										<!-- /.box-body -->
									</div>
									<!-- /.box -->
								</c:if>
								<div class="box-footer">

									<div class="col-lg-1 col-xs-2">
										<c:if test="${readOnly == false && isCreate == false}">
											<!-- back button for modify -->
											<c:choose>
												<c:when test="${siteForm.referrer == 's'}">
													<a onclick="showLoading()" class="btn btn-primary " href="DoSearchSite.do"><spring:message
															code="button.back.to.search" /></a>
												</c:when>
												<c:otherwise>
													<!-- this view page will go to search -->
													<a onclick="showLoading()" class="btn btn-primary "
														href="ViewSite.do?key=${siteForm.key}&r=s"><spring:message
															code="button.back" /></a>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:if test="${readOnly == true}">
											<!-- back button for view -->
											<a onclick="showLoading()" class="btn btn-primary " href="DoSearchSite.do"><spring:message
													code="button.back.to.search" /></a>
										</c:if>
									</div>
									<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9">
										<c:if test="${readOnly == false}">
											<button onclick="showLoading()" type="submit" class="btn btn-primary ">
												<spring:message code="button.submit" />
											</button>
										</c:if>
									</div>
									<div class="col-lg-1 col-xs-2 ">
										<c:if test="${readOnly == true}">
											<c:if test="${siteForm.delete == false}">
												<button onclick="showLoading()" type="submit" class="btn btn-primary ">
													<spring:message code="button.modify" />
												</button>
											</c:if>
										</c:if>
										<!--  reset button -->
										<c:if test="${readOnly == false}">
											<c:if test="${isCreate == true}">
												<a onclick="showLoading()" href="CreateSite.do" class="btn btn-primary "><spring:message
														code="button.reset" /></a>
											</c:if>
											<c:if test="${isCreate == false}">
												<a onclick="showLoading()"
													href="ModifySite.do?key=${siteForm.key}&r=${siteForm.referrer}"
													class="btn btn-primary "><spring:message
														code="button.reset" /></a>
											</c:if>
										</c:if>
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
	
	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script>
	menu_toggle("sub_system");
	menu_toggle("sub_system_site");
	if(${isCreate}){
		menu_select("createSite");
	} else 
		menu_select("searchSite");
</script>


