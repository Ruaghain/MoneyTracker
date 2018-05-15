(function () {
    'use strict';

    /**
     * This controller handles everything to do with the creation of a user screen.
     *
     * User: rowan.massey
     * Date: 03/01/2015
     */

    moneyApp.controller("UserCreateController", ["$scope", "$state", "mnyUserService",
        "mnyAddressService", "mnyBaseService",
        function ($scope, $state, mnyUserService, mnyAddressService, mnyBaseService) {

            var vm = this;

            vm.user = {};
            vm.userLocation = "";
            vm.addressLocation = "";

            /**
             * This method saves a new user.
             */
            vm.addUser = function () {
                var user = vm.getUserDto(vm.user);
                mnyUserService.save(user).then(function (userData) {
                    vm.userLocation = userData.headers().location;
                    //mnyAuthService.login(user.userName, user.password);
                    var address = vm.getAddressDto(vm.user);
                    mnyAddressService.save(address).then(function (addressData) {
                        vm.addressLocation = addressData.headers().location;
                        mnyBaseService.relate(vm.userLocation, "address", vm.addressLocation).then(function (relationshipData) {
                            vm.user = {};
                        });
                    });
                    $state.transitionTo("user.list");
                });
            };

            /**
             * This method cancels the current operation, and redirects to the users page.
             */
            vm.cancel = function () {
                $state.transitionTo("user.list");
            };

            /**
             * This method converts the user object defined in the controller, into a user object that will be posted
             * to the server.
             *
             * @param user The user object defined in the controller.
             * @returns {} user object with only relevant user object information populated.
             */
            vm.getUserDto = function (user) {
                var result = {};

                result.userName = user.userName;
                result.password = user.password;
                result.firstName = user.firstName;
                result.lastName = user.lastName;
                result.dateOfBirth = new Date(user.dateOfBirth).toISOString();

                return result;
            };

            /**
             * This method converts the user object defined in the controller, into an address object that will be posted
             * to the server.
             *
             * @param user The user object defined in the controller with address information.
             * @returns {} address object, with only address relevant information populated.
             */
            vm.getAddressDto = function (user) {
                var addressResult = {};

                addressResult.address1 = user.address1;
                addressResult.address2 = user.address2;
                addressResult.address3 = user.address3;
                addressResult.address4 = user.address4;
                addressResult.city = user.city;
                addressResult.county = user.county;

                return addressResult;
            };
        }
    ]);
})();