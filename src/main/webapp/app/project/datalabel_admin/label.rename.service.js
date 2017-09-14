(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labelrenameservice', Labelrenameservice);

    Labelrenameservice.$inject = ['$resource'];


    function Labelrenameservice ($resource) {
        var service = $resource('api/Labelrenameservice', {}, {
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
