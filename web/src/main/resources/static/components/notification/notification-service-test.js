/**
 * This file tests all of the exception handler decoration, for notifications
 *
 * User: rowan.massey
 * Date: 17/03/2015
 * Time: 16:14
 */

describe('Service: mnyNotificationService', function () {
    beforeEach(module("moneyApp"));

    var mnyNotificationService,
        $rootScope;

    beforeEach(inject(function (_mnyNotificationService_, _$rootScope_) {
        mnyNotificationService = _mnyNotificationService_;
        $rootScope = _$rootScope_;
        spyOn($rootScope, "$broadcast");
    }));

    it("Should broadcast a warning message", function () {
        mnyNotificationService.send("This is a warning message", "warning");
        expect($rootScope.$broadcast).toHaveBeenCalledWith("mnyNotificationEvent",
            {
                notificationMessage: "This is a warning message",
                notificationType: "warning"
            });
    });

    it("Should broadcast an information message", function () {
        mnyNotificationService.send("This is an information message", "information");
        expect($rootScope.$broadcast).toHaveBeenCalledWith("mnyNotificationEvent",
            {
                notificationMessage: "This is an information message",
                notificationType: "information"
            });
    });

    it("Should broadcast an error message", function () {
        mnyNotificationService.send("This is an error message", "error");
        expect($rootScope.$broadcast).toHaveBeenCalledWith("mnyNotificationEvent",
            {
                notificationMessage: "This is an error message",
                notificationType: "error"
            });
    });
});
