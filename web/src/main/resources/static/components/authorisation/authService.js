(function () {
    'use strict';

    moneyApp.service('mnyAuthService', ['$localStorage', 'mnyUserService', function ($localStorage, mnyUserService) {

        var authService = this;

        authService.userName = "";
        authService.authToken = null;

        authService.storage = $localStorage;

        if (authService.storage.authService === null)
            authService.storage.authService = {};

        authService.storage = authService.storage.authService;

        /**
         *
         * @param username
         * @param password
         * @returns {*}
         */
        authService.login = function (username, password) {
//        authService.authToken = 'Basic ' + CryptoJS.enc.Utf8.parse(username + ':' + CryptoJS.SHA256(password).toString(CryptoJS.enc.Base64)).toString(CryptoJS.enc.Base64);
            authService.userName = username;
            authService.authToken = "Basic " + CryptoJS.enc.Utf8.parse(username + ':' + password).toString(CryptoJS.enc.Base64);
            return authService.isAuthenticated();
        };

        /**
         *
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        authService.isAuthenticated = function () {
            return mnyUserService.userExists(authService.userName);
        };

        /**
         *
         * @returns {null|*}
         */
        authService.getAuthToken = function () {
            if (authService.storage.authToken !== null) {
                authService.authToken = authService.storage.authToken;
            }
            return authService.authToken;
        };

        /**
         *
         */
        authService.logout = function () {
            authService.authToken = null;
            authService.firstName = "";
            authService.lastName = "";
            delete authService.storage.authToken;
            delete authService.storage.userName;
        };
    }]);
})();