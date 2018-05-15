(function () {
    'use strict';

    /**
     * This controller handles everything to do with the creation of an account type screen.
     *
     * User: rowan.massey
     * Date: 17/07/2015
     */

    moneyApp.controller("AccountTypeCreateController", ["$scope", "$state", "mnyAccountTypeService",
        function ($scope, $state, mnyAccountTypeService) {

            var vm = this;

            vm.accountType = {};
            vm.accountTypeLocation = "";

            /**
             * This method saves a new account type.
             */
            vm.addAccountType = function () {
                var accountType = vm.getAccountTypeDto(vm.accountType);
                mnyAccountTypeService.save(accountType).then(function (accountTypeData) {
                    vm.accountTypeLocation = accountTypeData.headers().location;
                    $state.transitionTo("accountType.list");
                });
            };

            /**
             * This method cancels the current operation, and redirects to the account type list page.
             */
            vm.cancel = function () {
                $state.transitionTo("accountType.list");
            };

            /**
             * This method converts the account typr object defined in the controller, into an account type object that will be posted
             * to the server.
             *
             * @param accountType The currency object defined in the controller.
             * @returns {} account type object with only relevant account type object information populated.
             */
            vm.getAccountTypeDto = function (accountType) {
                var result = {};

                result.name = accountType.name;

                return result;
            };
        }
    ]);
})();