app.controller('loginController', ['$rootScope',
                                   '$scope', 
                                   '$location',
                                   '$http',
                                   'halClient',
	function($rootScope, $scope, $location, $http, halClient) {

	 	$scope.error = $location.search()['error'];

		var authenticate = function(callback) {

			$http.get('user').success(function(data) {
				if (data.name) {
					$rootScope.authenticated = true;
				} else {
					$rootScope.authenticated = false;
				}
				callback && callback();
			}).error(function() {
				$rootScope.authenticated = false;
				callback && callback();
			});
		};

		authenticate();

		$scope.credentials = {};
		$scope.login = function() {
			$http.post('login', $.param($scope.credentials), {
				headers : {
					"content-type" : "application/x-www-form-urlencoded"
				}
			}).success(function(data) {
				authenticate(function() {
					if ($rootScope.authenticated) {
						console.log("Login succeeded")
						$location.path("/users");
						$scope.error = false;
						$rootScope.authenticated = true;
					} else {
						console.log("Login failed with redirect")
						$location.path("/login");
						$scope.error = true;
						$rootScope.authenticated = false;
					}
				});
			}).error(function(data) {
				console.log("Login failed")
				$location.path("/login");
				$scope.error = true;
				$rootScope.authenticated = false;
			})
		};

		$scope.logout = function() {
			$http.post('logout', {}).success(function() {
				$rootScope.authenticated = false;
				$location.path("/");
			}).error(function(data) {
				console.log("Logout failed")
				$rootScope.authenticated = false;
			});
		};

	}]);
