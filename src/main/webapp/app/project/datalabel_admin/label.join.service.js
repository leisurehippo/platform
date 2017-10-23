(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labeljoinservice', Labeljoinservice);

    Labeljoinservice.$inject = ['$resource'];


    function Labeljoinservice ($resource) {
        var service = $resource('api/Labeljoinservice', {}, {
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
