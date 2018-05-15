/**
 * This controller deals with all of the front end functionality that deletes a user.
 *
 * User: rowan.massey
 * Date: 03/01/2015
 */

(function () {
    "use strict";

    moneyApp.controller("UserListController", ["$scope", "$location", "mnyUserService", "users",
        function ($scope, $location, mnyUserService, users) {

            var vm = this;

            vm.users = users;

            /**
             * This method gets all of the users from the database.
             */
            vm.getUsers = function () {
                mnyUserService.getAllUsers().then(function (userData) {
                    vm.users = userData;
                });
            };

            /**
             * This method removes the given user location from the database.
             */
            vm.remove = function (id) {
                mnyUserService.remove(id).then(function () {
                    vm.getUsers();
                });
            };
        }]);
})();