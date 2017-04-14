/**
 * Created by OlyLis on 2017/3/11.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('forumsGetThreadById', forumsGetThreadById);

    forumsGetThreadById.$inject = ['$resource'];

    function forumsGetThreadById ($resource) {
        var service = $resource('api/ms-thread/get-by-id/thread/:id', {}, {
            'get': { method: 'GET', isArray: false,
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
