/**
 * Created by gsy on 2017/3/18.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('ThreadEditTop', ThreadEditTop);

    ThreadEditTop.$inject = ['$resource'];

    function ThreadEditTop ($resource) {
        var service = $resource('api/ms-thread/edit/thread/:id', {}, {
            'post': { method: 'post', params: {}, isArray: false,
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
