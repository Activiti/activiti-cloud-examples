'use strict';

appControllers.controller('MainController', ['$rootScope', '$scope', '$http', 'authorization',
    function($rootScope, $scope, $http, authorization) {
        $scope.status = 'running...';
        $scope.profile = authorization.profile;
        $scope.isUser = authorization.hasRealmRole('user');
        $scope.routesUrl = "http://localhost:8080/application/routes";
        $scope.auditUrl = "http://localhost:8080/audit/v1/events";
        $scope.tasksUrl = "http://localhost:8080/query/v1/tasks";
        $scope.processInstancesUrl = "http://localhost:8080/query/v1/process-instances";
        $scope.processDefinitionsUrl = "http://localhost:8080/my-runtime-bundle/v1/process-definitions";
        $scope.startProcessUrl = "http://localhost:8080/my-runtime-bundle/v1/process-instances";
        $scope.startProcessPostBody = "{\n\"processDefinitionId\": \"SimpleProcess:1:4\",         \n\"variables\": {         \n\"firstName\": \"Paulo\",         \n\"lastName\": \"Silva\",         \n\"aget\": 25     \n},\n\"commandType\":\"StartProcessInstanceCmd\"\n}";
        $scope.claimTaskUrl = "http://localhost:8080/my-runtime-bundle/v1/tasks/{taskId}/claim?assignee=testuser";
        $scope.completeTaskUrl = "http://localhost:8080/my-runtime-bundle/v1/tasks/{taskId}/complete";

        $scope.getRoutes = function() {
            $http.get($scope.routesUrl).success(function(data) {
            	$scope.gwroutes = data;
            });
        }

        $scope.getAuditEvents = function() {
            $http.get($scope.auditUrl).success(function(data) {
                $scope.auditEvents = data;
            });
        }

        $scope.getTasks = function() {
            $http.get($scope.tasksUrl).success(function(data) {
                $scope.tasks = data;
            });
        }

        $scope.getProcessInstances = function() {
            $http.get($scope.processInstancesUrl).success(function(data) {
                $scope.processInstances = data;
            });
        }

        $scope.getProcessDefinitions = function() {
            $http.get($scope.processDefinitionsUrl).success(function(data) {
                $scope.processDefinitions = data;
            });
        }

        $scope.startProcess = function() {
            $http.post($scope.startProcessUrl, $scope.startProcessPostBody).then(function(data) {
                $scope.startProcessResponse = data.data;
            });
        }

        $scope.claimTask = function() {
            $http.post($scope.claimTaskUrl).then(function(data) {
                $scope.claimTaskResponse = data.data;
            });
        }

        $scope.completeTask = function() {
            $http.post($scope.completeTaskUrl).then(function(data) {
                $scope.completeTaskResponse = data.data;
                if(!$scope.completeTaskResponse){ //i.e. null or empty
                    $scope.completeTaskResponse = 'OK';
                }
            });
        }

        $scope.logout = function() {
        	authorization.logout();
        }
    }
]);