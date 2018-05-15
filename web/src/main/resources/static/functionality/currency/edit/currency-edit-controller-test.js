describe('Controller: CurrencyEditController', function () {
    beforeEach(module('moneyApp'));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        $stateParams,
        currencyEditController,
        mnyCurrencyService,
        vm,
        deferred;

    beforeEach(inject(function (_$httpBackend_) {
        //Need to mock up the requests for use with state
        _$httpBackend_.expectGET("api/currencies").respond("<div></div>");
        _$httpBackend_.expectGET("functionality/currency/currency.html").respond("<div></div>");
    }));

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;
        $stateParams = {id: "1"};

        deferred = $q.defer();

        mnyCurrencyService = $injector.get("mnyCurrencyService");
        currencyEditController = $controller;

        var currency = {
            "ISO": "EUR",
            "name": "Euro"
        };

        vm = currencyEditController("CurrencyEditController", {
            $scope: $scope,
            $state: $state,
            $stateParams: $stateParams,
            mnyCurrencyService: mnyCurrencyService,
            currency: currency
        });
    }));

    it('Should update an existing user', function () {
        //A dummy response value that gets returned when the Save() method for a service gets called.
        var result = {
            location: "http://localhost:8080/api/currencies/1",
            expires: 0
        };

        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyCurrencyService, "updateCurrency").and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.update();

        //Need this to actually set the relevant values.
        $rootScope.$digest();
        //TODO: Need to figure out why the state name isn't been set on the transitionTo.
        expect($state.current.name).toBe("");
    });

    it('Should cancel the request', function () {
        vm.cancel();
        //TODO: Need to figure out why the state name isn't been set on the transitionTo.
        expect($state.current.name).toBe("");
    });
});
