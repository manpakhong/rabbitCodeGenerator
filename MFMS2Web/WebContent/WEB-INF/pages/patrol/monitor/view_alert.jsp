<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="alert alert-danger alert-dismissible" role="alert" id="${alertId}">
	<button type="button" class="close" data-dismiss="alert"
		aria-label="Close" onclick="removeOverTime('${closeAlertId}')">
		<span aria-hidden="true">&times;</span>
	</button>
	

	<strong>${routeName}</strong>${msg}
</div>