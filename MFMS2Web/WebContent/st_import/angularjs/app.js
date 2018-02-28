'use strict';

var API_SERVER = "http://119.247.242.207:3000";

var app = angular.module("app", []);

app.directive('convertToNumber', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attrs, ngModel) {
            ngModel.$parsers.push(function (val) {
                return parseInt(val, 10);
            });
            ngModel.$formatters.push(function (val) {
                return '' + val;
            });
        }
    };
});

app.directive('convertToDate', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attrs, ngModel) {
            ngModel.$parsers.push(function (val) {
                if (val) {
                    return val.toISOString().slice(0, 10);
                }
            });
            ngModel.$formatters.push(function (val) {
                if (val) {
                    return new Date(val);
                }
            });
        }
    };
});

app.controller('mainController', function ($scope, $http) {

    $scope.isUpdating = false;

    $http.get(API_SERVER + "/roles").then(function (resp) {
        $scope.roles = resp.data;

    });

    $http.get(API_SERVER + "/genders").then(function (resp) {
        $scope.genders = resp.data;
    });

    $http.get(API_SERVER + "/buildings").then(function (resp) {
        $scope.buildings = resp.data;
    });

    $scope.selectTower = function (tower_id) {
        $http.get(API_SERVER + "/buildings/" + tower_id + "/floors").then(function (resp) {
            $scope.floors = resp.data;
        });
    };

    $scope.selectFloor = function (floor_id) {
        $http.get(API_SERVER + "/floors/" + floor_id + "/rooms").then(function (resp) {
            $scope.rooms = resp.data;
        });
    };

    $scope.search = function () {
        console.log($scope.selectedTower, $scope.selectedFloor, $scope.selectedRoom);

        if ($scope.selectedRoom) {
            $http.get(API_SERVER + "/rooms/" + $scope.selectedRoom + "/tenantToRooms")
                .then(function (resp) {
                    if (resp.data.length === 1) {
                        return $http.get(API_SERVER + "/tenants/" + resp.data[0].tenantId);
                    }
                    return null;
                })
                .then(function (resp) {
                    if (resp !== null) {
                        $scope.masterTenant = angular.copy(resp.data);
                        $scope.tenant = resp.data;
                        return $http.get(API_SERVER + "/tenants/" + $scope.tenant.id + "/peoples");
                    }
                    return null;
                })
                .then(function (resp) {
                    if (resp !== null) {
                        var data = resp.data.reduce(function(main, obj) {
                            main[obj.id] = obj;
                            return main;
                        }, {});
                        $scope.masterPeoples = angular.copy(data);
                        $scope.peoples = data;
                        $scope.creatingIdx = -1;
                        var firstObj = $scope.peoples[Object.keys($scope.peoples)[0]];
                        $scope.peoples[$scope.creatingIdx] = {
                            id: $scope.creatingIdx,
                            name: "",
                            primaryContact: "",
                            otherContact: "",
                            email: "",
                            remark: "",
                            tenantId: firstObj.tenantId,
                            roleId: null,
                            genderId: null
                        };
                        $("html, body").animate({scrollTop: $(document).height()});
                    } else {
                        $scope.clear();
                    }
                });
        } else {
            $scope.clear();
        }
    };

    $scope.clear = function () {
        $scope.masterTenant = null;
        $scope.tenant = null;
        $scope.masterPeoples = null;
        $scope.peoples = null;
    };

    $scope.reset = function () {
        $scope.tenant = angular.copy($scope.masterTenant);
        $scope.peoples = angular.copy($scope.masterPeoples);
        $scope.creatingIdx = -1;
        var firstObj = $scope.peoples[Object.keys($scope.peoples)[0]];
        $scope.peoples[$scope.creatingIdx] = {
            id: $scope.creatingIdx,
            name: "",
            primaryContact: "",
            otherContact: "",
            email: "",
            remark: "",
            tenantId: firstObj.tenantId,
            roleId: null,
            genderId: null
        };
    };

    $scope.update = function () {
        $scope.isUpdating = true;

        var updateCount = 0;

        if (!angular.equals($scope.masterTenant, $scope.tenant)) {
            updateCount += 1;
            $http.put(API_SERVER + "/tenants/" + $scope.masterTenant.id, $scope.tenant).then(function (resp) {
                $scope.masterTenant = resp.data;
                updateCount -= 1;
                updateCount === 0 && $scope.updated();
            });
        }

        for (var id in $scope.peoples) {
            var before = $scope.masterPeoples[id];
            var after = $scope.peoples[id];
            if (!angular.equals(before, after)) {
                if (!before && after.roleId !== null) {
                    updateCount += 1;
                    delete after.id;
                    $http.post(API_SERVER + "/peoples", after).then(function (resp) {
                        $scope.masterPeoples[resp.data.id] = resp.data;
                        updateCount -= 1;
                        updateCount === 0 && $scope.updated();
                    });
                } else if (before) {
                    updateCount += 1;
                    if (after.deleted) {
                        $http.delete(API_SERVER + "/peoples/" + after.id).then($.proxy(function (resp) {
                            delete $scope.masterPeoples[this];
                            updateCount -= 1;
                            updateCount === 0 && $scope.updated();
                        }, after.id));
                    } else {
                        $http.put(API_SERVER + "/peoples/" + after.id, after).then(function (resp) {
                            $scope.masterPeoples[resp.data.id] = resp.data;
                            updateCount -= 1;
                            updateCount === 0 && $scope.updated();
                        });
                    }
                }
            }
        }

        if (updateCount === 0) {
            $scope.isUpdating = false;
        }
    };

    $scope.updated = function () {
        var $alert = $('#alert-update-success');
        $alert.removeClass("hidden");
        clearTimeout($scope.alertTimer);
        $scope.alertTimer = setTimeout(function () {
            $alert.addClass("hidden");
        }, 2000);
        $scope.isUpdating = false;
        $scope.reset();
        console.log($scope.peoples);
    };

    $scope.editing = function (id) {
        if (id === $scope.creatingIdx) {
            var firstObj = $scope.peoples[Object.keys($scope.peoples)[0]];
            $scope.creatingIdx--;
            $scope.peoples[$scope.creatingIdx] = {
                id: $scope.creatingIdx,
                name: "",
                primaryContact: "",
                otherContact: "",
                email: "",
                remark: "",
                tenantId: firstObj.tenantId,
                roleId: null,
                genderId: null
            };
        }
    };

    $scope.remove = function(id) {
        $scope.peoples[id].deleted = true;
        console.log($scope.peoples);
    };

});