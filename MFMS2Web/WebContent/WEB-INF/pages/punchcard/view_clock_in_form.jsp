<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<!-- <script src="import/underscorejs/underscore-min.1.8.3.js"></script> -->

<%-- <spring:url value="/submitClockIn.do" var="submitClockInUrl" /> --%>
<script src="import/punchcard/viewclockin.js"></script>
<div id="modalClockIn">
	<!-- Modal content -->
	<div class="modal-content">
		Are you sure to clock In?
		<form >
			<table>
				<tr>
					<td>Username:</td>
					<td><span class="userAccountName">${userAccount.name}</span></td>
				</tr>
				<tr>
					<td>Time:</td>
					<td><span class="currentDateTimeString">${currentDateTime}</span></td>
				</tr>
				<tr>
					<td>Remark Location:</td>
					<td><form:select path="locationList" cssClass="locationListSelect">							
							<form:option value="0" label="Select an Option" />
							<form:options items="${locationList}" />
						</form:select></td>
				</tr>
				<tr>
					<td>Remarks:</td>
					<td><textarea cols="28" rows="5" class="remarks" type="text" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
