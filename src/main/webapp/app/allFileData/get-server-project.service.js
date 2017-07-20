/**
 * Created by Gsy on 2017/7/20.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('GetServerProject', GetServerProject);

    GetServerProject.$inject = ['$resource'];

    function GetServerProject ($resource) {
        var service = $resource('api/getServerProject', {}, {
            'get': {method: 'GET', param:{},isArray: true
            }
        });
        return service;
    }
})();
