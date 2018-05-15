/**
 * This file tests all of the address service functionality.
 *
 * User: rowan.massey
 * Date: 30/03/2015
 * Time: 13:30
 */

describe('Service: mnyAddressService', function () {
    beforeEach(module('moneyApp'));

    var $injector,
        $httpBackend,
        $q,
        deferred,
        mnyAddressService,
        address,
        result;


    beforeEach(inject(function (_$injector_, _$httpBackend_, _$q_) {
        $injector = _$injector_;
        $httpBackend = _$httpBackend_;
        $q = _$q_;

        deferred = $q.defer();

        mnyAddressService = $injector.get("mnyAddressService");

        address = {
            address1: "Address 1",
            address2: "Address 2",
            address3: "Address 3",
            address4: "Address 4",
            county: "Dublin",
            city: "Dublin"
        };

    }));

    it('Should save a new address', function () {
        $httpBackend.whenPOST("api/addresses").respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost/addresses/1",
                        expires: 0
                    };
                }
            }
        });
        mnyAddressService.save(address).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.data.headers().location).toBe("http://localhost/addresses/1");
    });

    it('Should respond with an error when saving a new address', function () {
        $httpBackend.whenPOST("api/addresses").respond(500, '');
        expect(function () {
            mnyAddressService.save(address);
            $httpBackend.flush();
        }).toThrow();
    });

    it('Should get an address', function () {
        $httpBackend.whenGET("api/addresses/1").respond({
            "address1": "Address 1",
            "address2": "Address 2",
            "address3": "Address 3",
            "address4": "Address 4",
            "county": "Dublin",
            "city": "Dublin",
            "_links": {
                "self": {
                    "href": "http://localhost:8080/api/addresses/1"
                },
                "country": {
                    "href": "http://localhost:8080/api/addresses/1/country"
                }
            }
        });
        mnyAddressService.getAddress("api/addresses/1").then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.address1).toBe("Address 1");
        expect(result.address2).toBe("Address 2");
        expect(result.address3).toBe("Address 3");
        expect(result.address4).toBe("Address 4");
        expect(result.county).toBe("Dublin");
        expect(result.city).toBe("Dublin");
    });

    it('Should respond with an error when getting an address.', function () {
        $httpBackend.whenGET("api/addresses/1").respond(500, '');
        expect(function () {
            mnyAddressService.getAddress("api/addresses/1");
            $httpBackend.flush();
        }).toThrow();
    });

});

