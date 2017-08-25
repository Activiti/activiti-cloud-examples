'use strict';

var app = angular.module('activiti-cloud-demo', [
                                               'ngRoute',
                                               'appControllers'
                                               ]);

// configure routes
app.config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	when('/main', {
		templateUrl: 'views/main.html',
		controller: 'MainController'
	}).
	otherwise({
		redirectTo: '/main'
	});
}]);

// use bearer token when calling backend
app.config(['$httpProvider', function($httpProvider) {
	var isExpired = window._keycloak.isTokenExpired();
	var token = window._keycloak.token;
	
	if (isExpired) {
		window._keycloak.updateToken(5)
		.success(function() {
			$httpProvider.defaults.headers.common['Authorization'] = 'BEARER ' + token;
		})
		.error(function() {
			console.error('Failed to refresh token');
		});
	}

	$httpProvider.defaults.headers.common['Authorization'] = 'BEARER ' + token;

    //Enable cross domain calls
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}]);

app.config(['$sceDelegateProvider', function($sceDelegateProvider) {
        $sceDelegateProvider.resourceUrlWhitelist(['self', 'http://*/**', 'http://*/**']);

    }])

// application startup hook
app.run(['$rootScope', '$location', '$http', '$route',
         function ($rootScope, $location, $http, usersService, $route) {

}]);

var appControllers = angular.module('appControllers', []);

// provide keycloak as factory to make it injectable
angular.module('activiti-cloud-demo').factory('authorization', function ($window) {
	return $window._keycloak;
});

// on every request, authenticate user first
angular.element(document).ready(() => {
	window._keycloak = Keycloak('keycloak/keycloak.json');

	window._keycloak.init({
		onLoad: 'login-required'
	})
	.success((authenticated) => {
		if(authenticated) {
			window._keycloak.loadUserProfile().success(function(profile){
				angular.bootstrap(document, ['activiti-cloud-demo']); // manually bootstrap Angular
			});
		}
		else {
			window.location.reload();
		}
	})
	.error(function () {
		window.location.reload();
	});
});