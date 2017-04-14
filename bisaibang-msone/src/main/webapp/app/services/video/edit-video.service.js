/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('EditVideo', EditVideo);

    EditVideo.$inject = ['$resource'];

    function EditVideo($resource) {
        var service = $resource('/api/ms-video/edit/video/:id', {}, {
            'save': { method: 'POST' }
        });
        return service;
    }
})();
