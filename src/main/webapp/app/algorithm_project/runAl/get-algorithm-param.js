/**
 * Created by Gsy on 2017/7/31.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetAlgorithmParams', GetAlgorithmParams);

    GetAlgorithmParams.$inject = ['$resource'];

    function GetAlgorithmParams ($resource) {
        var service = $resource('api/getAlgorithmParameter', {}, {
            'get': {method: 'GET', params:{}, isArray: true
            }

        });

        return service;
    }
})();
