(function() {
    'use strict';
    angular
        .module('app')
        .factory('Term', Term);

    Term.$inject = ['$resource'];

    function Term ($resource) {
        var resourceUrl =  'api/terms/:id';

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
