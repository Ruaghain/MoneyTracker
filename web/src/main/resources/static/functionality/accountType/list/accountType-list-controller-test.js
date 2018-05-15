/**
 *
 * User: rowan.massey
 * Date: 11/07/2015
 *
 */
describe("Controller: AccountTypeListController", function () {
    beforeEach(module("moneyApp"));

    var $rootScope,
        $scope,
        $location,
        $injector,
        $q,
        allAccountTypes,
        accountTypeListController,
        mnyAccountTypeService,
        vm,
        deferred;

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$location_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $location = _$location_;
        $injector = _$injector_;
        $q = _$q_;

        deferred = $q.defer();

        mnyAccountTypeService = $injector.get("mnyAccountTypeService");
        accountTypeListController = $controller;

        allAccountTypes = {
            "accountTypes": [{
                "id": 1,
                "name": "Current",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/accountTypes/1"
                    }
                }
            }, {
                "id": 2,
                "name": "Credit Card",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/accountTypes/2"
                    }
                }
            }]
        };

        vm = accountTypeListController("AccountTypeListController", {
            $scope: $scope,
            $location: $location,
            mnyAccountTypeService: mnyAccountTypeService,
            accountTypes: allAccountTypes
        });
    }));

    it('Should get all account types', function () {
        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyAccountTypeService, "getAllAccountTypes").and.returnValue(deferred.promise);
        deferred.resolve(allAccountTypes);

        vm.getAccountTypes();

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(vm.accountTypes.accountTypes[0].name).toBe("Current");
    });

    it('Should remove a given account type', function () {
        var result = {
            data: {
                status: "201"
            }
        };

        spyOn(vm, "getAccountTypes").and.returnValue(deferred.promise);
        deferred.resolve(allAccountTypes);

        spyOn(mnyAccountTypeService, "remove").and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.remove("api/accountTypes/1");

        $rootScope.$digest();

        expect(mnyAccountTypeService.remove).toHaveBeenCalled();
    });
});
