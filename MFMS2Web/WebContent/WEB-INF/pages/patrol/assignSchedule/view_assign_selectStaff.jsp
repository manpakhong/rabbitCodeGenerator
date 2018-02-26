<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<script>
	function showSelectStaffModal() {
		
		
		if ($("#eventModal").hasClass('in')) {
			
			console.log("test : selected :"+selectedStaffJson + "|| edit : "+eventEditStaffJson);
			
			var temp_select = selectedStaffJson;
			var temp_edit = eventEditStaffJson;
			
			if('undefined' != typeof currentMode){
				if(currentMode=="current"){
				
					temp_select = currentEventEditStaffJson;
					temp_edit = edit_selectedStaffJson;
				
				}else if(currentMode == "series"){
			
					temp_select = parentEventEditStaffJson;
					temp_edit = edit_parentSelectedStaffJson;
				
				}
			}
			
			console.log("temp_select : "+temp_select+"|| temp_edit"+temp_edit);
			
			$.ajax({
				method : "POST",
				url : "ShowStaffSelected.do",
				data : "edit=true&selectedStaffJson=" + temp_select+"&eventEditStaffJson="+temp_edit,
				success : function(data) {

					$('#view_selectedStaff').html(data).promise().done(
							function() {
								$('#selectStaffModal').modal('show');
								$("#searchedList").find(".patrol-childlist-li").show();
								slideUpThePrompt();
								for(i=0; i<selectedKeys.length; i++){
									$("#searched_staff_" + selectedKeys[i]).parent().hide();
								}
							})

				}
			});

			/*
			$('#view_selectedStaff').load(
					"ShowStaffSelected.do?edit=true&selectedStaffJson="
							+ selectedStaffJson, function() {
						$('#selectStaffModal').modal('show');
					});
			 */
		} else {
			
			$.ajax({
				method : "POST",
				url : "ShowStaffSelected.do",
				data : "edit=false&selectedStaffJson=" + selectedStaffJson+"&eventEditStaffJson="+eventEditStaffJson,
				success : function(data) {

					$('#view_selectedStaff').html(data).promise().done(
							function() {
								$('#selectStaffModal').modal('show');
							})

				}
			});

			/*
			$('#view_selectedStaff').load("ShowStaffSelected.do?edit=false&selectedStaffJson"+selectedStaffJson,
					function() {
						$('#selectStaffModal').modal('show');
					});
			 */
		}

	}
</script>


<div id="accountDiv" class="col-lg-4 col-md-4 col-sm-4 col-xs-4 ">
	<label class="control-label form-control" id="accountLabel"
		style="border: none;"><spring:message
			code="patrol.schedule.account" /> <a class="btn" style="margin: 0px;"
		data-toggle="modal" onclick="showSelectStaffModal()"><span
			class="glyphicon glyphicon-user"></span></a> </label>
</div>

<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8"
	style="display: inline-flex;" id="noIn">
	<form:select path="staff" cssClass="form-control" multiple="true"
		id="staff${htmlId}">
		<form:options items="${staff}" />
	</form:select>
</div>