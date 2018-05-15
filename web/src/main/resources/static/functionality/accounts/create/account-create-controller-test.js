describe('Controller: AccountCreateController', function () {
    beforeEach(module('moneyApp'));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        accountCreateController,
        mnyAccountService,
        mnyCurrencyService,
        mnyAccountTypeService,
        vm,
        deferred,
        allCurrencies,
        allAccountTypes;

    //Need to mock up the requests for use with state.
    beforeEach(inject(function (_$httpBackend_) {
        _$httpBackend_.expectGET("api/accounts").respond("<div></div>");
        _$httpBackend_.expectGET("functionality/accounts/account.html").respond("<div></div>");
    }));

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;

        deferred = $q.defer();

        mnyAccountService = $injector.get("mnyAccountService");
        mnyCurrencyService = $injector.get("mnyCurrencyService");
        mnyAccountTypeService = $injector.get("mnyAccountTypeService");

        accountCreateController = $controller;

        vm = accountCreateController('AccountCreateController', {
            $scope: $scope,
            $state: $state,
            mnyAccountService: mnyAccountService,
            mnyCurrencyService: mnyCurrencyService,
            mnyAccountTypeService: mnyAccountTypeService
        });

        vm.account = {
            "name": "Current",
            "number": "12345",
            "balance": "2000.00",
            "openingDate": "2015-03-17T18:49:41Z"
        };

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

        allAccountTypes = {
            "accountTypes": [{
                "id": 1,
                "name": "Credit",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/accountTypes/1"
                    }
                }
            }, {
                "id": 2,
                "name": "Savings",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/accountTypes/2"
                    }
                }
            }]
        };
    }));

    it('Should add a new account', function () {
        //A dummy response value that gets returned when the Save() method for a service gets called.
        var result = {
            headers: function () {
                return {
                    location: "http://localhost:8080/api/accounts/1",
                    expires: 0
                };
            }
        };

        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyAccountService, 'save').and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.addAccount();

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(vm.accountLocation).toBe("http://localhost:8080/api/accounts/1");
        expect($state.current.name).toBe("");
    });

    it('Should cancel the request', function () {
        vm.cancel();
        expect($state.current.name).toBe("");
    });

    it('Should create an accountDto object', function () {
        var result = vm.getAccountDto(vm.account);
        expect(result.name).toBe("Current");
        expect(result.number).toBe("12345");
        expect(result.balance).toBe("2000.00");
        expect(result.openingDate).toBe("2015-03-17T18:49:41Z");
    });

    it('Should get all currencies', function () {
        spyOn(mnyCurrencyService, "searchCurrency").and.returnValue(deferred.promise);
        deferred.resolve(allCurrencies);
        var currencies = [];
        vm.searchCurrency().then(function (data) {
            currencies = data;
        });

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(currencies.currencies[0].name).toBe("Euro");
        expect(currencies.currencies[1].name).toBe("British Pound");
    });

    it('Should get all account types', function () {
        spyOn(mnyAccountTypeService, "searchAccountTypes").and.returnValue(deferred.promise);
        deferred.resolve(allAccountTypes);
        var accountTypes = [];
        vm.searchAccountTypes().then(function (data) {
            accountTypes = data;
        });

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(accountTypes.accountTypes[0].name).toBe("Credit");
        expect(accountTypes.accountTypes[1].name).toBe("Savings");
    });
});
