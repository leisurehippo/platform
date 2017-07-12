(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetHdfsFile', GetHdfsFile);

    GetHdfsFile.$inject = ['$resource'];

    function GetHdfsFile ($resource) {
        var service = $resource('api/getHdfsData', {}, {
            'get': {method: 'GET', params:{}, isArray: true,
                responseType: "text"
            }

        });

        return service;
    }
})();
