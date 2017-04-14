/**
 * Created by gsy on 2017/3/13.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('GetMyPlayersInfo', GetMyPlayersInfo);

    GetMyPlayersInfo.$inject = ['$resource'];

    function GetMyPlayersInfo ($resource) {
        var service = $resource('api/registrations/get-my-registration', {}, {
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

