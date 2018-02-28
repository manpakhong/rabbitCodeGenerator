<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:if test="${not empty totalPages}">
	<nav style="text-align: center;">
		<ul class="pagination">
			<c:if test="${startPage gt 1 }">
				<li><a class="btn" aria-label="Previous"
					onclick="gotoPage(${startPage-1})"><span aria-hidden="true">&laquo;</span>
				</a></li>
			</c:if>
			<c:forEach begin="${startPage}" end="${startPage + pageSize - 1}"
				varStatus="loop">
				<c:if test="${loop.index le totalPages }">
					<li
						class="<c:if test="${currentPage eq loop.index }">active</c:if>"><a
						class="btn" onclick="gotoPage(${loop.index})">${loop.index}</a></li>
				</c:if>
			</c:forEach>
			<c:if test="${ totalPages gt startPage + pageSize - 1 }">
				<li><a class="btn" aria-label="Next"
					onclick="gotoPage(${startPage + pageSize})"><span
						aria-hidden="true">»</span></a></li>
			</c:if>

		</ul>
	</nav>
</c:if>
