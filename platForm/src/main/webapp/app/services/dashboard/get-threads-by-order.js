/**
 * Created by gsy on 2017/3/22.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('GetThreadsByOrder', GetThreadsByOrder);

    GetThreadsByOrder.$inject = ['$resource'];

    function GetThreadsByOrder ($resource) {
        var service = $resource('api/ms-thread/find-by-section/thread-all', {}, {
            'post': { method: 'POST', params: {section: '='} ,isArray: false,
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
