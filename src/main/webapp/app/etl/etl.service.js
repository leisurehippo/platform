(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('ETLService', ETLService);

    ETLService.$inject = ['$resource'];

    function ETLService ($resource) {
        var service = $resource('api/getServerProjectList', {}, {
            'get': {method: 'GET', param:{},isArray: true
            }
        });
        return service;
    }
})();
