/**
 * This file tests all of the exception handler decoration, for notifications
 *
 * User: rowan.massey
 * Date: 17/03/2015
 * Time: 16:14
 */

describe('Decorator: $exceptionHandler', function () {
    beforeEach(module('moneyApp'));

    var mnyNotificationService,
        $rootScope,
        $timeout,
        $log;

    beforeEach(inject(function (_$log_, _$timeout_, _$rootScope_, _mnyNotificationService_) {
        $log = _$log_;
        $timeout = _$timeout_;
        $rootScope = _$rootScope_;
        mnyNotificationService = _mnyNotificationService_;

        spyOn($rootScope, "$broadcast");
    }));

    it("Should log a thrown exception", function () {
        spyOn(mnyNotificationService, "send").and.callThrough();

        function errorTimer() {
            $timeout(function () {
                $log.error("This is an error");
                throw new Error("An exception which will get notified to the user.");
            });
            $timeout.flush();
        }

        expect(errorTimer).toThrow();
        expect(mnyNotificationService.send).toHaveBeenCalled();
        expect($rootScope.$broadcast).toHaveBeenCalledWith("mnyNotificationEvent",
            {
                notificationMessage: "An exception which will get notified to the user.",
                notificationType: "error"
            });
        expect($log.error.logs).toEqual([["This is an error"]]);
    });
});
