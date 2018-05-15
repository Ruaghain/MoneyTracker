/**
 * This file tests all of the relationship service functionality.
 *
 * User: rowan.massey
 * Date: 31/03/2015
 * Time: 13:30
 */

describe('Service: mnyBaseService', function () {
    beforeEach(module('moneyApp'));

    var $injector,
        $httpBackend,
        $q,
        deferred,
        mnyBaseService,
        address,
        result,
        parentLocation,
        childLocation,
        childEntity,
        relationshipLocation;


    beforeEach(inject(function (_$injector_, _$httpBackend_, _$q_) {
        $injector = _$injector_;
        $httpBackend = _$httpBackend_;
        $q = _$q_;

        deferred = $q.defer();

        mnyBaseService = $injector.get("mnyBaseService");

        parentLocation = "api/users/1";
        childLocation = "api/address/1";
        childEntity = "address";

        relationshipLocation = "api/users/1/address";

    }));

    it('Should associate a new entity with another', function () {
        $httpBackend.whenPUT(relationshipLocation).respond({
            data: {
                headers: function () {
                    return {
                        location: "http://localhost/addresses/1",
                        expires: 0
                    };
                }
            }
        });
        mnyBaseService.relate(parentLocation, childEntity, childLocation).then(function (data) {
            result = data;
        });
        $httpBackend.flush();

        expect(result).toBeDefined();
        expect(result.data.data.headers().location).toBe("http://localhost/addresses/1");
    });

    it('Should respond with an error when associating a entities between each other.', function () {
        $httpBackend.whenPUT(relationshipLocation).respond(500, '');
        expect(function () {
            mnyBaseService.relate(parentLocation, childEntity, childLocation);
            $httpBackend.flush();
        }).toThrow();
    });
});

