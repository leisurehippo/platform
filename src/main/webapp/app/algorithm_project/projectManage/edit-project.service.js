/**
 * Created by Gsy on 2017/7/24.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('EditProject', EditProject);

    EditProject.$inject = ['$resource'];

    function EditProject ($resource) {
        var service = $resource('api/editProject', {}, {
            'get': {method: 'GET', params:{}, isArray: false
            }

        });

        return service;
    }
})();
