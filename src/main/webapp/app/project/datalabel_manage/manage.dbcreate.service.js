(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('DBcreateservice', DBcreateservice);

    DBcreateservice.$inject = ['$resource'];

    function DBcreateservice ($resource) {
        var service = $resource('api/DBcreateservice', {}, {
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
