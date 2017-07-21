(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetAlgorithmData', GetAlgorithmData);

    GetAlgorithmData.$inject = ['$resource'];

    function GetAlgorithmData ($resource) {
        var service = $resource('api/getServerAlgorithm', {}, {
            'get': {method: 'GET', params:{}, isArray: true,
                responseType: "text",
            }

        });

        return service;
    }
})();
