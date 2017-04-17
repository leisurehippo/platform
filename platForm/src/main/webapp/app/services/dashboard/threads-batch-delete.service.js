/**
 * Created by gsy on 2017/3/15.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .factory('ThreadsBatchDelete', ThreadsBatchDelete);

    ThreadsBatchDelete.$inject = ['$resource'];

    function ThreadsBatchDelete ($resource) {
        var service = $resource('api/ms-thread/delete-many', {}, {
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
