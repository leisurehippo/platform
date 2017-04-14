/**
 * Created by arslan on 3/8/17.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('CreateComment', CreateComment);

    CreateComment.$inject = ['$resource'];

    function CreateComment($resource) {
        var service = $resource('api/ms-post/create', {}, {
            'save': { method: 'POST'}
        });
        return service;
    }
})();
