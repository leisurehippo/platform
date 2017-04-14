/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('CreateVideoTag', CreateVideoTag);

    CreateVideoTag.$inject = ['$resource'];

    function CreateVideoTag($resource) {
        var service = $resource('api/ms-video-tag/create', {}, {
            'save': { method: 'POST' }
        });
        return service;
    }
})();
