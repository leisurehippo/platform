/**
 * Created by OlyLis on 2017/3/15.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('LeaderPostGetAllPost', LeaderPostGetAllPost);

    LeaderPostGetAllPost.$inject = ['$resource'];

    function LeaderPostGetAllPost ($resource) {
        var service = $resource('api/ms-post/all-post/leader-post/:id', {}, {
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
