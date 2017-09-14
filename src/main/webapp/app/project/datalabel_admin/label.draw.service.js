(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labeldrawservice', Labeldrawservice);

    Labeldrawservice.$inject = ['$resource'];


    function Labeldrawservice ($resource) {
        var service = $resource('api/Labeldrawservice', {}, {
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
