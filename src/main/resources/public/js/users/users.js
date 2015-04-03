app.controller('usersController', ['$scope',
                                   'usersResource',
		function($scope, usersResource) {

			$scope.resource = usersResource;
			
			usersResource.$get('users')
				.then(function(users) {
					$scope.users = users;
				});

			// placeholder object for a new user
			$scope.newUser = {roles:["USER"]};
			
			// roles available for selection
			$scope.roles = ['USER', 'COORD', 'ADMIN'];

			// toggle selection for a given role
			$scope.toggleSelection = function(role, user) {
				var idx = user.roles.indexOf(role);
		
				// is currently selected
				if (idx > -1) {
					user.roles.splice(idx, 1);
				}
				// is newly selected
				else {
					user.roles.push(role);
				}
			};
			
			$scope.onClickAdd = function(newUser) {
				$scope.resource.$post('create', {}, newUser)
				// then re-fetch the users resource
				.then(function(response) {
					$scope.newUser = {roles: ["USER"]};
					return $scope.resource.$get('self');
				})
				.then(function(resource) {
					$scope.resource = resource;
					return $scope.resource.$get('users');
				})
				.then(function(users) {
					$scope.users = users;
				});
			};
			
			$scope.onClickRemove = function(user) {
				user.$del('delete', {})
				// then re-fetch the users resource
				.then(function(response) {
					return $scope.resource.$get('self');
				})
				.then(function(resource) {
					$scope.resource = resource;
					return $scope.resource.$get('users');
				})
				.then(function(users) {
					$scope.users = users;
				});
			};
			
			$scope.onClickSetPassword = function(user) {
				var newPassword = prompt("Please enter password");
				user.$put('password', {}, {password: newPassword});
			};
		} ]);
