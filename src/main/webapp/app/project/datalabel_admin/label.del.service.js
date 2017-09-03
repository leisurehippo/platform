(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labeldelservice', Labeldelservice);

    Labeldelservice.$inject = ['$resource'];


    function Labeldelservice ($resource) {
        var service = $resource('api/Labeldelservice', {}, {
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
