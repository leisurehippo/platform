/**
 * Created by Zhaoxuan on 2017/7/13.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetModels', GetModels);

    GetModels.$inject = ['$resource'];

    function GetModels ($resource) {
        var service = $resource('api/getModel', {}, {
            'get': {method: 'GET', params:{}, isArray: true,
                responseType: "text"
            }

        });

        return service;
    }
})();