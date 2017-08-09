/**
 * Created by Gsy on 2017/7/24.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('DeleteProject', DeleteProject);

    DeleteProject.$inject = ['$resource'];

    function DeleteProject ($resource) {
        var service = $resource('api/deleteProject', {}, {
            'get': {method: 'GET', params:{}, isArray: false
            }

        });

        return service;
    }
})();
