/**
 * Created by OlyLis on 2017/3/31.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('forumsGetAllThreadByState', forumsGetAllThreadByState);

    forumsGetAllThreadByState.$inject = ['$resource'];

    function forumsGetAllThreadByState ($resource) {
        var service = $resource('api/ms-thread/find-by-section/thread-all/test/state/:state', {}, {
            'post': { method: 'POST', params: {section: '='}, isArray: false,
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
