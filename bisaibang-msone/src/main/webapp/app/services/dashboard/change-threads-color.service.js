/**
 * Created by gsy on 2017/3/20.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('ChangeThreadsColor', ChangeThreadsColor);

    ChangeThreadsColor.$inject = ['$resource'];

    function ChangeThreadsColor ($resource) {
        var service = $resource('/api/ms-thread/many-change-color/color/:color', {}, {
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
