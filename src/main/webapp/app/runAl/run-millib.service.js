/**
 * Created by Gsy on 2017/8/3.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('RunMLlib', RunMLlib);

    RunMLlib.$inject = ['$resource'];

    function RunMLlib ($resource) {
        var service = $resource('api/runMLlib', {}, {
            'get': {method: 'GET', params:{}, isArray: true
            }

        });

        return service;
    }
})();
