/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('CreateVideo', CreateVideo);

    CreateVideo.$inject = ['$resource'];

    function CreateVideo($resource) {
        var service = $resource('api/ms-video/create', {}, {
            'save': { method: 'POST' }
        });
        return service;
    }
})();
