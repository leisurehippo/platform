(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('VideoGetAllByTag', VideoGetAllByTag);

    VideoGetAllByTag.$inject = ['$resource'];

    function VideoGetAllByTag ($resource) {

        var service = $resource('api/ms-video/get-video-by/tag/:id', {}, {
            'query': {method: 'GET', params: {}, isArray: false}
        });

        return service;
    }
})();
