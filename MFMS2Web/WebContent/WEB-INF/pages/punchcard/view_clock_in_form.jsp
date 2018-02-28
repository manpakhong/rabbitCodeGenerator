<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<script src="import/underscorejs/underscore-min.1.8.3.js"></script>
<script src="import/punchcard/viewclockin.js"></script>

<div id="modalClockIn">
	<!-- Modal content -->
	<div class="modal-content">
		Are you sure to clock In?
		<form>
			<table>
				<tr>
					<td>Username:</td>
					<td><span></span></td>
				</tr>
				<tr>
					<td>Time:</td>
					<td><span>YYYY-MM-DD HH:MM:SS</span></td>
				</tr>
				<tr>
					<td>Remark Location:</td>
					<td><select>
							<option value="volvo">Volvo</option>
							<option value="saab">Saab</option>
							<option value="mercedes">Mercedes</option>
							<option value="audi">Audi</option>
					</select></td>
				</tr>
								<tr>
					<td>Reamrks:</td>
					<td><input type="text" /></td>
				</tr>
			</table>
		</form>
	</div>
</div>
