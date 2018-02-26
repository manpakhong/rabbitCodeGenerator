<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<c:set var="company">
	<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
</c:set>

<jsp:include page="../common/left.jsp" />
<!-- Content Wrapper. Contains page content -->
<style>
section.content{
	padding-top: 0 !important;
	padding-bottom: 0 !important;
}

.header-table{
	width: 100%;
	
}
.header-table td{
	padding: 5px;
}

.header-table .title{
	font-size: 26px;
}

.header-table .sub-title{
	font-size: 16px;
	text-align: right;
}

.header-table .header-icon .fa{
	margin-right: 3px;
}

.header-table .header-block{
	margin-right: 30px;
	display: inline-block;
}

.header-table .header-block-alert{
	width: 40px;
	text-align: center;
	display: inline-block;
}

.header-table .header-icon{
	margin-right: 5px;
	padding: 0px 5px;
}

.clickable:hover{
	cursor: pointer;
	opacity: 0.8;
}

.fixed-div{
	overflow-x: scroll; 
	overflow-y: scroll; 
	border: 1px solid #eee;
}

.fixed-div table{
	table-layout: fixed;
}

.fixed-div td, .fixed-div th{
	word-wrap:break-word;
}

#left-table, #right-table{
	float: left;
}

.tr-parent{
	color: #3d3d99;
	font-weight: bold;
}
.tr-child{
	color: #000;
}

.alert-1{
	background-color: yellow; 
}
.alert-2{
	background-color: orange; 	
}
.alert-3{	
	background-color: #ff8484;
}
.btn-expand{
	color: green;
}
.btn-collpase{
	color: blue;
}
</style>

<div class="content-wrapper" ng-app="alarm-console-app">
	<section class="content-header">
      <ol class="breadcrumb">
		<li><a href="DefectManagement.do"><i
				class="icon-icon_defect_management_b"></i> <spring:message
					code="menu.defectMgt" /></a></li>
		<li class="active">
			<c:choose>
				<c:when test="${company=='jec'}">
					<spring:message code="menu.alarmconsole.jec" />
				</c:when>
				<c:otherwise>
					<spring:message code="menu.alarmconsole" />
				</c:otherwise>
			</c:choose>
		</li>
      </ol>
    </section>
    <div ng-controller="listController">
		<section class="content" >
			<table class="header-table">
				<tbody>
					<tr style="background: #fff;">
						<td class="sub-title" colspan="99">
							<spring:message code="alarmCondole.today" /> : {{today}}<br/>
							<spring:message code="alarmCondole.total" /> : {{totalNum}} <spring:message code="alarmCondole.items" />
						</td>
					</tr>
					<tr style="background: #ddd;">
						<td colspan="99">
							<div class="header-block">						
								<span class="header-icon clickable" ng-click="expandAll()"><i class="fa fa-plus-circle" style="color: green;"></i><spring:message code="alarmCondole.expandAll" /></span>
								<span class="header-icon clickable" ng-click="collapseAll()"><i class="fa fa-minus-circle" style="color: blue;"></i><spring:message code="alarmCondole.collapseAll" /></span>
							</div>						
							<div class="header-block"><spring:message code="alarmCondole.alert" /> : {{alertStatusNum}}</div>
							<div class="header-block"><spring:message code="alarmCondole.typeOfAlert" /> : 
							<c:set var="alertRed">
								<spring:eval expression="@propertyConfigurer.getProperty('alert.red')" />
							</c:set>
							<c:if test="${alertRed=='Y'}">          
								<div class="header-block-alert alert-3">{{alertStatusNum3}}</div>							
							</c:if>
								<div class="header-block-alert alert-2">{{alertStatusNum2}}</div>
								<div class="header-block-alert alert-1">{{alertStatusNum1}}</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="fixed-div" ng-style="resizeWithOffset(308)" resize notifier="notifyServiceOnChage(params)">
				<div class="table-wrapper" style="min-width: 1500px">
					<table class="table table-bordered" id="left-table">	
						<thead>
							<tr>
								<th style="width: 30px;">&nbsp;</th>
								<th style="width: 95px;"><spring:message code="alarmCondole.woCode" /></th>
								<th style="width: 90px;"><spring:message code="alarmCondole.issuedDate" /></th>
								<th style="width: 110px;"><spring:message code="alarmCondole.location" /></th>
								<th style="width: 110px;"><spring:message code="alarmCondole.pcName" /></th>
								<th style="width: 110px;"><spring:message code="alarmCondole.woDesc" /></th>
								<th style="width: 90px;"><spring:message code="alarmCondole.targetFinishDate" /></th>
								<th style="width: 110px;"><spring:message code="alarmCondole.assignedGroup" /></th>
								<th style="width: 110px;"><spring:message code="alarmCondole.assignedAccount" /></th>
								<th style="width: 110px;"><spring:message code="alarmCondole.status" /></th>
								<th style="width: 130px;"><spring:message code="alarmCondole.status.changedDate" /></th>
								<th style="width: 90px;"><spring:message code="alarmCondole.escalation" /></th>			
							</tr>
						</thead>
						<tbody ng-repeat="record in records">
							<tr class="tr-parent" ng-class="{'alert-1': record.alertStatus == 1 && record.isAlert, 'alert-2': record.alertStatus == 2 && record.isAlert, 'alert-3': record.alertStatus == 3 && record.isAlert} ">
								<td>
									<div ng-click="expand(record)" ng-if="record.details.length > 0">
										<i class="clickable fa fa-plus-circle" style="color: green;" ng-if="!record.isExpanded"></i>
										<i class="clickable fa fa-minus-circle" style="color: blue;" ng-if="record.isExpanded"></i>									
									</div>
								</td>								
								<td>{{record.summary.defect_Code}}</td>
								<td>
									{{record.summary.defect_IssueDateTime | moment: 'YYYY/MM/DD'}}<br/>
									{{record.summary.defect_IssueDateTime | moment: 'HH:mm:ss'}}
								</td>
								<td>{{record.summary.location_Name}}</td>
								<td>{{record.summary.problemCode_Name}}</td>
								<td>{{record.summary.defect_Desc}}</td>
								<td>
									{{record.summary.defect_TargetFinishDateTime | moment: 'YYYY/MM/DD'}}<br/>
									{{record.summary.defect_TargetFinishDateTime | moment: 'HH:mm:ss'}}
								</td>
								<td>{{record.summary.acountGroup_Name}}</td>
								<td>{{record.summary.account_Name}}<br/>{{record.summary.account_ContactNumber}}</td>
								<td>
									{{record.summary.status_Name | split:' ':0}}<br/>
									{{record.summary.status_Name | split:' ':1}}
								</td>
								<td>
									{{record.summary.defect_ReplyTime | moment: 'YYYY/MM/DD'}}<br/>
									{{record.summary.defect_ReplyTime | moment: 'HH:mm:ss'}}
								</td>
								<td>{{record.summary.escalation}}</td>
							</tr>			
							<tr class="tr-child" ng-repeat="detail in record.details" ng-if="record.isExpanded">
								<td>&nbsp;</td>
								<td>{{detail.defect_Code}}</td>
								<td>&nbsp;</td>
								<td>{{detail.location_Name}}</td>
								<td>{{detail.problemCode_Name}}</td>
								<td>{{detail.defect_Desc}}</td>
								<td>&nbsp;</td>
								<td>{{detail.acountGroup_Name}}</td>
								<td>{{detail.account_Name}}<br/>{{detail.account_ContactNumber}}</td>
								<td>
									{{detail.status_Name | split:' ':0}}<br/>
									{{detail.status_Name | split:' ':1}}
								</td>								
								<td>
									{{detail.defect_ReplyTime | moment: 'YYYY/MM/DD'}}<br/>
									{{detail.defect_ReplyTime | moment: 'HH:mm:ss'}}
								</td>
								<td>{{detail.escalation}}</td>
							</tr>				
						</tbody>
					</table>
					<table class="table table-bordered" id="right-table">	
						<thead>
							<tr>
								<th style="width: 100px;"><spring:message code="alarmCondole.status.changedBy" /></th>
								<th style="width: 100px;"><spring:message code="alarmCondole.contactName" /></th>
								<th style="width: 50px;"><spring:message code="alarmCondole.remarks" /></th>					
							</tr>
						</thead>
						<tbody ng-repeat="record in records">
							<tr class="tr-parent" ng-class="{'alert-1': record.alertStatus == 1 && record.isAlert, 'alert-2': record.alertStatus == 2 && record.isAlert, 'alert-3': record.alertStatus == 3 && record.isAlert} ">
								<td>{{record.summary.defect_ReplyBy}}</td>
								<td>{{record.summary.defect_ContactName}}<br/>{{record.summary.defect_ContactTel}}</td>
								<td>{{record.summary.defect_Remarks}}</td>
							</tr>			
							<tr class="tr-child" ng-repeat="detail in record.details" ng-if="record.isExpanded">
								<td>{{detail.defect_ReplyBy}}</td>
								<td>{{detail.defect_ContactName}}<br/>{{detail.defect_ContactTel}}</td>
								<td>{{detail.defect_Remarks}}</td>
							</tr>				
						</tbody>
					</table>	
				</div>
			</div>
		</section>
    </div>
</div>

<script type="text/javascript" src="import/angularjs/angular.min.js"></script>
<script type="text/javascript" src="import/fullcalendar/moment.min.js"></script>
<script>
	var app = angular.module('alarm-console-app', []);
	app.config(function ($httpProvider) {
		$httpProvider.defaults.useXDomain = true;
		delete $httpProvider.defaults.headers.common['X-Requested-With'];
	});
	app.filter('moment', function() {
	    return function(dateString, format) {
	    	if(dateString == "" || dateString == "null" || dateString == null || dateString == "undefined" || dateString == undefined)
	    		return "";
	    	else	    		
	    		return moment(dateString).format(format);
	    };
	});
	app.filter('split', function() {
        return function(input, splitChar, splitIndex) {
			var splits = input.split(splitChar);
			if(splits.length > splitIndex)
            	return splits[splitIndex];
			else 
				return "";
        }
    });
	app.directive('resize', function ($window) {
	    return function (scope, element, attr) {
	        var w = angular.element($window);
	        scope.$watch(function () {
	            return {
	                'h': w.height(), 
	                'w': w.width()
	            };
	        }, function (newValue, oldValue) {
	            scope.windowHeight = newValue.h;
	            scope.windowWidth = newValue.w;
	            scope.resizeWithOffset = function (offsetH) {
	                scope.$eval(attr.notifier);
	                return { 
	                    'height': (newValue.h - offsetH) + 'px'
	                    //,'width': (newValue.w - 100) + 'px' 
	                };
	            };

	        }, true);

	        w.bind('resize', function () {
	            scope.$apply();
	        });
	    }
	}); 
	
	app.controller('listController', function($scope, $http, $timeout, $interval, $filter) {
		// footer = 70
		// header = 100
		// page header = 86
		// table header = 42
		// buffer = 20		
		$(window).resize(function() {
			var $tableWrapper = $('.table-wrapper'),
				$tableContainer = $tableWrapper.parent(),
				$tables = $tableWrapper.children(),
				tableWidth = $tableContainer.width() - 15;			
			$tableWrapper.css('width', (tableWidth*2) + 'px');
			$tables.css('width', tableWidth + 'px');			
			$('#right-table').css('width', '0px');
			trResize();	
		}).trigger('resize');		
		
		function trResize() {	
			$timeout(function () {
				$("#left-table tr").each(function(index, element){
				   	$(this).css('height', '0px');
				});
				$("#right-table tr").each(function(index, element){
				   	$(this).css('height', '0px');
				});
				
				$("#left-table tr").each(function(index, element){
				    var rowOneHeight = $(this).height();
				    var rowTwo = $("#right-table tr:eq(" + index + ")");
				    if(!rowTwo.length){ 
				    	return false;
				    }
				    var rowTwoHeight = rowTwo.height();
				    if(rowOneHeight > rowTwoHeight){
				        rowTwo.css('height', rowOneHeight + 'px');
				    	//rowTwo.width(rowOneHeight);
				    }else{
					   	$(this).css('height', rowTwoHeight + 'px');
				    	//$(this).width(rowOneHeight);
				    }				    
				});	
			},100);
		}
		
		$scope.notifyServiceOnChage = function(){
			//console.log($scope.windowHeight);   
		}
		
		var dateFormat = "DD-MMM-YYYY HH:mm:ss";
		var host = "import/angularjs/";
		$scope.today = new moment().format(dateFormat);
		$scope.totalNum = 0;
		$scope.alertStatusNum = 0;
		$scope.alertStatusNum1 = 0;
		$scope.alertStatusNum2 = 0;
		$scope.alertStatusNum3 = 0;	
		
		$scope.records = [];	
		$interval(getData, 60000);
		
		$scope.expand = function(record){
			record.isExpanded = !record.isExpanded;
			trResize();	
		}
		
		$scope.expandAll = function(){
			for(var i=0; i<$scope.records.length; i++){
				$scope.records[i].isExpanded = true;
			}	
			trResize();	
		}
		
		$scope.collapseAll = function(){			
			for(var i=0; i<$scope.records.length; i++){
				$scope.records[i].isExpanded = false;
			}	
			trResize();	
		}
		
		function getData(){
			// refresh
			$scope.today = new moment().format(dateFormat);
			//$scope.alertStatusNum = 0;
			//$scope.alertStatusNum1 = 0;
			//$scope.alertStatusNum2 = 0;
			//$scope.alertStatusNum3 = 0;	
			//$scope.records = [];
			//$scope.totalNum = 0;
			//$("#load_screen").show();
			$http({
				method: 'GET',
				url:   'GetAllAlarmConsole.do',	//host + 'data.json',
				headers: {
			      "Accept": "application/json;charset=utf-8"
			   },
			   dataType:"json"
			}).then(function successCallback(response) {
				//$("#load_screen").hide();
				$scope.totalNum = response.data.length;
				if($scope.records.length == 0){
					for(var i=response.data.length-1; i>=0; i--){
						response.data[i].isExpanded = false;
						response.data[i].isAlert = false;
						response.data[i].issueDate = response.data[i].summary.defect_IssueDateTime;
						response.data[i].statusID = response.data[i].summary.defect_StatusId;
						$scope.records.push(response.data[i]);
						//$scope.records.unshift(response.data[i]);
						//console.log("all new case");
					}
				}
				else{
					for(var i=0; i<response.data.length; i++){					 
						var existObj = $filter('filter')($scope.records, function (item) {
							return item.summary.defect_Code == response.data[i].summary.defect_Code;
						})[0];

						if(existObj != null){
							existObj.alertStatus = response.data[i].alertStatus;
							existObj.issueDate = response.data[i].summary.defect_IssueDateTime;	
							existObj.statusID = response.data[i].summary.defect_StatusId;
							existObj.summary = response.data[i].summary;
							//existObj.details = response.data[i].details;	
							for(var k=0; k<response.data[i].details.length; k++){
								var existDetail = $filter('filter')(existObj.details, function (item) {
									return item.defect_ReplyTime == response.data[i].details[k].defect_ReplyTime;
								})[0];
								if(existDetail != null){
									existDetail = response.data[i].details[k];
								}else{
									existObj.details.unshift(response.data[i].details[k]);
								}								
							}				

							for(var k=0; k<existObj.details.length; k++){	
								var needDelete = true;
								for(var j=0; j<response.data[i].details.length; j++){
									if(response.data[i].details[j].defect_ReplyTime == existObj.details[k].defect_ReplyTime){
										needDelete = false;	
										break;
									}
								}	
								if(needDelete){			
									var idx = existObj.details.indexOf(existObj.details[k]);
									if(idx > -1) {
										existObj.details.splice(idx, 1);
									}
								}						
							}
							
							existObj.details = $filter('orderBy')(existObj.details, ['-defect_ReplyTime']);	
							//console.log("existObj update");
						}else{
							response.data[i].isExpanded = false;
							response.data[i].isAlert = false;
							$scope.records.push(response.data[i]);
							//console.log("new case insert");
						}
					}

					for(var i=0; i<$scope.records.length; i++){	
						//console.log("find need delete");	
						var needDelete = true;
						for(var j=0; j<response.data.length; j++){
							if(response.data[j].summary.defect_Code == $scope.records[i].summary.defect_Code){
								//console.log("no delete: " + $scope.records[i].summary.defect_Code);	
								needDelete = false;	
								break;
							}
						}	
						if(needDelete){			
							var idx = $scope.records.indexOf($scope.records[i]);
							if(idx > -1) {
								$scope.records.splice(idx, 1);
								//console.log("existObj delete: " + $scope.records[i].summary.defect_Code);
							}
						}						
					}
				}
				
				$scope.norecords = $filter('filter')($scope.records, function(item){
			        return item.statusID != 'C' && item.statusID != 'D';
				});					

				$scope.dcrecords = $filter('filter')($scope.records, function(item){
			        return item.statusID == 'C' || item.statusID == 'D';
				});
				
				$scope.dcrecords = $filter('orderBy')($scope.dcrecords, ['-statusID', '-alertStatus', '-issueDate']);
				$scope.norecords = $filter('orderBy')($scope.norecords, ['-alertStatus', '-issueDate']);					
				$scope.records = $scope.norecords.concat($scope.dcrecords);
				
				$timeout(function () {
					trResize();	
					$scope.alertStatusNum = 0;
					$scope.alertStatusNum1 = 0;
					$scope.alertStatusNum2 = 0;
					$scope.alertStatusNum3 = 0;	
					for(var i=0; i<$scope.records.length; i++){	
						if($scope.records[i].alertStatus != 0){
							$scope.records[i].isAlert = true;
							$scope.alertStatusNum++;
							if($scope.records[i].alertStatus == 1){
								$scope.alertStatusNum1++;
							}else if($scope.records[i].alertStatus == 2){
								$scope.alertStatusNum2++;
							}else if($scope.records[i].alertStatus == 3){
								$scope.alertStatusNum3++;
							}
						}else{
							$scope.records[i].isAlert = false;							
						}
					}
					
					$scope.$apply();
				}, 100);
			});
		}
		
		getData();
	});
</script>	
<script>
menu_toggle("sub_dm");
menu_select("sub_dm_alarmConsole");
</script>
