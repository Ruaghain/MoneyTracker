/**
 *
 * User: rowan.massey
 * Date: 11/07/2015
 *
 */
describe('Controller: CurrencyCreateController', function () {
    beforeEach(module('moneyApp'));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        currencyCreateController,
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

        deferred = $q.defer();

        mnyCurrencyService = $injector.get("mnyCurrencyService");

        currencyCreateController = $controller;

        vm = currencyCreateController('CurrencyCreateController', {
            $scope: $scope,
            $state: $state,
            mnyCurrencyService: mnyCurrencyService
        });

        vm.currency = {
            "id": 1,
            "name": "Euro",
            "iso": "EUR",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/currencies/1"
                }
            }
        };
    }));

    it('Should add a new currency', function () {
        //A dummy response value that gets returned when the Save() method for a service gets called.
        var result = {
            headers: function () {
                return {
                    location: "http://localhost:8080/api/currencies/1",
                    expires: 0
                };
            }
        };

        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyCurrencyService, 'save').and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.addCurrency();

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(vm.currencyLocation).toBe("http://localhost:8080/api/currencies/1");
        expect($state.current.name).toBe("");
    });

    it('Should cancel the request', function () {
        vm.cancel();
        expect($state.current.name).toBe("");
    });

    it('Should create a CurrencyDto object', function () {
        var result = vm.getCurrencyDto(vm.currency);
        expect(result.iso).toBe("EUR");
        expect(result.name).toBe("Euro");
    });
});
