/**
 * Created by gsy on 2017/3/24.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .factory('DeleteTeam', DeleteTeam);

    DeleteTeam.$inject = ['$resource'];

    function DeleteTeam ($resource) {
        var service = $resource('api/registrations/admin/leave-many/registration', {}, {
            'update': { method: 'POST', isArray: false,
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
