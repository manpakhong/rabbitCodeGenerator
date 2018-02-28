<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="../common/left.jsp" />

<c:set var="submitUrl" value="DoAssignRole.do" />

<script>
	var origAvailId = [];
	<c:forEach var="unassignedAccountId" items="${assignRoleForm.unassignedAccountList}">
	origAvailId.push('<c:out value="${unassignedAccountId}"/>');
	</c:forEach>

	var origSelectedId = [];
	<c:forEach var="assignedAccountId" items="${assignRoleForm.assignedAccountList}">
	origSelectedId.push('<c:out value="${assignedAccountId}"/>');
	</c:forEach>

	var newAvailId = origAvailId.slice();
	var newSelectedId = origSelectedId.slice();

	function createAvailableItem(userId) {

		var newItem = document.createElement('li');
		newItem.className = 'list-group-item';
		var addBtn = document.createElement('span');
		addBtn.className = 'fa fa-plus-circle pull-right';
		addBtn.setAttribute('style', 'color:#5cb85c;cursor:pointer');
		addBtn.setAttribute('onclick', 'moveToSelected(this.parentNode);');
		newItem.appendChild(addBtn);

		newItem.appendChild(document.createTextNode(userId));

		return newItem;
	}

	function moveToAvailable(item) {

		var availableUserList = document.getElementById('availableUserList');

		var selectedUserList = document.getElementById('selectedUserList');

		var userId = item.textContent.trim();

		var newItem = createAvailableItem(userId);

		availableUserList.appendChild(newItem);
		newAvailId.push(userId);
		selectedUserList.removeChild(item);
		var index = newSelectedId.indexOf(userId);
		if (index >= 0) {
			newSelectedId.splice(index, 1);
		}
		//alert(newAvailId);
		//alert(newSelectedId);
	}

	function moveAllToAvailable() {

		var selectedUserList = document.getElementById('selectedUserList');

		var list = selectedUserList.getElementsByTagName("li");

		while (list.length > 0) {
			moveToAvailable(list[0]);
			/*
			if (selectedUserList.firstChild.tagName == 'li') {
				moveToAvailable(selectedUserList.firstChild);
			}else {
				selectedUserList.removeChild(selectedUserList.firstChild);
			}
			 */
		}
	}

	function createSelectedItem(userId) {

		var newItem = document.createElement('li');
		newItem.className = 'list-group-item';
		var removeBtn = document.createElement('span');
		removeBtn.className = 'fa fa-times-circle pull-right';
		removeBtn.setAttribute('style', 'color:red;cursor:pointer');
		removeBtn.setAttribute('onclick', 'moveToAvailable(this.parentNode);');
		newItem.appendChild(removeBtn);

		newItem.appendChild(document.createTextNode(userId));

		return newItem;
	}

	function moveToSelected(item) {

		var availableUserList = document.getElementById('availableUserList');

		var selectedUserList = document.getElementById('selectedUserList');

		var userId = item.textContent.trim();

		var newItem = createSelectedItem(userId)

		selectedUserList.appendChild(newItem);
		newSelectedId.push(userId);
		availableUserList.removeChild(item);
		var index = newAvailId.indexOf(userId);
		if (index >= 0) {
			newAvailId.splice(index, 1);
		}
		//alert(newAvailId);
		//alert(newSelectedId);
	}

	function moveAllToSelected() {

		var availableUserList = document.getElementById('availableUserList');

		var list = availableUserList.getElementsByTagName("li");

		while (list.length > 0) {

			moveToSelected(list[0]);

			/*
			alert(availableUserList.firstChild.tagName);
			if (availableUserList.firstChild.tageName == 'li') {
				moveToSelected(availableUserList.firstChild);
			} else {
				availableUserList.removeChild(availableUserList.firstChild);
			}
			 */

		}
	}

	function submitAssignForm() {
		
		showLoading();

		var assignRoleForm = document.getElementById('assignRoleForm');
		//alert(newSelectedId);
		for (var i = 0; i < newSelectedId.length; i++) {
			var input = document.createElement('input');
			input.setAttribute('name', 'assignedAccountList[' + i + ']');
			input.setAttribute('type', 'hidden');
			input.value = newSelectedId[i];

			assignRoleForm.appendChild(input);
			//alert(input.innerHTML);
		}

		assignRoleForm.submit();
	}

	function resetAssignForm() {
		var assignRoleForm = document.getElementById('assignRoleForm');
		assignRoleForm.reset();

		// reset list
		newAvailId = origAvailId.slice();
		newSelectedId = origSelectedId.slice();

		// select list
		var selectedUserList = document.getElementById('selectedUserList');
		// remove all
		while (selectedUserList.hasChildNodes()) {
			selectedUserList.removeChild(selectedUserList.firstChild);
		}
		// readd
		for (var i = 0; i < newSelectedId.length; i++) {

			var newItem = createSelectedItem(newSelectedId[i]);

			selectedUserList.appendChild(newItem);
		}

		// avail list
		var availableUserList = document.getElementById('availableUserList');
		// remove all
		while (availableUserList.hasChildNodes()) {
			availableUserList.removeChild(availableUserList.firstChild);
		}
		// readd
		for (var i = 0; i < newAvailId.length; i++) {

			var newItem = createAvailableItem(newAvailId[i]);

			availableUserList.appendChild(newItem);
		}
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
			<li class="active"><spring:message
					code="menu.accountMgt.role.assign" /></li>
		</ol>
	</section>
	<!-- Main content -->
	<section class="content">

		<div class="row">
			<div class="col-xs-12">
				<!-- Horizontal Form -->
				<div class="box">
					<!-- form start -->
					<form:form cssClass="form-horizontal" commandName="assignRoleForm"
						method="post" action="${submitUrl}">

						<!-- validation error messages -->
						<spring:bind path="*">
							<c:if test="${status.error}">
								<div class="form-group alert alert-danger">
									<form:errors path="*" />
								</div>
							</c:if>

						</spring:bind>
						<c:if test="${assignRoleForm.success}">
							<div class="form-group alert alert-success">
								<spring:message code="role.assign.success" />
							</div>
						</c:if>

						<form:hidden path="key" />
						<form:hidden path="referrer" />
						<div class="box-body">
							<div
								class="form-group <spring:bind path="name"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-xs-2 control-label"><spring:message
										code="role.name" />*</label>
								<div class="col-xs-5">
									<form:input path="name" cssClass="form-control" readonly="true" />
								</div>
							</div>
							<div
								class="form-group  <spring:bind path="desc"><c:if test="${status.error}">has-error</c:if></spring:bind>">
								<label for="" class="col-xs-2 control-label"><spring:message
										code="role.description" /></label>
								<div class="col-xs-10">
									<form:textarea path="desc" cssClass="form-control"
										readonly="true" />
								</div>
							</div>
							<div class="form-group col-xs-12">
								<br />

								<div class="col-xs-5">
									<div class="panel panel-default" style="height: 500px;">
										<div class="panel-heading">
											<label> <spring:message code="role.availableUsers" />
											</label>
										</div>
										<ul class="list-group" id="availableUserList"
											style="max-height: 90%; overflow-y: auto;">
										</ul>
									</div>
								</div>
								<div class="col-xs-2 " style="vertical-align: middle;">
									<button type="button" title="Add All"
										class="btn btn-default center-block"
										onclick="moveAllToSelected()">
										<i class="fa fa-chevron-right"></i><i
											class="fa fa-chevron-right"></i>
									</button>
									<button type="button" title="Remove All"
										class="btn btn-default center-block"
										onclick="moveAllToAvailable()">
										<i class="fa fa-chevron-left"></i><i
											class="fa fa-chevron-left"></i>
									</button>
								</div>
								<div class="col-xs-5">
									<div class="panel panel-default" style="height: 500px;">
										<div class="panel-heading">
											<label> <spring:message code="role.assignedUsers" />
											</label>
										</div>

										<ul class="list-group" id="selectedUserList"
											style="max-height: 90%; overflow-y: auto;">


										</ul>
									</div>
								</div>

							</div>
						</div>
						<!-- /.box-body -->
						<div class="box-footer">
							<div class="col-xs-2 col-lg-1 ">
								<c:choose>
									<c:when test="${assignRoleForm.referrer == 's'}">
										<!-- this view page will go to search -->
										<a onclick="showLoading()" class="btn btn-primary "
											href="<c:url value="DoSearchRole.do"></c:url>"><spring:message
												code="button.back" /></a>
									</c:when>
									<c:otherwise>
										<a onclick="showLoading()" class="btn btn-primary "
											href="ModifyRole.do?key=${assignRoleForm.key}&r=${assignRoleForm.referrer}"><spring:message
												code="button.back" /></a>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="col-lg-1 col-xs-2 col-xs-offset-6 col-lg-offset-9 ">
								<a  class="btn btn-primary " onclick="submitAssignForm()"><spring:message
										code="button.submit" /></a>
							</div>
							<div class="col-xs-2 col-lg-1 ">

								<a onclick="showLoading()" class="btn btn-primary "
									href="AssignRole.do?key=${assignRoleForm.key}&r=${assignRoleForm.referrer}"><spring:message
										code="button.reset" /></a>
							</div>
						</div>
						<!-- /.box-footer -->
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
	resetAssignForm();
</script>
