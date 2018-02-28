<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<c:choose>
	<c:when test="${defectForm.readOnly}">
		<c:set var="dateBackgroundColour" value="" />
		<c:set var="readOnly" value="true" />
		<c:set var="isCreate" value="false" />
		<c:if test="${defectForm.delete == true}">
			<c:set var="submitUrl" value="DoDeleteDefect.do" />
		</c:if>
		<c:if test="${defectForm.delete == false}">
			<c:set var="submitUrl" value="ModifyDefect.do" />
		</c:if>
		<c:set var="disabled" value="disabled" />
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="false" />
		<c:set var="disabled" value="" />
		<c:set var="dateBackgroundColour" value="background-color: #fff;" />
		<c:choose>
			<c:when test="${empty defectForm.key}">
				<c:set var="isCreate" value="true" />
				<c:set var="submitUrl" value="DoCreateModifyDefect.do" />
			</c:when>
			<c:otherwise>
				<c:set var="isCreate" value="false" />
				<c:set var="submitUrl" value="DoCreateModifyDefect.do" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<div class="row">
	<div class="col-xs-12">
		<br>
		<table id="example1" class="table table-bordered table-striped"
			style="table-layout: fixed;">
			<thead>
				<tr>
					<th></th>
					<th style="word-wrap: break-word;"><spring:message
							code="defect.fileType" /></th>
				<%-- 	<th style="word-wrap: break-word;"><spring:message
							code="defect.fileName" /></th> --%>
					<th style="word-wrap: break-word;"><spring:message
							code="defect.fileDesc" /></th>
				<%-- 	<th style="word-wrap: break-word;"><spring:message
							code="defect.fileSize" /></th> --%>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${fileList}" var="file">
					<tr>
						<td class="text-center"><c:if test="${file.fileExist}">
								<c:choose>
									<c:when test="${file.fileType == 'Image'}">
										<a href="#" data-path="<c:out value="${file.path}"/>"
											data-name="<c:out value="${file.desc}"/>" data-toggle="modal"
											data-target="#displayPhotoModal">
											<i class="fa fa-search"></i>
										</a> &nbsp; 
									</c:when>
									<c:when test="${file.fileType == 'Video'}">
										<a href="#" data-path="<c:out value="${file.path}"/>"
											data-name="<c:out value="${file.desc}"/>" data-toggle="modal"
											data-target="#displayVideoModal">
											<i class="fa fa-search"></i>
										</a>  &nbsp; 
									</c:when>
								</c:choose>
								<a href="<c:url value="DoDownloadFile.do"><c:param name="unicodePath" value="${file.unicodePath}"/><c:param name="fileType" value="${file.fileType}"/></c:url>">
									<i class="fa fa-download"></i>
								</a> &nbsp; 
							</c:if> <c:if test="${!readOnly}">

								<i onclick="showModifyFileDescription('${file.name}', '${file.unicodePath}', '${file.fileType}', '${file.desc}');"
									class="fa fa-pencil"></i> &nbsp; 
							
								<i onclick="showDeleteFile('${file.name}', '${file.unicodePath}', '${file.fileType}');"
									class="fa fa-trash-o"></i>

							</c:if></td>
						<c:choose>
							<c:when test="${file.fileType == 'Image'}">
								<td><spring:message code="defect.image" /></td>
							</c:when>
							<c:when test="${file.fileType == 'Video'}">
								<td><spring:message code="defect.video" /></td>
							</c:when>
							<c:otherwise>
								<td>Unknown Type</td>
							</c:otherwise>
						</c:choose>
					<%-- 	<td style="word-wrap: break-word; white-space: pre-wrap;"><c:out
								value="${file.name}" /></td> --%>
						<td style="word-wrap: break-word; white-space: pre-wrap;"><c:out
								value="${file.desc}" /></td>
					<%-- <c:choose>
							<c:when test="${file.fileExist}">
								<td><c:out value="${file.fileSize}" /></td>
							</c:when>
							<c:otherwise>
								<td><spring:message code="defect.file.miss" /></td>
							</c:otherwise>
						</c:choose> --%>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</div>

