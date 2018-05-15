describe('Controller: UserEditController', function () {
    beforeEach(module('moneyApp'));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        $stateParams,
        userCreateController,
        mnyUserService,
        vm,
        deferred;

    beforeEach(inject(function (_$httpBackend_) {
        //Need to mock up the requests for use with state
        _$httpBackend_.expectGET("api/users").respond("<div></div>");
        _$httpBackend_.expectGET("functionality/users/user.html").respond("<div></div>");
    }));

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;
        $stateParams = {id: "1"};

        deferred = $q.defer();

        mnyUserService = $injector.get("mnyUserService");
        userCreateController = $controller;

        var testUser = {
            "userName": "Admin",
            "password": "Passw0rd",
            "firstName": "Admin",
            "lastName": "User",
            "dateOfBirth": "01/01/2001"
        };

        vm = userCreateController("UserEditController", {
            $scope: $scope,
            $state: $state,
            $stateParams: $stateParams,
            mnyUserService: mnyUserService,
            user: testUser
        });
    }));

    it('Should update an existing user', function () {
        //A dummy response value that gets returned when the Save() method for a service gets called.
        var result = {
            location: "http://localhost:8080/api/users",
            expires: 0
        };

        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyUserService, "updateUser").and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.update();

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect($state.current.name).toBe("");
    });

    it('Should cancel the request', function () {
        vm.cancel();
        expect($state.current.name).toBe("");
    });
});
