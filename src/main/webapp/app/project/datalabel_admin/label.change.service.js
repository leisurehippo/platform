(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labelchangeservice', Labelchangeservice);

    Labelchangeservice.$inject = ['$resource'];


    function Labelchangeservice ($resource) {
        var service = $resource('api/Labelchangeservice', {}, {
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
