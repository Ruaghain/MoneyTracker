/**
 * This controller handles everything to do with the edit functionality for a given account type.
 *
 * User: rowan.massey
 * Date: 03/01/2015
 */

(function () {
    'use strict';

    moneyApp.controller("CurrencyEditController", ["$scope", "$state", "$stateParams", "mnyCurrencyService", "currency",
        function ($scope, $state, $stateParams, mnyCurrencyService, currency) {

            var vm = this;
            vm.currency = currency;

            /**
             * This method updates the user details, changed in the scope object.
             */
            vm.update = function () {
                mnyCurrencyService.updateCurrency($stateParams.id, vm.currency).then(function () {
                    $state.transitionTo("currency.list");
                });
            };

            /**
             * This method cancels the current operation.
             */
            vm.cancel = function () {
                $state.transitionTo("currency.list");
            };
        }]);
})();