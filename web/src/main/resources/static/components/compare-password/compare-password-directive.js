/**
 * This directive compares the values of two fields.
 *
 * User: rowan.massey
 * Date: 10/12/2014
 */
(function () {
    'use strict';

    var mnyComparePassword = function () {
        return {
            require: "ngModel",
            scope: {
                mnyComparePassword: "="
            },
            link: function (scope, element, attributes, ngModel) {
                scope.$watch(function () {
                    var combined;

                    if (scope.mnyComparePassword || ngModel.$viewValue) {
                        combined = scope.mnyComparePassword + '_' + ngModel.$viewValue;
                    }
                    return combined;
                }, function (value) {
                    if (value) {
                        ngModel.$parsers.unshift(function (viewValue) {
                            var origin = scope.mnyComparePassword;
                            if (origin !== viewValue) {
                                ngModel.$setValidity("mnyComparePassword", false);
                                return undefined;
                            } else {
                                ngModel.$setValidity("mnyComparePassword", true);
                                return viewValue;
                            }
                        });
                    }
                });
            }
        };
    };

    moneyApp.directive("mnyComparePassword", mnyComparePassword);

})();
