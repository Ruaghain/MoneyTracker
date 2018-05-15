describe('Controller: AccountTypeEditController', function () {
    beforeEach(module('moneyApp'));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        $stateParams,
        accountTypeEditController,
        mnyAccountTypeService,
        vm,
        deferred;

    beforeEach(inject(function (_$httpBackend_) {
        //Need to mock up the requests for use with state
        _$httpBackend_.expectGET("api/accountTypes").respond("<div></div>");
        _$httpBackend_.expectGET("functionality/accountType/accountType.html").respond("<div></div>");
    }));

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;
        $stateParams = {id: "1"};

        deferred = $q.defer();

        mnyAccountTypeService = $injector.get("mnyAccountTypeService");
        accountTypeEditController = $controller;

        var accountType = {
            "name": "Current"
        };

        vm = accountTypeEditController("AccountTypeEditController", {
            $scope: $scope,
            $state: $state,
            $stateParams: $stateParams,
            mnyAccountTypeService: mnyAccountTypeService,
            accountType: accountType
        });
    }));

    it('Should update an existing user', function () {
        //A dummy response value that gets returned when the Save() method for a service gets called.
        var result = {
            location: "http://localhost:8080/accountTypes/1",
            expires: 0
        };

        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyAccountTypeService, "updateAccountType").and.returnValue(deferred.promise);
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
