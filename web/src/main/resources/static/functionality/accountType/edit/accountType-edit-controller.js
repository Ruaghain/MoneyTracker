/**
 * This controller handles everything to do with the edit functionality for a given account type.
 *
 * User: rowan.massey
 * Date: 03/01/2015
 */

(function () {
    'use strict';

    moneyApp.controller("AccountTypeEditController", ["$scope", "$state", "$stateParams", "mnyAccountTypeService", "accountType",
        function ($scope, $state, $stateParams, mnyAccountTypeService, accountType) {

            var vm = this;
            vm.accountType = accountType;

            /**
             * This method updates the user details, changed in the scope object.
             */
            vm.update = function () {
                mnyAccountTypeService.updateAccountType($stateParams.id, vm.accountType).then(function () {
                    $state.transitionTo("accountType.list");
                });
            };

            /**
             * This method cancels the current operation.
             */
            vm.cancel = function () {
                $state.transitionTo("accountType.list");
            };
        }]);
})();