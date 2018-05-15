/**
 * This controller handles everything to do with the edit functionality for a given account type.
 *
 * User: rowan.massey
 * Date: 18/07/2015
 */

(function () {
    'use strict';

    moneyApp.controller("AccountEditController", ["$scope", "$state", "$stateParams", "mnyAccountService", "mnyCurrencyService", "mnyAccountTypeService", "account",
        function ($scope, $state, $stateParams, mnyAccountService, mnyCurrencyService, mnyAccountTypeService, account) {

            var vm = this;
            vm.account = account;

            /**
             * This method updates the user details, changed in the scope object.
             */
            vm.updateAccount = function () {
                mnyAccountService.updateAccount($stateParams.id, vm.account).then(function () {
                    $state.transitionTo("account.list");
                });
            };

            /**
             * This method cancels the current operation.
             */
            vm.cancel = function () {
                $state.transitionTo("account.list");
            };

            /**
             * This method searches for the typed in currency.
             *
             * @param currency The name of the currency which needs to be searched for.
             */
            vm.searchCurrency = function (currency) {
                return mnyCurrencyService.searchCurrency(currency);
            };

            /**
             * This method searches for the typed in type of account.
             *
             * @param accountType The name of the account type which needs to be searched for.
             */
            vm.searchAccountTypes = function (accountType) {
                return mnyAccountTypeService.searchAccountTypes(accountType);
            };

        }]);
})();