<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
 
<!-- Footer -->
 <sec:authorize var="loggedIn" access="isAuthenticated()" />
 <c:choose>
     <c:when test="${loggedIn}">
         <c:set var="footerStyle" value="margin-left: 0px !important; position: absolute;width: 100%;z-index: 860;" />
     </c:when>
     <c:otherwise>
         <c:set var="footerStyle" value="margin-left: 0px;" />
     </c:otherwise>
 </c:choose>
      <footer class="main-footer" style="${footerStyle}">
 
        <div class="pull-right hidden-xs">
          <b><spring:message code="footer.version" /></b> &nbsp;<spring:eval expression="@propertyConfigurer.getProperty('mfms2.version')" />
          &nbsp;&nbsp;&nbsp;&nbsp;

			<c:set var="company">
				<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
			</c:set>
			<c:choose>
			     <c:when test="${company=='ebsl'}">          
			          <img src="import/img2/logo_ebsl_white_s.png" alt="">
			     </c:when>
			     <c:when test="${company=='jec'}">          
			     </c:when>
			     <c:when test="${company=='mss'}">          
			          <img src="import/img2/logo_mss_white_s.png" style="margin-top:-10px" alt="">
			     </c:when>
			     <c:otherwise> 
			     	<img src="import/img2/logo_ebsl_white_s.png" alt="">
			     </c:otherwise>
			</c:choose>
        </div>
        <jsp:useBean id="date" class="java.util.Date" />
		<fmt:formatDate value="${date}" pattern="yyyy" var="year"/>
		<spring:message code="footer.copyright" arguments="${year}"/>
      </footer>
 