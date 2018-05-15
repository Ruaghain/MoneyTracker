/**
 * This service deals with the setting up of relationships between two entities.
 *
 */

moneyApp.service("mnyBaseService", ["$http", "$q", function ($http, $q) {

    var baseService = this;

    /**
     * This method associates the given child location, with the given parent location to allow for relationship definition.
     *
     * @param parentLocation The location of the saved parent record.
     * @param childEntity The actual name of the child entity, this is used to build up the PUT request.
     * @param childLocation The location of the saved child record.
     * @returns {*|promise}
     */
    baseService.relate = function (parentLocation, childEntity, childLocation) {
        var deferred = $q.defer();
        var request = {
            method: "PUT",
            url: parentLocation + "/" + childEntity,
            headers: {
                "Content-Type": "text/uri-list"
            },
            data: childLocation
        };
        $http(request).then(function (data) {
            deferred.resolve(data);
        }, function (error) {
            deferred.reject(error);
            throw new Error("There was an error associating child record " + childLocation + " with parent record " + parentLocation + ": " + error.message);
        });
        return deferred.promise;
    };

    /**
     * This method updates the account type, with the given payload
     *
     * @param location The location of the record that will be updated.
     * @param data The payload of the account type which will be updated.
     * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
     */
    baseService.updateRecord = function (location, data) {
        var deferred = $q.defer();
        $http.put(location, data).then(function (result) {
            deferred.resolve(result.data);
        }, function (error) {
            deferred.reject(error);
            throw new Error("There was an error updating the account type: " + error.data.message);
        });
        return deferred.promise;
    };

    /**
     * This method will remove the record from the given location.
     *
     * @param location The location of the record to delete.
     * @returns {*}
     */
    baseService.remove = function (location) {
        var deferred = $q.defer();
        $http.delete(location).then(function (data) {
            deferred.resolve(data);
        }, function (error) {
            deferred.reject(error);
            throw new Error("There was an error deleting the record: " + error.data.message);
        });
        return deferred.promise;
    };

    /**
     *This method retrieves the record from the given location.
     *
     * @param location The location of the record to retrieve.
     * @returns {*}
     */
    baseService.getRecord = function (location) {
        var deferred = $q.defer();
        $http.get(location).then(function (data) {
            deferred.resolve(data.data);
        }, function (error) {
            deferred.reject(error);
            throw new Error("There was an error getting the record: " + error.data.message);
        });
        return deferred.promise;
    };

    /**
     * This method returns a list of all of the currencies defined in the database.
     *
     * @returns {*|promise}
     */
    baseService.getAllRecords = function (path, node) {
        var deferred = $q.defer();
        $http.get(path).then(function (data) {
            if (data.data._embedded) {
                deferred.resolve(data.data._embedded[node]);
            } else {
                //Resolve if nothing is found, the request was a success.
                deferred.resolve();
            }
        }, function (error) {
            deferred.reject(error);
            //TODO: Display the path and node in the error message.
            throw new Error("There was an error retrieving all of the records: " + error.data.message);
        });
        return deferred.promise;
    };


    /**
     * This method saves the provided data, for the given path.
     *
     * @param path The path of the entity.
     * @param data The payload which will be saved to the given location.
     * @returns {*}
     */
    baseService.saveRecord = function (path, data) {
        var deferred = $q.defer();
        $http.post(path, data).then(function (result) {
            deferred.resolve(result);
        }, function (error) {
            deferred.reject(error);
            throw new Error("There was an error saving the record: " + error.data.message);
        });
        return deferred.promise;
    };


}]);

