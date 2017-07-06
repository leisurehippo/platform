(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('runLocal', runLocal);

    runLocal.$inject = ['$resource'];

    function runLocal ($resource) {
        var service = $resource('api/runLocal', {}, {
            'get': {method: 'GET', params:{}, isArray: true,
                responseType: "text",
            //     transformResponse: function(data) {  // 转换response
            //     return JSON.stringify(data);
            // }
            }

        });

        return service;
    }
})();
