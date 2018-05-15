/**
 *
 * User: rowan.massey
 * Date: 11/07/2015
 *
 */
describe('Controller: AccountTypeCreateController', function () {
    beforeEach(module('moneyApp'));

    var $rootScope,
        $state,
        $injector,
        $q,
        accountTypeCreateController,
        mnyAccountTypeService,
        vm,
        deferred,
        $httpBackend;

    //Need to mock up the requests for use with state.
    beforeEach(inject(function (_$httpBackend_) {
        _$httpBackend_.expectGET("api/accountTypes").respond("<div></div>");
        _$httpBackend_.expectGET("functionality/accountType/accountType.html").respond("<div></div>");
    }));

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_, _$httpBackend_) {

        $rootScope = _$rootScope_.$new();

        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;
        $httpBackend = _$httpBackend_;

        deferred = $q.defer();

        mnyAccountTypeService = $injector.get("mnyAccountTypeService");

        accountTypeCreateController = $controller;

        vm = accountTypeCreateController('AccountTypeCreateController', {
            $scope: $rootScope,
            $state: $state,
            mnyCurrencyService: mnyAccountTypeService
        });

        vm.currency = {
            "id": 1,
            "name": "Current",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accountTypes/1"
                }
            }
        };
    }));

    it('Should add a new account type', function () {

        //A dummy response value that gets returned when the Save() method for a service gets called.
        var result = {
            headers: function () {
                return {
                    location: "http://localhost:8080/api/accountTypes/1",
                    expires: 0
                };
            }
        };

        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyAccountTypeService, 'save').and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.addAccountType();

        $rootScope.$digest();
        expect(vm.accountTypeLocation).toBe("http://localhost:8080/api/accountTypes/1");
    });

    it('Should cancel the request', function () {
        vm.cancel();

        $rootScope.$digest();

        //TODO: Need to figure out why the state name isn't been set on the transiationTo.
        expect($state.current.name).toBe("");
    });

    it('Should create an AccountTypeDto object', function () {
        var result = vm.getAccountTypeDto(vm.currency);
        expect(result.name).toBe("Current");
    });
});
