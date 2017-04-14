(function() {
    'use strict';
    angular
        .module('bsbmsoneApp')
        .factory('Forum', Forum);

    Forum.$inject = ['$resource'];

    function Forum ($resource) {
        var resourceUrl =  'api/forums/:id';

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
