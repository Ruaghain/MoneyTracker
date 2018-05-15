/**
 * This service facilitates all of the address functionality, and interaction with the server.
 *
 */

(function () {
    'use strict';

    moneyApp.service("mnyAddressService", ["$http", "$q", "mnyBaseService", function ($http, $q, mnyBaseService) {

        var addressService = this;
        var PATH = "api/addresses";

        /**
         * This method saves an address record, and returns the location of the saved resource.
         *
         * @param address The address object, containing all the relevant information.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        addressService.save = function (address) {
            return mnyBaseService.saveRecord(PATH, address);
        };

        /**
         * This method retrieves the address for the given URL.
         * @param location The location of the address to retrieve.
         */
        addressService.getAddress = function (location) {
            return mnyBaseService.getRecord(location);
        };
    }]);
})();
