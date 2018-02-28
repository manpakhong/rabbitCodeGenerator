<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>



<spring:bind path="*">
	<c:if test="${patrolCreateForm.status.error}">
		<div class="form-group alert alert-danger">
			<form:errors path="*" />
		</div>
	</c:if>
</spring:bind>

