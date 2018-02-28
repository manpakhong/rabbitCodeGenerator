<%@tag description="display the whole locationTree" pageEncoding="UTF-8"%>
<%@attribute name="node" type="hk.ebsl.mfms.model.LocationTreeNode" required="true"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<li class="list-group-item" cssClass="form-control" style="cursor: pointer">
	<label style = "max-width: 80%; word-wrap: break-word;">
		<form:radiobutton path="locationKey" value="${node.location.key}" class = 'validation'
			disabled="${!node.privileged}"
			onclick="onLocationChange('${node.location.key}', '${node.location.code}', '${node.location.name}');" />
		<c:out value="${node.location.code}" /> - <c:out
			value="${node.location.name}" />
	</label> 
	<c:if test="${! empty node.children}">
		<div class=" pull-right">
			<i class="fa fa-angle-left" style="cursor: pointer"></i>
		</div>
		<ul class="list-group">
			<c:forEach var="child" items="${node.children}">
				<template:radioButtonLocationTree node="${child}" />
			</c:forEach>
		</ul>
	</c:if>
</li>
