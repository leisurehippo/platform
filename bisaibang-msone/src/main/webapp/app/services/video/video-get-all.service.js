(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('VideoGetAll', VideoGetAll);

    VideoGetAll.$inject = ['$resource'];

    function VideoGetAll ($resource) {

        var service = $resource('api/ms-video/get-all-video', {}, {
            'query': {method: 'GET', params: {}, isArray: false}
        });

        return service;
    }
})();
