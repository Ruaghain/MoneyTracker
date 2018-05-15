/**
 * This class extends the existing AngularJS $exceptionHandler to add the error to the notification service that
 * will be created at a later date.
 *
 * User: rowan.massey
 * Date: 20/03/2015
 * Time: 20:58
 *
 */
(function () {
    "use strict";

    moneyApp.config(function ($provide) {
        $provide.decorator("$exceptionHandler", ["$delegate", "$injector", function ($delegate, $injector) {
            return function (exception, cause) {
                var mnyNotificationService = $injector.get("mnyNotificationService");
                mnyNotificationService.send(exception.message, "error");
                $delegate(exception, cause);
            };
        }]);
    });
})();