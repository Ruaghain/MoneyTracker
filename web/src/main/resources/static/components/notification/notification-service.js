/**
 * This is the notification service that will broadcast all of the message notifications which will be intercepted
 * by the relevant directive.
 *
 * User: rowan.massey
 * Date: 20/03/2015
 * Time: 21:08
 *
 */

(function () {
    "use strict";

    moneyApp.service("mnyNotificationService", ["$rootScope", function ($rootScope) {

        var notificationService = this;

        notificationService.send = function (message, type) {
            $rootScope.$broadcast("mnyNotificationEvent", {
                notificationMessage: message,
                notificationType: type
            });
        };
    }]);
})();