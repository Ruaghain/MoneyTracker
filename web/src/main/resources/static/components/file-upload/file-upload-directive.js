/**
 * This directive facilitates the uploading of a file to the server.
 *
 * User: rowan.massey
 * Date: 02/01/2015
 */
(function () {
    'use strict';

    var mnyFileUpload = function ($parse) {
        return {
            restrict: 'E',
            //scope: {
            //    datasource: '=', //This means an attribute called datasource will be passed in a reference.
            //    add: '&' //This means that an attribute called 'add' will defined the function to be called.
            //},
            link: function (scope, element, attributes) {
            },
            controller: function ($scope) {
                $scope.processHeading = function (file) {
                    var rawFile = new XMLHttpRequest();
                    rawFile.open("GET", file.files[0], false);
                    rawFile.onreadystatechange = function () {
                        if (rawFile.readyState === 4) {
                            if (rawFile.status === 200 || rawFile.status === 0) {
                                var allText = rawFile.responseText;
                            }
                        }
                    };
                    rawFile.send(null);
                };
            },
            templateUrl: "/components/file-upload/file-upload.html"
        };
    };

    moneyApp.directive("mnyFileUpload", mnyFileUpload);
})();
