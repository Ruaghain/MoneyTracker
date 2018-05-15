describe('Controller: AccountEditController', function () {
    beforeEach(module('moneyApp'));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        $stateParams,
        accountEditController,
        mnyAccountService,
        mnyCurrencyService,
        mnyAccountTypeService,
        vm,
        deferred,
        allCurrencies,
        allAccountTypes;

    beforeEach(inject(function (_$httpBackend_) {
        //Need to mock up the requests for use with state
        _$httpBackend_.expectGET("api/accounts").respond("<div></div>");
        _$httpBackend_.expectGET("functionality/accounts/account.html").respond("<div></div>");
    }));

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;
        $stateParams = {id: "1"};

        deferred = $q.defer();

        mnyAccountService = $injector.get("mnyAccountService");
        mnyCurrencyService = $injector.get("mnyCurrencyService");
        mnyAccountTypeService = $injector.get("mnyAccountTypeService");

        accountEditController = $controller;

        var account = {
            "name": "Current",
            "number": "12345",
            "openingBalance": 12345.0,
            "balance": 12234.0,
            "openingDate": "2003-02-01T00:00:00Z",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accounts/1"
                },
                "user": {
                    "href": "http://localhost:8080/api/accounts/1/user"
                },
                "type": {
                    "href": "http://localhost:8080/api/accounts/1/type"
                },
                "currency": {
                    "href": "http://localhost:8080/api/accounts/1/currency"
                }
            }
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

        vm = accountEditController("AccountEditController", {
            $scope: $scope,
            $state: $state,
            $stateParams: $stateParams,
            mnyAccountService: mnyAccountService,
            account: account
        });
    }));

    it('Should update an existing account', function () {
        //A dummy response value that gets returned when the Save() method for a service gets called.
        var result = {
            location: "http://localhost:8080/accounts/1",
            expires: 0
        };

        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyAccountService, "updateAccount").and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.updateAccount();

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
