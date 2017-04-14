/**
 * Created by OlyLis on 2017/4/5.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('VideoGetAllComment', VideoGetAllComment);

    VideoGetAllComment.$inject = ['$resource'];

    function VideoGetAllComment($resource) {
        var service = $resource('api/ms-comment/get-comments/video/:id/level/:level', {}, {
            'get': { method: 'GET' }
        });
        return service;
    }
})();
