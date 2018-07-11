/**
 * Created by Gsy on 2017/7/24.
 */
(function () {
    'use strict';

    angular
        .module('jhipsterSampleApplicationApp')
        .factory('CreateProject', CreateProject);

    CreateProject.$inject = ['$resource'];

    function CreateProject ($resource) {
        var service = $resource('api/createProject', {}, {
            'get': {method: 'GET', params:{}, isArray: false
            }

        });

        return service;
    }
})();
