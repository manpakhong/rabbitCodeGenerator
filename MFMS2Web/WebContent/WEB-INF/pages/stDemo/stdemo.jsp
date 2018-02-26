<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="template" %>
<jsp:include page="../common/left.jsp"/>

<!-- another head here -->
<head>
    <script src="st_import/angularjs/angular.min.js"></script>
    <script src="st_import/angularjs/app.js"></script>
</head>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper" ng-controller="mainController">
    <!-- Content Header (Page header) -->
    <div>
        <section class="content-header">
            <ol class="breadcrumb">
                <li><i class="fa fa-building"></i> Shun Tak Centre</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content" style="padding-bottom: 50px;">
            <div class="row">
                <div class="col-xs-5">
                    <div class="box">
                        <div class="box-body">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label for="" class="col-sm-2 control-label">Building</label>
                                    <div class="col-sm-10">
                                        <div class="form-group">
                                            <select
                                                    class="form-control"
                                                    ng-options="tower.id as tower.name for tower in buildings track by tower.id"
                                                    ng-model="selectedTower"
                                                    ng-change="selectTower(selectedTower)"
                                            >
                                                <option value="">Please Select</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="" class="col-sm-2 control-label">Floor</label>
                                    <div class="col-sm-10">
                                        <div class="form-group">
                                            <select
                                                    class="form-control"
                                                    ng-options="floor.id as floor.level for floor in floors track by floor.id"
                                                    ng-model="selectedFloor"
                                                    ng-change="selectFloor(selectedFloor)"
                                            >
                                                <option value="">Please Select</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="" class="col-sm-2 control-label">Room</label>
                                    <div class="col-sm-10">
                                        <div class="form-group">
                                            <select
                                                    class="form-control"
                                                    ng-options="room.id as room.number for room in rooms track by room.id"
                                                    ng-model="selectedRoom"
                                            >
                                                <option value="">Please Select</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="" class="col-sm-2 control-label">&nbsp;</label>
                                    <div class="col-sm-10">
                                        <button ng-disabled="!selectedTower || !selectedFloor || !selectedRoom"
                                                type="button" class="btn btn-primary" ng-click="search()">
                                            Search
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-xs-7">
                    <div class="box">
                        <div class="box-body">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <strong>Remark</strong>
                                    </div>
                                </div>
                                <div class="form-group" ng-hide="!tenant && tenant.length !== 0">
                                    <div class="col-sm-12">
                                        <input type="text"
                                               class="form-control"
                                               ng-model="tenant.name"
                                               value="{{tenant.name}}"
                                               style="margin-left: -5px"/>
                                    </div>
                                </div>
                                <div class="form-group" ng-hide="!tenant && tenant.length !== 0">
                                    <label for="" class="col-sm-6 control-label">Commencement Date</label>
                                    <div class="col-sm-6">
                                        <input type="date" class="form-control"
                                               ng-model="tenant.commencementDate"
                                               value="{{tenant.commencementDate}}"
                                               convert-to-date
                                               style="margin-left: -5px"/>
                                    </div>
                                </div>
                                <div class="form-group" ng-hide="!tenant && tenant.length !== 0">
                                    <label for="" class="col-sm-6 control-label">Office Hours</label>
                                    <div class="col-sm-6">
                                        <input type="text" class="form-control"
                                               ng-model="tenant.officeHour"
                                               value="{{tenant.officeHour}}"
                                               style="margin-left: -5px"/>
                                    </div>
                                </div>
                                <div class="form-group" ng-hide="!tenant && tenant.length !== 0">
                                    <label for="" class="col-sm-6 control-label">Handover Date</label>
                                    <div class="col-sm-6">
                                        <input type="date" class="form-control"
                                               ng-model="tenant.handoverDate"
                                               value="{{tenant.handoverDate}}"
                                               convert-to-date
                                               style="margin-left: -5px"/>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <form>
                                <table class="table table-bordered table-hover">
                                    <thead>
                                    <tr>
                                        <th></th>
                                        <th class="col-md-1">Role</th>
                                        <th class="col-md-2">Name</th>
                                        <th class="col-md-1">Gender</th>
                                        <th class="col-md-2">Primary Contact</th>
                                        <th class="col-md-2">Other Contact</th>
                                        <th class="col-md-2">Email</th>
                                        <th class="col-md-2">Remark</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="people in peoples track by people.id" ng-hide="people.deleted">
                                        <td style="vertical-align: middle;">
                                            <a href="javascript:;" ng-click="remove(people.id)" ng-hide="$last">
                                                <span class="glyphicon glyphicon-minus-sign text-danger"></span>
                                            </a>
                                        </td>
                                        <td>
                                            <select
                                                    class="form-control"
                                                    ng-model="people.roleId"
                                                    ng-change="editing(people.id)"
                                                    convert-to-number
                                            >
                                                <option ng-repeat="role in roles"
                                                        value="{{role.id}}">
                                                    {{role.name}}
                                                </option>
                                            </select>
                                        </td>
                                        <td><input
                                                type="text"
                                                class="form-control"
                                                ng-model="people.name"
                                                ng-change="editing(people.id)"/>
                                        </td>
                                        <td>
                                            <select
                                                    class="form-control"
                                                    ng-model="people.genderId"
                                                    ng-change="editing(people.id)"
                                                    convert-to-number
                                            >
                                                <option ng-repeat="gender in genders"
                                                        value="{{gender.id}}">
                                                    {{gender.name}}
                                                </option>
                                            </select>
                                        </td>
                                        <td><input
                                                type="text"
                                                class="form-control"
                                                ng-model="people.primaryContact"
                                                ng-change="editing(people.id)"/></td>
                                        <td><input
                                                type="text"
                                                class="form-control"
                                                ng-model="people.other_contact"
                                                ng-change="editing(people.id)"/></td>
                                        <td><input
                                                type="text"
                                                class="form-control"
                                                ng-model="people.email"
                                                ng-change="editing(people.id)"/></td>
                                        <td><input
                                                type="text"
                                                class="form-control"
                                                ng-model="people.remark"
                                                ng-change="editing(people.id)"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div id="alert-update-success" style="padding: 5px;"
                 class="hidden pull-left alert alert-success">
                <strong>Update Success!</strong>
            </div>
            <button type="button" ng-disabled="!peoples || !tenant || isUpdating" class="pull-right btn btn-default"
                    ng-click="reset()">
                Reset
            </button>
            <button type="button" ng-disabled="!peoples || !tenant || isUpdating" class="pull-right btn btn-primary"
                    ng-click="update()">
                Update
            </button>
        </section><!-- /.content -->
    </div>
</div><!-- /.content-wrapper -->