<%@tag description="display the whole locationTree" pageEncoding="UTF-8"%>
<%@attribute name="node" type="hk.ebsl.mfms.model.LocationTreeNode"
	required="true"%>
<%@taglib prefix="template" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<c:if test="${not empty node.location.key}">

	<li class="list-group-item"><label
		style="max-width: 80%; word-wrap: break-word;"> <form:checkbox
				path="selectedLocationKeyList" value="${node.location.key}" class = 'checkBox'/> <c:out
				value="${node.location.code}" /> - <c:out
				value="${node.location.name}" /></label> 
				
	<c:if test="${! empty node.children}">
			<div class=" pull-right">
				<i class="fa fa-angle-left" style="cursor: pointer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</i>
				<input type="checkbox" class="checkAll"
					title="Select/Deselect all child" />
			</div>
			<ul class="list-group">
				<c:forEach var="child" items="${node.children}">
					<template:checkableLocationTree node="${child}" />
				</c:forEach>
			</ul>
		</c:if>
	</li>

</c:if>

<!--  check childrent checkbox
<script>
var c_all = $("li.list-group-item").find("li").find("div").find("input:checkbox"); 
var count = 0;

$(document).ready(function(){
	
	for(i=0;i<c_all.length;i++){
		var childrent= $(c_all[i].parentNode.parentNode).find("ul").find("label").find("input:checkbox");
		
		for(j=0;j<childrent.size();j++){
			if($($(c_all[i].parentNode.parentNode).find("ul").find("label").find("input:checkbox")[j]).prop("checked")){
				count ++;
				}
			else{
				break;
				}
			}
			
		if(count==childrent.size() && $($(c_all[i].parentNode.parentNode).find("label").find("input:checkbox")).prop("checked")){
				
			$(c_all[i]).prop("checked", true);
			}
		
		count=0;
		}

});
</script>
-->
