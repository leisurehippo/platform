(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('UpdateRegistrationTeamPlayer', UpdateRegistrationTeamPlayer);

    UpdateRegistrationTeamPlayer.$inject = ['$resource'];

    function UpdateRegistrationTeamPlayer($resource) {
        var service = $resource('/api/registrations/update-registration/registration/:id', {}, {
            'update': { method: 'PUT'}
        });
        return service;
    }
})();
