(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labelmergeservice', Labelmergeservice);

    Labelmergeservice.$inject = ['$resource'];


    function Labelmergeservice ($resource) {
        var service = $resource('api/Labelmergeservice', {}, {
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
