/**
 * This directive will display a notification to the user, depending on the type.
 *
 * User: rowan.massey
 * Date: 22/03/2015
 */

(function () {
    "use strict";

    var mnyNotification = function ($timeout) {
        return {
            restrict: "E",
            templateUrl: "components/notification/notification.html",
            replace: true,
            scope: {},
            link: function ($scope, $element) {
                $scope.$on("mnyNotificationEvent", function (notificationObject, notificationContent) {
                    var type;
                    if (notificationContent) {
                        type = notificationContent.notificationType;
                        $scope.message = notificationContent.notificationMessage;
                    }
                    //Need to remove any existing classes so they don't just accumulate.
                    $element.removeClass();
                    //Set the class to be the type of notification i.e. "error", "warning", "information" and "success".
                    $element.addClass(type);
                    $element.css("display", "block");

                    //If the type is information, only display the notification of 5 seconds.
                    if (type === "information") {
                        $timeout(function () {
                            $element.css("display", "none");
                        }, 5000);
                    }
                });
            },
            controller: function ($scope, $element) {
                $scope.closeNotification = function () {
                    $element.css("display", "none");
                };
            }
        };
    };
    moneyApp.directive("mnyNotification", mnyNotification);
})();