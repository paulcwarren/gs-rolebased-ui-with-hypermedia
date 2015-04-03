var app = angular.module('gsRoleBasedUI', [ 'ngRoute', 'angular-hal' ]);

app.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '/js/app/app.html',
		controller : 'appController'
	})
	$routeProvider.when('/users', {
		templateUrl : '/js/users/users.html',
		controller : 'usersController',
		resolve: {
       	 usersResource: function($rootScope) {
       		if ($rootScope.resource)
        		 return $rootScope.resource.$get('users', {
        	            linksAttribute: "_links",
        	            embeddedAttribute: "_embedded"
        	        });
       		else
       			 return {};
       	 	}
        }
	}).when('/login', {
		templateUrl : '/js/login/login.html',
		controller : 'loginController'
	}).otherwise({
		redirectTo : '/'
	});
})

.controller('appController', ['$rootScope',
                              '$scope',
			                  'halClient',
		function($rootScope, $scope, halClient) {

			$scope.root = function() {
				halClient.$get('/api', {
					linksAttribute : "_links"
				}).then(function(resource) {
					$rootScope.resource = resource;
				});
			};

			$scope.root();

		} ]);
