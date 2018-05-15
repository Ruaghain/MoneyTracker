/**
 * This controller handles everything to do with the edit functionality for a given user.
 *
 * User: rowan.massey
 * Date: 03/01/2015
 */

(function () {
    'use strict';

    moneyApp.controller("UserEditController", ["$scope", "$state", "$stateParams", "mnyUserService", "user",
        function ($scope, $state, $stateParams, mnyUserService, user) {

            var vm = this;
            vm.user = user;

            /**
             * This method updates the user details, changed in the scope object.
             */
            vm.update = function () {
                mnyUserService.updateUser($stateParams.id, vm.user).then(function () {
                    $state.transitionTo("user.list");
                });
            };

            /**
             * This method cancels the current operation.
             */
            vm.cancel = function () {
                $state.transitionTo("user.list");
            };
        }]);
})();