/**
 * Created by gsy on 2017/3/27.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .factory('RegisterBatchR', RegisterBatchR);

    RegisterBatchR.$inject = ['$resource'];

    function RegisterBatchR ($resource) {
        var service = $resource('api/ms-task/enroll-r/:k', {}, {
            'update': { method: 'post', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });

        return service;
    }
})();
