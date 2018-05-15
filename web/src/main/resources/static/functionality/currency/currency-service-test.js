/**
 * This file tests all of the currency service functionality.
 *
 * User: rowan.massey
 * Date: 11/07/2015
 */

describe('Service: mnyCurrencyService', function () {
    beforeEach(module('moneyApp'));

    var $injector,
        $httpBackend,
        $q,
        deferred,
        mnyCurrencyService,
        currency,
        currencies,
        result;


    beforeEach(inject(function (_$injector_, _$httpBackend_, _$q_) {
        $injector = _$injector_;
        $httpBackend = _$httpBackend_;
        $q = _$q_;

        deferred = $q.defer();

        mnyCurrencyService = $injector.get("mnyCurrencyService");

        currency = {
            "id": 1,
            "name": "Euro",
            "iso": "EUR",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/currencies/1"
                }
            }
        };

        currencies = {
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/currencies{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/currencies/search"
                }
            },
            "_embedded": {
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

    it('Should save a new currency', function () {
        $httpBackend.whenPOST("api/currencies").respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost:8080/api/currencies/1",
                        expires: 0
                    };
                }
            }
        });
        mnyCurrencyService.save(currency).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.data.headers().location).toBe("http://localhost:8080/api/currencies/1");
    });

    it('Should respond with an error when saving currency.', function () {
        $httpBackend.whenPOST("api/currencies").respond(500, '');
        expect(function () {
            mnyCurrencyService.save(currency);
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should remove an existing currency', function () {
        $httpBackend.whenDELETE("api/currencies/1").respond({
            data: {
                status: "201"
            }
        });

        mnyCurrencyService.remove("1").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.config.method).toBe("DELETE");
        expect(result.status).toBe(200);
    });

    it('Should respond with an error when deleting a currency.', function () {
        $httpBackend.whenDELETE("api/currencies/1").respond(500, '');
        expect(function () {
            mnyCurrencyService.remove("1");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should get all currencies.', function () {
        $httpBackend.whenGET("api/currencies").respond(currencies);

        mnyCurrencyService.getAllCurrencies().then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.length).toBeGreaterThan(1);
        expect(result[0].name).toBe("Euro");
        expect(result[0].iso).toBe("EUR");

        expect(result[1].name).toBe("British Pound");
        expect(result[1].iso).toBe("GBP");
    });

    it('Should resolve to blank when there is no payload in _embedded.', function () {
        $httpBackend.whenGET("api/currencies").respond({
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/currencies{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/currencies/search"
                }
            },
            "page": {
                "size": 20,
                "totalElements": 2,
                "totalPages": 1,
                "number": 0
            }
        });
        mnyCurrencyService.getAllCurrencies().then(function (data) {
            result = data;
        });
        $httpBackend.flush();
        expect(result).toBeUndefined();
    });

    it('Should respond with an error when getting all currencies.', function () {
        $httpBackend.whenGET("api/currencies").respond(500, '');
        expect(function () {
            mnyCurrencyService.getAllCurrencies("api/currencies");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should get a currency.', function () {
        $httpBackend.whenGET("api/currencies/2").respond(currency);
        mnyCurrencyService.getCurrency("2").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.name).toBe("Euro");
        expect(result.iso).toBe("EUR");
    });

    it('Should respond with an error when getting currency.', function () {
        $httpBackend.whenGET("api/currencies/2").respond(500, '');
        expect(function () {
            mnyCurrencyService.getCurrency("2");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should update an existing currency.', function () {
        $httpBackend.whenPUT("api/currencies/1").respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost:8080/api/currencies/1",
                        expires: 0
                    };
                }
            }
        });
        mnyCurrencyService.updateCurrency("1", currency).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.headers().location).toBe("http://localhost:8080/api/currencies/1");
    });

    it('Should respond with an error when updating a currency.', function () {
        $httpBackend.whenPUT("api/currency/1").respond(500, '');
        expect(function () {
            mnyCurrencyService.updateCurrency("1");
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should search for a currency.', function () {
        $httpBackend.whenGET("api/currencies/search/findByName?name=%25Euro%25").respond(currencies);

        mnyCurrencyService.searchCurrency("Euro").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result[0].name).toBe("Euro");
        expect(result[0].iso).toBe("EUR");
    });

    it('Should return nothing for a currency with no embedded data.', function () {
        $httpBackend.whenGET("api/currencies/search/findByName?name=%25Euro%25").respond({
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/currencies{?page,size,sort}",
                    "templated": true
                },
                "search": {
                    "href": "http://localhost:8080/api/currencies/search"
                }
            },
            "page": {
                "size": 20,
                "totalElements": 2,
                "totalPages": 1,
                "number": 0
            }
        });

        mnyCurrencyService.searchCurrency("Euro").then(function (data) {
            result = data;
        });
        $httpBackend.flush();
        expect(result).not.toBeDefined();
    });

    it('Should respond with an error when searching for a currency.', function () {
        $httpBackend.whenGET("api/currencies/search/findByName?name=%25Pound%25").respond(500, '');
        expect(function () {
            mnyCurrencyService.searchCurrency("Pound");
            $httpBackend.flush();
        }).toThrow();
    });

});

