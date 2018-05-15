/**
 * This controller handles all of the account list requests made to the server, and facilitates their access from
 * the front-end.
 *
 * User: rowan.massey
 * Date: 16/01/2015
 */

(function () {
    'use strict';

    moneyApp.controller("AccountListController", ["$scope", "$location", "mnyAccountService", "accounts",
        function ($scope, $location, mnyAccountService, accounts) {

            var vm = this;

            vm.accounts = accounts;

            /**
             * This method gets all of the accounts from the database.
             */
            vm.getAccounts = function () {
                mnyAccountService.getAllAccounts().then(function (accountData) {
                    vm.accounts = accountData;
                });
            };

            /**
             * This method removes the given account location from the database.
             */
            vm.remove = function (id) {
                mnyAccountService.remove(id).then(function () {
                    vm.getAccounts();
                });
            };
        }]);
})();