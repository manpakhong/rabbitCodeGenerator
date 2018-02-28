<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:forEach var="currentPatrolResultDeatil"
	items="${currentPatrolResultDeatil}" varStatus="status">

	<td>${currentSeq}/<c:choose>
			<c:when test="${currentPatrolResultDeatil.sequence eq 0}">-</c:when>
			<c:otherwise>${currentPatrolResultDeatil.sequence}</c:otherwise>
		</c:choose></td>
	<td id="cur_result_loc_${currentPatrolResultDeatil.patrolResultKey}">${currentPatrolResultDeatil.locationCode}
		- ${currentPatrolResultDeatil.locationName}</td>
	<td>${currentPatrolResultDeatil.attendTime}</td>
	<td id="cur_result_ac_${currentPatrolResultDeatil.patrolResultKey}">${currentPatrolResultDeatil.accountName}</td>
	<td>${currentPatrolResultDeatil.remark}</td>

	<input type="hidden" value="${currentSeq}"
		id="cur_result_index_${currentPatrolResultDeatil.patrolResultKey}" />

	<script>
		$('#cur_result_loc_${currentPatrolResultDeatil.patrolResultKey}')
				.text(
						_
								.unescape(document
										.getElementById('cur_result_loc_${currentPatrolResultDeatil.patrolResultKey}').innerHTML));
		
		$('#cur_result_ac_${currentPatrolResultDeatil.patrolResultKey}')
		.text(
				_
						.unescape(document
								.getElementById('cur_result_ac_${currentPatrolResultDeatil.patrolResultKey}').innerHTML));
		
	</script>

</c:forEach>