/**
 * Created by Gsy on 2017/7/25.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetProjectDes', GetProjectDes);

    GetProjectDes.$inject = ['$resource'];

    function GetProjectDes ($resource) {
        var service = $resource('api/getServerProjectDes', {}, {
            'get': {method: 'GET', params:{}, isArray: false
            }

        });

        return service;
    }
})();
