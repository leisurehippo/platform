/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('app')
        .factory('DeleteArticle', DeleteArticle);

    DeleteArticle.$inject = ['$resource'];

    function DeleteArticle($resource) {
        var service = $resource('api/ms-article/delete/article/:id', {}, {
            'save': { method: 'POST' }
        });
        return service;
    }
})();
