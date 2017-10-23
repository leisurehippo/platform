(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Labelcreateservice', Labelcreateservice);

    Labelcreateservice.$inject = ['$resource'];


    function Labelcreateservice ($resource) {
        var service = $resource('api/Labelcreateservice', {}, {
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
