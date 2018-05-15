/**
 * This service facilitates all of the user functionality, and interaction with the server. Only ONE instance of a
 * service is created for the application. So it can be reused.
 *
 * NOTE: Could possibly implement AngularJS $resource functionality to replace items below.
 */

(function () {
    'use strict';

    moneyApp.service("mnyUserService", ["$http", "$q", "mnyBaseService", function ($http, $q, mnyBaseService) {

        var userService = this;
        var PATH = "api/users";

        /**
         * This method checks to see if a user, with the given username exists in the database.
         *
         * @param userName userName The name of the user to
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise} Returns true, if the user exists in the database.
         */
        userService.userExists = function (userName) {
            //This deferred object will contain the promise that we'll return. The called should ALWAYS expect a promise.
            //I could use "success", and "error" as opposed to "then" - the latter however is standard.
            var deferred = $q.defer();
            $http.get("api/users/search/existsByUserName?userName=" + userName).then(function (data) {
                //On success we want to resolve the. We could use a caching mechanism here by assigning the value to a self.exists
                //variable, and resolve to that if it's populated. e.g  . if (self.name != null) { deferred.resolve(self.name); }
                deferred.resolve(data);
            }, function (error) {
                //Reject the promise if there is a failure.
                deferred.reject(error);
                throw new Error("There was an error determining if the username was unique: " + error.message);
            });
            return deferred.promise;
        };

        /**
         * This method saves a new instance of a user to the database.
         *
         * @param user The user object that the user wants saved to the database.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        userService.save = function (user) {
            return mnyBaseService.saveRecord(PATH, user);
        };

        /**
         * This method removes a user for the given location.
         *
         * @param location
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        userService.remove = function (id) {
            var location = PATH + "/" + id;
            return mnyBaseService.remove(location);
        };

        /**
         * This function returns all the users from the database.
         *
         * @returns {*|promise}
         */
        userService.getAllUsers = function () {
            return mnyBaseService.getAllRecords(PATH, "users");
        };

        /**
         * This method returns a single user from the given location.
         *
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        userService.getUser = function (id) {
            var location = PATH + "/" + id;
            return mnyBaseService.getRecord(location);
        };

        /**
         * This method updates a user for the given location, and with the data provided.
         *
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        userService.updateUser = function (id, data) {
            var location = PATH + "/" + id;
            return mnyBaseService.updateRecord(location, data);
        };
    }]);
})();