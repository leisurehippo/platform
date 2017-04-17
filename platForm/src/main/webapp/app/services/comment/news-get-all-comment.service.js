/**
 * Created by OlyLis on 2017/3/25.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('NewsGetAllComment', NewsGetAllComment);

    NewsGetAllComment.$inject = ['$resource'];

    function NewsGetAllComment($resource) {
        var service = $resource('api/ms-comment/get-comments/article/:articleId/level/:level', {}, {
            'get': { method: 'GET' }
        });
        return service;
    }
})();
