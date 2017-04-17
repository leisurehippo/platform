(function() {
    'use strict';
    angular
        .module('bsbmsoneApp')
        .factory('Registration', Registration);

    Registration.$inject = ['$resource'];

    function Registration ($resource) {
        var resourceUrl =  'api/registrations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
