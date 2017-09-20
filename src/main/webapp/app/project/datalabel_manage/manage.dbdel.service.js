(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('DBdelservice', DBdelservice);

    DBdelservice.$inject = ['$resource'];

    function DBdelservice ($resource) {
        var service = $resource('api/DBdelservice', {}, {
            'get': {method: 'GET', params:{}, isArray: false,
                responseType: "text",
            //     transformResponse: function(data) {  // 转换response
            //     return JSON.stringify(data);
            // }
            }

        });

        return service;

    }
})();
