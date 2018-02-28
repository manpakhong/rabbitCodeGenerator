<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<c:set var="company">
	<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
</c:set>

<c:choose>
	<c:when test="${roleForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:set var="submitUrl" value="ModifyRole.do" />
		<c:set var="disabled" value="disabled" />
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="disabled" value="" />
		<c:choose>
			<c:when test="${empty roleForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyRole.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyRole.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
<spring:eval expression="@propertyConfigurer['site.admin.role.name']"
	var="defaultSiteAdminRoleName" />

<c:set var="isSiteAdminRole"
	value="${defaultSiteAdminRoleName == roleForm.name }" />

<script>

function recalculateParentCB(privilegeCatCode) {
	//console.log( "recalculateParentCB" );
	var chkboxes = $('#'+privilegeCatCode+'_div').find('input[type=checkbox]');
	//console.log( chkboxes );
	var numCheck = 0;
	var numUncheck = 0;
	chkboxes.each(function(index) {
		//console.log( index + ": " + this.value + "," + this.checked);
		if (this.checked) numCheck++;
		else numUncheck++
	});
	var parentCB = $('#'+privilegeCatCode+'_cb');
	//console.log( numCheck );
	//console.log( numUncheck );
	//console.log( parentCB );
	if (numCheck > 0) {
		if (numUncheck == 0) {
			parentCB.prop("checked", true);
			parentCB.prop("indeterminate", false);
		} else {
			parentCB.prop("indeterminate", true);
		}
	} else {
		parentCB.prop("checked", false);
		parentCB.prop("indeterminate", false);
	}
}

function recalculateAllParentCB() {
	<c:forEach items="${roleForm.privilegeCategoryList}" var="privilegeCategory">
	recalculateParentCB("${privilegeCategory.code}");
	</c:forEach>
}

</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="AccountManagement.do"><i
					class="icon-icon_account_management_b"></i> <spring:message
						code="menu.accountMgt" /></a></li>
			<li><a href="Role.do"> <spring:message
						code="menu.accountMgt.role" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<li class="active"><spring:message
							code="menu.accountMgt.role.view" /></li>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.accountMgt.role.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.accountMgt.role.modify" /></li>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form cssClass="form-horizontal" commandName="roleForm"
						method="post" action="${submitUrl}">

						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>

						</spring:bind>
						<c:if test="${roleForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="role.create.success" />
							</div>
						</c:if>
						<c:if test="${roleForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="role.modify.success" />
							</div>
						</c:if>

						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<div class="box-body">
							<div
								class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="role.name" />*</label>
								<div class="col-sm-5">

									<spring:message code="role.name" var="name" />

									<form:input path="name" cssClass="form-control"
										placeholder="${name}"
										readonly="${readOnly or isSiteAdminRole}" />
								</div>
							</div>
							<div
								class="form-group  <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="role.description" /></label>
								<div class="col-sm-10">

									<spring:message code="role.description" var="description" />

									<form:textarea path="desc" cssClass="form-control"
										placeholder="${description}" readonly="${readOnly}" />
								</div>
							</div>
							<div
								class="form-group  <spring:bind path="modeKey"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-sm-2 control-label"><spring:message
										code="role.status.access.mode" /></label>
								<div class="col-sm-10">
									<form:select path="modeKey" cssClass="form-control"
										disabled="${readOnly}">
										<!--<form:option value="-1"><spring:message code="common.pleaseSelect"/></form:option>-->
										<form:options items="${roleForm.modeList}" itemLabel="name"
											itemValue="modeKey" />
									</form:select>
								</div>
							</div>

							<div class="col-lg-2 ">
							<spring:eval
								expression="@privilegeMap.getProperty('privilege.code.assignAccount')"
								var="assignAccount" />
							<c:if
								test="${currRole.grantedPrivilegeCodeList.contains(assignAccount)}">
								<c:if test="${readOnly == false && isCreate == false}">
									<a onclick="showLoading()" class="btn btn-primary "
										href="AssignRole.do?key=${roleForm.key}"><spring:message
											code="button.account.assign" /></a>
								<!-- 
									<a onclick="showLoading()" class="btn btn-primary "
										href="AssignRole.do?key=${roleForm.key}&r=${roleForm.referrer}"><spring:message
											code="button.account.assign" /></a>
								 -->
								</c:if>
							</c:if>
							</div>
							<div class="form-group col-sm-12">
								<c:if test="${readOnly == false}">
								<button id="checkAll" class="btn btn-primary " type="button"><spring:message
											code="button.checkAll" /></button>
								<button id="uncheckAll" class="btn btn-primary " type="button"><spring:message
											code="button.uncheckAll" /></button>
								</c:if>

								<c:forEach items="${roleForm.privilegeCategoryList}"
									var="privilegeCategory">
									<!-- create box for each category -->
									<div class="box box-solid box-default">
										<div class="box-header with-border">
											<h3 class="box-title checkbox">
												<label> <input id="${privilegeCategory.code}_cb"
													type="checkbox" value=""
													onClick="$('#${privilegeCategory.code}_div').find('input[type=checkbox]').prop('checked', this.checked);"
													${disabled} /> <spring:message
														code="privilegeCategory.code.${privilegeCategory.code}" />
												</label>
											</h3>
										</div>
										<!-- /.box-header -->
										<div class="box-body" id="${privilegeCategory.code}_div">
											<!-- create checkbox for each privilege -->
											<c:forEach items="${privilegeCategory.privilegeList}"
												var="privilege">
												<div class="form-group col-sm-4 ">
													<div class="checkbox">
														<label> <!--<form:checkbox path="grantedPrivilegeList" value="${privilege.code}" onClick="recalculateParentCB('${privilegeCategory.code}')" disabled="${readOnly}"/><c:out value="${privilege.name}"/>-->
								<c:choose>
									<c:when test="${privilege.code=='AlarmConsole'}">
										<c:choose>
											<c:when test="${company=='jec'}">
															<form:checkbox path="grantedPrivilegeList"
																value="${privilege.code}"
																onClick="recalculateParentCB('${privilegeCategory.code}')"
																disabled="${readOnly}" /> <spring:message
																code="privilege.code.${privilege.code}.jec" />
											</c:when>
											<c:otherwise>
															<form:checkbox path="grantedPrivilegeList"
																value="${privilege.code}"
																onClick="recalculateParentCB('${privilegeCategory.code}')"
																disabled="${readOnly}" /> <spring:message
																code="privilege.code.${privilege.code}" />
											</c:otherwise>
										</c:choose>
     								</c:when>
     								<c:otherwise>
															<form:checkbox path="grantedPrivilegeList"
																value="${privilege.code}"
																onClick="recalculateParentCB('${privilegeCategory.code}')"
																disabled="${readOnly}" /> <spring:message
																code="privilege.code.${privilege.code}" />
     								</c:otherwise>
     							</c:choose>
														</label>
													</div>
												</div>
											</c:forEach>
										</div>
										<!-- /.box-body -->
									</div>
									<!-- /.box -->
								</c:forEach>
							</div>



							<div class="box-footer">
								<div class="col-xs-2 col-lg-1 ">
									<c:if test="${readOnly == false && isCreate == false}">
										<!-- back button for modify -->
										<c:choose>
											<c:when test="${roleForm.referrer == 's'}">
												<a onclick="showLoading()" class="btn btn-primary "
													href="DoSearchRole.do"><spring:message
														code="button.back.to.search" /></a>
											</c:when>
											<c:otherwise>
												<!-- this view page will go to search -->
												<a onclick="showLoading()" class="btn btn-primary "
													href="ViewRole.do?key=${roleForm.key}&r=s"><spring:message
														code="button.back" /></a>
											</c:otherwise>
										</c:choose>

									</c:if>
									<c:if test="${readOnly == true}">
										<!-- back button for view -->
										<a onclick="showLoading()" class="btn btn-primary "
											href="DoSearchRole.do"><spring:message
												code="button.back.to.search" /></a>
									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<c:if test="${readOnly == false}">
										<button onclick="showLoading()" type="submit"
											class="btn btn-primary ">
											<spring:message code="button.submit" />
										</button>
									</c:if>
								</div>
								<div class="col-xs-2 col-lg-1 ">
									<spring:eval
										expression="@privilegeMap.getProperty('privilege.code.modifyAccountRole')"
										var="modifyAccountRole" />
									<c:if
										test="${currRole.grantedPrivilegeCodeList.contains(modifyAccountRole)}">
									<c:if test="${readOnly == true}">
									<c:choose>
										<c:when test="${!isSiteAdminRole}">
										<a onclick="showLoading()"
											href="${submitUrl}?key=${roleForm.key}&r=v"
											class="btn btn-primary "><spring:message
												code="button.modify" /></a>
										</c:when>
										<c:otherwise>
											<c:if test="${roleForm.isSiteAdmin=='Y'}">
											<a onclick="showLoading()"
												href="${submitUrl}?key=${roleForm.key}&r=v"
												class="btn btn-primary "><spring:message
													code="button.modify" /></a>
											</c:if>
										</c:otherwise>
									</c:choose>
									</c:if>
									</c:if>
									<!--  reset button -->
									<c:if test="${readOnly == false}">
										<c:if test="${isCreate == true}">
											<a onclick="showLoading()" href="CreateRole.do"
												class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
										<c:if test="${isCreate == false}">
											<a onclick="showLoading()"
												href="ModifyRole.do?key=${roleForm.key}&r=${roleForm.referrer}"
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
  menu_toggle("sub_am");
  menu_toggle("sub_am_role");
  recalculateAllParentCB();
  
	if(${isCreate}){
		menu_select("createRole");
	} else 
		menu_select("searchRole");
    
	
	$( "#checkAll" ).click(function() {
		$('input[type=checkbox]').each(function(){ 
	        this.checked = true; 
	    });
	});

	$( "#uncheckAll" ).click(function() {
	    $('input[type=checkbox]').each(function(){ 
	        this.checked = false; 
	    });
	});
	
</script>
