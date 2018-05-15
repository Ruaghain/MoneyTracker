/**
 *
 * User: rowan.massey
 * Date: 02/01/2015
 *
 */

(function () {
    'use strict';

    moneyApp.service("mnyDataUploadService", ['$http', '$q', function ($http, $q) {

        var dataUploadService = this;
        /**
         * This method saves a new instance of a data upload to the database.
         *
         * @param dataUpload The data upload object that the user wants saved to the database.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        dataUploadService.save = function (dataUpload) {
            var deferred = $q.defer();
            $http.post("api/dataUpload", dataUpload).then(function (data) {
                deferred.resolve(data);
            }, function (error) {
                console.log("There was an error saving the data upload." + error);
                deferred.reject(error);
            });
            return deferred.promise;
        };

        /**
         * This method deletes the record, for the given location.
         *
         * @param location
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        dataUploadService.remove = function (location) {
            var deferred = $q.defer();
            $http.delete(location).then(function (data) {
                deferred.resolve(data);
            }, function (error) {
                console.log("There was an error deleting the data upload." + error);
                deferred.reject(error);
            });
            return deferred.promise;

        };

        /**
         * This method returns all of the data upload records from the database.
         *
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        dataUploadService.getAllDataUploads = function () {
            var deferred = $q.defer();
            $http.get("api/dataUpload").then(function (data) {
                deferred.resolve(data.data._embedded.dataUpload);
            }, function (error) {
                console.log("There was an error getting all the data uploads." + error);
                deferred.reject(error);
            });
            return deferred.promise;
        };

        /**
         * This method leverages the functionality provided by HATEOAS to retrieve ALL of the entities exposed
         * as REST resources.
         *
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        dataUploadService.getListOfEntities = function () {
            var deferred = $q.defer();
            var entityList = [];
            $http.get("api/").then(function (data) {
                $.each(data.data._links, function (key) {
                    var dataUploadEntity = {};
                    dataUploadEntity.name = capitaliseFirstLetter(key);
                    entityList.push(dataUploadEntity);
                });
                deferred.resolve(entityList);
            }, function (error) {
                console.log("There was an error getting a list of exposed entities." + error);
                deferred.reject(error);
            });
            return deferred.promise;
        };

        /**
         *
         * @param stringValue
         * @returns {string}
         */
        function capitaliseFirstLetter(stringValue) {
            //TODO:Need to move this to a a util script file.
            return stringValue.charAt(0).toUpperCase() + stringValue.slice(1);
        }

        /**
         * This method retrieves the schema information for the provided entity.
         *
         * @param entity The name of the entity whose schema information we need to retrieve.
         * @returns {promise.promise|jQuery.promise|jQuery.ready.promise}
         */
        dataUploadService.getEntitySchema = function (entity) {
            var fieldList = [];
            var deferred = $q.defer();
            var request = {
                method: "GET",
                url: "api/" + entity.name.toLowerCase() + "/schema",
                headers: {
                    "Accept": "application/schema+json"
                }
            };
            $http(request).then(function (data) {
                var field = {};
                $.each(data.data.links, function (key, value) {
                    field = {};
                    field.name = value.rel;
                    fieldList.push(field);
                });
                $.each(data.data.properties, function (key) {
                    field = {};
                    field.name = key;
                    fieldList.push(field);
                });

                deferred.resolve(fieldList);
            }, function (error) {
                console.log("There was an error retrieving the metadata for entity '" + entity + "'. Error was: " + error);
                deferred.reject(error);
            });
            return deferred.promise;

        };
    }]);
})();