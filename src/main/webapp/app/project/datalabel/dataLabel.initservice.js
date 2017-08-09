(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Initservice',Initservice);

    Initservice.$inject = ['$resource'];

    function Initservice ($resource) {
        var service = $resource('api/Initservice', {}, {
            'get': {method: 'GET', params:{}, isArray: false,
                 responseType: "text",
            }

        });

        return service;
    }
})();
