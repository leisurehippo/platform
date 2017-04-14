(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('CheckTeamName', CheckTeamName);

    CheckTeamName.$inject = ['$resource'];

    function CheckTeamName($resource) {
        var service = $resource('/api/registrations/check/team-name/:teamname', {}, {
            'get': { method: 'GET', isArray:false  }
        });
        return service;
    }
})();
