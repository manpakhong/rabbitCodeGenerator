<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<link rel="stylesheet" type="text/css"
	href="import/duallistbox/bootstrap-duallistbox.css">
<script src="import/duallistbox/jquery.bootstrap-duallistbox.js"></script>
<jsp:include page="../common/left.jsp" />

<c:choose>
	<c:when test="${accountGroupForm.readOnly}">
		<c:set var="readOnly" value="true" />
		<c:set var="agr" value="" />
		<c:set var="aga" value="" />
		<c:set var="isCreate" value="false" />
		<c:if test="${accountGroupForm.delete == true}">
			<c:set var="submitUrl" value="DoDeleteAccountGroup.do" />
		</c:if>
		<c:if test="${accountGroupForm.delete == false}">
			<c:set var="submitUrl" value="ModifyAccountGroup.do" />
		</c:if>
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="agr" value="userAccountBox1" />
		<c:set var="aga" value="userAccountBox2" />
		<c:choose>
			<c:when test="${empty accountGroupForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyAccountGroup.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyAccountGroup.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.createAccountGroup')"
	var="createAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.searchAccountGroup')"
	var="searchAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.modifyAccountGroup')"
	var="modifyAccountGroup" />
<spring:eval
	expression="@privilegeMap.getProperty('privilege.code.removeAccountGroup')"
	var="removeAccountGroup" />

<script>

// available responsible account
var origAvailRespAcc = [];
// selected responsible account
var origSelectedRespAcc = [];

// available member account
var origAvailMemAcc = [];
// selected member account
var origSelectedMemAcc = [];

<c:forEach var="userAccount" items="${accountGroupForm.userAccountList}">
	<c:choose>
		<c:when test="${accountGroupForm.selectedResponsibleAccountKeyList.contains(userAccount.key)}">
			origSelectedRespAcc.push({key:${userAccount.key}, loginId:'${userAccount.loginId}'});
		</c:when>
		<c:otherwise>
			origAvailRespAcc.push({key:${userAccount.key}, loginId:'${userAccount.loginId}'});
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${accountGroupForm.selectedAccountKeyList.contains(userAccount.key)}">
			origSelectedMemAcc.push({key:${userAccount.key}, loginId:'${userAccount.loginId}'});
		</c:when>
		<c:otherwise>
			origAvailMemAcc.push({key:${userAccount.key}, loginId:'${userAccount.loginId}'});
		</c:otherwise>
	</c:choose>
</c:forEach>


var newAvailRespAcc = origAvailRespAcc.slice();
var newSelectedRespAcc = origSelectedRespAcc.slice();

var newAvailMemAcc = origAvailRespAcc.slice();
var newSelectedMemAcc = origSelectedMemAcc.slice();

function createAvailableItem(key, userId, type) {
	
	// type: mem / resp
	
	var newItem = document.createElement('li');
	newItem.className = 'list-group-item';
	var addBtn = document.createElement('span');
	addBtn.className = 'fa fa-plus-circle pull-right';
	addBtn.setAttribute('style', 'color:#5cb85c;cursor:pointer');
	
	if (type == 'resp')
		addBtn.setAttribute('onclick', 'moveToSelected("resp", this.parentNode);');
	else if (type == 'mem')
		addBtn.setAttribute('onclick', 'moveToSelected("mem", this.parentNode);');
	
	newItem.appendChild(addBtn);
	
	newItem.value = key;
	newItem.appendChild(document.createTextNode(userId));
	
	return newItem;
}

<c:if test="${not readOnly}">

function moveToAvailable(type, item) {
	
	var availableList;
	var selectedList;
	var newAvailAccList;
	var newSelectedAccList;
	
	if (type == 'resp') {
		availableList = document.getElementById('availableRespList');
		
		selectedList = document.getElementById('selectedRespList');
		
		newAvailAccList = newAvailRespAcc;
		
		newSelectedAccList = newSelectedRespAcc;
		
	} else if (type == 'mem') {
		availableList = document.getElementById('availableMemList');
		
		selectedList = document.getElementById('selectedMemList');
		
		newAvailAccList = newAvailMemAcc;
		
		newSelectedAccList = newSelectedMemAcc;
	}
	
	var userId = item.textContent.trim();
	
	var key = item.value;
	
	var newItem = createAvailableItem(key, userId, type);
	
	availableList.appendChild(newItem);
	
	var acc = {key: key, loginId: userId};
	
	newAvailAccList.push(acc);
	
	selectedList.removeChild(item);
	
	var index = -1;
	
	for (var i = 0; i < newSelectedAccList.length; i++) {
		if (key == newSelectedAccList[i].key) {
			index = i;
			break;
		}
	}
	
	if (index >= 0) {
		newSelectedAccList.splice(index, 1);
	}
	
}

function moveAllToAvailable(type) {
	
	var selectedList;
	
	if (type == 'resp') {
		
		selectedList = document.getElementById('selectedRespList');
		
	} else if (type == 'mem') {
		
		selectedList = document.getElementById('selectedMemList');
		
	}
	
	var list = selectedList.getElementsByTagName("li");
	
	while (list.length > 0) {  
		
		moveToAvailable(type, list[0]);
		
	}
}

</c:if>

function createSelectedItem(key, userId, type) {
	
	// type: mem / resp
	
	var newItem = document.createElement('li');
	newItem.className = 'list-group-item';
	
	<c:if test="${not readOnly}">
	var removeBtn = document.createElement('span');
	removeBtn.className = 'fa fa-times-circle pull-right';
	removeBtn.setAttribute('style', 'color:red;cursor:pointer');
	
	if (type == 'resp')
		removeBtn.setAttribute('onclick', 'moveToAvailable("resp", this.parentNode);');
	else if (type == 'mem')
		removeBtn.setAttribute('onclick', 'moveToAvailable("mem", this.parentNode);');
	
	newItem.appendChild(removeBtn);
	
	</c:if>
	
	newItem.value = key;
	newItem.appendChild(document.createTextNode(userId));
	
	return newItem;
}

<c:if test="${not readOnly}">

function moveToSelected(type, item) {
	
	var availableList;
	var selectedList;
	var newAvailAccList;
	var newSelectedAccList;
	
	if (type == 'resp') {
		availableList = document.getElementById('availableRespList');
		
		selectedList = document.getElementById('selectedRespList');
		
		newAvailAccList = newAvailRespAcc;
		
		newSelectedAccList = newSelectedRespAcc;
		
	} else if (type == 'mem') {
		availableList = document.getElementById('availableMemList');
		
		selectedList = document.getElementById('selectedMemList');
		
		newAvailAccList = newAvailMemAcc;
		
		newSelectedAccList = newSelectedMemAcc;
	}
	
	
	
	var userId = item.textContent.trim();
	
	var key = item.value;
	
	var newItem = createSelectedItem(key, userId, type);
	
	selectedList.appendChild(newItem);
	
	var acc = {key: key, loginId: userId};
	
	newSelectedAccList.push(acc);
	
	availableList.removeChild(item);
	
	var index = -1;
	
	for (var i = 0; i < newAvailAccList.length; i++) {
		if (key == newAvailAccList[i].key) {
			index = i;
			break;
		}
	}
	
	if (index >= 0) {
		newAvailAccList.splice(index, 1);
	}
	
}

function moveAllToSelected(type) {
	
	var availableList;
	
	if (type == 'resp') {
		
		availableList = document.getElementById('availableRespList');
		
	} else if (type == 'mem') {
		
		availableList = document.getElementById('availableMemList');
		
	}
	
	var list = availableList.getElementsByTagName("li");
	
	while (list.length > 0) {  
		
		moveToSelected(type, list[0]);
		
	}
}

</c:if>

function submitAccountGroupForm() {
	
	
	showLoading();
	
	var accountGroupForm = document.getElementById('accountGroupForm');
	
	for (var i = 0; i < newSelectedRespAcc.length; i++) {
		var input = document.createElement('input');
		input.setAttribute('name', 'selectedResponsibleAccountKeyList');
		input.setAttribute('type', 'hidden');
		input.value = newSelectedRespAcc[i].key;
		
		accountGroupForm.appendChild(input);
		//alert(input.innerHTML);
	}
	
	for (var i = 0; i < newSelectedMemAcc.length; i++) {
		var input = document.createElement('input');
		input.setAttribute('name', 'selectedAccountKeyList');
		input.setAttribute('type', 'hidden');
		input.value = newSelectedMemAcc[i].key;
		
		accountGroupForm.appendChild(input);
		//alert(input.innerHTML);
	}
	// submit referrer if in view mode
	<c:if test="${readOnly}">
	var input = document.createElement('input');
	input.setAttribute('name', 'r');
	input.setAttribute('type', 'hidden');
	input.value = 'v';	
	accountGroupForm.appendChild(input);
	</c:if>
	
	accountGroupForm.submit();
}

function resetAccountGroupForm() {
	
	var accountGroupForm = document.getElementById('accountGroupForm');
	accountGroupForm.reset();
	
	// reset list
	newAvailRespAcc = origAvailRespAcc.slice();
	newSelectedRespAcc = origSelectedRespAcc.slice();
	
	newAvailMemAcc = origAvailMemAcc.slice();
	newSelectedMemAcc = origSelectedMemAcc.slice();
	
	// select list
	var selectedRespList = document.getElementById('selectedRespList');
	var selectedMemList = document.getElementById('selectedMemList');
	
	// remove all
	while (selectedRespList.hasChildNodes()) {   
		selectedRespList.removeChild(selectedRespList.firstChild);
	}
	while (selectedMemList.hasChildNodes()) {   
		selectedMemList.removeChild(selectedMemList.firstChild);
	}
	// readd
	for (var i = 0; i < newSelectedRespAcc.length; i++) {
		
		
		var newItem = createSelectedItem(origSelectedRespAcc[i].key, origSelectedRespAcc[i].loginId, "resp");
		
		selectedRespList.appendChild(newItem);
	}
	
	for (var i = 0; i < newSelectedMemAcc.length; i++) {
		
		
		var newItem = createSelectedItem(origSelectedMemAcc[i].key, origSelectedMemAcc[i].loginId, "mem");
		
		selectedMemList.appendChild(newItem);
	}
	
	<c:if test="${not readOnly}">
	// avail list
	var availableRespList = document.getElementById('availableRespList');
	var availableMemList = document.getElementById('availableMemList');
	
	// remove all
	while (availableRespList.hasChildNodes()) {   
		availableRespList.removeChild(availableRespList.firstChild);
	}
	while (availableMemList.hasChildNodes()) {   
		availableMemList.removeChild(availableMemList.firstChild);
	}
	// readd
	for (var i = 0; i < newAvailRespAcc.length; i++) {
		
		var newItem = createAvailableItem(newAvailRespAcc[i].key, newAvailRespAcc[i].loginId, "resp");
		
		availableRespList.appendChild(newItem);
	}
	
	for (var i = 0; i < newAvailMemAcc.length; i++) {
		
		var newItem = createAvailableItem(newAvailMemAcc[i].key, newAvailMemAcc[i].loginId, "mem");
		
		availableMemList.appendChild(newItem);
	}
	</c:if>
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
			<li><a href="AccountGroup.do"> <spring:message
						code="menu.accountMgt.accountGroup" /></a></li>
			<c:choose>
				<c:when test="${readOnly}">
					<c:choose>
						<c:when test="${accountGroupForm.delete}">
							<li class="active"><spring:message code="search.remove" />
								<spring:message code="menu.accountMgt.accountGroup" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.accountMgt.accountGroup.view" /></li>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isCreate}">
							<li class="active"><spring:message
									code="menu.accountMgt.accountGroup.create" /></li>
						</c:when>
						<c:otherwise>
							<li class="active"><spring:message
									code="menu.accountMgt.accountGroup.modify" /></li>
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
					<form:form cssClass="form-horizontal"
						commandName="accountGroupForm" method="post"
						action="${submitUrl }">
						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>
						</spring:bind>

						<c:if test="${accountGroupForm.referrer == 'c'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.create.success" />
							</div>
						</c:if>
						<c:if test="${accountGroupForm.referrer == 'm'}">
							<div class="form-group alert alert-success">
								<spring:message code="defect.modify.success" />
							</div>
						</c:if>


						<form:hidden path="key" />
						<form:hidden path="siteKey" />
						<div class="box-body">
							<div
								class="form-group col-xs-12 <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-xs-2 control-label"><spring:message
										code="accountGroup.name" />*</label>
								<div class="col-xs-10">

									<spring:message code="accountGroup.name" var="name" />

									<form:input path="name" cssClass="form-control"
										placeholder="${name}" readonly="${readOnly or not isCreate}" />
								</div>
							</div>
							<div
								class="form-group col-xs-12 <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-xs-2 control-label"><spring:message
										code="defect.description" /></label>
								<div class="col-xs-10">

									<spring:message code="defect.description" var="description" />

									<form:textarea path="desc" placeholder="${description}"
										cssClass="form-control" readonly="${readOnly}" />
								</div>
							</div>
							<!-- Nav tabs -->
							<ul class="nav nav-tabs" role="tablist">
							  <li role="presentation" class="active" style="width: 50%;">
								<a href="#tab_responsible" aria-controls="current" role="tab"
									data-toggle="tab">
								  <label><spring:message code="account.account.responsible" />*</label>
								</a>
							  </li>
							  <li role="presentation" style="width: 50%;">
							    <a href="#tab_member" aria-controls="series" role="tab" data-toggle="tab"></label>
							      <label><spring:message code="account.account" />
							    </a>
							  </li>
							</ul>
							<!-- Tab panes -->
							<div class="tab-content">
								<div role="tabpanel" class="tab-pane active"
									id="tab_responsible">

									<div
										class="form-group col-xs-12 <spring:bind path="selectedResponsibleAccountKeyList"><c:if test="${status.error}">has-error</c:if></spring:bind>">

										<br />
										<div class="col-xs-12">
											<c:if test="${not readOnly}">
												<div class="col-xs-5">
													<div class="panel panel-default" style="height: 500px;">
														<div class="panel-heading">
															<label> <spring:message
																	code="role.availableUsers" />
															</label>
														</div>
														<ul class="list-group" id="availableRespList"
															style="max-height: 90%; overflow-y: auto;">


														</ul>
													</div>
												</div>
											</c:if>
											<c:if test="${not readOnly}">
												<div class="col-xs-2 " style="vertical-align: middle;">
													<button type="button" title="Add All"
														class="btn btn-default center-block"
														onclick="moveAllToSelected('resp')">
														<i class="fa fa-chevron-right"></i><i
															class="fa fa-chevron-right"></i>
													</button>
													<button type="button" title="Remove All"
														class="btn btn-default center-block"
														onclick="moveAllToAvailable('resp')">
														<i class="fa fa-chevron-left"></i><i
															class="fa fa-chevron-left"></i>
													</button>
												</div>
											</c:if>
											<div class="col-xs-5">
												<div class="panel panel-default" style="height: 500px;">
													<div class="panel-heading">
														<label> <spring:message code="role.assignedResponsibleUsers" /> 
														</label>
													</div>

													<ul class="list-group" id="selectedRespList"
														style="max-height: 90%; overflow-y: auto;">


													</ul>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div role="tabpanel" class="tab-pane" id="tab_member">
									<div class="form-group col-xs-12">

										<br />

										<div class="col-xs-12">
											<c:if test="${not readOnly}">
												<div class="col-xs-5">
													<div class="panel panel-default" style="height: 500px;">
														<div class="panel-heading">
															<label> <spring:message
																	code="role.availableUsers" />
															</label>
														</div>
														<ul class="list-group" id="availableMemList"
															style="max-height: 90%; overflow-y: auto;">


														</ul>
													</div>
												</div>
											</c:if>
											<c:if test="${not readOnly}">
												<div class="col-xs-2 " style="vertical-align: middle;">
													<button type="button" title="Add All"
														class="btn btn-default center-block"
														onclick="moveAllToSelected('mem')">
														<i class="fa fa-chevron-right"></i><i
															class="fa fa-chevron-right"></i>
													</button>
													<button type="button" title="Remove All"
														class="btn btn-default center-block"
														onclick="moveAllToAvailable('mem')">
														<i class="fa fa-chevron-left"></i><i
															class="fa fa-chevron-left"></i>
													</button>
												</div>
											</c:if>
											<div class="col-xs-5">
												<div class="panel panel-default" style="height: 500px;">
													<div class="panel-heading">
														<label> <spring:message code="role.assignedUsers" />
														</label>
													</div>
													<ul class="list-group" id="selectedMemList"
														style="max-height: 90%; overflow-y: auto;">
													</ul>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="box-footer">
								<div class="col-xs-2 col-lg-1 ">
									<c:if
										test="${readOnly == false && isCreate == false && accountGroupForm.referrer == 'v'}">
										<a onclick="showLoading()" class="btn btn-primary "
											href="ViewAccountGroup.do?key=${accountGroupForm.key}"><spring:message
												code="button.back" /></a>
									</c:if>
									<c:if
										test="${readOnly == true || accountGroupForm.referrer == 's'}">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(searchAccountGroup)}">
											<a onclick="showLoading()" class="btn btn-primary "
												href="SearchAccountGroup.do"><spring:message
													code="button.back.to.search" /></a>
										</c:if>
									</c:if>
								</div>
								<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
									<c:if test="${readOnly == false}">
										<c:if
											test="${currRole.grantedPrivilegeCodeList.contains(modifyAccountGroup) || currRole.grantedPrivilegeCodeList.contains(createAccountGroup)}">
											<button type="button" class="btn btn-primary "
												onclick="submitAccountGroupForm();">
												<spring:message code="button.submit" />
											</button>
										</c:if>
									</c:if>
								</div>
								<div class="col-xs-2 col-lg-1 ">
									<c:if test="${readOnly == true}">
										<c:if test="${accountGroupForm.delete == false}">
											<c:if
												test="${currRole.grantedPrivilegeCodeList.contains(modifyAccountGroup)}">
												<button type="button" class="btn btn-primary "
													onclick="submitAccountGroupForm();">
													<spring:message code="button.modify" />
												</button>
											</c:if>
										</c:if>

									</c:if>
									<!--  reset button -->
									<c:if test="${readOnly == false}">
										<c:if test="${isCreate == true}">
											<a onclick="showLoading()" href="CreateAccountGroup.do"
												class="btn btn-primary "><spring:message
													code="button.reset" /></a>
										</c:if>
										<c:if test="${isCreate == false}">
											<a onclick="showLoading()"
												href="ModifyAccountGroup.do?key=${accountGroupForm.key}&r=${accountGroupForm.referrer}"
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
	resetAccountGroupForm();
</script>

<script>  
  menu_toggle("sub_am");
  menu_toggle("sub_am_group");
	if(${isCreate}){
		menu_select("createAccountGroup");
	} else 
		menu_select("listAccountGroup");
</script>
