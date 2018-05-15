/**
 *
 * User: rowan.massey
 * Date: 17/07/2015
 * Time: 22:48
 *
 */
/**
 * This file tests all of the account type service functionality.
 *
 * User: rowan.massey
 * Date: 11/07/2015
 */

describe('Service: mnyAccountTypeService', function () {
    beforeEach(module('moneyApp'));

    var $injector,
        $httpBackend,
        $q,
        deferred,
        mnyAccountTypeService,
        accountType,
        accountTypes,
        result;


    beforeEach(inject(function (_$injector_, _$httpBackend_, _$q_) {
        $injector = _$injector_;
        $httpBackend = _$httpBackend_;
        $q = _$q_;

        deferred = $q.defer();

        mnyAccountTypeService = $injector.get("mnyAccountTypeService");

        accountType = {
            "id": 1,
            "name": "Current",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accountTypes/1"
                }
            }
        };

        accountTypes = {
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accountTypes{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/accountTypes/search"
                }
            },
            "_embedded": {
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
            },
            "page": {
                "size": 20,
                "totalElements": 2,
                "totalPages": 1,
                "number": 0
            }
        };

        result = {};
    }));

    it('Should save a new account type', function () {
        $httpBackend.whenPOST("api/accountTypes").respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost:8080/api/accountTypes/1",
                        expires: 0
                    };
                }
            }
        });
        mnyAccountTypeService.save(accountType).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.data.headers().location).toBe("http://localhost:8080/api/accountTypes/1");
    });

    it('Should respond with an error when saving account type.', function () {
        $httpBackend.whenPOST("api/accountTypes").respond(500, '');
        expect(function () {
            mnyAccountTypeService.save(accountType);
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should remove an existing account type', function () {
        $httpBackend.whenDELETE("api/accountTypes/1").respond({
            data: {
                status: "201"
            }
        });

        mnyAccountTypeService.remove("1").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.config.method).toBe("DELETE");
        expect(result.status).toBe(200);
    });

    it('Should respond with an error when deleting an account type.', function () {
        $httpBackend.whenDELETE("api/accountTypes/1").respond(500, '');
        expect(function () {
            mnyAccountTypeService.remove("1");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should get all account types.', function () {
        $httpBackend.whenGET("api/accountTypes").respond(accountTypes);

        mnyAccountTypeService.getAllAccountTypes().then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.length).toBeGreaterThan(1);
        expect(result[0].name).toBe("Current");

        expect(result[1].name).toBe("Credit Card");
    });

    it('Should resolve to blank when there is no payload in _embedded.', function () {
        $httpBackend.whenGET("api/accountTypes").respond({
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accountTypes{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/accountTypes/search"
                }
            },
            "page": {
                "size": 20,
                "totalElements": 2,
                "totalPages": 1,
                "number": 0
            }
        });
        mnyAccountTypeService.getAllAccountTypes().then(function (data) {
            result = data;
        });
        $httpBackend.flush();
        expect(result).toBeUndefined();
    });

    it('Should respond with an error when getting all account types.', function () {
        $httpBackend.whenGET("api/accountTypes").respond(500, '');
        expect(function () {
            mnyAccountTypeService.getAllAccountTypes("api/accountTypes");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should get an account type.', function () {
        $httpBackend.whenGET("api/accountTypes/2").respond(accountType);
        mnyAccountTypeService.getAccountType("2").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.name).toBe("Current");
    });

    it('Should respond with an error when getting account type.', function () {
        $httpBackend.whenGET("api/accountTypes/2").respond(500, '');
        expect(function () {
            mnyAccountTypeService.getAccountType("api/accountTypes/2");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should update an existing account type.', function () {
        $httpBackend.whenPUT("api/accountTypes/1").respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost:8080/api/accountTypes/1",
                        expires: 0
                    };
                }
            }
        });
        mnyAccountTypeService.updateAccountType("1", accountType).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.headers().location).toBe("http://localhost:8080/api/accountTypes/1");
    });

    it('Should respond with an error when updating a account type.', function () {
        $httpBackend.whenPUT("api/accountTypes/1").respond(500, '');
        expect(function () {
            mnyAccountTypeService.updateAccountType("1", accountType);
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should search for an account type.', function () {
        $httpBackend.whenGET("api/accountTypes/search/findByName?name=%25Current%25").respond(accountTypes);

        mnyAccountTypeService.searchAccountTypes("Current").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result[0].name).toBe("Current");
    });

    it('Should return nothing for an account type with no embedded data.', function () {
        $httpBackend.whenGET("api/accountTypes/search/findByName?name=%25Current%25").respond({
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accountTypes{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/accountTypes/search"
                }
            },
            "page": {
                "size": 20,
                "totalElements": 2,
                "totalPages": 1,
                "number": 0
            }
        });

        mnyAccountTypeService.searchAccountTypes("Current").then(function (data) {
            result = data;
        });
        $httpBackend.flush();
        expect(result).not.toBeDefined();
    });

    it('Should respond with an error when searching for an account type.', function () {
        $httpBackend.whenGET("api/accountTypes/search/findByName?name=%25Current%25").respond(500, '');
        expect(function () {
            mnyAccountTypeService.searchAccountTypes("Current");
            $httpBackend.flush();
        }).toThrow();
    });

});

