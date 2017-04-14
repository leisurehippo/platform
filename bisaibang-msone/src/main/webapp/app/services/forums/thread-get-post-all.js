/**
 * Created by OlyLis on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('threadGetAllPost', threadGetAllPost);

    threadGetAllPost.$inject = ['$resource'];

    function threadGetAllPost ($resource) {
        var service = $resource('api/ms-post/admin/all-post/thread/:id', {}, {
            'post': { method: 'POST', isArray: false,
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
