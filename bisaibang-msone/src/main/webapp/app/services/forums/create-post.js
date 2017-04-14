/**
 * Created by OlyLis on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('createPost', createPost);

    createPost.$inject = ['$resource'];

    function createPost($resource) {
        var service = $resource('api/ms-post/create', {}, {
            'post': { method: 'POST'}
        });
        return service;
    }
})();
