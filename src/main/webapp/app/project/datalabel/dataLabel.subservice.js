(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('Submitservice', Submitservice);

    Submitservice.$inject = ['$resource'];

    function Submitservice ($resource) {
        var service = $resource('api/Submitservice', {}, {
            'post': {method: 'POST', params:{}, isArray: true,
                 responseType: "text",
            }

        });

        return service;
    }
})();
