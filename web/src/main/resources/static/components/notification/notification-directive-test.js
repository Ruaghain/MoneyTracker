describe("Directive: mnyNotification", function () {
    var $compile,
        $rootScope,
        $timeout,
        element;

    beforeEach(module("moneyApp"));

    beforeEach(module("templates"));

    beforeEach(inject(function (_$rootScope_, _$compile_, _$timeout_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;
        $timeout = _$timeout_;

        element = angular.element("<mny-notification></mny-notification>");
        $compile(element)($rootScope.$new());
    }));

    it("Replaces the element with the appropriate content", function () {

        $rootScope.$digest();

        expect(element.html()).toContain('<span id="close" ng-click="closeNotification()">');
        expect(element.html()).toContain('<div class="ng-binding"></div>');
    });

    it("Replaces the element with the appropriate content for information broadcast", function () {
        $rootScope.$digest();

        $rootScope.$broadcast("mnyNotificationEvent", {
            notificationMessage: "Hello this is an information message.",
            notificationType: "information"
        });

        expect(element[0].className).toEqual("ng-isolate-scope information");
        expect(element[0].attributes.style.value).toEqual("display: block;");
        $timeout.flush();
        expect(element[0].attributes.style.value).toEqual("display: none;");
    });

    it("Replaces the element with the appropriate content for error broadcast", function () {
        $rootScope.$digest();

        $rootScope.$broadcast("mnyNotificationEvent", {
            notificationMessage: "Hello this is an error message.",
            notificationType: "error"
        });

        expect(element[0].className).toEqual("ng-isolate-scope error");
        expect(element[0].attributes.style.value).toEqual("display: block;");

        var scope = element.isolateScope() || element.scope();
        scope.closeNotification();
        expect(element[0].attributes.style.value).toEqual("display: none;");
    });

    it("Replaces the element with the appropriate content for warning broadcast", function () {
        $rootScope.$digest();

        $rootScope.$broadcast("mnyNotificationEvent", {
            notificationMessage: "Hello this is a warning message.",
            notificationType: "warning"
        });

        expect(element[0].className).toEqual("ng-isolate-scope warning");
        expect(element[0].attributes.style.value).toEqual("display: block;");
    });

    it("Replaces the element with the appropriate content for success broadcast", function () {
        $rootScope.$digest();

        $rootScope.$broadcast("mnyNotificationEvent", {
            notificationMessage: "Hello this is a success message.",
            notificationType: "success"
        });

        expect(element[0].className).toEqual("ng-isolate-scope success");
        expect(element[0].attributes.style.value).toEqual("display: block;");
    });
});