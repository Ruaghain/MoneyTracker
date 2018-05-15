/**
 * This service facilitates all of the accountType functionality, and interaction with the server.
 */

(function () {
    'use strict';

    moneyApp.service("mnyAccountTypeService", ["$http", "$q", "mnyBaseService", function ($http, $q, mnyBaseService) {

        var accountTypeService = this;
        var PATH = "api/accountTypes";

        /**
         * This method saves a new instance of a account type to the database.
         *
         * @param accountType The currency object that the user wants saved to the database.
         * @returns {*|promise}
         */
        accountTypeService.save = function (accountType) {
            return mnyBaseService.saveRecord(PATH, accountType);
        };

        /**
         * This method deletes the currency for the given location.
         *
         * @param id The id of the currency which needs to be deleted.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        accountTypeService.remove = function (id) {
            var location = PATH + "/" + id;
            return mnyBaseService.remove(location);
        };

        /**
         * This method returns a list of all of the currencies defined in the database.
         *
         * @returns {*|promise}
         */
        accountTypeService.getAllAccountTypes = function () {
            return mnyBaseService.getAllRecords(PATH, "accountTypes");
        };

        /**
         * This method returns a specific account type for the given location.
         *
         * @param id The id of the account type which will be retrieved.
         * @returns {*|promise}
         */
        accountTypeService.getAccountType = function (id) {
            var location = PATH + "/" + id;
            return mnyBaseService.getRecord(location);
        };

        /**
         * This method updates the account type, with the given payload
         *
         * @param id The id of the record to update.
         * @param data The payload of the account type which will be updated.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        accountTypeService.updateAccountType = function (id, data) {
            var location = PATH + "/" + id;
            return mnyBaseService.updateRecord(location, data);
        };

        /**
         * This method searches for a list of account types that matches the name passed in.
         *
         * @param accountType The name of the account type for whom you are searching.
         * @returns {*}
         */
        accountTypeService.searchAccountTypes = function (accountType) {
            if (accountType) {
                accountType = encodeURIComponent('%' + accountType + '%');
            }
            var deferred = $q.defer();
            $http.get("api/accountTypes/search/findByName?name=" + accountType).then(function (data) {
                if (data.data._embedded) {
                    deferred.resolve(data.data._embedded.accountTypes);
                } else {
                    deferred.resolve(undefined);
                }
            }, function (error) {
                deferred.reject(error);
                throw new Error("There was an error searching for account type: " + error.message);
            });
            return deferred.promise;

        };

    }]);
})();