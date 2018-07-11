(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetHdfsData', GetHdfsData);

    GetHdfsData.$inject = ['$resource'];

    function GetHdfsData ($resource) {
        var service = $resource('api/getHdfsData', {}, {
            'get': {method: 'GET', param:{},isArray: true
            }
        });
        return service;
    }
})();
