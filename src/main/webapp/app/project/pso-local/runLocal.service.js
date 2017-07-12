(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('runLocal', runLocal);

    runLocal.$inject = ['$resource'];

    function runLocal ($resource) {
        var service = $resource('api/runLocal', {}, {
            'get': {method: 'GET', params:{}, isArray: true,
                responseType: "text"
            }

        });

        return service;
    }
})();
