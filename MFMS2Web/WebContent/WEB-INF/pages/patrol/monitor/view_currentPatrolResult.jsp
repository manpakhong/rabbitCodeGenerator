<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h3 class="modal-title">
	<spring:message code="patrol.monitor.section.sequence" /><br/>
	<label id="currentRouteCode"></label> - <label id="currentRouteName"></label>
</h3>
<div class="box-footer">
	<table class="table table-striped table-hover table-responsive">
		<thead>
			<tr>
				<th><spring:message code="patrol.monitor.order" /></th>
				<th><spring:message code="patrol.monitor.location" /></th>
				<th><spring:message code="patrol.monitor.attendTime" /></th>
				<th><spring:message code="patrol.monitor.personAttended" /></th>
				<th><spring:message code="patrol.monitor.remark" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="currentPatrolResultDeatil"
				items="${currentPatrolResultDeatil}" varStatus="status">
				<tr id="cur_result_${currentPatrolResultDeatil.patrolResultKey}"
					class="<c:if test="${currentPatrolResultDeatil.status eq 'Normal'}">result-current-location</c:if><c:if test="${currentPatrolResultDeatil.status eq 'Overtime'}">result-overtime-location</c:if>">

					<!--<c:if test="${status.index eq 1}">-->
						<script>
							$("#currentRouteName").text("${currentPatrolResultDeatil.routeName}");
							$("#currentRouteCode").text("${currentPatrolResultDeatil.routeCode}");
						</script>
					<!--</c:if>-->

					<td>${status.index +1}/<c:choose>
							<c:when test="${currentPatrolResultDeatil.sequence eq 0}">-</c:when>
							<c:otherwise>${currentPatrolResultDeatil.sequence}</c:otherwise>
						</c:choose></td>
					<td
						id="cur_result_loc_${currentPatrolResultDeatil.patrolResultKey}">${currentPatrolResultDeatil.locationCode}
						- ${currentPatrolResultDeatil.locationName}</td>
					<td id="cur_result_at_${currentPatrolResultDeatil.patrolResultKey}"
						class="at">${currentPatrolResultDeatil.attendTime}</td>
					<td id="cur_result_ac_${currentPatrolResultDeatil.patrolResultKey}"
						class="ac">${currentPatrolResultDeatil.accountName}</td>
					<td
						id="cur_result_rmk_${currentPatrolResultDeatil.patrolResultKey}"
						class="rmk">${currentPatrolResultDeatil.remark}</td>

					<script>
						$(
								'#cur_result_loc_${currentPatrolResultDeatil.patrolResultKey}')
								.text(
										_
												.unescape(document
														.getElementById('cur_result_loc_${currentPatrolResultDeatil.patrolResultKey}').innerHTML));

						$(
								'#cur_result_at_${currentPatrolResultDeatil.patrolResultKey}')
								.text(
										_
												.unescape(document
														.getElementById('cur_result_at_${currentPatrolResultDeatil.patrolResultKey}').innerHTML));
						$(
								'#cur_result_ac_${currentPatrolResultDeatil.patrolResultKey}')
								.text(
										_
												.unescape(document
														.getElementById('cur_result_ac_${currentPatrolResultDeatil.patrolResultKey}').innerHTML));
						$(
								'#cur_result_rmk_${currentPatrolResultDeatil.patrolResultKey}')
								.text(
										_
												.unescape(document
														.getElementById('cur_result_rmk_${currentPatrolResultDeatil.patrolResultKey}').innerHTML));
					</script>
					<input type="hidden" value="${status.index +1}"
						id="cur_result_index_${currentPatrolResultDeatil.patrolResultKey}" />
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>