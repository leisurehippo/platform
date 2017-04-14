(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('forumsGetAllThread', forumsGetAllThread);

    forumsGetAllThread.$inject = ['$resource'];

    function forumsGetAllThread ($resource) {
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
