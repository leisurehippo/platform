/**
 * Created by Gsy on 2017/7/19.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetServerData', GetServerData);

    GetServerData.$inject = ['$resource'];

    function GetServerData ($resource) {
        var service = $resource('api/getServerData', {}, {
            'get': {method: 'GET', param:{},isArray: true
            }
        });
        return service;
    }
})();
