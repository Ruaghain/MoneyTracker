describe("Controller: UserListController", function () {
    beforeEach(module("moneyApp"));

    var $rootScope,
        $scope,
        $state,
        $injector,
        $q,
        allUsers,
        userListController,
        mnyUserService,
        vm,
        deferred;

    beforeEach(inject(function (_$rootScope_, $controller, _$q_, _$state_, _$injector_) {

        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();
        $state = _$state_;
        $injector = _$injector_;
        $q = _$q_;

        deferred = $q.defer();

        mnyUserService = $injector.get("mnyUserService");
        userListController = $controller;

        allUsers = {
            "users": [{
                "userName": "Admin",
                "password": "Passw0rd",
                "firstName": "Admin",
                "lastName": "User",
                "dateOfBirth": "2015-03-17T18:49:41Z",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/users/1"
                    },
                    "accounts": {
                        "href": "http://localhost:8080/api/users/1/accounts"
                    },
                    "address": {
                        "href": "http://localhost:8080/api/users/1/address"
                    },
                    "groups": {
                        "href": "http://localhost:8080/api/users/1/groups"
                    }
                }
            },
                {
                    "userName": "Admin1",
                    "password": "Passw0rd",
                    "firstName": "Admin1",
                    "lastName": "User",
                    "dateOfBirth": "2015-03-17T18:49:41Z",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/api/users/2"
                        },
                        "accounts": {
                            "href": "http://localhost:8080/api/users/2/accounts"
                        },
                        "address": {
                            "href": "http://localhost:8080/api/users/2/address"
                        },
                        "groups": {
                            "href": "http://localhost:8080/api/users/2/groups"
                        }
                    }
                }]
        };

        vm = userListController("UserListController", {
            $scope: $scope,
            $state: $state,
            mnyUserService: mnyUserService,
            users: allUsers
        });
    }));

    it('Should get all users', function () {
        //Create spies for the relevant service functions that are called in the addUser function.
        spyOn(mnyUserService, "getAllUsers").and.returnValue(deferred.promise);
        deferred.resolve(allUsers);

        vm.getUsers();

        //Need this to actually set the relevant values.
        $rootScope.$digest();

        expect(vm.users.users[0].userName).toBe("Admin");
        expect(vm.users.users[1].userName).toBe("Admin1");
    });

    it('Should remove given user', function () {
        var result = {
            data: {
                status: "201"
            }
        };

        spyOn(vm, "getUsers").and.returnValue(deferred.promise);
        deferred.resolve(allUsers);

        spyOn(mnyUserService, "remove").and.returnValue(deferred.promise);
        deferred.resolve(result);

        vm.remove("1");

        $rootScope.$digest();

        expect(mnyUserService.remove).toHaveBeenCalled();
    });
});
