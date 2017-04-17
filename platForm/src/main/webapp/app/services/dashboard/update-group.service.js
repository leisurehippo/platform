/**
 * Created by gsy on 2017/3/30.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('UpdateGroup', UpdateGroup);

    UpdateGroup.$inject = ['$resource'];

    function UpdateGroup ($resource) {
        var service = $resource('api/registrations/update-group/registration/:registrationid/:groupid', {}, {
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
