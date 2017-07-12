(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetAllData', GetAllData);

    GetAllData.$inject = ['$resource'];

    function GetAllData ($resource) {
        var service = $resource('api/getAllData', {}, {
            'get': {method: 'GET', param:{},isArray: true
            }
        });
        return service;
    }
})();
