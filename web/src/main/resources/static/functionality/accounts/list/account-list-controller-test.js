describe("Controller: AccountListController", function () {
    beforeEach(module("moneyApp"));

    var $rootScope,
        $scope,
        $location,
        $injector,
        $q,
        allAccounts,
        accountListController,
        mnyAccountService,
        vm,
        deferred;

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$location_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $location = _$location_;
        $injector = _$injector_;
        $q = _$q_;

        deferred = $q.defer();

        mnyAccountService = $injector.get("mnyAccountService");
        accountListController = $controller;

        allAccounts = {
            "accounts": [{
                "name": "Current",
                "number": "12345",
                "balance": "2000.00",
                "openingDate": "2015-03-17T18:49:41Z"
            },
                {
                    "name": "Credit Card",
                    "number": "1234567",
                    "balance": "-1700.00",
                    "openingDate": "2015-03-17T18:49:41Z"
                }
            ]
        };

        vm = accountListController("AccountListController", {
            $scope: $scope,
            $location: $location,
            mnyAccountService: mnyAccountService,
            accounts: allAccounts
        });
    }));

    it('Should get all accounts', function () {
        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyAccountService, "getAllAccounts").and.returnValue(deferred.promise);
        deferred.resolve(allAccounts);

        vm.getAccounts();

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(vm.accounts.accounts[0].name).toBe("Current");
        expect(vm.accounts.accounts[1].name).toBe("Credit Card");
    });

    it('Should remove given account', function () {
        var result = {
            data: {
                status: "201"
            }
        };

        spyOn(vm, "getAccounts").and.returnValue(deferred.promise);
        deferred.resolve(allAccounts);

        spyOn(mnyAccountService, "remove").and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.remove("1");

        $rootScope.$digest();

        expect(mnyAccountService.remove).toHaveBeenCalled();
    });
});
