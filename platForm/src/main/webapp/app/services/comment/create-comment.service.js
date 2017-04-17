/**
 * Created by OlyLis on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .factory('createComment', createComment);

    createComment.$inject = ['$resource'];

    function createComment($resource) {
        var service = $resource('api/ms-comment/create', {}, {
            'post': { method: 'POST'}
        });
        return service;
    }
})();
