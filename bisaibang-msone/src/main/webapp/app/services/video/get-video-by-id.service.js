(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('GetVideoById', GetVideoById);

    GetVideoById.$inject = ['$resource'];

    function GetVideoById ($resource) {
        var service = $resource('/api/ms-video/get-video/:id', {}, {
            'get': { method: 'GET'}
        });

        return service;
    }
})();
