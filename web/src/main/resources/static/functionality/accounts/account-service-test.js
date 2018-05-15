/**
 * This file tests all of the user service functionality.
 *
 * User: rowan.massey
 * Date: 17/03/2015
 * Time: 16:14
 */

describe('Service: mnyAccountService', function () {
    beforeEach(module('moneyApp'));

    var $injector,
        $httpBackend,
        $q,
        deferred,
        mnyAccountService,
        account,
        result;


    beforeEach(inject(function (_$injector_, _$httpBackend_, _$q_) {
        $injector = _$injector_;
        $httpBackend = _$httpBackend_;
        $q = _$q_;

        deferred = $q.defer();

        mnyAccountService = $injector.get("mnyAccountService");

        account = {
            "name": "Current",
            "number": "12345",
            "balance": "2000.00",
            "openingDate": "2015-03-17T18:49:41Z"
        };
    }));

    it('Should save a new account', function () {
        $httpBackend.whenPOST("api/accounts").respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost/account/1",
                        expires: 0
                    };
                }
            }
        });
        mnyAccountService.save(account).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.data.headers().location).toBe("http://localhost/account/1");
    });

    it('Should respond with an error when saving account.', function () {
        $httpBackend.whenPOST("api/accounts").respond(500, '');
        expect(function () {
            mnyAccountService.save(account);
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should remove an existing account', function () {
        $httpBackend.whenDELETE("api/accounts/1").respond({
            data: {
                status: "201"
            }
        });

        mnyAccountService.remove("1").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.config.method).toBe("DELETE");
        expect(result.status).toBe(200);
    });

    it('Should respond with an error when deleting an account.', function () {
        $httpBackend.whenDELETE("api/accounts/1").respond(500, '');
        expect(function () {
            mnyAccountService.remove("1");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should get all accounts.', function () {
        $httpBackend.whenGET("api/accounts").respond({
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accounts{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/accounts/search"
                }
            },
            "_embedded": {
                "accounts": [{
                    "name": "Current",
                    "number": "12345",
                    "balance": "2000.00",
                    "openingDate": "2015-03-17T18:49:41Z",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/api/accounts/1"
                        },
                        "users": {
                            "href": "http://localhost:8080/api/accounts/1/users"
                        }
                    }
                },
                    {
                        "name": "Wallet",
                        "number": "1234",
                        "balance": "50.00",
                        "openingDate": "2015-03-17T18:49:41Z",
                        "_links": {
                            "self": {
                                "href": "http://localhost:8080/api/accounts/2"
                            },
                            "users": {
                                "href": "http://localhost:8080/api/accounts/2/users"
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
        });

        mnyAccountService.getAllAccounts().then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.length).toBeGreaterThan(1);
        expect(result[0].name).toBe("Current");
        expect(result[0].number).toBe("12345");
        expect(result[0].balance).toBe("2000.00");
        expect(result[0]._links.users.href).toBe("http://localhost:8080/api/accounts/1/users");

        expect(result[1].name).toBe("Wallet");
        expect(result[1].number).toBe("1234");
        expect(result[1].balance).toBe("50.00");
        expect(result[1]._links.users.href).toBe("http://localhost:8080/api/accounts/2/users");
    });

    it('Should resolve to blank when there is no payload in _embedded.', function () {
        $httpBackend.whenGET("api/accounts").respond({
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accounts{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/accounts/search"
                }
            },
            "page": {
                "size": 20,
                "totalElements": 2,
                "totalPages": 1,
                "number": 0
            }
        });
        mnyAccountService.getAllAccounts().then(function (data) {
            result = data;
        });
        $httpBackend.flush();
        expect(result).toBeUndefined();
    });

    it('Should respond with an error when getting all accounts.', function () {
        $httpBackend.whenGET("api/accounts").respond(500, '');
        expect(function () {
            mnyAccountService.getAllAccounts("api/users");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should get an account.', function () {
        $httpBackend.whenGET("api/accounts/2").respond({
            "name": "Wallet",
            "number": "1234",
            "balance": "50.00",
            "openingDate": "2015-03-17T18:49:41Z",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/accounts/2"
                },
                "users": {
                    "href": "http://localhost:8080/api/accounts/2/users"
                }
            }
        });
        mnyAccountService.getAccount("2").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.name).toBe("Wallet");
        expect(result.number).toBe("1234");
        expect(result.balance).toBe("50.00");
        expect(result._links.users.href).toBe("http://localhost:8080/api/accounts/2/users");
    });

    it('Should respond with an error when getting account.', function () {
        $httpBackend.whenGET("api/accounts/2").respond(500, '');
        expect(function () {
            mnyAccountService.getAccount("2");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should update an existing account.', function () {
        $httpBackend.whenPUT("api/accounts/1").respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost:8080/api/accounts/1",
                        expires: 0
                    };
                }
            }
        });
        mnyAccountService.updateAccount("1", account).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.headers().location).toBe("http://localhost:8080/api/accounts/1");
    });

    it('Should respond with an error when updating an account.', function () {
        $httpBackend.whenPUT("api/account/1").respond(500, '');
        expect(function () {
            mnyAccountService.updateAccount("1", account);
            $httpBackend.flush();
        }).toThrow();
    });

});

