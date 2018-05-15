/**
 * This controller handles all of the account type list requests made to the server, and facilitates their access from
 * the front-end.
 *
 * User: rowan.massey
 * Date: 17/07/2015
 */

(function () {
    'use strict';

    moneyApp.controller("AccountTypeListController", ["$scope", "$location", "mnyAccountTypeService", "accountTypes",
        function ($scope, $location, mnyAccountTypeService, accountTypes) {

            var vm = this;

            vm.accountTypes = accountTypes;

            /**
             * This method gets all of the currencies from the database.
             */
            vm.getAccountTypes = function () {
                mnyAccountTypeService.getAllAccountTypes().then(function (accountTypeData) {
                    vm.accountTypes = accountTypeData;
                });
            };

            /**
             * This method removes the given currency from the database, for the given id .
             */
            vm.remove = function (id) {
                mnyAccountTypeService.remove(id).then(function () {
                    vm.getAccountTypes();
                });
            };
        }]);
})();