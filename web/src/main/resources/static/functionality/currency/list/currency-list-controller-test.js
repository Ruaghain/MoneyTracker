/**
 *
 * User: rowan.massey
 * Date: 11/07/2015
 *
 */
describe("Controller: CurrencyListController", function () {
    beforeEach(module("moneyApp"));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        allCurrencies,
        currencyListController,
        mnyCurrencyService,
        vm,
        deferred;

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;

        deferred = $q.defer();

        mnyCurrencyService = $injector.get("mnyCurrencyService");
        currencyListController = $controller;

        allCurrencies = {
            "currencies": [{
                "id": 1,
                "name": "Euro",
                "iso": "EUR",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/currencies/1"
                    }
                }
            }, {
                "id": 2,
                "name": "British Pound",
                "iso": "GBP",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/currencies/2"
                    }
                }
            }]
        };

        vm = currencyListController("CurrencyListController", {
            $scope: $scope,
            $state: $state,
            mnyCurrencyService: mnyCurrencyService,
            currencies: allCurrencies
        });
    }));

    it('Should get all accounts', function () {
        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyCurrencyService, "getAllCurrencies").and.returnValue(deferred.promise);
        deferred.resolve(allCurrencies);

        vm.getCurrencies();

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(vm.currencies.currencies[0].name).toBe("Euro");
        expect(vm.currencies.currencies[1].name).toBe("British Pound");
    });

    it('Should remove a given currency', function () {
        var result = {
            data: {
                status: "201"
            }
        };

        spyOn(vm, "getCurrencies").and.returnValue(deferred.promise);
        deferred.resolve(allCurrencies);

        spyOn(mnyCurrencyService, "remove").and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.remove("api/currencies/1");

        $rootScope.$digest();

        expect(mnyCurrencyService.remove).toHaveBeenCalled();
    });
});
