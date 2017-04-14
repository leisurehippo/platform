/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('GetArticleById', GetArticleById);

    GetArticleById.$inject = ['$resource'];

    function GetArticleById($resource) {
        var service = $resource('api/ms-article/get-article/:id', {}, {
            'query': { method: 'GET', isArray: true}
        });
        return service;
    }
})();
