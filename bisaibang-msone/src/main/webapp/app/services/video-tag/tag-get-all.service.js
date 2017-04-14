(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('GetAllVideoTag', GetAllVideoTag);

    GetAllVideoTag.$inject = ['$resource'];

    function GetAllVideoTag ($resource) {

        var service = $resource('api/ms-video-tag/get-all-video-tags', {}, {
            'get': {method: 'GET',isArray:true}
        });

        return service;
    }
})();
