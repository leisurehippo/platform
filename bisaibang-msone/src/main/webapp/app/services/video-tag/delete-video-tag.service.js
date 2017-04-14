/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('DeleteVideoTag', DeleteVideoTag);

    DeleteVideoTag.$inject = ['$resource'];

    function DeleteVideoTag($resource) {
        var service = $resource('api/ms-video-tag/delete/video-tag/:id', {}, {
            'save': { method: 'POST' }
        });
        return service;
    }
})();
