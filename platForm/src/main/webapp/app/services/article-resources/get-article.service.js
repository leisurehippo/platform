/**
 * Created by arslan2012 on 2017/02/04.
 */
(function() {
    'use strict';

    angular
        .module('bsbmsoneApp')
        .factory('GetArticle', GetArticle);

    GetArticle.$inject = ['$resource'];

    function GetArticle($resource) {
        var service = $resource('api/ms-article/get-all-article', {}, {
            'query': { method: 'GET', isArray: true}
        });
        return service;
    }
})();
