/**
 * Created by gsy on 2017/3/13.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('adminGetAllPlayersInfo', adminGetAllPlayersInfo);

    adminGetAllPlayersInfo.$inject = ['$resource'];

    function adminGetAllPlayersInfo ($resource) {
        var service = $resource('api/registrations/get-all-registrations', {}, {
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

