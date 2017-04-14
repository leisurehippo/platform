(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('AccountCurrent', AccountCurrent);

    AccountCurrent.$inject = ['$resource'];

    function AccountCurrent ($resource) {
        var service = $resource('api/account/current', {}, {
            'get': { method: 'GET'}
        });

        return service;
    }
})();
