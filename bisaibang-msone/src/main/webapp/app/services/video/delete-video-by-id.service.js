(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('DeleteVideoById', DeleteVideoById);

    DeleteVideoById.$inject = ['$resource'];

    function DeleteVideoById ($resource) {
        var service = $resource('/api/ms-video/delete/video/:id', {}, {
            'save': { method: 'POST'}
        });

        return service;
    }
})();
