(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('RegistrationsEnrollTeamPlayer', RegistrationsEnrollTeamPlayer);

    RegistrationsEnrollTeamPlayer.$inject = ['$resource'];

    function RegistrationsEnrollTeamPlayer($resource) {
        var service = $resource('/api/registrations/enroll-team-player', {}, {
            'post': { method: 'POST'}
        });
        return service;
    }
})();
