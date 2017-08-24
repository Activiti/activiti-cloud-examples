'use strict';

appControllers.controller('MainController', ['$rootScope', '$scope', '$http', 'authorization',
    function($rootScope, $scope, $http, authorization) {
        $scope.status = 'running...';
        $scope.profile = authorization.profile;
        $scope.isUser = authorization.hasRealmRole('user')
        $scope.isManager = authorization.hasRealmRole('manager')
        
        $scope.getRoutes = function() {
            $http.get("http://localhost:8080/application/routes").success(function(data) {
            	$scope.gwroutes = data;
            });
        }

        $scope.getAuditEvents = function() {
            $http.get("http://localhost:8080/audit/v1/events").success(function(data) {
                $scope.auditEvents = data;
            });
        }

        $scope.getTasks = function() {
            $http.get("http://localhost:8080/query/v1/tasks").success(function(data) {
                $scope.tasks = data;
            });
        }

        $scope.getProcessInstances = function() {
            $http.get("http://localhost:8080/query/v1/process-instances").success(function(data) {
                $scope.processInstances = data;
            });
        }

        $scope.logout = function() {
        	authorization.logout();
        }
    }
]);