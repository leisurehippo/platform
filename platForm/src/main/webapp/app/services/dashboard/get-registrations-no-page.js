/**
 * Created by gsy on 2017/4/5.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .factory('GetRegistrationNoPage', GetRegistrationNoPage);

    GetRegistrationNoPage.$inject = ['$resource'];

    function GetRegistrationNoPage ($resource) {
        var service = $resource('api/registrations/get-all-registrations-no-page', {}, {
            'get': { method: 'GET', isArray: true,
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
