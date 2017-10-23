(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labeltrainservice', Labeltrainservice);

    Labeltrainservice.$inject = ['$resource'];

    function Labeltrainservice ($resource) {
        var service = $resource('api/Labeltrainservice', {}, {
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
