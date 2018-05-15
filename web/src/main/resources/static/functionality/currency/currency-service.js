/**
 * This service facilitates all of the currency functionality, and interaction with the server. Only ONE instance of a
 * service is created for the application. So it can be reused.
 *
 * NOTE: Could possibly implement AngularJS $resource functionality to replace items below.
 */

(function () {
    'use strict';

    moneyApp.service("mnyCurrencyService", ["$http", "$q", "mnyBaseService", function ($http, $q, mnyBaseService) {

        var currencyService = this;
        var PATH = "api/currencies";

        /**
         * This method saves a new instance of a currency to the database.
         *
         * @param currency The currency object that the user wants saved to the database.
         * @returns {*|promise}
         */
        currencyService.save = function (currency) {
            return mnyBaseService.saveRecord(PATH, currency);
        };

        /**
         * This method deletes the currency for the given location.
         *
         * @param location The location of the currency which needs to be deleted.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        currencyService.remove = function (id) {
            var location = PATH + "/" + id;
            return mnyBaseService.remove(location);
        };

        /**
         * This method returns a list of all of the currencies defined in the database.
         *
         * @returns {*|promise}
         */
        currencyService.getAllCurrencies = function () {
            return mnyBaseService.getAllRecords(PATH, "currencies");
        };

        /**
         * This method returns a specific currency for the given location.
         *
         * @param location The location of the currency which will be retrieved.
         * @returns {*|promise}
         */
        currencyService.getCurrency = function (id) {
            var location = PATH + "/" + id;
            return mnyBaseService.getRecord(location);
        };

        /**
         * This method updates the currency, with the given payload
         *
         * @param location The location of the record to update.
         * @param data The payload of the currency which will be updated.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        currencyService.updateCurrency = function (id, data) {
            var location = PATH + "/" + id;
            return mnyBaseService.updateRecord(location, data);
        };

        /**
         * This method searches for a list of currencies that matches the name passed in.
         *
         * @param currencyValue The name of the currency for whom you are searching.
         * @returns {*}
         */
        currencyService.searchCurrency = function (currencyValue) {
            if (currencyValue) {
                currencyValue = encodeURIComponent('%' + currencyValue + '%');
            }
            var deferred = $q.defer();
            $http.get("api/currencies/search/findByName?name=" + currencyValue).then(function (data) {
                if (data.data._embedded) {
                    deferred.resolve(data.data._embedded.currencies);
                } else {
                    deferred.resolve(undefined);
                }
            }, function (error) {
                deferred.reject(error);
                throw new Error("There was an error searching for currency: " + error.message);
            });
            return deferred.promise;
        };
    }]);
})();