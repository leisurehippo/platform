/**
 * Created by OlyLis on 2017/3/15.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('ThreadGetAllPostByLevel', ThreadGetAllPostByLevel);

    ThreadGetAllPostByLevel.$inject = ['$resource'];

    function ThreadGetAllPostByLevel ($resource) {
        var service = $resource('api/ms-post/all-post/thread/:id/level/:level', {}, {
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
