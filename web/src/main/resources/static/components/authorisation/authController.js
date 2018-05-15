(function () {
    'use strict';

    moneyApp.controller("AuthController", ["$scope", "mnyAuthService", "$location",
        function ($scope, authService, $location) {

            var authController = $scope.authController = {};

            authController.username = "";
            authController.password = "";

            authController.login = function () {
                authService.login(authController.username, authController.password)
                    .then(function (response) {
                        $location.path("/landingPage");
                    }, function (reject) {
                        console.log("Login error");
                    });
                authController.password = "";
            };

            authController.isAuthenticated = function () {
                return authService.getAuthToken() !== undefined;
            };

            authController.logout = function () {
                authController.authService.logout();
                authController.username = "";
                authController.password = "";
                authController.location.path('/');
            };
        }]);
})();