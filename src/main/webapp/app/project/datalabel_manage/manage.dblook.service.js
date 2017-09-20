(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('DBlookservice', DBlookservice);

    DBlookservice.$inject = ['$resource'];

    function DBlookservice ($resource) {
        var service = $resource('api/DBlookservice', {}, {
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
